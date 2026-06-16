package br.com.helper.javaapi.modelos;

import br.com.javaapi.calculos.Classificavel;

public class Filme extends Titulo implements Classificavel {
	private String diretor;

	public Filme(String nome, int anoDeLancamento) {
		super(nome, anoDeLancamento);
	}

	@Override
	public int getClassificacao() {
		return (int) pegaMedia() / 2;
	}

	public String getDiretor() {
		return diretor;
	}

	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}

	@Override
	public String toString() {
		return "Filme: " + this.getNome() + " (" + this.getAnoDeLancamento() + ")";
	}
}
