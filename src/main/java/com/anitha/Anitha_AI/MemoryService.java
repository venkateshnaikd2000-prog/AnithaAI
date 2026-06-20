package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryService {

    private final List<String> memories = new ArrayList<>();
    private final int MAX_MEMORIES = 100;

    public void saveMemory(String userMessage, String aiMessage) {

        String memory =
                "User: " + userMessage + "\n" +
                "Anitha AI: " + aiMessage + "\n" +
                "------------------------";

        memories.add(memory);

        if (memories.size() > MAX_MEMORIES) {
            memories.remove(0);
        }
    }

    public String loadMemory() {

        if (memories.isEmpty()) {
            return "";
        }

        int start = Math.max(0, memories.size() - 20);

        StringBuilder result = new StringBuilder();

        for (int i = start; i < memories.size(); i++) {
            result.append(memories.get(i)).append("\n");
        }

        return result.toString();
    }

    public void clearMemory() {
        memories.clear();
    }
}