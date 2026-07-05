package br.com.helper.designpartterns.facadefactory;

import java.math.BigDecimal;
import java.util.List;

import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;

public class ClienteFactory {
    public static void main(String[] args) {
        // A fachada é criada automaticamente baseada no ambiente
        FacadeBancaria facade = FacadeBancariaFactory.getFacade();
        
        try {
            ExtratoBancario extrato = facade.obterExtratoCompleto("12345-6", 30);
            System.out.println(extrato);
            
            boolean transferencia = facade.realizarTransferencia("12345-6", "98765-4", 
                                                                new BigDecimal("100.00"));
            System.out.println("Transferência realizada: " + transferencia);
            
            List<ProdutoFinanceiro> produtos = facade.consultarProdutos("12345-6");
            System.out.println("Produtos: " + produtos);
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}

