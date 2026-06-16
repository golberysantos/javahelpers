package br.com.helper.javaapi.modelos;

import com.google.gson.annotations.SerializedName;

import br.com.javaapi.execoes.ErroDeConversaoDeAnoException;

public class Titulo implements Comparable<Titulo> {

	@SerializedName("Title")
	private String nome;
	@SerializedName("Yer")
	private int anoDeLancamento;
	private boolean incluidoNoPlano;
	private double somaDasAvaliacoes;
	private int totalDeAvaliacoes;
	private int duracaoEmMinutos;

	public Titulo() {

	}

	public Titulo(String nome, int anoDeLancamento) {
		this.nome = nome;
		this.anoDeLancamento = anoDeLancamento;
	}

	public Titulo(TituloOmdb meuTituloOmdb) {
		this.nome = meuTituloOmdb.title();

		if (meuTituloOmdb.year().length() > 4) {
			throw new ErroDeConversaoDeAnoException("Não consegui converter o ano porque tem mais de 04 caracteres.");
		}
		this.anoDeLancamento = Integer.valueOf(meuTituloOmdb.year());
		this.duracaoEmMinutos = Integer.valueOf(meuTituloOmdb.runtime().substring(0, 2));
	}

	public void avalia(double nota) {
		somaDasAvaliacoes += nota;
		totalDeAvaliacoes++;
	}

	@Override
	public int compareTo(Titulo outroTitulo) {
		return this.getNome().compareTo(outroTitulo.getNome());
	}

	public void exibeFichaTecnica() {
		System.out.println("Nome do filme: " + nome);
		System.out.println("Ano de lançamento: " + anoDeLancamento);
	}

	public int getAnoDeLancamento() {
		return anoDeLancamento;
	}

	public int getDuracaoEmMinutos() {
		return duracaoEmMinutos;
	}

	public String getNome() {
		return nome;
	}

	public int getTotalDeAvaliacoes() {
		return totalDeAvaliacoes;
	}

	public boolean isIncluidoNoPlano() {
		return incluidoNoPlano;
	}

	public double pegaMedia() {
		return somaDasAvaliacoes / totalDeAvaliacoes;
	}

	public void setAnoDeLancamento(int anoDeLancamento) {
		this.anoDeLancamento = anoDeLancamento;
	}

	public void setDuracaoEmMinutos(int duracaoEmMinutos) {
		this.duracaoEmMinutos = duracaoEmMinutos;
	}

	public void setIncluidoNoPlano(boolean incluidoNoPlano) {
		this.incluidoNoPlano = incluidoNoPlano;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {

		return "nome='" + nome + ", anoDeLancamento=" + anoDeLancamento;
	}
}
