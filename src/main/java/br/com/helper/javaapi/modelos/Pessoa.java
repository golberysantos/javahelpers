package br.com.helper.javaapi.modelos;

public class Pessoa {
	private String nome;
	private int idade;
	private String email;

	public Pessoa() {

	}

	public String getEmail() {
		return email;
	}

	public int getIdade() {
		return idade;
	}

	public String getNome() {
		return nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
