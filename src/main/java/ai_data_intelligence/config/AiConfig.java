package ai_data_intelligence.config;

import ai_data_intelligence.agent.SalesAgent;
import ai_data_intelligence.tool.SalesTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    @Bean
    public ChatModel chatModel(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model-name}") String modelName) {

        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .temperature(0.3)
                .timeout(Duration.ofSeconds(120))
                .build();
    }

    @Bean
    public SalesAgent salesAgent(ChatModel chatModel, SalesTool salesTool) {

        return AiServices.builder(SalesAgent.class)
                .chatModel(chatModel)
                .tools(salesTool)
                .build();
    }

}
