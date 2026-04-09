// File: src/main/java/com/sns/hackathon/GeminiService.java
package com.sns.hackathon;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    public Intervention generateInterventionForPatient(String patientContext) {
        String prompt = "You are a helpful healthcare AI assistant specialized in patient adherence.\n" +
                        "Patient info: " + patientContext + 
                        "\n\nReturn ONLY valid JSON in this exact format:\n" +
                        "{\"reminders\": [\"reminder1\", \"reminder2\"], " +
                        "\"educationalContent\": [\"tip1\", \"tip2\"], " +
                        "\"careTeamNotifications\": [\"notification1\"]}";

        try {
            Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );

            String jsonBody = gson.toJson(body);
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Gemini API error: " + response.statusCode());
            }

            JsonObject jsonResp = gson.fromJson(response.body(), JsonObject.class);
            String text = jsonResp.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            text = text.replaceAll("```json|```", "").trim();
            return gson.fromJson(text, Intervention.class);

        } catch (Exception e) {
            // Fallback when Gemini fails or no API key
            Intervention fallback = new Intervention();
            fallback.setReminders(List.of("Take medicine after breakfast", "Set phone reminder for evening dose"));
            fallback.setEducationalContent(List.of("Consistent medication improves long-term health outcomes"));
            fallback.setCareTeamNotifications(List.of("Patient showing declining adherence - schedule follow-up"));
            return fallback;
        }
    }
}