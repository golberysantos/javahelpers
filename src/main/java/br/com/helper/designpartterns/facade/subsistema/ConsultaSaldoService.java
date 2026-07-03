package br.com.helper.designpartterns.facade.subsistema;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsultaSaldoService {
    private static final Logger logger = LoggerFactory.getLogger(ConsultaSaldoService.class);
    
    public BigDecimal consultarSaldo(String numeroConta) {
        logger.info("Consultando saldo da conta: {}", numeroConta);
        // Em produção: chamada a banco de dados ou serviço externo
        return new BigDecimal("1500.00");
    }
}

