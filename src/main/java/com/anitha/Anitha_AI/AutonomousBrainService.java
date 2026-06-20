package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AutonomousBrainService {

    private final PlannerService plannerService;
    private final AgentService agentService;
    private final WebSearchService webSearchService;
    private final MemoryService memoryService;
    private final ProfileMemoryService profileMemoryService;
    private final RelationshipMemoryService relationshipMemoryService;

    public AutonomousBrainService(
            PlannerService plannerService,
            AgentService agentService,
            WebSearchService webSearchService,
            MemoryService memoryService,
            ProfileMemoryService profileMemoryService,
            RelationshipMemoryService relationshipMemoryService
    ) {
        this.plannerService = plannerService;
        this.agentService = agentService;
        this.webSearchService = webSearchService;
        this.memoryService = memoryService;
        this.profileMemoryService = profileMemoryService;
        this.relationshipMemoryService = relationshipMemoryService;
    }

    public String thinkAutonomously(String question, boolean internetMode) {
        try {
            profileMemoryService.learnFromUser(question);
            relationshipMemoryService.learnRelationship(question);

            String relationshipAnswer =
                    relationshipMemoryService.answerRelationshipQuestion(question);

            if (!relationshipAnswer.isBlank()) {
                memoryService.saveMemory(question, relationshipAnswer);
                return relationshipAnswer;
            }

            String plan = plannerService.createPlan(question, internetMode);
            String model = chooseStableModel(plan);

            String webInfo = "";
            if (plan.equals("INTERNET_RESEARCH")) {
                webInfo = webSearchService.searchWeb(question);
            }

            String prompt =
                    "You are Anitha AI, created by Dhanavath Venkatesh. " +
                    "Today is " + LocalDate.now() + ". " +
                    "Your identity is always Anitha AI. " +
                    "Never say you are Llama, Meta, Qwen, Mistral, DeepSeek, OpenAI, Google, or Gemini. " +
                    "Use simple words and answer like a helpful friend. " +
                    "If the user asks about personal relationships, use relationship memory only. " +
                    "If you do not know a personal fact, say you do not know yet and ask the user to tell you.\n\n" +

                    "Profile memory:\n" + readFile("anitha-profile.txt") + "\n\n" +
                    "Relationship memory:\n" + readFile("anitha-relationships.txt") + "\n\n" +
                    "Long-term memory:\n" + limitText(memoryService.loadMemory(), 1800) + "\n\n" +
                    "Internet information:\n" + limitText(webInfo, 1800) + "\n\n" +
                    "Task type: " + plan + "\n\n" +
                    "User question:\n" + question + "\n\n" +
                    "Anitha AI final answer:";

            String answer = agentService.askAgent(model, prompt);

            memoryService.saveMemory(question, answer);

            return answer;

        } catch (Exception e) {
            return "Autonomous brain failed.\nProblem: " + e.getMessage();
        }
    }

    private String chooseStableModel(String plan) {
        switch (plan) {
            case "CODING_AGENT":
                return "llama3.2:1b";
            case "REASONING_AGENT":
            case "INTERNET_RESEARCH":
                return "llama3.2:latest";
            case "SUMMARY_AGENT":
                return "llama3.2:1b";
            case "CASUAL_CHAT":
            case "GENERAL_AGENT":
            default:
                return "llama3.2:1b";
        }
    }

    private String readFile(String fileName) {
        try {
            return java.nio.file.Files.readString(
                    java.nio.file.Paths.get(fileName)
            );
        } catch (Exception e) {
            return "";
        }
    }

    private String limitText(String text, int maxChars) {
        if (text == null) return "";
        if (text.length() <= maxChars) return text;
        return text.substring(text.length() - maxChars);
    }
}