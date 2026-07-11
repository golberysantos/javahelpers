package br.com.javahelperai.util;

public class PromptBuilder {

	private PromptBuilder() {
	}

	public static String criarPromptTraducao(String texto) {

		return """
				Traduza o texto abaixo para português do Brasil.

				Texto:
				%s
				""".formatted(texto);
	}

	public static String criarPromptResumo(String texto) {

		return """
				Faça um resumo do texto abaixo.

				Texto:
				%s
				""".formatted(texto);
	}

	public static String criarPromptExplicacaoJava(String assunto) {

		return """
				Explique o seguinte assunto de Java de forma didática,
				com exemplos de código.

				Assunto:
				%s
				""".formatted(assunto);
	}

}
