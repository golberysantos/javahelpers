package br.com.javahelperai.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockKnowledgeBaseTest {

	private MockKnowledgeBase knowledgeBase;

	@BeforeEach
	void setUp() {

		knowledgeBase = new MockKnowledgeBase();
		knowledgeBase.carregarConhecimento();

	}

	@Test
	void deveRetornarRespostaQuandoPalavraConhecidaExistir() {

		// Arrange
		String pergunta = "Explique herança";

		// Act
		String resposta = knowledgeBase.buscarResposta(pergunta);

		// Assert
		assertEquals("Herança permite reutilizar atributos e métodos de outra classe.", resposta);

	}

}