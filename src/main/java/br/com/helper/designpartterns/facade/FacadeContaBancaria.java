package br.com.helper.designpartterns.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//===== FACADE =====
//Interface simplificada para o cliente

public class FacadeContaBancaria {
	private static final Logger logger = LoggerFactory.getLogger(FacadeContaBancaria.class);
	private final ValidacaoContaService validador;
	private final ConsultaSaldoService saldoService;
	private final HistoricoTransacoesService historicoService;

	// Injeção de dependências via construtor
	public FacadeContaBancaria(ValidacaoContaService validador, ConsultaSaldoService saldoService,
			HistoricoTransacoesService historicoService) {
		this.validador = validador;
		this.saldoService = saldoService;
		this.historicoService = historicoService;
	}

	public ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias) throws ContaInvalidaException {

		logger.info("Obtendo extrato completo para conta: {}", numeroConta);

		// 1. Validar conta
		if (!validador.validarConta(numeroConta)) {
			throw new ContaInvalidaException("Número de conta inválido: " + numeroConta);
		}

		// 2. Buscar saldo atual
		BigDecimal saldo = saldoService.consultarSaldo(numeroConta);

		// 3. Buscar histórico
		List<Transacao> transacoes = historicoService.consultarHistorico(numeroConta, ultimosDias);

		// Retornar extrato completo
		return new ExtratoBancario(numeroConta, saldo, transacoes);
	}
}
