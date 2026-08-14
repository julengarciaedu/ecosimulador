// src/main/java/com/ecosimulator/model/Habitat.java
package maricomputa.ecosimulator.modelo.entidad;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Habitat {
    private int id;
    private String nombre;
    private String tipoBioma;
    private String pais;
    private String region;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer altitudMin;
    private Integer altitudMax;
    private BigDecimal areaTotal;
    private BigDecimal temperaturaPromedio;
    private Integer precipitacionAnualPromedio;
    private BigDecimal humedadPromedio;
    private BigDecimal phSueloPromedio;
    private String calidadAgua;
    private BigDecimal biodiversidadIndex;
    private BigDecimal productividadPrimaria;
    private String amenazas;
    private String medidasConservacion;
    private String estatusProteccion;
    private String descripcion;
    private String caracteristicasUnicas;
    private String imagenPortada;
    private Integer creadoPor;
    private Timestamp creadoEn;
    private Timestamp actualizadoEn;
    private Date fechaMonitoreo;
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipoBioma() { return tipoBioma; }
    public void setTipoBioma(String tipoBioma) { this.tipoBioma = tipoBioma; }
    
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }
    
    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
    
    public Integer getAltitudMin() { return altitudMin; }
    public void setAltitudMin(Integer altitudMin) { this.altitudMin = altitudMin; }
    
    public Integer getAltitudMax() { return altitudMax; }
    public void setAltitudMax(Integer altitudMax) { this.altitudMax = altitudMax; }
    
    public BigDecimal getAreaTotal() { return areaTotal; }
    public void setAreaTotal(BigDecimal areaTotal) { this.areaTotal = areaTotal; }
    
    public BigDecimal getTemperaturaPromedio() { return temperaturaPromedio; }
    public void setTemperaturaPromedio(BigDecimal temperaturaPromedio) { 
        this.temperaturaPromedio = temperaturaPromedio; 
    }
    
    public Integer getPrecipitacionAnualPromedio() { return precipitacionAnualPromedio; }
    public void setPrecipitacionAnualPromedio(Integer precipitacionAnualPromedio) {
        this.precipitacionAnualPromedio = precipitacionAnualPromedio;
    }
    
    public BigDecimal getHumedadPromedio() { return humedadPromedio; }
    public void setHumedadPromedio(BigDecimal humedadPromedio) { 
        this.humedadPromedio = humedadPromedio; 
    }
    
    public BigDecimal getPhSueloPromedio() { return phSueloPromedio; }
    public void setPhSueloPromedio(BigDecimal phSueloPromedio) { 
        this.phSueloPromedio = phSueloPromedio; 
    }
    
    public String getCalidadAgua() { return calidadAgua; }
    public void setCalidadAgua(String calidadAgua) { this.calidadAgua = calidadAgua; }
    
    public BigDecimal getBiodiversidadIndex() { return biodiversidadIndex; }
    public void setBiodiversidadIndex(BigDecimal biodiversidadIndex) { 
        this.biodiversidadIndex = biodiversidadIndex; 
    }
    
    public BigDecimal getProductividadPrimaria() { return productividadPrimaria; }
    public void setProductividadPrimaria(BigDecimal productividadPrimaria) {
        this.productividadPrimaria = productividadPrimaria;
    }
    
    public String getAmenazas() { return amenazas; }
    public void setAmenazas(String amenazas) { this.amenazas = amenazas; }
    
    public String getMedidasConservacion() { return medidasConservacion; }
    public void setMedidasConservacion(String medidasConservacion) {
        this.medidasConservacion = medidasConservacion;
    }
    
    public String getEstatusProteccion() { return estatusProteccion; }
    public void setEstatusProteccion(String estatusProteccion) { 
        this.estatusProteccion = estatusProteccion; 
    }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getCaracteristicasUnicas() { return caracteristicasUnicas; }
    public void setCaracteristicasUnicas(String caracteristicasUnicas) {
        this.caracteristicasUnicas = caracteristicasUnicas;
    }
    
    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }
    
    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }
    
    public Timestamp getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Timestamp creadoEn) { this.creadoEn = creadoEn; }
    
    public Timestamp getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Timestamp actualizadoEn) { this.actualizadoEn = actualizadoEn; }
    
    public Date getFechaMonitoreo() { return fechaMonitoreo; }
    public void setFechaMonitoreo(Date fechaMonitoreo) { this.fechaMonitoreo = fechaMonitoreo; }
    
    // Métodos de utilidad
    public String getTipoBiomaDisplay() {
        if (tipoBioma == null) return "Desconocido";
        return tipoBioma.replace("_", " ").toUpperCase();
    }
    
    public String getEstatusProteccionDisplay() {
        if (estatusProteccion == null) return "Sin protección";
        return estatusProteccion.replace("_", " ").toUpperCase();
    }
    
    public String getCalidadAguaDisplay() {
        if (calidadAgua == null) return "Desconocida";
        return calidadAgua.toUpperCase();
    }

    public String getBiomaIcono() {
        if (tipoBioma == null) return "🏞️";
        switch (tipoBioma) {
            case "bosque_tropical": return "🌴";
            case "bosque_templado": return "🌳";
            case "bosque_boreal": return "🌲";
            case "pradera_tropical":
            case "pradera_templada": return "🌾";
            case "humedal": return "🌊";
            case "desierto_calido":
            case "desierto_frio": return "🏜️";
            case "tundra_artica": return "❄️";
            case "sabana": return "🦒";
            case "manglar": return "🌿";
            case "marino_costero": return "🌊";
            case "arrecife_coral": return "🐠";
            default: return "🏞️";
        }
    }
}