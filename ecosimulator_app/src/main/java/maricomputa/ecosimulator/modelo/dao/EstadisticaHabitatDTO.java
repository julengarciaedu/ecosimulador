// src/main/java/com/ecosimulator/dto/EstadisticaHabitatDTO.java
package maricomputa.ecosimulator.modelo.dao;

public class EstadisticaHabitatDTO {
    private String tipoBioma;
    private int total;
    private double avgBiodiversidad;
    private double maxBiodiversidad;
    private double minBiodiversidad;
    
    // Constructor, getters y setters

    public EstadisticaHabitatDTO(String tipoBioma, int total, double avgBiodiversidad, double maxBiodiversidad, double minBiodiversidad) {
        this.tipoBioma = tipoBioma;
        this.total = total;
        this.avgBiodiversidad = avgBiodiversidad;
        this.maxBiodiversidad = maxBiodiversidad;
        this.minBiodiversidad = minBiodiversidad;
    }

    public EstadisticaHabitatDTO() {
    }
    
    

    public String getTipoBioma() {
        return tipoBioma;
    }

    public void setTipoBioma(String tipoBioma) {
        this.tipoBioma = tipoBioma;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getAvgBiodiversidad() {
        return avgBiodiversidad;
    }

    public void setAvgBiodiversidad(double avgBiodiversidad) {
        this.avgBiodiversidad = avgBiodiversidad;
    }

    public double getMaxBiodiversidad() {
        return maxBiodiversidad;
    }

    public void setMaxBiodiversidad(double maxBiodiversidad) {
        this.maxBiodiversidad = maxBiodiversidad;
    }

    public double getMinBiodiversidad() {
        return minBiodiversidad;
    }

    public void setMinBiodiversidad(double minBiodiversidad) {
        this.minBiodiversidad = minBiodiversidad;
    }
    
    
}