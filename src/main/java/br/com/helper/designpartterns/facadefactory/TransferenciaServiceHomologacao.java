package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.TransferenciaException;

/**
 * Serviço de transferência para ambiente de HOMOLOGAÇÃO.
 * 
 * <p>
 * Este serviço simula transferências bancárias em ambiente de homologação, com
 * validações realísticas e registro de operações.
 * </p>
 * 
 * @author Seu Nome
 * @version 1.0
 */
public class TransferenciaServiceHomologacao extends TransferenciaService {

	private static final Logger logger = LoggerFactory.getLogger(TransferenciaServiceHomologacao.class);

	private final ConsultaSaldoServiceHomologacao saldoService;
	private final HistoricoTransacoesServiceHomologacao historicoService;
	private final ConcurrentHashMap<String, LimiteTransferencia> limitesDiarios;
	private final AtomicLong contadorTransferencias = new AtomicLong(0);

	// Configurações de homologação
	private static final BigDecimal LIMITE_DIARIO_PADRAO = new BigDecimal("5000.00");
	private static final BigDecimal LIMITE_MAXIMO = new BigDecimal("10000.00");

	public TransferenciaServiceHomologacao() {
		this.saldoService = new ConsultaSaldoServiceHomologacao();
		this.historicoService = new HistoricoTransacoesServiceHomologacao();
		this.limitesDiarios = new ConcurrentHashMap<>();
		logger.info("TransferenciaServiceHomologacao inicializado");
	}

	/**
	 * Executa uma transferência no ambiente de homologação.
	 * 
	 * @param origem  Conta de origem
	 * @param destino Conta de destino
	 * @param valor   Valor a transferir
	 * @return true se transferência executada com sucesso
	 * @throws TransferenciaException Se ocorrer erro na transferência
	 */
	@Override
	public boolean executarTransferencia(String origem, String destino, BigDecimal valor)
			throws TransferenciaException {

		logger.info("[HOMOLOGAÇÃO] Iniciando transferência de {} para {} no valor R$ {}", origem, destino, valor);

		long idTransferencia = contadorTransferencias.incrementAndGet();
		LocalDateTime inicio = LocalDateTime.now();

		try {
			// === VALIDAÇÕES ===
			validarParametros(origem, destino, valor);

			// 1. Verificar se conta origem tem saldo suficiente
			BigDecimal saldoOrigem = saldoService.consultarSaldo(origem);
			if (saldoOrigem.compareTo(valor) < 0) {
				logger.warn("[HOMOLOGAÇÃO] Saldo insuficiente na conta {}: R$ {} < R$ {}", origem, saldoOrigem, valor);
				throw new TransferenciaException(String
						.format("Saldo insuficiente. Disponível: R$ %.2f, Solicitado: R$ %.2f", saldoOrigem, valor));
			}

			// 2. Verificar limite diário
			verificarLimiteDiario(origem, valor);

			// 3. Verificar limite máximo por transação
			if (valor.compareTo(LIMITE_MAXIMO) > 0) {
				logger.warn("[HOMOLOGAÇÃO] Valor excede limite máximo de R$ {}", LIMITE_MAXIMO);
				throw new TransferenciaException(
						String.format("Valor excede o limite máximo de R$ %.2f por transação", LIMITE_MAXIMO));
			}

			// 4. Simular processamento (como se fosse uma transação bancária real)
			boolean sucesso = processarTransferencia(origem, destino, valor);

			if (sucesso) {
				// Registrar transações no histórico
				historicoService.registrarTransacao(origem, "TRANSFERENCIA_ENVIADA", valor.negate());
				historicoService.registrarTransacao(destino, "TRANSFERENCIA_RECEBIDA", valor);

				// Atualizar saldos (em homologação, atualizamos os saldos manualmente)
				BigDecimal novoSaldoOrigem = saldoOrigem.subtract(valor);
				saldoService.atualizarSaldoParaTeste(origem, novoSaldoOrigem);

				BigDecimal saldoDestino = saldoService.consultarSaldo(destino);
				BigDecimal novoSaldoDestino = saldoDestino.add(valor);
				saldoService.atualizarSaldoParaTeste(destino, novoSaldoDestino);

				logger.info("[HOMOLOGAÇÃO] Transferência #{} concluída com sucesso", idTransferencia);
				return true;
			} else {
				logger.error("[HOMOLOGAÇÃO] Falha no processamento da transferência #{}", idTransferencia);
				throw new TransferenciaException("Erro interno ao processar transferência");
			}

		} catch (TransferenciaException e) {
			logger.error("[HOMOLOGAÇÃO] Transferência #{} falhou: {}", idTransferencia, e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("[HOMOLOGAÇÃO] Erro inesperado na transferência #{}", idTransferencia, e);
			throw new TransferenciaException("Erro inesperado: " + e.getMessage(), e);
		} finally {
			long duracao = java.time.Duration.between(inicio, LocalDateTime.now()).toMillis();
			logger.debug("[HOMOLOGAÇÃO] Transferência #{} durou {}ms", idTransferencia, duracao);
		}
	}

	/**
	 * Valida os parâmetros da transferência.
	 */
	private void validarParametros(String origem, String destino, BigDecimal valor) throws TransferenciaException {

		if (origem == null || origem.trim().isEmpty()) {
			throw new TransferenciaException("Conta de origem não pode ser vazia");
		}

		if (destino == null || destino.trim().isEmpty()) {
			throw new TransferenciaException("Conta de destino não pode ser vazia");
		}

		if (origem.equals(destino)) {
			throw new TransferenciaException("Conta de origem e destino não podem ser iguais");
		}

		if (valor == null) {
			throw new TransferenciaException("Valor não pode ser nulo");
		}

		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new TransferenciaException("Valor deve ser positivo");
		}

		if (valor.compareTo(new BigDecimal("0.01")) < 0) {
			throw new TransferenciaException("Valor mínimo para transferência é R$ 0,01");
		}
	}

