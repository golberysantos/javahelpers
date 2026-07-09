package br.com.helper.javaapi.openaigptapi.service;

import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;

@Service
public class ChatGPTService {

    private final OpenAIClient client;

    public ChatGPTService(OpenAIClient client) {
        this.client = client;
    }

    public String perguntar(String pergunta) {

        // Implementaremos a chamada para a OpenAI aqui
        return "Recebi sua pergunta: " + pergunta;
    }
}