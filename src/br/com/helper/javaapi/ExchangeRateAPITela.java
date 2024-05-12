package br.com.helper.javaapi;

import java.io.IOException;
import java.util.Scanner;

public class ExchangeRateAPITela {
	public void exibir() throws IOException, InterruptedException {
		Scanner ler = new Scanner(System.in);
		String baseCode = ler.next();
		ler.close();
		ExchangeRateAPI erapi = new ExchangeRateAPI(baseCode, suaChaveAPI());
		System.out.println(erapi.rate());
	}

	private String suaChaveAPI() {
		APIInfo info = new APIInfo();
		if (info.getChaveapi().equals(null)) {
			System.out.println("INFORME A CHAVE DA API: ");
			Scanner ler = new Scanner(System.in);
			info.setChaveapi(ler.next());
			ler.close();
		}
		return info.getChaveapi();
	}
}
