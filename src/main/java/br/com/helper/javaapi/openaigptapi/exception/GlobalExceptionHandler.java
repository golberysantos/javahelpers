package br.com.helper.javaapi.openaigptapi.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(OpenAIException.class)
	public ResponseEntity<Map<String, Object>> tratarOpenAIException(OpenAIException ex) {

		Map<String, Object> erro = new LinkedHashMap<>();

		erro.put("timestamp", LocalDateTime.now());
		erro.put("status", HttpStatus.BAD_REQUEST.value());
		erro.put("erro", "Erro ao comunicar com a OpenAI");
		erro.put("mensagem", ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> tratarException(Exception ex) {

		Map<String, Object> erro = new LinkedHashMap<>();

		erro.put("timestamp", LocalDateTime.now());
		erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		erro.put("erro", "Erro interno");
		erro.put("mensagem", ex.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

}
