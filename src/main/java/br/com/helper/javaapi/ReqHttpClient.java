package br.com.helper.javaapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReqHttpClient {
	private String url;
	private String json;

	public ReqHttpClient() {
		// TODO Auto-generated constructor stub
	}

	public ReqHttpClient(String url) {
		this.url = url;
	}

	public String getJson() throws IOException, InterruptedException {
		json = requisitar();
		return json;
	}

	private String requisitar() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
}
