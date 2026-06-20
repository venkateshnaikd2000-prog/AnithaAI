package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;

@Service
public class ImageMemoryService {

    private String lastImage = "";

    public void saveLastImage(String imagePath) {
        lastImage = imagePath;
    }

    public String getLastImage() {
        return lastImage;
    }

    public void clearImageMemory() {
        lastImage = "";
    }
}