package br.com.helper.designpartterns.principal;

import br.com.helper.designpartterns.singleton.SingletonEager;
import br.com.helper.designpartterns.singleton.SingletonHoldInternoBillPughController;
import br.com.helper.designpartterns.strategy.FreteController;

public class DesignpartternsPrincipal {

	public static void main(String[] args) {
		
		//SingletonEager se = SingletonEager.getInstancia();
		//SingletonHoldInternoBillPughController.exibir();
		FreteController.exibir(); 

	}

}
