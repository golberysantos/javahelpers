package br.com.helper.designpartterns.facadesingleton;

import br.com.helper.designpartterns.facadeclassica.ContaInvalidaException;
import br.com.helper.designpartterns.facadeclassica.ExtratoBancario;

//===== CLIENTE =====

public class ClienteSingleton {
public static void main(String[] args) {
   // Acesso global à fachada singleton
   FacadeContaBancariaSingleton facade = FacadeContaBancariaSingleton.getInstance();
   
   try {
       // Primeira chamada - consulta real
       ExtratoBancario extrato1 = facade.obterExtratoCompleto("12345-6", 30);
       System.out.println(extrato1);
       
       // Segunda chamada - retorna do cache (mais rápido)
       ExtratoBancario extrato2 = facade.obterExtratoCompleto("12345-6", 30);
       System.out.println("Cache funcionando? " + (extrato1 != extrato2));
       
   } catch (ContaInvalidaException e) {
       System.err.println("Erro: " + e.getMessage());
   }
}
}
