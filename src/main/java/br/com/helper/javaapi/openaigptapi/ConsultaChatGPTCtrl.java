package br.com.helper.javaapi.openaigptapi;

import java.util.Scanner;

public class ConsultaChatGPTCtrl {
	Scanner ler = new Scanner(System.in);
	public void consultar() {
		System.out.println("INFORME UM TEXTO PARA TRADUZIR PARA PORTUGUÊS:");
		String txt = ler.next();
		System.out.println("Resultado da tradução: "+ConsultaChatGPT.obterTraducao(txt));
	}
}
