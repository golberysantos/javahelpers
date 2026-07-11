package br.com.javahelperai.exception;

public class OpenAIException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenAIException(String message) {
		super(message);
	}

	public OpenAIException(String message, Throwable cause) {
		super(message, cause);
	}

}
