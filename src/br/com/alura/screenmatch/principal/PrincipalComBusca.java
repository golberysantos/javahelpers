package br.com.alura.screenmatch.principal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import br.com.javaapi.exemplos.MovieInformationOMDbApi;

public class PrincipalComBusca {

	public static void main(String[] args) throws IOException, InterruptedException {
		var leitura = new Scanner(System.in);
		String busca;
		var parameter = "t";
		var apikey = "fb2fff63";
		String resultadojson;

		System.out.println("Digite o título do filme para a busca: ");
		busca = leitura.nextLine();
		busca = "Game+of+Thrones";
		MovieInformationOMDbApi OmdbApi = new MovieInformationOMDbApi();
		resultadojson = OmdbApi.exibir(busca, parameter, apikey);
		System.out.println(resultadojson);
				
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		
		//Titulo meuTituloOmdb = gson.fromJson(resultadojson, Titulo.class);
        TituloOmdb meuTituloOmdb = gson.fromJson(resultadojson, TituloOmdb.class);
        System.out.println(meuTituloOmdb);
		
	}

}
