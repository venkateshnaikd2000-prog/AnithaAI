package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.nio.file.*;

@Service
public class ImageMemoryService {

    private final String imageMemoryFile = "anitha-last-image.txt";

    public void saveLastImage(String imagePath) {
        try {
            Files.writeString(
                    Paths.get(imageMemoryFile),
                    imagePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (Exception e) {
            System.out.println("Image memory save failed: " + e.getMessage());
        }
    }

    public String getLastImage() {
        try {
            Path path = Paths.get(imageMemoryFile);

            if (!Files.exists(path)) {
                return "";
            }

            return Files.readString(path);

        } catch (Exception e) {
            return "";
        }
    }
}