package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.List;

@Service
public class MemoryService {

    private final String memoryFile = "anitha-memory.txt";
    private final int MAX_LINES = 200;

    public void saveMemory(String userMessage, String aiMessage) {

        try {

            String memory =
                    "User: " + userMessage + System.lineSeparator() +
                    "Anitha AI: " + aiMessage + System.lineSeparator() +
                    "------------------------" + System.lineSeparator();

            Path path = Paths.get(memoryFile);

            Files.write(
                    path,
                    memory.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            trimMemory();

        } catch (Exception e) {
            System.out.println("Memory save failed: " + e.getMessage());
        }
    }

    public String loadMemory() {

        try {

            Path path = Paths.get(memoryFile);

            if (!Files.exists(path)) {
                return "";
            }

            List<String> lines = Files.readAllLines(path);

            int start = Math.max(0, lines.size() - 80);

            return String.join("\n", lines.subList(start, lines.size()));

        } catch (Exception e) {
            return "";
        }
    }

    public void clearMemory() {

        try {
            Files.deleteIfExists(Paths.get(memoryFile));
        } catch (Exception e) {
            System.out.println("Memory clear failed: " + e.getMessage());
        }

    }

    private void trimMemory() {

        try {

            Path path = Paths.get(memoryFile);

            if (!Files.exists(path)) {
                return;
            }

            List<String> lines = Files.readAllLines(path);

            if (lines.size() > MAX_LINES) {

                lines = lines.subList(lines.size() - MAX_LINES, lines.size());

                Files.write(
                        path,
                        lines,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            }

        } catch (Exception e) {
            System.out.println("Memory trim failed: " + e.getMessage());
        }

    }
}