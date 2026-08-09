package br.com.javahelperai.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.javahelperai.dto.PerguntaDTO;
import br.com.javahelperai.dto.RespostaDTO;
import br.com.javahelperai.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	// 1. A dependência é declarada usando o tipo da INTERFACE
	private final ChatService service;

	// 2. O Spring injeta a implementação através do construtor
	public ChatController(ChatService service) {
		this.service = service;
	}

	@PostMapping
	public RespostaDTO perguntar(@RequestBody PerguntaDTO pergunta) {

		String resposta = service.perguntar(pergunta.getPergunta());

		return new RespostaDTO(resposta);
	}
}
