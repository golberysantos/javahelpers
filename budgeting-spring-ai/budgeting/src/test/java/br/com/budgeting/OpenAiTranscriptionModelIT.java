package br.com.budgeting;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiTranscriptionModelIT {

    @Autowired
    private OpenAiAudioTranscriptionModel openAiTranscriptionModel;

    @ParameterizedTest
    @CsvSource({
            "recording-1.m4a, 80 reais",
            "recording-2.m4a, 40 reais",
            "recording-3.m4a, 120 reais",
            "recording-4.m4a, 90 reais",
            "recording-5.m4a, 200 reais",
            "recording-6.m4a, 60 reais",
    })
    public void should_containExpectedKeywords_when_audioFilesAreProcessed(String fileName, String expectedKeyword) {
        var recording = new ClassPathResource("audio/" + fileName);

        // 1. Empacota o recurso de áudio no AudioTranscriptionPrompt
        var prompt = new AudioTranscriptionPrompt(recording);

        // 2. Executa a chamada do modelo
        var response = openAiTranscriptionModel.call(prompt);

        // 3. Extrai o texto transcrito
        String transcriptText = response.getResult().getOutput();

        assertThat(transcriptText).containsIgnoringCase(expectedKeyword);
        System.out.println("Transcrição de " + fileName + ": " + transcriptText);
    }
}