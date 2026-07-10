package br.com.helper.javaapi.openaigptapi.knowledge;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MockKnowledgeBase {

	private final Map<String, String> conhecimento = new HashMap<>();

	public MockKnowledgeBase() {

		conhecimento.put("polimorfismo", """
				Polimorfismo é a capacidade de um objeto assumir
				diferentes comportamentos.

				Exemplo:

				Animal animal = new Cachorro();

				animal.emitirSom();
				""");

		conhecimento.put("herança", """
				Herança permite reutilização de código entre classes.
				""");

		conhecimento.put("encapsulamento", """
				Encapsulamento protege os atributos utilizando
				modificadores de acesso e métodos getters/setters.
				""");

		conhecimento.put("interface", """
				Uma interface define um contrato que as classes
				devem implementar.
				""");

		conhecimento.put("spring boot", """
				Spring Boot facilita a criação de aplicações Java,
				oferecendo configuração automática e servidor embarcado.
				""");

	}

	public String buscarResposta(String pergunta) {

		String texto = pergunta.toLowerCase();

		return conhecimento.entrySet().stream().filter(entry -> texto.contains(entry.getKey())).map(Map.Entry::getValue)
				.findFirst().orElse(null);

	}

}
