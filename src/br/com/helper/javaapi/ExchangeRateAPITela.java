package br.com.helper.javaapi;

import java.io.IOException;
import java.util.Scanner;

public class ExchangeRateAPITela {
	Scanner ler = new Scanner(System.in);

	public void exibir() throws IOException, InterruptedException {

		System.out.println(") \nBEM VINDO AO CONVERSOR DE MOEDA =]");		
		System.out.println("INFORME O CÓDIGO BASE.");
		System.out.println("EXEMPLO: BRL, USD, EUR ...:");
		String baseCode = ler.nextLine();
		ExchangeRateAPI erapi = new ExchangeRateAPI(baseCode, suaChaveAPI());
		System.out.println(erapi.rate());
	}

	private String suaChaveAPI() {
		APIInfo info = new APIInfo();
		if (info.getChaveapi().equals("")) {
			System.out.println("INFORME A CHAVE DA API: ");
			String chave = ler.next();
			info.setChaveapi(chave);
		}
		return info.getChaveapi();
	}
}
