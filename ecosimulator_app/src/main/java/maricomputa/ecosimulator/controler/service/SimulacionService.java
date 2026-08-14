// src/main/java/com/ecosimulator/service/SimulacionService.java
package maricomputa.ecosimulator.controler.service;

import maricomputa.ecosimulator.modelo.dao.*;
import maricomputa.ecosimulator.modelo.entidad.*;
import maricomputa.ecosimulator.utils.SimulacionEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class SimulacionService {
    
    private SimulacionDao simulacionDAO;
    private EspecieDao especieDAO;
    private HabitatDao habitatDAO;
    private SimulacionEngine engine;
    private ObjectMapper objectMapper;
    
    public SimulacionService() {
        this.simulacionDAO = new SimulacionDao();
        this.especieDAO = new EspecieDao();
        this.habitatDAO = new HabitatDao();
        this.engine = new SimulacionEngine();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Crea una nueva simulación
     */
    public Simulacion crearSimulacion(int usuarioId, String nombre, String descripcion, 
                                      Integer habitatId, Map<String, Object> parametros) 
                                      throws Exception {
        Simulacion simulacion = new Simulacion();
        simulacion.setUuid(UUID.randomUUID().toString());
        simulacion.setNombre(nombre);
        simulacion.setDescripcion(descripcion);
        simulacion.setUsuarioId(usuarioId);
        simulacion.setHabitatId(habitatId);
        simulacion.setParametrosConfiguracion(objectMapper.writeValueAsString(parametros));
        simulacion.setEstado("configurando");
        simulacion.setProgreso(0);
        simulacion.setModeloIa("deepseek-chat");
        simulacion.setRiesgoEstimado("medio");
        
        simulacionDAO.insert(simulacion);
        return simulacion;
    }
    
    /**
     * Ejecuta una simulación
     */
    public void ejecutarSimulacion(int simulacionId) throws Exception {
        Simulacion simulacion = simulacionDAO.findById(simulacionId);
        if (simulacion == null) {
            throw new IllegalArgumentException("Simulación no encontrada");
        }
        
        // Actualizar estado //OJO redundante
        simulacion.setEstado("ejecutando");
        simulacion.setFechaInicio(new Timestamp(System.currentTimeMillis()));
        //simulacionDAO.updateEstado(simulacionId, "ejecutando");
        simulacionDAO.update(simulacion);
        
        // Obtener datos necesarios
        Habitat habitat = null;
        if (simulacion.getHabitatId() != null) {
            habitat = habitatDAO.findById(simulacion.getHabitatId());
        }
        
        List<Especie> especies = especieDAO.findAll();
        
        // Configurar parámetros
        Map<String, Object> parametros = objectMapper.readValue(
            simulacion.getParametrosConfiguracion(), Map.class);
        
        // Ejecutar simulación
        Map<String, Object> resultados = engine.ejecutar(especies, habitat, parametros);
        
        // Guardar resultados
        simulacion.setResultadosGenerales(objectMapper.writeValueAsString(resultados));
        simulacion.setMetricasCalculadas(objectMapper.writeValueAsString(
            engine.calcularMetricas(resultados)));
        
        // Calcular puntuaciones
        simulacion.setPuntuacionSostenibilidad(
            engine.calcularSostenibilidad(resultados));
        simulacion.setPuntuacionBiodiversidad(
            engine.calcularBiodiversidad(resultados));
        
        // Riesgo estimado
        simulacion.setRiesgoEstimado(
            engine.estimarRiesgo(resultados));
        
        // Completar simulación
        simulacion.setEstado("completada");
        simulacion.setProgreso(100);
        simulacion.setFechaFin(new Timestamp(System.currentTimeMillis()));
        simulacion.setDuracionSimulada(
            (Integer) parametros.getOrDefault("duracionSimulada", 50));
        
        simulacionDAO.update(simulacion);
        
        // Al finalizar, usar el método específico
//    simulacionDAO.completarSimulacion(
//        simulacionId,
//        objectMapper.writeValueAsString(resultados),
//        objectMapper.writeValueAsString(metricas),
//        sostenibilidad,
//        biodiversidad,
//        riesgo
//    );
    }
    
    /**
     * Obtiene todas las simulaciones de un usuario
     */
    public List<Simulacion> getSimulacionesByUsuario(int usuarioId) throws SQLException {
        return simulacionDAO.findByUsuario(usuarioId);
    }
    
    /**
     * Obtiene simulaciones públicas
     */
    public List<Simulacion> getSimulacionesPublicas() throws SQLException {
        return simulacionDAO.findPublicas();
    }
    
    /**
     * Genera análisis con IA (simulado o real con DeepSeek)
     */
    public String generarAnalisisIA(int simulacionId) throws Exception {
        Simulacion simulacion = simulacionDAO.findById(simulacionId);
        if (simulacion == null) {
            throw new IllegalArgumentException("Simulación no encontrada");
        }
        
        // Preparar prompt
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analiza los siguientes resultados de simulación ecológica:\n\n");
        prompt.append("Nombre: ").append(simulacion.getNombre()).append("\n");
        prompt.append("Duración simulada: ").append(simulacion.getDuracionSimulada()).append(" años\n");
        prompt.append("Resultados: ").append(simulacion.getResultadosGenerales()).append("\n");
        prompt.append("Métricas: ").append(simulacion.getMetricasCalculadas()).append("\n\n");
        prompt.append("Proporciona un análisis detallado de la sostenibilidad, ");
        prompt.append("biodiversidad, riesgos y recomendaciones.");
        
        simulacion.setPromptIa(prompt.toString());
        
        // Aquí se integraría con DeepSeek API
        // Por ahora, generamos un análisis simulado
        String analisis = generarAnalisisSimulado(simulacion);
        
        simulacion.setRespuestaIa(analisis);
        simulacion.setAnalisisIa(objectMapper.writeValueAsString(
            Map.of("analisis", analisis, "modelo", "deepseek-chat")));
        
        simulacionDAO.update(simulacion);
        
        return analisis;
    }
    
    private String generarAnalisisSimulado(Simulacion simulacion) {
        int sostenibilidad = simulacion.getPuntuacionSostenibilidad() != null ? 
                           simulacion.getPuntuacionSostenibilidad() : 50;
        int biodiversidad = simulacion.getPuntuacionBiodiversidad() != null ? 
                           simulacion.getPuntuacionBiodiversidad() : 50;
        
        StringBuilder analisis = new StringBuilder();
        analisis.append("ANÁLISIS DE LA SIMULACIÓN\n");
        analisis.append("==========================\n\n");
        
        analisis.append("Puntuación de Sostenibilidad: ").append(sostenibilidad).append("/100\n");
        analisis.append("Puntuación de Biodiversidad: ").append(biodiversidad).append("/100\n");
        analisis.append("Riesgo Estimado: ").append(simulacion.getRiesgoEstimado()).append("\n\n");
        
        if (sostenibilidad >= 70 && biodiversidad >= 70) {
            analisis.append("✅ El ecosistema muestra una alta sostenibilidad y biodiversidad.\n");
            analisis.append("   Las especies clave están en equilibrio y los recursos son suficientes.\n");
        } else if (sostenibilidad >= 50 && biodiversidad >= 50) {
            analisis.append("⚠️ El ecosistema presenta un equilibrio moderado.\n");
            analisis.append("   Se recomiendan medidas de conservación para evitar el declive.\n");
        } else {
            analisis.append("❌ El ecosistema muestra signos de deterioro.\n");
            analisis.append("   Se requiere intervención urgente para restaurar el equilibrio.\n");
        }
        
        analisis.append("\nRECOMENDACIONES:\n");
        analisis.append("1. Monitorear especies con mayor vulnerabilidad\n");
        analisis.append("2. Implementar corredores ecológicos\n");
        analisis.append("3. Reducir factores de estrés humano\n");
        
        return analisis.toString();
    }
}