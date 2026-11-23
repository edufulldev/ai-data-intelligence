package ai_data_intelligence.controller;

import ai_data_intelligence.service.AgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> request) {

        String question = request.get("question");
        String answer = agentService.ask(question);

        if(answer == null || answer.isBlank()) {
            System.out.println("AgentController: resposta nula ou vazia recebida do agente" );

            return Map.of("answer", "O agente não conseguiu gerar uma resposta.");
        }

        return Map.of("answer", answer);
    }
}
