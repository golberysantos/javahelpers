package br.com.javaapi.screenmatch.principal;

import com.google.gson.Gson;

import br.com.alura.screenmatch.modelos.Pessoa;

public class TestaJar {

	public static void main(String[] args) {
		String json = """
				  {
				    "nome" : "João",
				    "idade" : 30,
				    "email" : "joao@email.com"
				  }
				""";

		Gson gson2 = new Gson();
		Pessoa pessoa = gson2.fromJson(json, Pessoa.class);
		System.out.println(pessoa.getIdade());

	}

}
