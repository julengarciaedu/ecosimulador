// src/main/java/com/ecosimulator/utils/SimulacionEngine.java
package maricomputa.ecosimulator.utils;

import maricomputa.ecosimulator.modelo.entidad.Especie;
import maricomputa.ecosimulator.modelo.entidad.Habitat;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulacionEngine {
    
    private Map<String, Object> resultados;
    
    /**
     * Ejecuta la simulación del ecosistema
     */
    public Map<String, Object> ejecutar(List<Especie> especies, Habitat habitat, 
                                        Map<String, Object> parametros) {
        resultados = new HashMap<>();
        
        int anios = (Integer) parametros.getOrDefault("duracionSimulada", 50);
        double temperaturaInicial = parametros.containsKey("temperaturaInicial") ?
            ((Number) parametros.get("temperaturaInicial")).doubleValue() : 15.0;
        double precipitacionInicial = parametros.containsKey("precipitacionInicial") ?
            ((Number) parametros.get("precipitacionInicial")).doubleValue() : 1000.0;
        
        // Inicializar población
        Map<Integer, Double> poblaciones = new HashMap<>();
        Map<Integer, Double> salud = new HashMap<>();
        
        for (Especie especie : especies) {
            double poblacionInicial = 100 + ThreadLocalRandom.current().nextDouble(900);
            poblaciones.put(especie.getId(), poblacionInicial);
            salud.put(especie.getId(), 0.8 + ThreadLocalRandom.current().nextDouble(0.2));
        }
        
        // Datos históricos por año
        List<Map<String, Object>> historico = new ArrayList<>();
        
        for (int anio = 1; anio <= anios; anio++) {
            Map<String, Object> anioData = new HashMap<>();
            anioData.put("anio", anio);
            
            // Variaciones climáticas
            double temp = temperaturaInicial + 
                ThreadLocalRandom.current().nextDouble(-3.0, 3.0);
            double precip = precipitacionInicial + 
                ThreadLocalRandom.current().nextDouble(-200, 200);
            
            anioData.put("temperatura", Math.round(temp * 10) / 10.0);
            anioData.put("precipitacion", Math.round(precip));
            
            // Actualizar poblaciones
            Map<Integer, Double> nuevasPoblaciones = new HashMap<>();
            
            for (Especie especie : especies) {
                double poblacionActual = poblaciones.get(especie.getId());
                double saludActual = salud.get(especie.getId());
                
                // Factores de crecimiento y mortalidad
                double tasaReproduccion = especie.getTasaReproduccion() != null ?
                    especie.getTasaReproduccion().doubleValue() : 1.0;
                double impacto = especie.getImpactoEcologico() != null ?
                    especie.getImpactoEcologico().doubleValue() : 0.5;
                
                // Cálculo del cambio de población (modelo logístico)
                double capacidadCarga = 1000 * (1 + impacto * 0.5) * 
                    (1 + (especie.isEsProtegida() ? 0.3 : 0));
                
                double crecimiento = tasaReproduccion * poblacionActual * 
                    (1 - poblacionActual / capacidadCarga);
                
                // Factor de mortalidad por condiciones climáticas
                double temperaturaOptima = especie.getTemperaturaOptima() != null ?
                    especie.getTemperaturaOptima().doubleValue() : temperaturaInicial;
                double mortalidadClima = Math.abs(temp - temperaturaOptima) / 20.0;
                mortalidadClima = Math.min(mortalidadClima, 0.5);
                
                // Factor de humedad
                double humedadOptima = especie.getHumedadOptima() != null ?
                    especie.getHumedadOptima().doubleValue() : 60.0;
                double mortalidadHumedad = Math.abs(precip / 10 - humedadOptima) / 100.0;
                mortalidadHumedad = Math.min(mortalidadHumedad, 0.3);
                
                // Interacciones entre especies (depredación/competencia)
                double interaccion = calcularInteraccion(especie, especies, poblaciones);
                
                // Nueva población
                double nuevaPoblacion = poblacionActual + crecimiento - 
                    (poblacionActual * (mortalidadClima + mortalidadHumedad)) + interaccion;
                
                nuevaPoblacion = Math.max(0, nuevaPoblacion);
                nuevasPoblaciones.put(especie.getId(), nuevaPoblacion);
                
                // Actualizar salud
                double nuevaSalud = saludActual - mortalidadClima * 0.1 - mortalidadHumedad * 0.1 + 
                    (especie.isEsProtegida() ? 0.05 : 0);
                nuevaSalud = Math.max(0.1, Math.min(1.0, nuevaSalud));
                salud.put(especie.getId(), nuevaSalud);
            }
            
            // Guardar datos del año
            anioData.put("poblaciones", new HashMap<>(nuevasPoblaciones));
            anioData.put("salud", new HashMap<>(salud));
            historico.add(anioData);
            
            // Actualizar poblaciones para el siguiente año
            poblaciones = nuevasPoblaciones;
        }
        
        resultados.put("historico", historico);
        resultados.put("poblacionFinal", poblaciones);
        resultados.put("saludFinal", salud);
        resultados.put("aniosSimulados", anios);
        
        return resultados;
    }
    
    /**
     * Calcula la interacción entre especies
     */
    private double calcularInteraccion(Especie especie, List<Especie> todas, 
                                      Map<Integer, Double> poblaciones) {
        double interaccion = 0.0;
        
        // Depredación
        if ("carnivoro".equals(especie.getDieta())) {
            // Buscar presas (herbívoros)
            for (Especie otra : todas) {
                if (otra.getId() != especie.getId() &&
                    "herbivoro".equals(otra.getDieta())) {
                    double presas = poblaciones.getOrDefault(otra.getId(), 0.0);
                    double impacto = especie.getImpactoEcologico() != null ?
                        especie.getImpactoEcologico().doubleValue() : 0.5;
                    interaccion += presas * 0.001 * impacto;
                }
            }
        }
        
        // Competencia entre herbívoros
        if ("herbivoro".equals(especie.getDieta())) {
            for (Especie otra : todas) {
                if (otra.getId() != especie.getId() && 
                    "herbivoro".equals(otra.getDieta())) {
                    double competidores = poblaciones.getOrDefault(otra.getId(), 0.0);
                    interaccion -= competidores * 0.0005;
                }
            }
        }
        
        return interaccion;
    }
    
    /**
     * Calcula métricas de la simulación
     */
    public Map<String, Object> calcularMetricas(Map<String, Object> resultados) {
        Map<String, Object> metricas = new HashMap<>();
        
        List<Map<String, Object>> historico = 
            (List<Map<String, Object>>) resultados.get("historico");
        Map<Integer, Double> poblacionFinal = 
            (Map<Integer, Double>) resultados.get("poblacionFinal");
        
        // Calcular biodiversidad (Simpson)
        double sumaP2 = 0.0;
        double totalPoblacion = 0.0;
        
        for (double poblacion : poblacionFinal.values()) {
            totalPoblacion += poblacion;
        }
        
        for (double poblacion : poblacionFinal.values()) {
            double proporcion = poblacion / totalPoblacion;
            sumaP2 += proporcion * proporcion;
        }
        
        double biodiversidadSimpson = 1 - sumaP2;
        metricas.put("biodiversidadSimpson", 
            Math.round(biodiversidadSimpson * 1000) / 1000.0);
        
        // Estabilidad del ecosistema (variación de poblaciones)
        double variacionTotal = 0.0;
        int totalAnios = historico.size();
        
        for (int anio = 1; anio < totalAnios; anio++) {
            Map<String, Object> anioActual = historico.get(anio);
            Map<String, Object> anioAnterior = historico.get(anio - 1);
            
            Map<Integer, Double> pobActual = 
                (Map<Integer, Double>) anioActual.get("poblaciones");
            Map<Integer, Double> pobAnterior = 
                (Map<Integer, Double>) anioAnterior.get("poblaciones");
            
            for (int especieId : pobActual.keySet()) {
                double actual = pobActual.getOrDefault(especieId, 0.0);
                double anterior = pobAnterior.getOrDefault(especieId, 0.0);
                variacionTotal += Math.abs(actual - anterior) / (anterior + 1);
            }
        }
        
        double estabilidad = 1.0 / (1 + variacionTotal / (totalAnios * poblacionFinal.size()));
        metricas.put("estabilidad", Math.round(estabilidad * 1000) / 1000.0);
        
        return metricas;
    }
    
    /**
     * Calcula puntuación de sostenibilidad
     */
    public int calcularSostenibilidad(Map<String, Object> resultados) {
        Map<String, Object> metricas = calcularMetricas(resultados);
        
        double biodiversidad = (double) metricas.get("biodiversidadSimpson");
        double estabilidad = (double) metricas.get("estabilidad");
        
        // Puntuación de sostenibilidad (0-100)
        double puntuacion = (biodiversidad * 50) + (estabilidad * 50);
        return (int) Math.round(Math.min(100, puntuacion));
    }
    
    /**
     * Calcula puntuación de biodiversidad
     */
    public int calcularBiodiversidad(Map<String, Object> resultados) {
        Map<String, Object> metricas = calcularMetricas(resultados);
        double biodiversidad = (double) metricas.get("biodiversidadSimpson");
        
        return (int) Math.round(Math.min(100, biodiversidad * 100));
    }
    
    /**
     * Estima el riesgo del ecosistema
     */
    public String estimarRiesgo(Map<String, Object> resultados) {
        int sostenibilidad = calcularSostenibilidad(resultados);
        int biodiversidad = calcularBiodiversidad(resultados);
        
        double promedio = (sostenibilidad + biodiversidad) / 2.0;
        
        if (promedio >= 70) {
            return "bajo";
        } else if (promedio >= 50) {
            return "medio";
        } else if (promedio >= 30) {
            return "alto";
        } else {
            return "critico";
        }
    }
}