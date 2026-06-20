package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class OllamaVisionService {

    public String analyzeImage(String imagePath, String question) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            String prompt =
                    "You are Anitha AI. Analyze this image carefully. " +
                    "Explain in simple words. " +
                    "If the image has text, read it. " +
                    "If it is a screenshot error, explain the problem and solution.\n\n" +
                    "User question: " + question;

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> request = Map.of(
                    "model", "llama3.2-vision:latest",
                    "prompt", prompt,
                    "images", List.of(base64Image),
                    "stream", false
            );

            Map response = restTemplate.postForObject(
                    "http://localhost:11434/api/generate",
                    request,
                    Map.class
            );

            if (response == null || response.get("response") == null) {
                return "I could not analyze the image.";
            }

            return response.get("response").toString();

        } catch (Exception e) {
            return "Image analysis failed: " + e.getMessage();
        }
    }
}