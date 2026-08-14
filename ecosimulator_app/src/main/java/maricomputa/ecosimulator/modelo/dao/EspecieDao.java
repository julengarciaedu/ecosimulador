// src/main/java/com/ecosimulator/dao/EspecieDAO.java
package maricomputa.ecosimulator.modelo.dao;

import maricomputa.ecosimulator.modelo.entidad.Especie;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecieDao extends BaseDao {
    
    public Especie findById(int id) throws SQLException {
        String sql = "SELECT * FROM especies WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEspecie(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    public List<Especie> findByHabitat(String habitat) throws SQLException {
        String sql = "SELECT * FROM especies WHERE habitat_preferido LIKE ? ORDER BY nombre_comun";
        List<Especie> especies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + habitat + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                especies.add(mapResultSetToEspecie(rs));
            }
            return especies;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    public List<Especie> findAll() throws SQLException {
        String sql = "SELECT * FROM especies ORDER BY nombre_comun";
        List<Especie> especies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                especies.add(mapResultSetToEspecie(rs));
            }
            return especies;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    public List<Especie> findByEstadoConservacion(String estado) throws SQLException {
        String sql = "SELECT * FROM especies WHERE estado_conservacion = ? ORDER BY nombre_comun";
        List<Especie> especies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                especies.add(mapResultSetToEspecie(rs));
            }
            return especies;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    public void insert(Especie especie) throws SQLException {
        String sql = "INSERT INTO especies (nombre_cientifico, nombre_comun, reino, familia, " +
                     "habitat_preferido, estado_conservacion, dieta, descripcion, caracteristicas, " +
                     "longevidad_promedio, tamano_promedio, temperatura_optima, humedad_optima, " +
                     "impacto_ecologico, tasa_reproduccion, es_endemica, es_invasora, es_protegida, " +
                     "imagen_url, creado_por, fuente_datos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, especie.getNombreCientifico());
            ps.setString(2, especie.getNombreComun());
            ps.setString(3, especie.getReino());
            ps.setString(4, especie.getFamilia());
            ps.setString(5, especie.getHabitatPreferido());
            ps.setString(6, especie.getEstadoConservacion());
            ps.setString(7, especie.getDieta());
            ps.setString(8, especie.getDescripcion());
            ps.setString(9, especie.getCaracteristicas());
            ps.setObject(10, especie.getLongevidadPromedio());
            ps.setObject(11, especie.getTamanoPromedio());
            ps.setObject(12, especie.getTemperaturaOptima());
            ps.setObject(13, especie.getHumedadOptima());
            ps.setObject(14, especie.getImpactoEcologico());
            ps.setObject(15, especie.getTasaReproduccion());
            ps.setBoolean(16, especie.isEsEndemica());
            ps.setBoolean(17, especie.isEsInvasora());
            ps.setBoolean(18, especie.isEsProtegida());
            ps.setString(19, especie.getImagenUrl());
            ps.setObject(20, especie.getCreadoPor());
            ps.setString(21, especie.getFuenteDatos());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                especie.setId(rs.getInt(1));
            }
        } finally {
            closeResources(ps, conn);
        }
    }
    
    public void update(Especie especie) throws SQLException {
        String sql = "UPDATE especies SET nombre_cientifico=?, nombre_comun=?, reino=?, familia=?, " +
                     "habitat_preferido=?, estado_conservacion=?, dieta=?, descripcion=?, " +
                     "caracteristicas=?, longevidad_promedio=?, tamano_promedio=?, " +
                     "temperatura_optima=?, humedad_optima=?, impacto_ecologico=?, " +
                     "tasa_reproduccion=?, es_endemica=?, es_invasora=?, es_protegida=?, " +
                     "imagen_url=?, fuente_datos=?, actualizado_en=CURRENT_TIMESTAMP " +
                     "WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, especie.getNombreCientifico());
            ps.setString(2, especie.getNombreComun());
            ps.setString(3, especie.getReino());
            ps.setString(4, especie.getFamilia());
            ps.setString(5, especie.getHabitatPreferido());
            ps.setString(6, especie.getEstadoConservacion());
            ps.setString(7, especie.getDieta());
            ps.setString(8, especie.getDescripcion());
            ps.setString(9, especie.getCaracteristicas());
            ps.setObject(10, especie.getLongevidadPromedio());
            ps.setObject(11, especie.getTamanoPromedio());
            ps.setObject(12, especie.getTemperaturaOptima());
            ps.setObject(13, especie.getHumedadOptima());
            ps.setObject(14, especie.getImpactoEcologico());
            ps.setObject(15, especie.getTasaReproduccion());
            ps.setBoolean(16, especie.isEsEndemica());
            ps.setBoolean(17, especie.isEsInvasora());
            ps.setBoolean(18, especie.isEsProtegida());
            ps.setString(19, especie.getImagenUrl());
            ps.setString(20, especie.getFuenteDatos());
            ps.setInt(21, especie.getId());
            
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM especies WHERE id = ?";
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
    
    private Especie mapResultSetToEspecie(ResultSet rs) throws SQLException {
        Especie especie = new Especie();
        especie.setId(rs.getInt("id"));
        especie.setNombreCientifico(rs.getString("nombre_cientifico"));
        especie.setNombreComun(rs.getString("nombre_comun"));
        especie.setReino(rs.getString("reino"));
        especie.setFamilia(rs.getString("familia"));
        especie.setHabitatPreferido(rs.getString("habitat_preferido"));
        especie.setEstadoConservacion(rs.getString("estado_conservacion"));
        especie.setDieta(rs.getString("dieta"));
        especie.setDescripcion(rs.getString("descripcion"));
        especie.setCaracteristicas(rs.getString("caracteristicas"));
        especie.setLongevidadPromedio(rs.getObject("longevidad_promedio", Integer.class));
        especie.setTamanoPromedio(rs.getObject("tamano_promedio", BigDecimal.class));
        especie.setTemperaturaOptima(rs.getObject("temperatura_optima", BigDecimal.class));
        especie.setHumedadOptima(rs.getObject("humedad_optima", BigDecimal.class));
        especie.setImpactoEcologico(rs.getObject("impacto_ecologico", BigDecimal.class));
        especie.setTasaReproduccion(rs.getObject("tasa_reproduccion", BigDecimal.class));
        especie.setEsEndemica(rs.getBoolean("es_endemica"));
        especie.setEsInvasora(rs.getBoolean("es_invasora"));
        especie.setEsProtegida(rs.getBoolean("es_protegida"));
        especie.setImagenUrl(rs.getString("imagen_url"));
        especie.setCreadoPor(rs.getObject("creado_por", Integer.class));
        especie.setFuenteDatos(rs.getString("fuente_datos"));
        especie.setCreadoEn(rs.getTimestamp("creado_en"));
        especie.setActualizadoEn(rs.getTimestamp("actualizado_en"));
        return especie;
    }
}