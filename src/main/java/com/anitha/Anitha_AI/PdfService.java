package com.anitha.Anitha_AI;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    private final String uploadFolder = "uploads";
    private String lastPdfText = "";

    public String uploadPdf(MultipartFile file) {
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

            PDDocument document = Loader.loadPDF(filePath.toFile());
            PDFTextStripper stripper = new PDFTextStripper();

            lastPdfText = stripper.getText(document);

            document.close();

            if (lastPdfText.isBlank()) {
                return "PDF uploaded, but I could not read text from it.";
            }

            return "PDF uploaded and read successfully. Now ask me questions about it.";

        } catch (Exception e) {
            return "PDF upload failed: " + e.getMessage();
        }
    }

    public String getLastPdfText() {
        if (lastPdfText == null || lastPdfText.isBlank()) {
            return "";
        }

        if (lastPdfText.length() > 6000) {
            return lastPdfText.substring(0, 6000);
        }

        return lastPdfText;
    }
}