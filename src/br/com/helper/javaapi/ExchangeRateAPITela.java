package br.com.helper.javaapi;

import java.io.IOException;
import java.util.Scanner;

public class ExchangeRateAPITela {
	Scanner ler = new Scanner(System.in);
	
	public void exibir() throws IOException, InterruptedException {		
		System.out.println("BASE CODE INFORMA: USD");
		String baseCode = "USD"; // ler.nextLine();		
		ExchangeRateAPI erapi = new ExchangeRateAPI(baseCode, suaChaveAPI());
		System.out.println(erapi.rate());
		ler.close();
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
