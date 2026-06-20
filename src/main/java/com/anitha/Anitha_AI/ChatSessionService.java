package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatSessionService {

    private final Map<String, ChatSession> chats = new LinkedHashMap<>();

    public ChatSession createChat(String title) {
        String id = UUID.randomUUID().toString();
        ChatSession chat = new ChatSession(id, title);
        chats.put(id, chat);
        return chat;
    }

    public Collection<ChatSession> getAllChats() {
        return chats.values();
    }

    public ChatSession getChat(String id) {
        return chats.get(id);
    }

    public void addMessage(String id, String message) {
        ChatSession chat = chats.get(id);
        if (chat != null) {
            chat.addMessage(message);

            if (chat.getTitle().equals("New Chat") && message.startsWith("User:")) {
                String title = message.replace("User:", "").trim();
                if (title.length() > 25) {
                    title = title.substring(0, 25) + "...";
                }
                chat.setTitle(title);
            }
        }
    }

    public void deleteChat(String id) {
        chats.remove(id);
    }

    public void clearAllChats() {
        chats.clear();
    }
}