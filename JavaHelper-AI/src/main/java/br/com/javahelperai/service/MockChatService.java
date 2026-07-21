package br.com.javahelperai.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import br.com.javahelperai.exception.OpenAIException;
import br.com.javahelperai.knowledge.KnowledgeBase;

@Service
@Primary
public class MockChatService implements ChatService  {

	private final KnowledgeBase knowledgeBase;
	
	// private final OpenAIKnowledgeBase knowledgeBase;

	public MockChatService(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	@Override
	public String perguntar(String pergunta) {

		if (pergunta == null || pergunta.isBlank()) {
			throw new OpenAIException("A pergunta não pode estar vazia.");
		}

		String resposta = knowledgeBase.buscarResposta(pergunta);

		if (resposta != null) {
			return resposta;
		}

		return """
				Esta é uma resposta simulada.

				Ainda não possuo conhecimento sobre:

				%s

				Em breve a OpenAI responderá esta pergunta.
				""".formatted(pergunta);

	}

}