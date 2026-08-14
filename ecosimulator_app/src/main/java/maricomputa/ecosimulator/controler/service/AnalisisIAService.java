package maricomputa.ecosimulator.controler.service;

/**
 * Contrato común para los distintos proveedores de IA que pueden
 * generar el análisis textual de una simulación.
 */
public interface AnalisisIAService {

    /**
     * Envía el prompt al modelo y devuelve el texto de análisis generado.
     */
    String analizar(String prompt) throws Exception;

    /**
     * Nombre legible del modelo, para guardar junto al análisis
     * (p. ej. "DeepSeek Chat", "Claude Sonnet").
     */
    String getNombreModelo();
}
