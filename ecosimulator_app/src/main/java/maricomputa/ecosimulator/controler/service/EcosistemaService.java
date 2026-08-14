// src/main/java/com/ecosimulator/service/EcosistemaService.java
package maricomputa.ecosimulator.controler.service;

import maricomputa.ecosimulator.modelo.dao.*;
import maricomputa.ecosimulator.modelo.entidad.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio principal de gestión del ecosistema
 * Maneja especies, hábitats, relaciones ecológicas y estadísticas
 */
public class EcosistemaService {
    
    private EspecieDao especieDAO;
    private HabitatDao habitatDAO;
    private SimulacionDao simulacionDAO;
    private ObjectMapper objectMapper;
    
    public EcosistemaService() {
        this.especieDAO = new EspecieDao();
        this.habitatDAO = new HabitatDao();
        this.simulacionDAO = new SimulacionDao();
        this.objectMapper = new ObjectMapper();
    }
    
    // ============================================
    // GESTIÓN DE ESPECIES
    // ============================================
    
    /**
     * Obtiene todas las especies
     */
    public List<Especie> getTodasLasEspecies() throws SQLException {
        return especieDAO.findAll();
    }
    
    /**
     * Obtiene especies por estado de conservación
     */
    public List<Especie> getEspeciesPorEstado(String estado) throws SQLException {
        return especieDAO.findByEstadoConservacion(estado);
    }
    
    /**
     * Obtiene especies por hábitat
     */
    public List<Especie> getEspeciesPorHabitat(String habitat) throws SQLException {
        return especieDAO.findByHabitat(habitat);
    }
    
