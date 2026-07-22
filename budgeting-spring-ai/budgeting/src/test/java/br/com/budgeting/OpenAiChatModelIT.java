package br.com.budgeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatModelIT {

    // O Spring gerencia a criação e injeção do modelo de chat
    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    void should_receiveResponse_when_chatModelIsCalled() {
        var options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.8)
                .build();

        // Faz a chamada passando o prompt e as opções de runtime
        var response = chatModel.call(
            new org.springframework.ai.chat.prompt.Prompt(
                "Gere um registro de budgeting, com descrição de gasto, valor em reais e local",
                options
            )
        );

        String textResponse = response.getResult().getOutput().getText();

        assertThat(textResponse).isNotEmpty();
        System.out.println(textResponse);
    }
}