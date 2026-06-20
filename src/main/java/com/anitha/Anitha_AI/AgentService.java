package com.anitha.Anitha_AI;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AgentService {

    public String askAgent(String model, String prompt) {

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
            return "Agent did not return a response.";
        }

        return response.get("response").toString();
    }
}