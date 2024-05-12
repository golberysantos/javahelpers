package br.com.helper.javaapi;

import java.io.IOException;
import java.util.Scanner;

public class ExchangeRateAPITela {
	Scanner ler = new Scanner(System.in);
	
	public void exibir() throws IOException, InterruptedException {		
		
		System.out.println(") \nBEM VINDO AO CONVERSOR DE MOEDA =]");
		System.out.println("1) Dólar >> Peso argentino");
		System.out.println("2) Peso argentino >> Dolar");
		System.out.println("3) Dólar >> Real brasileiro");
		System.out.println("4) Real brasileiro >> Dólar");
		System.out.println("5) Dólar >> Peso colombiano");
		System.out.println("6) Peso colombiano >> Dólar");
		System.out.println("7) Sair");
		System.out.println("Escolha uma opção válida: ");		
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
