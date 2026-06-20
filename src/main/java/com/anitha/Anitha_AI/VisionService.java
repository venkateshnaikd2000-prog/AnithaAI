package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VisionService {

    private final String uploadFolder = "uploads";
    private final ImageMemoryService imageMemoryService;
    private final OllamaVisionService ollamaVisionService;

    public VisionService(
            ImageMemoryService imageMemoryService,
            OllamaVisionService ollamaVisionService
    ) {
        this.imageMemoryService = imageMemoryService;
        this.ollamaVisionService = ollamaVisionService;
    }

    public String saveImage(MultipartFile file) {
        try {
            File folder = new File(uploadFolder);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            Path filePath = Paths.get(
                    uploadFolder,
                    System.currentTimeMillis() + "-" + file.getOriginalFilename()
            );

            Files.write(filePath, file.getBytes());

            imageMemoryService.saveLastImage(filePath.toString());

            return "Image uploaded successfully. Now ask me about the image.";

        } catch (Exception e) {
            return "Image upload failed: " + e.getMessage();
        }
    }

    public String analyzeLastImage(String question) {
        String imagePath = imageMemoryService.getLastImage();

        if (imagePath == null || imagePath.isBlank()) {
            return "Please upload an image first.";
        }

        return ollamaVisionService.analyzeImage(imagePath, question);
    }
}