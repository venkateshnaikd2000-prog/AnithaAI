package com.anitha.Anitha_AI;

import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public String createPlan(String question, boolean internetMode) {

        String q = question.toLowerCase();

        if (isCasual(q)) {
            return "CASUAL_CHAT";
        }

        if (internetMode && isCurrentInfo(q)) {
            return "INTERNET_RESEARCH";
        }

        if (isCoding(q)) {
            return "CODING_AGENT";
        }

        if (isReasoning(q)) {
            return "REASONING_AGENT";
        }

        if (isSummary(q)) {
            return "SUMMARY_AGENT";
        }

        return "GENERAL_AGENT";
    }

    private boolean isCasual(String q) {
        return q.equals("hi") ||
                q.equals("hello") ||
                q.equals("hey") ||
                q.equals("hii") ||
                q.contains("how are you") ||
                q.contains("good morning") ||
                q.contains("good night") ||
                q.contains("thanks") ||
                q.contains("thank you");
    }

    private boolean isCurrentInfo(String q) {
        return q.contains("latest") ||
                q.contains("current") ||
                q.contains("today") ||
                q.contains("now") ||
                q.contains("news") ||
                q.contains("recent") ||
                q.contains("live") ||
                q.contains("2025") ||
                q.contains("2026") ||
                q.contains("search") ||
                q.contains("internet");
    }

    private boolean isCoding(String q) {
        return q.contains("code") ||
                q.contains("program") ||
                q.contains("java") ||
                q.contains("python") ||
                q.contains("html") ||
                q.contains("css") ||
                q.contains("javascript") ||
                q.contains("spring boot") ||
                q.contains("bug") ||
                q.contains("error");
    }

    private boolean isReasoning(String q) {
        return q.contains("explain") ||
                q.contains("why") ||
                q.contains("how") ||
                q.contains("reason") ||
                q.contains("logic") ||
                q.contains("step by step") ||
                q.contains("solve");
    }

    private boolean isSummary(String q) {
        return q.contains("summary") ||
                q.contains("summarize") ||
                q.contains("short") ||
                q.contains("quick") ||
                q.contains("simple words");
    }
}