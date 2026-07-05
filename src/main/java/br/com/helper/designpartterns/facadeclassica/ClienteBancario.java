package br.com.helper.designpartterns.facadeclassica;

import br.com.helper.designpartterns.facadeclassica.subsistema.ConsultaSaldoService;
import br.com.helper.designpartterns.facadeclassica.subsistema.HistoricoTransacoesService;
import br.com.helper.designpartterns.facadeclassica.subsistema.ValidacaoContaService;

//===== CLIENTE =====

public class ClienteBancario {
 public static void main(String[] args) {
     // Configuração dos serviços (em produção seria feito por um framework como Spring)
     ValidacaoContaService validador = new ValidacaoContaService();
     ConsultaSaldoService saldoService = new ConsultaSaldoService();
     HistoricoTransacoesService historicoService = new HistoricoTransacoesService();
     
     // Fachada criada com as dependências
     FacadeContaBancaria facade = new FacadeContaBancaria(validador, saldoService, historicoService);
     
     try {
         // Cliente  usa a interface simplificada
         ExtratoBancario extrato = facade.obterExtratoCompleto("12345-6", 30);
         System.out.println(extrato);
     } catch (ContaInvalidaException e) {
         System.err.println("Erro ao obter extrato: " + e.getMessage());
     }
 }
}
