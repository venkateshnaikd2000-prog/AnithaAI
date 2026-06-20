package com.anitha.Anitha_AI;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    public String askOpenAI(String question) {
        try {
            String apiKey = System.getenv("OPENAI_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                return "OpenAI API key is missing.";
            }

            String url = "https://api.openai.com/v1/chat/completions";

            Map<String, Object> body = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content",
                                    "You are Anitha AI, created by Dhanavath Venkatesh. Never say you are ChatGPT or OpenAI. Answer simply and helpfully."
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", question
                            )
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.postForObject(url, entity, Map.class);

            List choices = (List) response.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            return "OpenAI brain failed: " + e.getMessage();
        }
    }
}