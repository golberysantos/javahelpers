package br.com.javahelperai.dto;

public class PerguntaDTO {

	private String pergunta;

	public PerguntaDTO() {
	}

	public PerguntaDTO(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}
}
