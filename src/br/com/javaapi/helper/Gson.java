package br.com.javaapi.helper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

/**
 * Gson é uma biblioteca Java que pode ser usada para converter objetos Java em
 * sua representação JSON. Também pode ser usado para converter uma string JSON
 * em um objeto Java equivalente. Fonte:
 * https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.5/com/google/gson/Gson.html
 * https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10
 */
public class Gson {

	public void serializar(Object ObjJava) {
		
	}

	public void desserializar(String Json) {
		
        try {

			System.out.println(Json);

			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

			TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
			System.out.println(meuTituloOmdb);

			Titulo meuTitulo = new Titulo(meuTituloOmdb);
			System.out.println("Titulo já convertido");
			System.out.println(meuTitulo);
		} catch (NumberFormatException e) {
			System.out.println("Aconteceu um erro: ");
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Algum erro de argumento na busca, verifique o endereço");
		} catch (Exception e) {
			System.out.println("Aconteceu algo, não sei o que");
		}

		System.out.println("O programa finalizou corretamente!");

	}

}
