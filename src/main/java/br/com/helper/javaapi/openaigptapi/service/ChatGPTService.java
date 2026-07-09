package br.com.helper.javaapi.openaigptapi.service;

import org.springframework.stereotype.Service;

import br.com.helper.javaapi.openaigptapi.exception.OpenAIException;
import br.com.helper.javaapi.openaigptapi.util.PromptBuilder;

@Service
public class ChatGPTService {

	public String perguntar(String pergunta) {

		if (pergunta == null || pergunta.isBlank()) {
			throw new OpenAIException("A pergunta não pode estar vazia.");
		}

		// Futuramente este prompt será enviado para a OpenAI
		String prompt = PromptBuilder.criarPromptExplicacaoJava(pergunta);

		return gerarRespostaMock(prompt);

	}

	private String gerarRespostaMock(String prompt) {

		if (prompt.toLowerCase().contains("polimorfismo")) {
			return """
					Polimorfismo é a capacidade de um objeto assumir
					diferentes comportamentos através da herança e interfaces.

					Exemplo:

					Animal animal = new Cachorro();
					animal.emitirSom();

					Saída:

					Au Au
					""";
		}

		if (prompt.toLowerCase().contains("encapsulamento")) {
			return """
					Encapsulamento consiste em proteger os atributos da classe,
					permitindo acesso através de métodos getters e setters.
					""";
		}

		if (prompt.toLowerCase().contains("herança")) {
			return """
					Herança permite que uma classe reutilize atributos
					e métodos de outra classe.
					""";
		}

		return """
				Esta é uma resposta simulada da OpenAI.

				Sua pergunta foi:

				%s
				""".formatted(prompt);

	}

}