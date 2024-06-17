package br.com.helper.javaapi.openaigptapi;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import br.com.giben.env.SecretKey;

/**
 * OpenAI-Java

Java libraries for using OpenAI's GPT apis. Supports GPT-3, ChatGPT, and GPT-4.

Includes the following artifacts:

    api : request/response POJOs for the GPT APIs.
    client : a basic retrofit client for the GPT endpoints, includes the api module
    service : A basic service class that creates and calls the client. This is the easiest way to get started.

 */
public class ConsultaChatGPT {
	public static String obterTraducao(String texto) {
		OpenAiService service = new OpenAiService(SecretKey.getMyAPIKeyOpenAI01());

		CompletionRequest requisicao = CompletionRequest.builder()
		.model("gpt-3.5-turbo-instruct")
		.prompt("traduza para o português o texto: " + texto)
		.maxTokens(1000)
		.temperature(0.7)
		.build();

		var resposta = service.createCompletion(requisicao);
		return resposta.getChoices().get(0).getText();
		}
}
