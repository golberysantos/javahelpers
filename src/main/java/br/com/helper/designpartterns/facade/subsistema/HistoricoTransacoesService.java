package br.com.helper.designpartterns.facade.subsistema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistoricoTransacoesService {
	private static final Logger logger = LoggerFactory.getLogger(HistoricoTransacoesService.class);

	public List<Transacao> consultarHistorico(String numeroConta, int ultimosDias) {
		logger.info("Consultando histórico de {} dias para conta: {}", ultimosDias, numeroConta);
		// Em produção: consulta a banco de dados
		List<Transacao> historico = new ArrayList<>();
		historico.add(new Transacao(LocalDateTime.now().minusDays(1), "DEPOSITO", new BigDecimal("500.00")));
		historico.add(new Transacao(LocalDateTime.now().minusDays(3), "SAQUE", new BigDecimal("200.00")));
		return historico;
	}
}
