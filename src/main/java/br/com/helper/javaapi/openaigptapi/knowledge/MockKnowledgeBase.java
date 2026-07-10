package br.com.helper.javaapi.openaigptapi.knowledge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Component
public class MockKnowledgeBase {

    private Map<String, String> conhecimento;

    @PostConstruct
    public void carregarConhecimento() {

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input =
                     new ClassPathResource(
                             "knowledge/knowledge-base.json")
                             .getInputStream()) {

            conhecimento = mapper.readValue(
                    input,
                    new TypeReference<Map<String, String>>() {
                    });

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao carregar knowledge-base.json",
                    e);

        }

    }

    public String buscarResposta(String pergunta) {

        String texto = pergunta.toLowerCase();

        return conhecimento.entrySet()
                .stream()
                .filter(entry ->
                        texto.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

    }

}