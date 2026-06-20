package com.anitha.Anitha_AI;

import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@CrossOrigin
public class ChatController {

    private final ChatSessionService chatSessionService;
    private final GeminiService geminiService;
    private final OpenAIService openAIService;
    private final MemoryService memoryService;
    private final ProfileMemoryService profileMemoryService;
    private final RelationshipMemoryService relationshipMemoryService;

    public ChatController(
            ChatSessionService chatSessionService,
            GeminiService geminiService,
            OpenAIService openAIService,
            MemoryService memoryService,
            ProfileMemoryService profileMemoryService,
            RelationshipMemoryService relationshipMemoryService
    ) {
        this.chatSessionService = chatSessionService;
        this.geminiService = geminiService;
        this.openAIService = openAIService;
        this.memoryService = memoryService;
        this.profileMemoryService = profileMemoryService;
        this.relationshipMemoryService = relationshipMemoryService;
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
            @RequestParam(defaultValue = "auto") String brain,
            @RequestParam(required = false) String chatId
    ) {
        profileMemoryService.learnFromUser(question);
        relationshipMemoryService.learnRelationship(question);

        String relationshipAnswer = relationshipMemoryService.answerRelationshipQuestion(question);
        String answer;

        if (!relationshipAnswer.isBlank()) {
            answer = relationshipAnswer;
        } else {
            String fullPrompt =
                    "You are Anitha AI.\n\n" +
                    "Profile memory:\n" + profileMemoryService.loadProfileMemory() +
                    "\n\nRelationship memory:\n" + relationshipMemoryService.loadRelationshipMemory() +
                    "\n\nRecent memory:\n" + memoryService.loadMemory() +
                    "\n\nUser question:\n" + question;

            if (brain.equalsIgnoreCase("openai")) {
                answer = openAIService.askOpenAI(fullPrompt);
            } else if (brain.equalsIgnoreCase("gemini")) {
                answer = geminiService.askGemini(fullPrompt);
            } else {
                answer = geminiService.askGemini(fullPrompt);

                if (
                        answer.toLowerCase().contains("gemini brain failed") ||
                        answer.toLowerCase().contains("gemini api key is missing") ||
                        answer.toLowerCase().contains("valid response")
                ) {
                    answer = openAIService.askOpenAI(fullPrompt);
                }
            }
        }

        memoryService.saveMemory(question, answer);

        if (chatId != null && !chatId.isBlank()) {
            chatSessionService.addMessage(chatId, "User: " + question);
            chatSessionService.addMessage(chatId, "Anitha AI: " + answer);
        }

        return answer;
    }
}