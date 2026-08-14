// src/main/java/com/ecosimulator/dao/SimulacionDAO.java
package maricomputa.ecosimulator.modelo.dao;

import maricomputa.ecosimulator.modelo.entidad.Simulacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimulacionDao extends BaseDao {
    
    /**
     * Busca una simulación por su ID
     */
    public Simulacion findById(int id) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSimulacion(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca una simulación por su UUID
     */
    public Simulacion findByUuid(String uuid) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE uuid = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSimulacion(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca simulaciones por usuario
     */
    public List<Simulacion> findByUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE usuario_id = ? ORDER BY creado_en DESC";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca simulaciones por estado
     */
    public List<Simulacion> findByEstado(String estado) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE estado = ? ORDER BY creado_en DESC";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca simulaciones públicas
     */
    public List<Simulacion> findPublicas() throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE es_publica = true AND estado = 'completada' ORDER BY creado_en DESC";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Busca simulaciones por hábitat
     */
    public List<Simulacion> findByHabitat(int habitatId) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE habitat_id = ? ORDER BY creado_en DESC";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, habitatId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Obtiene todas las simulaciones
     */
    public List<Simulacion> findAll() throws SQLException {
        String sql = "SELECT * FROM simulaciones ORDER BY creado_en DESC";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Obtiene simulaciones completadas con puntuaciones altas
     */
    public List<Simulacion> findTopSimulaciones(int limit) throws SQLException {
        String sql = "SELECT * FROM simulaciones WHERE estado = 'completada' " +
                     "ORDER BY puntuacion_sostenibilidad DESC LIMIT ?";
        List<Simulacion> simulaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                simulaciones.add(mapResultSetToSimulacion(rs));
            }
            return simulaciones;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Inserta una nueva simulación
     */
    public void insert(Simulacion simulacion) throws SQLException {
        String sql = "INSERT INTO simulaciones (" +
                     "uuid, nombre, descripcion, usuario_id, habitat_id, " +
                     "parametros_configuracion, estado, progreso, " +
                     "fecha_inicio, fecha_fin, duracion_segundos, duracion_simulada, " +
                     "resultados_generales, metricas_calculadas, " +
                     "analisis_ia, prompt_ia, respuesta_ia, modelo_ia, " +
                     "puntuacion_sostenibilidad, puntuacion_biodiversidad, riesgo_estimado, " +
                     "es_publica, hash_compartir, etiquetas, notas_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, simulacion.getUuid());
            ps.setString(2, simulacion.getNombre());
            ps.setString(3, simulacion.getDescripcion());
            ps.setInt(4, simulacion.getUsuarioId());
            ps.setObject(5, simulacion.getHabitatId());
            ps.setString(6, simulacion.getParametrosConfiguracion());
            ps.setString(7, simulacion.getEstado());
            ps.setInt(8, simulacion.getProgreso());
            ps.setObject(9, simulacion.getFechaInicio());
            ps.setObject(10, simulacion.getFechaFin());
            ps.setObject(11, simulacion.getDuracionSegundos());
            ps.setObject(12, simulacion.getDuracionSimulada());
            ps.setString(13, simulacion.getResultadosGenerales());
            ps.setString(14, simulacion.getMetricasCalculadas());
            ps.setString(15, simulacion.getAnalisisIa());
            ps.setString(16, simulacion.getPromptIa());
            ps.setString(17, simulacion.getRespuestaIa());
            ps.setString(18, simulacion.getModeloIa());
            ps.setObject(19, simulacion.getPuntuacionSostenibilidad());
            ps.setObject(20, simulacion.getPuntuacionBiodiversidad());
            ps.setString(21, simulacion.getRiesgoEstimado());
            ps.setBoolean(22, simulacion.isEsPublica());
            ps.setString(23, simulacion.getHashCompartir());
            ps.setString(24, simulacion.getEtiquetas());
            ps.setString(25, simulacion.getNotasUsuario());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                simulacion.setId(rs.getInt(1));
            }
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Actualiza una simulación existente
     */
    public void update(Simulacion simulacion) throws SQLException {
        String sql = "UPDATE simulaciones SET " +
                     "nombre = ?, descripcion = ?, usuario_id = ?, habitat_id = ?, " +
                     "parametros_configuracion = ?, estado = ?, progreso = ?, " +
                     "fecha_inicio = ?, fecha_fin = ?, duracion_segundos = ?, " +
                     "duracion_simulada = ?, resultados_generales = ?, " +
                     "metricas_calculadas = ?, analisis_ia = ?, prompt_ia = ?, " +
                     "respuesta_ia = ?, modelo_ia = ?, " +
                     "puntuacion_sostenibilidad = ?, puntuacion_biodiversidad = ?, " +
                     "riesgo_estimado = ?, es_publica = ?, hash_compartir = ?, " +
                     "etiquetas = ?, notas_usuario = ?, " +
                     "actualizado_en = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, simulacion.getNombre());
            ps.setString(2, simulacion.getDescripcion());
            ps.setInt(3, simulacion.getUsuarioId());
            ps.setObject(4, simulacion.getHabitatId());
            ps.setString(5, simulacion.getParametrosConfiguracion());
            ps.setString(6, simulacion.getEstado());
            ps.setInt(7, simulacion.getProgreso());
            ps.setObject(8, simulacion.getFechaInicio());
            ps.setObject(9, simulacion.getFechaFin());
            ps.setObject(10, simulacion.getDuracionSegundos());
            ps.setObject(11, simulacion.getDuracionSimulada());
            ps.setString(12, simulacion.getResultadosGenerales());
            ps.setString(13, simulacion.getMetricasCalculadas());
            ps.setString(14, simulacion.getAnalisisIa());
            ps.setString(15, simulacion.getPromptIa());
            ps.setString(16, simulacion.getRespuestaIa());
            ps.setString(17, simulacion.getModeloIa());
            ps.setObject(18, simulacion.getPuntuacionSostenibilidad());
            ps.setObject(19, simulacion.getPuntuacionBiodiversidad());
            ps.setString(20, simulacion.getRiesgoEstimado());
            ps.setBoolean(21, simulacion.isEsPublica());
            ps.setString(22, simulacion.getHashCompartir());
            ps.setString(23, simulacion.getEtiquetas());
            ps.setString(24, simulacion.getNotasUsuario());
            ps.setInt(25, simulacion.getId());
            
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Actualiza el progreso de una simulación
     */
    public void updateProgreso(int id, int progreso) throws SQLException {
        String sql = "UPDATE simulaciones SET progreso = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, progreso);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Actualiza el estado de una simulación
     */
    public void updateEstado(int id, String estado) throws SQLException {
        String sql = "UPDATE simulaciones SET estado = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Marca una simulación como completada y guarda resultados
     */
    public void completarSimulacion(int id, String resultados, String metricas, 
                                   int sostenibilidad, int biodiversidad, String riesgo) 
                                   throws SQLException {
        String sql = "UPDATE simulaciones SET " +
                     "estado = 'completada', progreso = 100, " +
                     "fecha_fin = CURRENT_TIMESTAMP, " +
                     "resultados_generales = ?, metricas_calculadas = ?, " +
                     "puntuacion_sostenibilidad = ?, puntuacion_biodiversidad = ?, " +
                     "riesgo_estimado = ?, actualizado_en = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, resultados);
            ps.setString(2, metricas);
            ps.setInt(3, sostenibilidad);
            ps.setInt(4, biodiversidad);
            ps.setString(5, riesgo);
            ps.setInt(6, id);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Elimina una simulación
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM simulaciones WHERE id = ?";
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
     * Elimina simulaciones antiguas (por fecha)
     */
    public void deleteAntiguas(int dias) throws SQLException {
        String sql = "DELETE FROM simulaciones WHERE creado_en < DATE_SUB(NOW(), INTERVAL ? DAY) " +
                     "AND estado IN ('pendiente', 'configurando', 'fallida', 'cancelada')";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dias);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }
    
    /**
     * Cuenta simulaciones por estado
     */
    public int countByEstado(String estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM simulaciones WHERE estado = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
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
     * Obtiene estadísticas de simulaciones por usuario
     */
    public int countByUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM simulaciones WHERE usuario_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
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
     * Obtiene el promedio de sostenibilidad de un usuario
     */
    public double getPromedioSostenibilidadByUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT AVG(puntuacion_sostenibilidad) FROM simulaciones " +
                     "WHERE usuario_id = ? AND estado = 'completada'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Simulacion
     */
    private Simulacion mapResultSetToSimulacion(ResultSet rs) throws SQLException {
        Simulacion simulacion = new Simulacion();
        
        simulacion.setId(rs.getInt("id"));
        simulacion.setUuid(rs.getString("uuid"));
        simulacion.setNombre(rs.getString("nombre"));
        simulacion.setDescripcion(rs.getString("descripcion"));
        simulacion.setUsuarioId(rs.getInt("usuario_id"));
        simulacion.setHabitatId(rs.getObject("habitat_id", Integer.class));
        simulacion.setParametrosConfiguracion(rs.getString("parametros_configuracion"));
        simulacion.setEstado(rs.getString("estado"));
        simulacion.setProgreso(rs.getInt("progreso"));
        simulacion.setFechaInicio(rs.getTimestamp("fecha_inicio"));
        simulacion.setFechaFin(rs.getTimestamp("fecha_fin"));
        simulacion.setDuracionSegundos(rs.getObject("duracion_segundos", Integer.class));
        simulacion.setDuracionSimulada(rs.getObject("duracion_simulada", Integer.class));
        simulacion.setResultadosGenerales(rs.getString("resultados_generales"));
        simulacion.setMetricasCalculadas(rs.getString("metricas_calculadas"));
        simulacion.setAnalisisIa(rs.getString("analisis_ia"));
        simulacion.setPromptIa(rs.getString("prompt_ia"));
        simulacion.setRespuestaIa(rs.getString("respuesta_ia"));
        simulacion.setModeloIa(rs.getString("modelo_ia"));
        simulacion.setPuntuacionSostenibilidad(rs.getObject("puntuacion_sostenibilidad", Integer.class));
        simulacion.setPuntuacionBiodiversidad(rs.getObject("puntuacion_biodiversidad", Integer.class));
        simulacion.setRiesgoEstimado(rs.getString("riesgo_estimado"));
        simulacion.setEsPublica(rs.getBoolean("es_publica"));
        simulacion.setHashCompartir(rs.getString("hash_compartir"));
        simulacion.setEtiquetas(rs.getString("etiquetas"));
        simulacion.setNotasUsuario(rs.getString("notas_usuario"));
        simulacion.setCreadoEn(rs.getTimestamp("creado_en"));
        simulacion.setActualizadoEn(rs.getTimestamp("actualizado_en"));
        
        return simulacion;
    }
}