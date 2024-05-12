package br.com.helper.javaapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The OMDb API is a RESTful web service to obtain movie information, all
 * content and images on the site are contributed and maintained by our users.
 * Primeiro, acesse o site do OMDb para cadastro de uma chave. Link:
 * https://www.omdbapi.com/apikey.aspx How to use it: http://www.omdbapi.com/
 * Usage Send all data requests to: http://www.omdbapi.com/?apikey=yourkey&
 * Poster API requests: http://img.omdbapi.com/?apikey=yourkey&
 */
public class MovieInformationOMDbApi {

	/**
	 * Parameters list By ID or Title: i = A valid IMDb ID (e.g. tt1285016) t =
	 * Movie title to search for type = Type of result to return y = Year of release
	 * plot = Return short or full plot r = The data type to return callback = JSONP
	 * callback name v = API version (reserved for future use)
	 *
	 * By Search s = Movie title to search for. type = Type of result to return. y =
	 * Year of release. r = The data type to return. page = Page number to return.
	 * callback = JSONP callback name. v = API version (reserved for future use).
	 */
	public String exibir(String busca, String parameter, String apikey) throws IOException, InterruptedException {

		String endereco = "http://www.omdbapi.com/?apikey=" + apikey + "&" + parameter + "=" + busca.replace(" ", "+");
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.body());

		return response.body();

	}
}
