// src/main/java/com/ecosimulator/model/Especie.java
package maricomputa.ecosimulator.modelo.entidad;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Especie {
    private int id;
    private String nombreCientifico;
    private String nombreComun;
    private String reino;
    private String familia;
    private String habitatPreferido;
    private String estadoConservacion;
    private String dieta;
    private String descripcion;
    private String caracteristicas;
    private Integer longevidadPromedio;
    private BigDecimal tamanoPromedio;
    private BigDecimal temperaturaOptima;
    private BigDecimal humedadOptima;
    private BigDecimal impactoEcologico;
    private BigDecimal tasaReproduccion;
    private boolean esEndemica;
    private boolean esInvasora;
    private boolean esProtegida;
    private String imagenUrl;
    private Integer creadoPor;
    private String fuenteDatos;
    private Timestamp creadoEn;
    private Timestamp actualizadoEn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public void setNombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    public String getReino() {
        return reino;
    }

    public void setReino(String reino) {
        this.reino = reino;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getHabitatPreferido() {
        return habitatPreferido;
    }

    public void setHabitatPreferido(String habitatPreferido) {
        this.habitatPreferido = habitatPreferido;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public Integer getLongevidadPromedio() {
        return longevidadPromedio;
    }

    public void setLongevidadPromedio(Integer longevidadPromedio) {
        this.longevidadPromedio = longevidadPromedio;
    }

    public BigDecimal getTamanoPromedio() {
        return tamanoPromedio;
    }

    public void setTamanoPromedio(BigDecimal tamanoPromedio) {
        this.tamanoPromedio = tamanoPromedio;
    }

    public BigDecimal getTemperaturaOptima() {
        return temperaturaOptima;
    }

    public void setTemperaturaOptima(BigDecimal temperaturaOptima) {
        this.temperaturaOptima = temperaturaOptima;
    }

    public BigDecimal getHumedadOptima() {
        return humedadOptima;
    }

    public void setHumedadOptima(BigDecimal humedadOptima) {
        this.humedadOptima = humedadOptima;
    }

    public BigDecimal getImpactoEcologico() {
        return impactoEcologico;
    }

    public void setImpactoEcologico(BigDecimal impactoEcologico) {
        this.impactoEcologico = impactoEcologico;
    }

    public BigDecimal getTasaReproduccion() {
        return tasaReproduccion;
    }

    public void setTasaReproduccion(BigDecimal tasaReproduccion) {
        this.tasaReproduccion = tasaReproduccion;
    }

    public boolean isEsEndemica() {
        return esEndemica;
    }

    public void setEsEndemica(boolean esEndemica) {
        this.esEndemica = esEndemica;
    }

    public boolean isEsInvasora() {
        return esInvasora;
    }

    public void setEsInvasora(boolean esInvasora) {
        this.esInvasora = esInvasora;
    }

    public boolean isEsProtegida() {
        return esProtegida;
    }

    public void setEsProtegida(boolean esProtegida) {
        this.esProtegida = esProtegida;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getFuenteDatos() {
        return fuenteDatos;
    }

    public void setFuenteDatos(String fuenteDatos) {
        this.fuenteDatos = fuenteDatos;
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
    
    // Getters y Setters
    // ... (todos los campos)
    
    // Método para obtener impacto como porcentaje
    public double getImpactoPorcentaje() {
        return impactoEcologico != null ? impactoEcologico.doubleValue() * 100 : 0;
    }
}