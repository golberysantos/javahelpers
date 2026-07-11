package br.com.javahelperai.service;

import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService implements ChatService {

	@Override
	public String perguntar(String pergunta) {

		throw new OpenAIException("""
				A integração com a OpenAI ainda não foi habilitada.

				Obtenha uma API Key para utilizar esta implementação.
				""");

	}

}