package br.com.javahelperai.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

	@Test
	void deveRetornarNullQuandoPalavraNaoExistir() {

		// Arrange
		String pergunta = "Explique programação funcional";

		// Act
		String resposta = knowledgeBase.buscarResposta(pergunta);

		// Assert
		assertNull(resposta);

	}

}