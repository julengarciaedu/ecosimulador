// src/main/java/com/ecosimulator/service/DeepSeekService.java
package maricomputa.ecosimulator.controler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeepSeekService {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;
    
    public DeepSeekService(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }
    
    public String analizarSimulacion(String prompt) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "Eres un ecólogo experto en simulación de ecosistemas."),
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        
        String json = mapper.writeValueAsString(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        Map<String, Object> result = mapper.readValue(response.body(), Map.class);
        Map<String, Object> choice = ((List<Map<String, Object>>) result.get("choices")).get(0);
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        
        return (String) message.get("content");
    }
}