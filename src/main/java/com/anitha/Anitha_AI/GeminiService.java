package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    public String askGemini(String question) {
        try {
            String apiKey = System.getenv("GEMINI_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                return "Gemini API key is missing. Please set GEMINI_API_KEY in environment variables.";
            }

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
                            + apiKey;

            String prompt =
                    "You are Anitha AI, created by Dhanavath Venkatesh. " +
                    "Answer in simple words. Be friendly and useful.\n\n" +
                    "User question: " + question;

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            RestTemplate restTemplate = new RestTemplate();

            Map response = restTemplate.postForObject(url, body, Map.class);

            List candidates = (List) response.get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            return firstPart.get("text").toString();

        } catch (Exception e) {
            return "Gemini brain failed: " + e.getMessage();
        }
    }
}