	/**
	 * Verifica o limite diário de transferências para uma conta.
	 */
	private void verificarLimiteDiario(String conta, BigDecimal valor) throws TransferenciaException {

		LimiteTransferencia limite = limitesDiarios.computeIfAbsent(conta,
				k -> new LimiteTransferencia(LIMITE_DIARIO_PADRAO, LocalDateTime.now()));

		// Verificar se é um novo dia (resetar limite)
		if (!limite.isMesmoDia(LocalDateTime.now())) {
			limite.resetar(LocalDateTime.now());
			limitesDiarios.put(conta, limite);
			logger.debug("[HOMOLOGAÇÃO] Limite diário resetado para conta {}", conta);
		}

		// Verificar se valor excede limite diário
		if (limite.getValorUtilizado().add(valor).compareTo(limite.getLimiteDiario()) > 0) {
			BigDecimal disponivel = limite.getLimiteDiario().subtract(limite.getValorUtilizado());
			throw new TransferenciaException(
					String.format("Limite diário excedido. Disponível hoje: R$ %.2f", disponivel));
		}

		// Atualizar limite utilizado
		limite.adicionarValor(valor);
		logger.debug("[HOMOLOGAÇÃO] Limite utilizado hoje: R$ {}", limite.getValorUtilizado());
	}

	/**
	 * Simula o processamento da transferência.
	 */
	private boolean processarTransferencia(String origem, String destino, BigDecimal valor) {
		// Simula processamento bancário com chance de falha
		// Em homologação, 95% de sucesso para testes
		try {
			Thread.sleep(200 + (int) (Math.random() * 300)); // 200-500ms
			return Math.random() < 0.95; // 95% de sucesso
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * Classe interna para controle de limite diário.
	 */
	private static class LimiteTransferencia {
		private final BigDecimal limiteDiario;
		private BigDecimal valorUtilizado;
		private LocalDateTime dataReferencia;

		public LimiteTransferencia(BigDecimal limiteDiario, LocalDateTime dataReferencia) {
			this.limiteDiario = limiteDiario;
			this.valorUtilizado = BigDecimal.ZERO;
			this.dataReferencia = dataReferencia;
		}

		public boolean isMesmoDia(LocalDateTime data) {
			return dataReferencia.toLocalDate().equals(data.toLocalDate());
		}

		public void resetar(LocalDateTime novaData) {
			this.valorUtilizado = BigDecimal.ZERO;
			this.dataReferencia = novaData;
		}

		public void adicionarValor(BigDecimal valor) {
			this.valorUtilizado = this.valorUtilizado.add(valor);
		}

		public BigDecimal getLimiteDiario() {
			return limiteDiario;
		}

		public BigDecimal getValorUtilizado() {
			return valorUtilizado;
		}
	}
}
