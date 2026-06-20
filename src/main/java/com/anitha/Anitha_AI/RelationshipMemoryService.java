package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RelationshipMemoryService {

    private final Map<String, String> relationships = new HashMap<>();

    public void learnRelationship(String userMessage) {
        String msg = userMessage.toLowerCase();

        if (msg.contains("mother")) save("Mother", userMessage);
        if (msg.contains("father")) save("Father", userMessage);
        if (msg.contains("girlfriend")) save("Girlfriend", userMessage);
        if (msg.contains("wife")) save("Wife", userMessage);
        if (msg.contains("husband")) save("Husband", userMessage);
        if (msg.contains("brother")) save("Brother", userMessage);
        if (msg.contains("sister")) save("Sister", userMessage);
        if (msg.contains("friend")) save("Friend", userMessage);
    }

    public String answerRelationshipQuestion(String question) {
        String q = question.toLowerCase();

        if (q.contains("mother") && relationships.containsKey("Mother")) {
            return "I remember this: " + relationships.get("Mother");
        }

        if (q.contains("father") && relationships.containsKey("Father")) {
            return "I remember this: " + relationships.get("Father");
        }

        if (q.contains("girlfriend") && relationships.containsKey("Girlfriend")) {
            return "I remember this: " + relationships.get("Girlfriend");
        }

        if (q.contains("wife") && relationships.containsKey("Wife")) {
            return "I remember this: " + relationships.get("Wife");
        }

        if (q.contains("husband") && relationships.containsKey("Husband")) {
            return "I remember this: " + relationships.get("Husband");
        }

        if (q.contains("brother") && relationships.containsKey("Brother")) {
            return "I remember this: " + relationships.get("Brother");
        }

        if (q.contains("sister") && relationships.containsKey("Sister")) {
            return "I remember this: " + relationships.get("Sister");
        }

        if (q.contains("friend") && relationships.containsKey("Friend")) {
            return "I remember this: " + relationships.get("Friend");
        }

        return "";
    }

    public String loadRelationshipMemory() {
        if (relationships.isEmpty()) {
            return "";
        }

        StringBuilder memory = new StringBuilder();

        for (Map.Entry<String, String> entry : relationships.entrySet()) {
            memory.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return memory.toString();
    }

    public void clearRelationshipMemory() {
        relationships.clear();
    }

    private void save(String relation, String text) {
        relationships.put(relation, text);
    }
}