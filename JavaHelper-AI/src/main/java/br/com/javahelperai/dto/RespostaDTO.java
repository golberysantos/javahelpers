package br.com.helper.javaapi.openaigptapi.dto;

public class RespostaDTO {

	private String resposta;

	public RespostaDTO() {
	}

	public RespostaDTO(String resposta) {
		this.resposta = resposta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
}
