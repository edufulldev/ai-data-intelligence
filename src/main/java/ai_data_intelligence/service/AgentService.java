package ai_data_intelligence.service;

import ai_data_intelligence.agent.SalesAgent;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final SalesAgent salesAgent;

    public AgentService(SalesAgent salesAgent) {
        this.salesAgent = salesAgent;
    }

    public String ask(String question) {
        return salesAgent.chat(question);
    }
}
