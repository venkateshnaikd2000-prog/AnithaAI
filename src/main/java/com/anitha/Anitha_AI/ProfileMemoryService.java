package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileMemoryService {

    private final List<String> profileMemories = new ArrayList<>();

    public void learnFromUser(String userMessage) {

        String msg = userMessage.toLowerCase();

        if (
                msg.contains("remember this") ||
                msg.contains("my name is") ||
                msg.contains("call me") ||
                msg.contains("my favorite") ||
                msg.contains("i like") ||
                msg.contains("i love") ||
                msg.contains("i am learning") ||
                msg.contains("i study") ||
                msg.contains("i live in") ||
                msg.contains("i am from") ||
                msg.contains("my girlfriend") ||
                msg.contains("my wife") ||
                msg.contains("my husband") ||
                msg.contains("my mother") ||
                msg.contains("my father") ||
                msg.contains("my brother") ||
                msg.contains("my sister") ||
                msg.contains("my friend") ||
                msg.contains("my goal") ||
                msg.contains("my dream")
        ) {
            saveProfileMemory(userMessage);
        }
    }

    public void saveProfileMemory(String memory) {

        if (!profileMemories.contains(memory)) {
            profileMemories.add(memory);
        }

        if (profileMemories.size() > 100) {
            profileMemories.remove(0);
        }
    }

    public String loadProfileMemory() {

        if (profileMemories.isEmpty()) {
            return "";
        }

        return String.join("\n", profileMemories);
    }

    public void clearProfileMemory() {
        profileMemories.clear();
    }
}