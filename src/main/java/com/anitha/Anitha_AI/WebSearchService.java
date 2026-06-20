package com.anitha.Anitha_AI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class WebSearchService {

    public String searchWeb(String question) {

        try {

            String url =
                    "https://html.duckduckgo.com/html/?q="
                            + question.replace(" ", "+");

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            StringBuilder resultText = new StringBuilder();

            int count = 0;

            for (Element result : doc.select(".result")) {

                if (count >= 5)
                    break;

                String title = result.select(".result__title").text();

                String snippet = result.select(".result__snippet").text();

                if (!title.isEmpty()) {

                    resultText.append("Title: ")
                            .append(title)
                            .append("\n");

                    resultText.append("Info: ")
                            .append(snippet)
                            .append("\n\n");

                    count++;
                }
            }

            if (resultText.isEmpty()) {
                return "No latest information found.";
            }

            return resultText.toString();

        }

        catch (Exception e) {

            return "Internet search failed: "
                    + e.getMessage();

        }
    }
}