    /**
     * Obtiene especies endémicas
     */
    public List<Especie> getEspeciesEndemicas() throws SQLException {
        List<Especie> todas = especieDAO.findAll();
        return todas.stream()
            .filter(Especie::isEsEndemica)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene especies protegidas
     */
    public List<Especie> getEspeciesProtegidas() throws SQLException {
        List<Especie> todas = especieDAO.findAll();
        return todas.stream()
            .filter(Especie::isEsProtegida)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene especies por tipo de dieta
     */
    public List<Especie> getEspeciesPorDieta(String dieta) throws SQLException {
        List<Especie> todas = especieDAO.findAll();
        return todas.stream()
            .filter(e -> dieta.equals(e.getDieta()))
            .collect(Collectors.toList());
    }
    
    /**
     * Crea una nueva especie
     */
    public Especie crearEspecie(Especie especie) throws SQLException {
        // Validar que no exista una especie con el mismo nombre científico
        List<Especie> existentes = especieDAO.findAll();
        boolean existe = existentes.stream()
            .anyMatch(e -> e.getNombreCientifico().equals(especie.getNombreCientifico()));
        
        if (existe) {
            throw new IllegalArgumentException("Ya existe una especie con ese nombre científico");
        }
        
        especieDAO.insert(especie);
        return especie;
    }
    
    /**
     * Actualiza una especie existente
     */
    public Especie actualizarEspecie(Especie especie) throws SQLException {
        Especie existente = especieDAO.findById(especie.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Especie no encontrada");
        }
        
        especieDAO.update(especie);
        return especie;
    }
    
    /**
     * Elimina una especie
     */
    public boolean eliminarEspecie(int especieId) throws SQLException {
        Especie existente = especieDAO.findById(especieId);
        if (existente == null) {
            return false;
        }
        
        especieDAO.delete(especieId);
        return true;
    }
    
    // ============================================
    // GESTIÓN DE HÁBITATS
    // ============================================
    
    /**
     * Obtiene todos los hábitats
     */
    public List<Habitat> getTodosLosHabitats() throws SQLException {
        return habitatDAO.findAll();
    }
    
    /**
     * Obtiene hábitats por tipo de bioma
     */
    public List<Habitat> getHabitatsPorBioma(String bioma) throws SQLException {
        return habitatDAO.findByTipoBioma(bioma);
    }
    
    /**
     * Obtiene hábitats por país
     */
    public List<Habitat> getHabitatsPorPais(String pais) throws SQLException {
        return habitatDAO.findByPais(pais);
    }
    
    /**
     * Obtiene hábitats protegidos
     */
    public List<Habitat> getHabitatsProtegidos() throws SQLException {
        List<Habitat> todos = habitatDAO.findAll();
        return todos.stream()
            .filter(h -> h.getEstatusProteccion() != null && 
                        !"sin_proteccion".equals(h.getEstatusProteccion()))
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene hábitats con alta biodiversidad
     */
    public List<Habitat> getHabitatsAltaBiodiversidad() throws SQLException {
        return habitatDAO.findAltaBiodiversidad();
    }
    
    /**
     * Crea un nuevo hábitat
     */
    public Habitat crearHabitat(Habitat habitat) throws SQLException {
        habitatDAO.insert(habitat);
        return habitat;
    }
    
    /**
     * Actualiza un hábitat existente
     */
    public Habitat actualizarHabitat(Habitat habitat) throws SQLException {
        Habitat existente = habitatDAO.findById(habitat.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Hábitat no encontrado");
        }
        
        habitatDAO.update(habitat);
        return habitat;
    }
    
    /**
     * Elimina un hábitat
     */
    public boolean eliminarHabitat(int habitatId) throws SQLException {
        Habitat existente = habitatDAO.findById(habitatId);
        if (existente == null) {
            return false;
        }
        
        habitatDAO.delete(habitatId);
        return true;
    }
    
    // ============================================
    // ANÁLISIS DEL ECOSISTEMA
    // ============================================
    
    /**
     * Obtiene estadísticas generales del ecosistema
     */
    public Map<String, Object> getEstadisticasEcosistema() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        List<Especie> especies = especieDAO.findAll();
        List<Habitat> habitats = habitatDAO.findAll();
        
        stats.put("totalEspecies", especies.size());
        stats.put("totalHabitats", habitats.size());
        
        // Especies por estado de conservación
        Map<String, Long> especiesPorEstado = especies.stream()
            .collect(Collectors.groupingBy(
                Especie::getEstadoConservacion,
                Collectors.counting()
            ));
        stats.put("especiesPorEstado", especiesPorEstado);
        
        // Especies por dieta
        Map<String, Long> especiesPorDieta = especies.stream()
            .collect(Collectors.groupingBy(
                Especie::getDieta,
                Collectors.counting()
            ));
        stats.put("especiesPorDieta", especiesPorDieta);
        
        // Hábitats por tipo de bioma
        Map<String, Long> habitatsPorBioma = habitats.stream()
            .collect(Collectors.groupingBy(
                Habitat::getTipoBioma,
                Collectors.counting()
            ));
        stats.put("habitatsPorBioma", habitatsPorBioma);
        
        // Promedio de biodiversidad
        OptionalDouble avgBiodiversidad = habitats.stream()
            .filter(h -> h.getBiodiversidadIndex() != null)
            .mapToDouble(h -> h.getBiodiversidadIndex().doubleValue())
            .average();
        stats.put("biodiversidadPromedio", avgBiodiversidad.orElse(0.0));
        
        // Especies endémicas
        long endemicas = especies.stream().filter(Especie::isEsEndemica).count();
        stats.put("especiesEndemicas", endemicas);
        
        // Especies protegidas
        long protegidas = especies.stream().filter(Especie::isEsProtegida).count();
        stats.put("especiesProtegidas", protegidas);
        
        // Especies invasoras
        long invasoras = especies.stream().filter(Especie::isEsInvasora).count();
        stats.put("especiesInvasoras", invasoras);
        
        return stats;
    }
    
    /**
     * Obtiene la cadena trófica simplificada de un hábitat
     */
    public Map<String, List<Especie>> getCadenaTrofica(int habitatId) throws SQLException {
        Habitat habitat = habitatDAO.findById(habitatId);
        if (habitat == null) {
            throw new IllegalArgumentException("Hábitat no encontrado");
        }
        
        // Obtener especies del hábitat
        List<Especie> especies = especieDAO.findByHabitat(habitat.getNombre());
        
        Map<String, List<Especie>> cadena = new HashMap<>();
        
        // Productores (autótrofos)
        List<Especie> productores = especies.stream()
            .filter(e -> "autotrofo".equals(e.getDieta()))
            .collect(Collectors.toList());
        cadena.put("productores", productores);
        
        // Consumidores primarios (herbívoros)
        List<Especie> consumidoresPrimarios = especies.stream()
            .filter(e -> "herbivoro".equals(e.getDieta()))
            .collect(Collectors.toList());
        cadena.put("consumidoresPrimarios", consumidoresPrimarios);
        
        // Consumidores secundarios (carnívoros, omnívoros)
        List<Especie> consumidoresSecundarios = especies.stream()
            .filter(e -> "carnivoro".equals(e.getDieta()) || "omnivoro".equals(e.getDieta()))
            .collect(Collectors.toList());
        cadena.put("consumidoresSecundarios", consumidoresSecundarios);
        
        // Descomponedores
        List<Especie> descomponedores = especies.stream()
            .filter(e -> "detritivoro".equals(e.getDieta()))
            .collect(Collectors.toList());
        cadena.put("descomponedores", descomponedores);
        
        return cadena;
    }
    
    /**
     * Calcula el índice de biodiversidad de un hábitat (Simpson)
     */
    public double calcularIndiceBiodiversidad(int habitatId) throws SQLException {
        Habitat habitat = habitatDAO.findById(habitatId);
        if (habitat == null) {
            throw new IllegalArgumentException("Hábitat no encontrado");
        }
        
        List<Especie> especies = especieDAO.findByHabitat(habitat.getNombre());
        if (especies.isEmpty()) {
            return 0.0;
        }
        
        // Asumir población uniforme para el cálculo
        int totalEspecies = especies.size();
        double sumaP2 = 0.0;
        
        for (Especie e : especies) {
            double proporcion = 1.0 / totalEspecies;
            sumaP2 += proporcion * proporcion;
        }
        
        return 1 - sumaP2;
    }
    
    /**
     * Evalúa la salud del ecosistema basado en múltiples factores
     */
    public Map<String, Object> evaluarSaludEcosistema(int habitatId) throws SQLException {
        Map<String, Object> evaluacion = new HashMap<>();
        
        Habitat habitat = habitatDAO.findById(habitatId);
        if (habitat == null) {
            throw new IllegalArgumentException("Hábitat no encontrado");
        }
        
        List<Especie> especies = especieDAO.findByHabitat(habitat.getNombre());
        
        // Factores de evaluación
        int totalEspecies = especies.size();
        long especiesProtegidas = especies.stream().filter(Especie::isEsProtegida).count();
        long especiesInvasoras = especies.stream().filter(Especie::isEsInvasora).count();
        long especiesAmenazadas = especies.stream()
            .filter(e -> e.getEstadoConservacion() != null)
            .filter(e -> List.of("CR", "EN", "VU").contains(e.getEstadoConservacion()))
            .count();
        
        // Índice de biodiversidad
        double biodiversidad = calcularIndiceBiodiversidad(habitatId);
        
        // Puntuación de salud (0-100)
        double puntuacion = 0.0;
        puntuacion += Math.min(30, totalEspecies * 2); // Diversidad de especies
        puntuacion += especiesProtegidas * 5; // Especies protegidas
        puntuacion -= especiesInvasoras * 10; // Especies invasoras (penalización)
        puntuacion -= especiesAmenazadas * 8; // Especies amenazadas (penalización)
        puntuacion += biodiversidad * 40; // Índice de biodiversidad
        
        // Ajustar a escala 0-100
        puntuacion = Math.max(0, Math.min(100, puntuacion));
        
        evaluacion.put("puntuacionSalud", puntuacion);
        evaluacion.put("nivelSalud", obtenerNivelSalud(puntuacion));
        evaluacion.put("totalEspecies", totalEspecies);
        evaluacion.put("especiesProtegidas", especiesProtegidas);
        evaluacion.put("especiesInvasoras", especiesInvasoras);
        evaluacion.put("especiesAmenazadas", especiesAmenazadas);
        evaluacion.put("biodiversidadSimpson", biodiversidad);
        
        // Recomendaciones
        List<String> recomendaciones = new ArrayList<>();
        if (especiesInvasoras > 0) {
            recomendaciones.add("Controlar especies invasoras: " + especiesInvasoras + " especies");
        }
        if (especiesAmenazadas > 0) {
            recomendaciones.add("Implementar programas de conservación para especies amenazadas");
        }
        if (puntuacion < 50) {
            recomendaciones.add("Se requiere intervención urgente para restaurar el ecosistema");
        }
        if (biodiversidad < 0.3) {
            recomendaciones.add("Baja biodiversidad. Considerar reintroducción de especies nativas");
        }
        evaluacion.put("recomendaciones", recomendaciones);
        
        return evaluacion;
    }
    
    private String obtenerNivelSalud(double puntuacion) {
        if (puntuacion >= 80) return "EXCELENTE";
        if (puntuacion >= 60) return "BUENO";
        if (puntuacion >= 40) return "REGULAR";
        if (puntuacion >= 20) return "MALO";
        return "CRÍTICO";
    }
    
    /**
     * Obtiene el impacto ecológico promedio de las especies en un hábitat
     */
    public double getImpactoEcologicoPromedio(int habitatId) throws SQLException {
        Habitat habitat = habitatDAO.findById(habitatId);
        if (habitat == null) {
            return 0.0;
        }
        
        List<Especie> especies = especieDAO.findByHabitat(habitat.getNombre());
        
        OptionalDouble promedio = especies.stream()
            .filter(e -> e.getImpactoEcologico() != null)
            .mapToDouble(e -> e.getImpactoEcologico().doubleValue())
            .average();
        
        return promedio.orElse(0.0);
    }
    
    // ============================================
    // RELACIONES ECOLÓGICAS
    // ============================================
    
    /**
     * Encuentra relaciones de depredación entre especies
     */
    public Map<String, List<String>> getRelacionesDepredacion(int habitatId) throws SQLException {
        Map<String, List<String>> relaciones = new HashMap<>();
        
        List<Especie> especies = especieDAO.findByHabitat(
            habitatDAO.findById(habitatId).getNombre()
        );
        
        // Depredadores (carnívoros y omnívoros)
        List<Especie> depredadores = especies.stream()
            .filter(e -> "carnivoro".equals(e.getDieta()) || "omnivoro".equals(e.getDieta()))
            .collect(Collectors.toList());
        
        // Presas (herbívoros y otros)
        List<Especie> presas = especies.stream()
            .filter(e -> "herbivoro".equals(e.getDieta()) || "insectivoro".equals(e.getDieta()))
            .collect(Collectors.toList());
        
        // Construir relaciones (simplificado)
        for (Especie depredador : depredadores) {
            List<String> presasDepredador = new ArrayList<>();
            for (Especie presa : presas) {
                if (Math.random() > 0.5) { // Simulación de relación
                    presasDepredador.add(presa.getNombreComun());
                }
            }
            relaciones.put(depredador.getNombreComun(), presasDepredador);
        }
        
        return relaciones;
    }
    
    /**
     * Encuentra relaciones de competencia entre especies
     */
    public List<Map<String, Object>> getRelacionesCompetencia(int habitatId) throws SQLException {
        List<Map<String, Object>> competencias = new ArrayList<>();
        
        List<Especie> especies = especieDAO.findByHabitat(
            habitatDAO.findById(habitatId).getNombre()
        );
        
        // Especies con la misma dieta compiten
        Map<String, List<Especie>> gruposDieta = especies.stream()
            .filter(e -> e.getDieta() != null && !"desconocido".equals(e.getDieta()))
            .collect(Collectors.groupingBy(Especie::getDieta));
        
        for (Map.Entry<String, List<Especie>> entry : gruposDieta.entrySet()) {
            List<Especie> grupo = entry.getValue();
            if (grupo.size() > 1) {
                Map<String, Object> competencia = new HashMap<>();
                competencia.put("dieta", entry.getKey());
                competencia.put("especies", grupo.stream()
                    .map(Especie::getNombreComun)
                    .collect(Collectors.toList()));
                competencia.put("nivelCompetencia", "MEDIA");
                competencias.add(competencia);
            }
        }
        
        return competencias;
    }
    
    // ============================================
    // MÉTRICAS Y REPORTES
    // ============================================
    
    /**
     * Genera un reporte completo del ecosistema
     */
    public Map<String, Object> generarReporteCompleto(int habitatId) throws SQLException {
        Map<String, Object> reporte = new HashMap<>();
        
        Habitat habitat = habitatDAO.findById(habitatId);
        if (habitat == null) {
            throw new IllegalArgumentException("Hábitat no encontrado");
        }
        
        reporte.put("habitat", habitat);
        reporte.put("estadisticas", getEstadisticasEcosistema());
        reporte.put("salud", evaluarSaludEcosistema(habitatId));
        reporte.put("cadenaTrofica", getCadenaTrofica(habitatId));
        reporte.put("biodiversidad", calcularIndiceBiodiversidad(habitatId));
        reporte.put("impactoPromedio", getImpactoEcologicoPromedio(habitatId));
        reporte.put("depredacion", getRelacionesDepredacion(habitatId));
        reporte.put("competencia", getRelacionesCompetencia(habitatId));
        reporte.put("fechaGeneracion", new Date());
        
        return reporte;
    }
    
    /**
     * Exporta datos del ecosistema en formato JSON
     */
    public String exportarDatosEcosistema(int habitatId) throws Exception {
        Map<String, Object> reporte = generarReporteCompleto(habitatId);
        return objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(reporte);
    }
    
    /**
     * Compara dos hábitats
     */
    public Map<String, Object> compararHabitats(int habitatId1, int habitatId2) throws SQLException {
        Map<String, Object> comparacion = new HashMap<>();
        
        Habitat h1 = habitatDAO.findById(habitatId1);
        Habitat h2 = habitatDAO.findById(habitatId2);
        
        if (h1 == null || h2 == null) {
            throw new IllegalArgumentException("Uno o ambos hábitats no existen");
        }
        
        List<Especie> especies1 = especieDAO.findByHabitat(h1.getNombre());
        List<Especie> especies2 = especieDAO.findByHabitat(h2.getNombre());
        
        // Comparar biodiversidad
        double bio1 = calcularIndiceBiodiversidad(habitatId1);
        double bio2 = calcularIndiceBiodiversidad(habitatId2);
        
        comparacion.put("habitat1", h1);
        comparacion.put("habitat2", h2);
        comparacion.put("biodiversidad1", bio1);
        comparacion.put("biodiversidad2", bio2);
        comparacion.put("especies1", especies1.size());
        comparacion.put("especies2", especies2.size());
        comparacion.put("diferenciaBiodiversidad", Math.abs(bio1 - bio2));
        comparacion.put("masBiodiverso", bio1 > bio2 ? h1.getNombre() : h2.getNombre());
        
        // Especies compartidas
        Set<String> nombres1 = especies1.stream()
            .map(Especie::getNombreCientifico)
            .collect(Collectors.toSet());
        Set<String> nombres2 = especies2.stream()
            .map(Especie::getNombreCientifico)
            .collect(Collectors.toSet());
        
        Set<String> compartidas = new HashSet<>(nombres1);
        compartidas.retainAll(nombres2);
        
        comparacion.put("especiesCompartidas", compartidas.size());
        comparacion.put("especiesUnicas1", nombres1.size() - compartidas.size());
        comparacion.put("especiesUnicas2", nombres2.size() - compartidas.size());
        
        return comparacion;
    }
}