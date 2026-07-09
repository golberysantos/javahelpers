package br.com.helper.javaapi.openaigptapi.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import br.com.helper.javaapi.openaigptapi.exception.OpenAIException;
import br.com.helper.javaapi.openaigptapi.util.PromptBuilder;

@Service
@Primary
public class MockChatService implements ChatService {

	@Override
	public String perguntar(String pergunta) {

		if (pergunta == null || pergunta.isBlank()) {
			throw new OpenAIException("A pergunta não pode estar vazia.");
		}

		String prompt = PromptBuilder.criarPromptExplicacaoJava(pergunta);

		return gerarRespostaMock(prompt);
	}

	private String gerarRespostaMock(String prompt) {

		String texto = prompt.toLowerCase();

		if (texto.contains("polimorfismo")) {
			return """
					Polimorfismo é a capacidade de um objeto
					assumir diferentes comportamentos.
					""";
		}

		if (texto.contains("encapsulamento")) {
			return """
					Encapsulamento protege os atributos da classe.
					""";
		}

		if (texto.contains("herança")) {
			return """
					Herança permite reutilizar código entre classes.
					""";
		}

		return """
				Resposta simulada da OpenAI.

				Pergunta:

				%s
				""".formatted(prompt);

	}

}