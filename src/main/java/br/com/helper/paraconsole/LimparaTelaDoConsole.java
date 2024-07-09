package br.com.helper.paraconsole;

public class LimparaTelaDoConsole {
	public static void limpar() {
		try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

	}
}
