// src/main/java/com/ecosimulator/model/Simulacion.java
package maricomputa.ecosimulator.modelo.entidad;

import java.sql.Timestamp;

public class Simulacion {
    private int id;
    private String uuid;
    private String nombre;
    private String descripcion;
    private int usuarioId;
    private Integer habitatId;
    private String parametrosConfiguracion; // JSON
    private String estado;
    private int progreso;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Integer duracionSegundos;
    private Integer duracionSimulada;
    private String resultadosGenerales; // JSON
    private String metricasCalculadas; // JSON
    private String analisisIa; // JSON
    private String promptIa;
    private String respuestaIa;
    private String modeloIa;
    private Integer puntuacionSostenibilidad;
    private Integer puntuacionBiodiversidad;
    private String riesgoEstimado;
    private boolean esPublica;
    private String hashCompartir;
    private String etiquetas; // JSON
    private String notasUsuario;
    private Timestamp creadoEn;
    private Timestamp actualizadoEn;
    
    // Getters y Setters
    // ... (todos los campos)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getHabitatId() {
        return habitatId;
    }

    public void setHabitatId(Integer habitatId) {
        this.habitatId = habitatId;
    }

    public String getParametrosConfiguracion() {
        return parametrosConfiguracion;
    }

    public void setParametrosConfiguracion(String parametrosConfiguracion) {
        this.parametrosConfiguracion = parametrosConfiguracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public Integer getDuracionSimulada() {
        return duracionSimulada;
    }

    public void setDuracionSimulada(Integer duracionSimulada) {
        this.duracionSimulada = duracionSimulada;
    }

    public String getResultadosGenerales() {
        return resultadosGenerales;
    }

    public void setResultadosGenerales(String resultadosGenerales) {
        this.resultadosGenerales = resultadosGenerales;
    }

    public String getMetricasCalculadas() {
        return metricasCalculadas;
    }

    public void setMetricasCalculadas(String metricasCalculadas) {
        this.metricasCalculadas = metricasCalculadas;
    }

    public String getAnalisisIa() {
        return analisisIa;
    }

    public void setAnalisisIa(String analisisIa) {
        this.analisisIa = analisisIa;
    }

    public String getPromptIa() {
        return promptIa;
    }

    public void setPromptIa(String promptIa) {
        this.promptIa = promptIa;
    }

    public String getRespuestaIa() {
        return respuestaIa;
    }

    public void setRespuestaIa(String respuestaIa) {
        this.respuestaIa = respuestaIa;
    }

    public String getModeloIa() {
        return modeloIa;
    }

    public void setModeloIa(String modeloIa) {
        this.modeloIa = modeloIa;
    }

    public Integer getPuntuacionSostenibilidad() {
        return puntuacionSostenibilidad;
    }

    public void setPuntuacionSostenibilidad(Integer puntuacionSostenibilidad) {
        this.puntuacionSostenibilidad = puntuacionSostenibilidad;
    }

    public Integer getPuntuacionBiodiversidad() {
        return puntuacionBiodiversidad;
    }

    public void setPuntuacionBiodiversidad(Integer puntuacionBiodiversidad) {
        this.puntuacionBiodiversidad = puntuacionBiodiversidad;
    }

    public String getRiesgoEstimado() {
        return riesgoEstimado;
    }

    public void setRiesgoEstimado(String riesgoEstimado) {
        this.riesgoEstimado = riesgoEstimado;
    }

    public boolean isEsPublica() {
        return esPublica;
    }

    public void setEsPublica(boolean esPublica) {
        this.esPublica = esPublica;
    }

    public String getHashCompartir() {
        return hashCompartir;
    }

    public void setHashCompartir(String hashCompartir) {
        this.hashCompartir = hashCompartir;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getNotasUsuario() {
        return notasUsuario;
    }

    public void setNotasUsuario(String notasUsuario) {
        this.notasUsuario = notasUsuario;
    }

    public Timestamp getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Timestamp creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Timestamp getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(Timestamp actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    // Métodos de utilidad para las plantillas Mustache
    // (Mustache no soporta helpers tipo {{#equals a b}}, así que se
    // precalculan aquí los valores derivados que las vistas necesitan)

    public boolean isPendiente() {
        return "pendiente".equals(estado);
    }

    public boolean isEjecutando() {
        return "ejecutando".equals(estado);
    }

    public boolean isCompletada() {
        return "completada".equals(estado);
    }

    public boolean isFallida() {
        return "fallida".equals(estado);
    }

    public boolean isCancelada() {
        return "cancelada".equals(estado);
    }

    public String getEstadoIcono() {
        if (estado == null) return "";
        switch (estado) {
            case "pendiente": return "⏳ Pendiente";
            case "configurando": return "⚙️ Configurando";
            case "ejecutando": return "🔄 Ejecutando";
            case "completada": return "✅ Completada";
            case "fallida": return "❌ Fallida";
            case "cancelada": return "⏹️ Cancelada";
            default: return estado;
        }
    }

    public String getRiesgoIcono() {
        if (riesgoEstimado == null) return "--";
        switch (riesgoEstimado) {
            case "bajo": return "🟢 Bajo";
            case "medio": return "🟡 Medio";
            case "alto": return "🟠 Alto";
            case "critico": return "🔴 Crítico";
            default: return riesgoEstimado;
        }
    }

    private static String claseParaPuntuacion(Integer puntuacion) {
        if (puntuacion == null) return "";
        if (puntuacion > 70) return "healthy";
        if (puntuacion > 50) return "warning";
        if (puntuacion > 30) return "danger";
        return "critical";
    }

    public String getSostenibilidadClase() {
        return claseParaPuntuacion(puntuacionSostenibilidad);
    }

    public String getBiodiversidadClase() {
        return claseParaPuntuacion(puntuacionBiodiversidad);
    }
}