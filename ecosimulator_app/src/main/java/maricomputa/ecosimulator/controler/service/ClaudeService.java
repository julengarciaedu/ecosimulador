package maricomputa.ecosimulator.controler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Genera el análisis de una simulación usando la API de Claude (Anthropic).
 */
public class ClaudeService implements AnalisisIAService {
    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String API_VERSION = "2023-06-01";
    private static final String MODELO = "claude-sonnet-5";

    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ClaudeService(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    @Override
    public String getNombreModelo() {
        return "Claude (Anthropic)";
    }

    @Override
    public String analizar(String prompt) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODELO);
        requestBody.put("max_tokens", 2000);
        requestBody.put("system", "Eres un ecólogo experto en simulación de ecosistemas.");
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));

        String json = mapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("x-api-key", apiKey)
            .header("anthropic-version", API_VERSION)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Claude respondió con error " + response.statusCode() + ": " + response.body());
        }

        Map<String, Object> result = mapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> contenido = (List<Map<String, Object>>) result.get("content");
        if (contenido == null || contenido.isEmpty()) {
            throw new IllegalStateException("Claude no devolvió contenido en la respuesta");
        }

        StringBuilder texto = new StringBuilder();
        for (Map<String, Object> bloque : contenido) {
            if ("text".equals(bloque.get("type"))) {
                texto.append((String) bloque.get("text"));
            }
        }
        return texto.toString();
    }
}
