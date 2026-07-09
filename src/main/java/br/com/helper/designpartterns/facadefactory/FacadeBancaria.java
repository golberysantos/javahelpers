package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.util.List;

import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;
import br.com.helper.designpartterns.facadeclassica.TransferenciaException;

public interface FacadeBancaria {
    ExtratoBancario obterExtratoCompleto(String numeroConta, int ultimosDias)
            throws ContaInvalidaException;

    boolean realizarTransferencia(String contaOrigem, String contaDestino, BigDecimal valor)
            throws TransferenciaException;
    List<ProdutoFinanceiro> consultarProdutos(String numeroConta);
}

