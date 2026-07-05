package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;
import br.com.helper.designpartterns.facadeclassica.TransferenciaException;
import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;

public abstract class FacadeBancariaPadrao implements FacadeBancaria {
    private static final Logger logger = LoggerFactory.getLogger(FacadeBancariaPadrao.class);
    private final ValidacaoContaService validador;
    private final ConsultaSaldoService saldoService;
    private final HistoricoTransacoesService historicoService;
    private final TransferenciaService transferenciaService;
    private final ProdutoFinanceiroService produtoService;
    
    public FacadeBancariaPadrao(ValidacaoContaService validador,
                                ConsultaSaldoService saldoService,
                                HistoricoTransacoesService historicoService,
                                TransferenciaService transferenciaService,
                                ProdutoFinanceiroService produtoService) {
        this.validador = validador;
        this.saldoService = saldoService;
        this.historicoService = historicoService;
        this.transferenciaService = transferenciaService;
        this.produtoService = produtoService;
    }
    
    @Override
    public ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias) 
            throws ContaInvalidaException {
        // Implementação similar à Facade Clássica
        // ...
        return null;
    }
    
    @Override
    public boolean realizarTransferencia(String contaOrigem, String contaDestino, 
                                         BigDecimal valor) throws TransferenciaException {
        logger.info("Realizando transferência de {} para {} no valor de {}", 
                   contaOrigem, contaDestino, valor);
        
        // Validação e processamento da transferência
        if (!validador.validarConta(contaOrigem) || !validador.validarConta(contaDestino)) {
            throw new TransferenciaException("Conta origem ou destino inválida");
        }
        
        BigDecimal saldoOrigem = saldoService.consultarSaldo(contaOrigem);
        if (saldoOrigem.compareTo(valor) < 0) {
            throw new TransferenciaException("Saldo insuficiente para transferência");
        }
        
        return transferenciaService.executarTransferencia(contaOrigem, contaDestino, valor);
    }
    
    @Override
    public List<ProdutoFinanceiro> consultarProdutos(String numeroConta) {
        logger.info("Consultando produtos para conta: {}", numeroConta);
        return produtoService.consultarProdutos(numeroConta);
    }
}

