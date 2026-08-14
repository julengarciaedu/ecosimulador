// src/main/java/com/ecosimulator/dao/HabitatDAO.java
package maricomputa.ecosimulator.modelo.dao;

import maricomputa.ecosimulator.modelo.entidad.Habitat;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitatDao extends BaseDao {
    
    /**
     * Busca un hábitat por su ID
     */
    public Habitat findById(int id) throws SQLException {
        String sql = "SELECT * FROM habitats WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToHabitat(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca hábitats por nombre (búsqueda parcial)
     */
    public List<Habitat> findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM habitats WHERE nombre LIKE ? ORDER BY nombre";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca hábitats por tipo de bioma
     */
    public List<Habitat> findByTipoBioma(String tipoBioma) throws SQLException {
        String sql = "SELECT * FROM habitats WHERE tipo_bioma = ? ORDER BY nombre";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, tipoBioma);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca hábitats por país
     */
    public List<Habitat> findByPais(String pais) throws SQLException {
        String sql = "SELECT * FROM habitats WHERE pais = ? ORDER BY nombre";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, pais);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca hábitats por estatus de protección
     */
    public List<Habitat> findByEstatusProteccion(String estatus) throws SQLException {
        String sql = "SELECT * FROM habitats WHERE estatus_proteccion = ? ORDER BY nombre";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estatus);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Obtiene todos los hábitats
     */
    public List<Habitat> findAll() throws SQLException {
        String sql = "SELECT * FROM habitats ORDER BY nombre";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Obtiene hábitats con alta biodiversidad (índice > 0.7)
     */
    public List<Habitat> findAltaBiodiversidad() throws SQLException {
        String sql = "SELECT * FROM habitats WHERE biodiversidad_index > 0.7 ORDER BY biodiversidad_index DESC";
        List<Habitat> habitats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                habitats.add(mapResultSetToHabitat(rs));
            }
            return habitats;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Inserta un nuevo hábitat
     */
    public void insert(Habitat habitat) throws SQLException {
        String sql = "INSERT INTO habitats (" +
                     "nombre, tipo_bioma, pais, region, latitud, longitud, " +
                     "altitud_min, altitud_max, area_total, temperatura_promedio, " +
                     "precipitacion_anual_promedio, humedad_promedio, ph_suelo_promedio, " +
                     "calidad_agua, biodiversidad_index, productividad_primaria, " +
                     "amenazas, medidas_conservacion, estatus_proteccion, " +
                     "descripcion, caracteristicas_unicas, imagen_portada, " +
                     "creado_por, fecha_monitoreo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, habitat.getNombre());
            ps.setString(2, habitat.getTipoBioma());
            ps.setString(3, habitat.getPais());
            ps.setString(4, habitat.getRegion());
            ps.setObject(5, habitat.getLatitud());
            ps.setObject(6, habitat.getLongitud());
            ps.setObject(7, habitat.getAltitudMin());
            ps.setObject(8, habitat.getAltitudMax());
            ps.setObject(9, habitat.getAreaTotal());
            ps.setObject(10, habitat.getTemperaturaPromedio());
            ps.setObject(11, habitat.getPrecipitacionAnualPromedio());
            ps.setObject(12, habitat.getHumedadPromedio());
            ps.setObject(13, habitat.getPhSueloPromedio());
            ps.setString(14, habitat.getCalidadAgua());
            ps.setObject(15, habitat.getBiodiversidadIndex());
            ps.setObject(16, habitat.getProductividadPrimaria());
            ps.setString(17, habitat.getAmenazas());
            ps.setString(18, habitat.getMedidasConservacion());
            ps.setString(19, habitat.getEstatusProteccion());
            ps.setString(20, habitat.getDescripcion());
            ps.setString(21, habitat.getCaracteristicasUnicas());
            ps.setString(22, habitat.getImagenPortada());
            ps.setObject(23, habitat.getCreadoPor());
            ps.setObject(24, habitat.getFechaMonitoreo());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                habitat.setId(rs.getInt(1));
            }
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Actualiza un hábitat existente
     */
    public void update(Habitat habitat) throws SQLException {
        String sql = "UPDATE habitats SET " +
                     "nombre = ?, tipo_bioma = ?, pais = ?, region = ?, " +
                     "latitud = ?, longitud = ?, altitud_min = ?, altitud_max = ?, " +
                     "area_total = ?, temperatura_promedio = ?, " +
                     "precipitacion_anual_promedio = ?, humedad_promedio = ?, " +
                     "ph_suelo_promedio = ?, calidad_agua = ?, " +
                     "biodiversidad_index = ?, productividad_primaria = ?, " +
                     "amenazas = ?, medidas_conservacion = ?, estatus_proteccion = ?, " +
                     "descripcion = ?, caracteristicas_unicas = ?, imagen_portada = ?, " +
                     "creado_por = ?, fecha_monitoreo = ?, " +
                     "actualizado_en = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, habitat.getNombre());
            ps.setString(2, habitat.getTipoBioma());
            ps.setString(3, habitat.getPais());
            ps.setString(4, habitat.getRegion());
            ps.setObject(5, habitat.getLatitud());
            ps.setObject(6, habitat.getLongitud());
            ps.setObject(7, habitat.getAltitudMin());
            ps.setObject(8, habitat.getAltitudMax());
            ps.setObject(9, habitat.getAreaTotal());
            ps.setObject(10, habitat.getTemperaturaPromedio());
            ps.setObject(11, habitat.getPrecipitacionAnualPromedio());
            ps.setObject(12, habitat.getHumedadPromedio());
            ps.setObject(13, habitat.getPhSueloPromedio());
            ps.setString(14, habitat.getCalidadAgua());
            ps.setObject(15, habitat.getBiodiversidadIndex());
            ps.setObject(16, habitat.getProductividadPrimaria());
            ps.setString(17, habitat.getAmenazas());
            ps.setString(18, habitat.getMedidasConservacion());
            ps.setString(19, habitat.getEstatusProteccion());
            ps.setString(20, habitat.getDescripcion());
            ps.setString(21, habitat.getCaracteristicasUnicas());
            ps.setString(22, habitat.getImagenPortada());
            ps.setObject(23, habitat.getCreadoPor());
            ps.setObject(24, habitat.getFechaMonitoreo());
            ps.setInt(25, habitat.getId());
            
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Elimina un hábitat por su ID
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM habitats WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Cuenta el total de hábitats
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM habitats";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Obtiene estadísticas de biodiversidad por tipo de bioma
     */
    public List<Habitat> getEstadisticasPorBioma() throws SQLException {
        String sql = "SELECT tipo_bioma, " +
                     "COUNT(*) as total, " +
                     "AVG(biodiversidad_index) as avg_biodiversidad, " +
                     "MAX(biodiversidad_index) as max_biodiversidad, " +
                     "MIN(biodiversidad_index) as min_biodiversidad " +
                     "FROM habitats " +
                     "GROUP BY tipo_bioma " +
                     "ORDER BY avg_biodiversidad DESC";
        
        List<Habitat> estadisticas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Habitat habitat = new Habitat();
                habitat.setTipoBioma(rs.getString("tipo_bioma"));
                // Nota: Estos campos no están en el modelo, pero puedes agregarlos
                // o usar un DTO específico para estadísticas
                estadisticas.add(habitat);
            }
            return estadisticas;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Habitat
     */
    private Habitat mapResultSetToHabitat(ResultSet rs) throws SQLException {
        Habitat habitat = new Habitat();
        
        habitat.setId(rs.getInt("id"));
        habitat.setNombre(rs.getString("nombre"));
        habitat.setTipoBioma(rs.getString("tipo_bioma"));
        habitat.setPais(rs.getString("pais"));
        habitat.setRegion(rs.getString("region"));
        habitat.setLatitud(rs.getObject("latitud", BigDecimal.class));
        habitat.setLongitud(rs.getObject("longitud", BigDecimal.class));
        habitat.setAltitudMin(rs.getObject("altitud_min", Integer.class));
        habitat.setAltitudMax(rs.getObject("altitud_max", Integer.class));
        habitat.setAreaTotal(rs.getObject("area_total", BigDecimal.class));
        habitat.setTemperaturaPromedio(rs.getObject("temperatura_promedio", BigDecimal.class));
        habitat.setPrecipitacionAnualPromedio(rs.getObject("precipitacion_anual_promedio", Integer.class));
        habitat.setHumedadPromedio(rs.getObject("humedad_promedio", BigDecimal.class));
        habitat.setPhSueloPromedio(rs.getObject("ph_suelo_promedio", BigDecimal.class));
        habitat.setCalidadAgua(rs.getString("calidad_agua"));
        habitat.setBiodiversidadIndex(rs.getObject("biodiversidad_index", BigDecimal.class));
        habitat.setProductividadPrimaria(rs.getObject("productividad_primaria", BigDecimal.class));
        habitat.setAmenazas(rs.getString("amenazas"));
        habitat.setMedidasConservacion(rs.getString("medidas_conservacion"));
        habitat.setEstatusProteccion(rs.getString("estatus_proteccion"));
        habitat.setDescripcion(rs.getString("descripcion"));
        habitat.setCaracteristicasUnicas(rs.getString("caracteristicas_unicas"));
        habitat.setImagenPortada(rs.getString("imagen_portada"));
        habitat.setCreadoPor(rs.getObject("creado_por", Integer.class));
        habitat.setCreadoEn(rs.getTimestamp("creado_en"));
        habitat.setActualizadoEn(rs.getTimestamp("actualizado_en"));
        habitat.setFechaMonitoreo(rs.getObject("fecha_monitoreo", Date.class));
        
        return habitat;
    }
}