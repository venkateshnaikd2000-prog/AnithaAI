package com.anitha.Anitha_AI;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;

@RestController
@CrossOrigin
public class ChatController {

    private final BrainService brainService;
    private final ChatSessionService chatSessionService;
    private final AutonomousBrainService autonomousBrainService;
    private final VisionService visionService;
    private final GeminiService geminiService;
    private final PdfService pdfService;

    public ChatController(
            BrainService brainService,
            ChatSessionService chatSessionService,
            AutonomousBrainService autonomousBrainService,
            VisionService visionService,
            GeminiService geminiService,
            PdfService pdfService
    ) {
        this.brainService = brainService;
        this.chatSessionService = chatSessionService;
        this.autonomousBrainService = autonomousBrainService;
        this.visionService = visionService;
        this.geminiService = geminiService;
        this.pdfService = pdfService;
    }

    @PostMapping("/chat/new")
    public ChatSession newChat(@RequestParam(defaultValue = "New Chat") String title) {
        return chatSessionService.createChat(title);
    }

    @GetMapping("/chat/all")
    public Collection<ChatSession> allChats() {
        return chatSessionService.getAllChats();
    }

    @GetMapping("/chat/{id}")
    public ChatSession getChat(@PathVariable String id) {
        return chatSessionService.getChat(id);
    }

    @DeleteMapping("/chat/{id}")
    public String deleteChat(@PathVariable String id) {
        chatSessionService.deleteChat(id);
        return "Chat deleted";
    }

    @GetMapping("/ask")
    public String ask(
            @RequestParam String question,
            @RequestParam(defaultValue = "local") String brain,
            @RequestParam(defaultValue = "auto") String model,
            @RequestParam(defaultValue = "false") boolean internet,
            @RequestParam(defaultValue = "false") boolean autonomous,
            @RequestParam(defaultValue = "false") boolean image,
            @RequestParam(defaultValue = "false") boolean pdf,
            @RequestParam(required = false) String chatId
    ) {
        String answer;

        if (pdf) {
            String pdfText = pdfService.getLastPdfText();

            if (pdfText.isBlank()) {
                answer = "Please upload a PDF first.";
            } else {
                answer = brainService.think(
                        "Use this PDF text to answer the question.\n\nPDF TEXT:\n"
                                + pdfText
                                + "\n\nQUESTION:\n"
                                + question,
                        model,
                        false
                );
            }

        } else if (brain.equalsIgnoreCase("gemini")) {
            answer = geminiService.askGemini(question);

        } else if (image) {
            answer = visionService.analyzeLastImage(question);

        } else if (autonomous) {
            answer = autonomousBrainService.thinkAutonomously(question, internet);

        } else {
            answer = brainService.think(question, model, internet);
        }

        if (chatId != null && !chatId.isBlank()) {
            chatSessionService.addMessage(chatId, "User: " + question);
            chatSessionService.addMessage(chatId, "Anitha AI: " + answer);
        }

        return answer;
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile file) {
        return visionService.saveImage(file);
    }

    @PostMapping("/upload-pdf")
    public String uploadPdf(@RequestParam("pdf") MultipartFile file) {
        return pdfService.uploadPdf(file);
    }
}