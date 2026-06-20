package com.anitha.Anitha_AI;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BrainService {

    private final WebSearchService webSearchService;
    private final MemoryService memoryService;
    private final ProfileMemoryService profileMemoryService;
    private final List<String> chatHistory = new ArrayList<>();

    public BrainService(
            WebSearchService webSearchService,
            MemoryService memoryService,
            ProfileMemoryService profileMemoryService
    ) {
        this.webSearchService = webSearchService;
        this.memoryService = memoryService;
        this.profileMemoryService = profileMemoryService;
    }

    public String think(String question, String model, boolean internet) {
        try {
            profileMemoryService.learnFromUser(question);

            String q = question.toLowerCase();
            boolean casualChat = isCasualChat(q);
            boolean needsInternet = internet && !casualChat && isCurrentQuestion(q);

            String selectedModel = chooseModel(question, model, needsInternet);

            String profile = readProfile();
            String pastMemory = profile + "\n" + limitText(memoryService.loadMemory(), 4000);
            String webInfo = needsInternet ? webSearchService.searchWeb(question) : "";

            String systemPrompt =
                    "You are Anitha AI, created by Dhanavath Venkatesh. " +
                    "Today is " + LocalDate.now() + ". " +
                    "Your identity is always Anitha AI. " +
                    "Use profile memory as permanent truth about the user. " +
                    "Remember the user's name, nickname, country, likes, and learning goals. " +
                    "Talk naturally like a helpful friend. Use simple words and short answers.";

            String prompt =
                    systemPrompt +
                    "\n\nProfile memory:\n" + profile +
                    "\n\nLong-term memory:\n" + pastMemory +
                    "\n\nRecent chat:\n" + getRecentHistory() +
                    "\n\nInternet results:\n" + limitText(webInfo, 3000) +
                    "\n\nUser question: " + question +
                    "\n\nAnitha AI answer:";

            String answer = callOllama(selectedModel, prompt);

            chatHistory.add("User: " + question);
            chatHistory.add("Anitha AI: " + answer);
            memoryService.saveMemory(question, answer);

            return answer;

        } catch (Exception e) {
            return "Anitha AI brain connection failed.\nProblem: " + e.getMessage();
        }
    }

    private String readProfile() {
        try {
            return java.nio.file.Files.readString(
                    java.nio.file.Paths.get("anitha-profile.txt")
            );
        } catch (Exception e) {
            return "";
        }
    }

    private String callOllama(String model, String prompt) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(120000);

        RestTemplate restTemplate = new RestTemplate(factory);

        Map<String, Object> request = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false
        );

        Map response = restTemplate.postForObject(
                "http://localhost:11434/api/generate",
                request,
                Map.class
        );

        if (response == null || response.get("response") == null) {
            throw new RuntimeException("No response from Ollama model: " + model);
        }

        return response.get("response").toString();
    }

    private String limitText(String text, int maxChars) {
        if (text == null) return "";
        if (text.length() <= maxChars) return text;
        return text.substring(text.length() - maxChars);
    }

    private boolean isCasualChat(String q) {
        return q.equals("hi") ||
                q.equals("hello") ||
                q.equals("hey") ||
                q.equals("hii") ||
                q.contains("how are you") ||
                q.contains("good morning") ||
                q.contains("good night") ||
                q.contains("thank you") ||
                q.contains("thanks");
    }

    private boolean isCurrentQuestion(String q) {
        return q.contains("latest") ||
                q.contains("current") ||
                q.contains("today") ||
                q.contains("now") ||
                q.contains("news") ||
                q.contains("2025") ||
                q.contains("2026") ||
                q.contains("recent") ||
                q.contains("live") ||
                q.contains("internet") ||
                q.contains("search");
    }

    private String getRecentHistory() {
        int start = Math.max(0, chatHistory.size() - 8);
        StringBuilder history = new StringBuilder();

        for (int i = start; i < chatHistory.size(); i++) {
            history.append(chatHistory.get(i)).append("\n");
        }

        return history.toString();
    }

    private String chooseModel(String question, String model, boolean needsInternet) {
        if (needsInternet) return "qwen2.5:latest";

        if (model != null && !model.isBlank() && !model.equalsIgnoreCase("auto")) {
            return model;
        }

        String q = question.toLowerCase();

        if (q.contains("code") || q.contains("program") || q.contains("bug") ||
                q.contains("error") || q.contains("java") || q.contains("python") ||
                q.contains("spring boot") || q.contains("html") ||
                q.contains("css") || q.contains("javascript")) {
            return "deepseek-r1:latest";
        }

        if (q.contains("explain") || q.contains("why") || q.contains("reason") ||
                q.contains("logic") || q.contains("step by step") || q.contains("solve")) {
            return "qwen2.5:latest";
        }

        if (q.contains("short") || q.contains("quick") || q.contains("fast") ||
                q.contains("summary")) {
            return "mistral:latest";
        }

        return "llama3.2:latest";
    }
}