package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;
import br.com.helper.designpartterns.facadeclassica.TransferenciaException;
import br.com.helper.designpartterns.facadeclassica.subsistema.Transacao;

public class FacadeBancariaMock implements FacadeBancaria {
    private static final Logger logger = LoggerFactory.getLogger(FacadeBancariaMock.class);
    
    @Override
    public ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias) 
            throws ContaInvalidaException {
        logger.info("MOCK - Obtendo extrato para: {}", numeroConta);
        
        if (!numeroConta.startsWith("12345")) {
            throw new ContaInvalidaException("Conta inválida no mock");
        }
        
        List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(new Transacao(LocalDateTime.now(), "MOCK-DEPOSITO", 
                       new BigDecimal("1000.00")));
        transacoes.add(new Transacao(LocalDateTime.now().minusDays(1), "MOCK-SAQUE", 
                       new BigDecimal("250.00")));
        
        return new ExtratoBancario(numeroConta, new BigDecimal("750.00"), transacoes);
    }
    
    @Override
    public boolean realizarTransferencia(String contaOrigem, String contaDestino, 
                                         BigDecimal valor) throws TransferenciaException {
        logger.info("MOCK - Transferência de {} para {} no valor {}", 
                   contaOrigem, contaDestino, valor);
        // Sempre sucesso nos mocks
        return true;
    }
    
    @Override
    public List<ProdutoFinanceiro> consultarProdutos(String numeroConta) {
        logger.info("MOCK - Produtos para conta: {}", numeroConta);
        return Arrays.asList(
            new ProdutoFinanceiro("POUPANCA", "1", new BigDecimal("10.00")),
            new ProdutoFinanceiro("CDB", "2", new BigDecimal("2.00"))
        );
    }
}

