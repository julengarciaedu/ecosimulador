package maricomputa.ecosimulator.controler.service;

/**
 * Resuelve qué proveedor de IA usar para analizar una simulación.
 * Las claves de API se leen SIEMPRE de variables de entorno, nunca del
 * código: DEEPSEEK_API_KEY y ANTHROPIC_API_KEY.
 */
public class AnalisisIAFactory {

    public static AnalisisIAService obtener(String identificador) {
        if ("claude".equalsIgnoreCase(identificador)) {
            String apiKey = System.getenv("ANTHROPIC_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException(
                    "No se ha configurado la variable de entorno ANTHROPIC_API_KEY en el servidor");
            }
            return new ClaudeService(apiKey);
        }

        // Por defecto, DeepSeek
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                "No se ha configurado la variable de entorno DEEPSEEK_API_KEY en el servidor");
        }
        return new DeepSeekService(apiKey);
    }
}
