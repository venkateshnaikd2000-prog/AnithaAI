package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelationshipMemoryService {

    private final String fileName = "anitha-relationships.txt";

    public void learnRelationship(String userMessage) {
        String msg = userMessage.toLowerCase();

        if (msg.contains("mother")) save("Mother", userMessage);
        if (msg.contains("father")) save("Father", userMessage);
        if (msg.contains("girlfriend")) save("Girlfriend", userMessage);
        if (msg.contains("wife")) save("Wife", userMessage);
        if (msg.contains("brother")) save("Brother", userMessage);
        if (msg.contains("sister")) save("Sister", userMessage);
        if (msg.contains("friend")) save("Friend", userMessage);
    }

    public String answerRelationshipQuestion(String question) {
        String q = question.toLowerCase();
        String memory = load();

        if (q.contains("mother") && memory.contains("Mother:")) return find("Mother", memory);
        if (q.contains("father") && memory.contains("Father:")) return find("Father", memory);
        if (q.contains("girlfriend") && memory.contains("Girlfriend:")) return find("Girlfriend", memory);
        if (q.contains("wife") && memory.contains("Wife:")) return find("Wife", memory);
        if (q.contains("brother") && memory.contains("Brother:")) return find("Brother", memory);
        if (q.contains("sister") && memory.contains("Sister:")) return find("Sister", memory);
        if (q.contains("friend") && memory.contains("Friend:")) return find("Friend", memory);

        return "";
    }

    private void save(String relation, String text) {
        try {
            String data = relation + ": " + text + System.lineSeparator();
            Files.write(
                    Paths.get(fileName),
                    data.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (Exception e) {
            System.out.println("Relationship memory failed: " + e.getMessage());
        }
    }

    private String load() {
        try {
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) return "";
            return Files.readString(path);
        } catch (Exception e) {
            return "";
        }
    }

    private String find(String relation, String memory) {
        String[] lines = memory.split("\\R");

        for (int i = lines.length - 1; i >= 0; i--) {
            if (lines[i].startsWith(relation + ":")) {
                return "I remember this: " + lines[i].replace(relation + ":", "").trim();
            }
        }

        return "";
    }
}