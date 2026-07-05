package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;

public class TransferenciaService {
	private static final Logger logger = LoggerFactory.getLogger(TransferenciaService.class);
	private final ConsultaSaldoService saldoService;

	public TransferenciaService() {
		this.saldoService = new ConsultaSaldoService();
	}

	public boolean executarTransferencia(String origem, String destino, BigDecimal valor) {
		logger.info("Executando transferência de {} para {}", origem, destino);
		// Em produção: seria uma transação bancária com rollback
		try {
			// Simular operação bancária
			if (valor.compareTo(new BigDecimal("10000")) > 0) {
				// Transferências acima de 10k precisam de autorização
				logger.warn("Transferência acima de 10k requer autorização");
				return false;
			}

			// Simular sucesso
			Thread.sleep(100); // Simular latência de rede
			return true;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}
}
