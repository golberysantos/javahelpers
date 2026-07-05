package br.com.helper.designpartterns.facadeclassica;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.Transacao;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;

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
