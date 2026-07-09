package br.com.helper.javaapi.openaigptapi.exception;

public class OpenAIException extends RuntimeException {

	public OpenAIException(String message) {
		super(message);
	}

	public OpenAIException(String message, Throwable cause) {
		super(message, cause);
	}

}
