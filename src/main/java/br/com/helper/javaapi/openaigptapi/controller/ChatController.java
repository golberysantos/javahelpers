package br.com.helper.javaapi.openaigptapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helper.javaapi.openaigptapi.dto.PerguntaDTO;
import br.com.helper.javaapi.openaigptapi.dto.RespostaDTO;
import br.com.helper.javaapi.openaigptapi.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping
    public RespostaDTO perguntar(@RequestBody PerguntaDTO pergunta) {

        String resposta = service.perguntar(pergunta.getPergunta());

        return new RespostaDTO(resposta);
    }
}
