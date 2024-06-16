package br.com.helper.javaapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.br.com.javaapi.screenmatch.modelos.Endereco;

public class BuscarEnderecoViaCep {

	public Endereco exibir(String cep) {

		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		URI endereco = URI.create("https://viacep.com.br/ws/" + cep + "/json");
		HttpRequest request = HttpRequest.newBuilder().uri(endereco).build();
		HttpClient client = HttpClient.newHttpClient();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String json = response.body();
			System.out.println(json);
			// returna o objeto Endereco
			return gson.fromJson(json, Endereco.class);

		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Não consegui obter o endereço a partir desse CEP.");
		}

	}
}
