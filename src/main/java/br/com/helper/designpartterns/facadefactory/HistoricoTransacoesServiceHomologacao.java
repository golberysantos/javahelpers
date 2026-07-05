package br.com.helper.designpartterns.facadefactory;

import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.Transacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de histórico de transações para ambiente de HOMOLOGAÇÃO.
 * 
 * <p>
 * Este serviço simula um histórico de transações para testes em ambiente de
 * homologação, com dados realísticos e persistência em memória.
 * </p>
 * 
 * @author Seu Nome
 * @version 1.0
 */
public class HistoricoTransacoesServiceHomologacao extends HistoricoTransacoesService {

	private static final Logger logger = LoggerFactory.getLogger(HistoricoTransacoesServiceHomologacao.class);

	// Banco de dados em memória para homologação
	private final Map<String, List<Transacao>> historicoPorConta = new ConcurrentHashMap<>();
	private final Random random = new Random();

	public HistoricoTransacoesServiceHomologacao() {
		inicializarHistoricoTeste();
		logger.info("HistoricoTransacoesServiceHomologacao inicializado");
	}

	/**
	 * Consulta o histórico de transações para uma conta em homologação.
	 * 
	 * @param numeroConta Número da conta
	 * @param ultimosDias Quantidade de dias para consultar
	 * @return Lista de transações do período
	 */
	@Override
	public List<Transacao> consultarHistorico(String numeroConta, int ultimosDias) {
		logger.info("[HOMOLOGAÇÃO] Consultando histórico para conta: {}, {} dias", numeroConta, ultimosDias);

		// Validação
		if (numeroConta == null || numeroConta.trim().isEmpty()) {
			logger.warn("[HOMOLOGAÇÃO] Tentativa de consulta com conta nula/vazia");
			return Collections.emptyList();
		}

		if (ultimosDias <= 0) {
			logger.warn("[HOMOLOGAÇÃO] Dias inválidos: {}, ajustando para 30", ultimosDias);
			ultimosDias = 30;
		}

		// Simular latência
		simularLatencia();

		// Buscar histórico
		List<Transacao> todasTransacoes = historicoPorConta.get(numeroConta);

		if (todasTransacoes == null || todasTransacoes.isEmpty()) {
			logger.warn("[HOMOLOGAÇÃO] Nenhum histórico encontrado para conta: {}", numeroConta);
			// Gerar histórico automático para contas novas
			todasTransacoes = gerarHistoricoAutomatico(numeroConta);
			historicoPorConta.put(numeroConta, todasTransacoes);
			logger.info("[HOMOLOGAÇÃO] Histórico automático gerado para conta: {}", numeroConta);
		}

		// Filtrar por período
		LocalDateTime dataCorte = LocalDateTime.now().minusDays(ultimosDias);

		List<Transacao> transacoesFiltradas = todasTransacoes.stream().filter(t -> t.getData().isAfter(dataCorte))
				.sorted((t1, t2) -> t2.getData().compareTo(t1.getData())) // Mais recentes primeiro
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		logger.info("[HOMOLOGAÇÃO] {} transações encontradas para período", transacoesFiltradas.size());
		return transacoesFiltradas;
	}

	/**
	 * Registra uma nova transação no histórico de homologação.
	 */
	public void registrarTransacao(String numeroConta, String tipo, BigDecimal valor) {
		logger.info("[HOMOLOGAÇÃO] Registrando transação: {} - {} - R$ {}", numeroConta, tipo, valor);

		Transacao transacao = new Transacao(LocalDateTime.now(), tipo, valor);

		List<Transacao> historico = historicoPorConta.get(numeroConta);
		if (historico == null) {
			historico = new ArrayList<>();
			historicoPorConta.put(numeroConta, historico);
		}

		historico.add(transacao);
		logger.debug("[HOMOLOGAÇÃO] Transação registrada com sucesso");
	}

	/**
	 * Inicializa dados de teste para homologação.
	 */
	private void inicializarHistoricoTeste() {
		// Conta 12345-6
		List<Transacao> historico1 = new ArrayList<>();
		historico1.add(new Transacao(LocalDateTime.now().minusDays(1), "DEPOSITO", new BigDecimal("500.00")));
		historico1.add(new Transacao(LocalDateTime.now().minusDays(3), "SAQUE", new BigDecimal("200.00")));
		historico1.add(new Transacao(LocalDateTime.now().minusDays(5), "DEPOSITO", new BigDecimal("1000.00")));
		historico1.add(new Transacao(LocalDateTime.now().minusDays(7), "TRANSFERENCIA", new BigDecimal("150.00")));
		historico1.add(new Transacao(LocalDateTime.now().minusDays(10), "PAGAMENTO", new BigDecimal("350.00")));
		historicoPorConta.put("12345-6", historico1);

		// Conta 98765-4
		List<Transacao> historico2 = new ArrayList<>();
		historico2.add(new Transacao(LocalDateTime.now().minusHours(2), "DEPOSITO", new BigDecimal("1000.00")));
		historico2.add(new Transacao(LocalDateTime.now().minusDays(2), "TRANSFERENCIA", new BigDecimal("300.00")));
		historico2.add(new Transacao(LocalDateTime.now().minusDays(4), "DEPOSITO", new BigDecimal("2000.00")));
		historicoPorConta.put("98765-4", historico2);

		// Conta 11111-1 (saldo baixo)
		List<Transacao> historico3 = new ArrayList<>();
		historico3.add(new Transacao(LocalDateTime.now().minusDays(1), "SAQUE", new BigDecimal("50.00")));
		historico3.add(new Transacao(LocalDateTime.now().minusDays(3), "DEPOSITO", new BigDecimal("100.00")));
		historicoPorConta.put("11111-1", historico3);

		logger.debug("[HOMOLOGAÇÃO] Histórico de teste inicializado");
	}

	/**
	 * Gera histórico automático para novas contas.
	 */
	private List<Transacao> gerarHistoricoAutomatico(String numeroConta) {
		List<Transacao> transacoes = new ArrayList<>();

		// Gera transações aleatórias dos últimos 30 dias
		int numTransacoes = 3 + random.nextInt(10); // 3 a 12 transações

		for (int i = 0; i < numTransacoes; i++) {
			int diasAtras = 1 + random.nextInt(30);
			String[] tipos = { "DEPOSITO", "SAQUE", "TRANSFERENCIA", "PAGAMENTO" };
			String tipo = tipos[random.nextInt(tipos.length)];
			BigDecimal valor = new BigDecimal(50 + random.nextInt(5000) / 100.0);

			transacoes.add(new Transacao(LocalDateTime.now().minusDays(diasAtras), tipo, valor));
		}

		// Ordenar por data
		transacoes.sort((t1, t2) -> t1.getData().compareTo(t2.getData()));

		return transacoes;
	}

	/**
	 * Simula latência para ambiente de homologação.
	 */
	private void simularLatencia() {
		try {
			int latencia = 50 + random.nextInt(200); // 50-250ms
			Thread.sleep(latencia);
			logger.trace("[HOMOLOGAÇÃO] Latência simulada: {}ms", latencia);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.warn("[HOMOLOGAÇÃO] Thread interrompida");
		}
	}
}
