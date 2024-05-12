package br.com.helper.javaapi;

import java.io.IOException;

public class ExchangeRateAPI {

	private String yourAPIKey;

	private String baseCode;

	public ExchangeRateAPI(String baseCode, String yourAPIKey) {
		this.baseCode = baseCode;
		this.yourAPIKey = yourAPIKey;
	}

	public ExchangeRateAPI() {
		// TODO Auto-generated constructor stub
	}

	public String rate() throws IOException, InterruptedException {
		ReqHttpClient req = new ReqHttpClient(
				"https://v6.exchangerate-api.com/v6/" + yourAPIKey + "/latest/" + baseCode + "");
		return req.getJson();

	}

	public void setYourAPIKey(String yourAPIKey) {
		this.yourAPIKey = yourAPIKey;
	}

}
