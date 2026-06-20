package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.nio.file.*;

@Service
public class ProfileMemoryService {

    private final String profileFile = "anitha-profile.txt";

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
        try {
            String data =
                    "Memory: " + memory + System.lineSeparator();

            Files.write(
                    Paths.get(profileFile),
                    data.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (Exception e) {
            System.out.println("Profile memory save failed: " + e.getMessage());
        }
    }

    public String loadProfileMemory() {
        try {
            Path path = Paths.get(profileFile);

            if (!Files.exists(path)) {
                return "";
            }

            return Files.readString(path);

        } catch (Exception e) {
            return "";
        }
    }

    public void clearProfileMemory() {
        try {
            Files.deleteIfExists(Paths.get(profileFile));
        } catch (Exception e) {
            System.out.println("Profile memory clear failed: " + e.getMessage());
        }
    }
}