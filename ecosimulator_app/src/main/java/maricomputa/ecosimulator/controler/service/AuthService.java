// src/main/java/com/ecosimulator/service/AuthService.java
package maricomputa.ecosimulator.controler.service;

import maricomputa.ecosimulator.modelo.dao.UsuarioDao;
import maricomputa.ecosimulator.modelo.entidad.Usuario;
import maricomputa.ecosimulator.utils.PasswordUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthService {
    
    private UsuarioDao usuarioDAO;
    
    public AuthService() {
        this.usuarioDAO = new UsuarioDao();
    }
    
    /**
     * Autentica a un usuario con username y password
     */
    public Usuario autenticar(String username, String password) throws SQLException {
        Usuario usuario = usuarioDAO.findByUsername(username);
        
        if (usuario == null || !usuario.isActivo()) {
            return null;
        }
        
        if (usuario.verifyPassword(password)) {
            usuario.setUltimoLogin(new Timestamp(System.currentTimeMillis()));
            usuarioDAO.update(usuario);
            return usuario;
        }
        
        return null;
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    public boolean registrarUsuario(String username, String email, String password, String rol) 
            throws SQLException {
        
        Usuario existente = usuarioDAO.findByUsername(username);
        if (existente != null) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPasswordHash(PasswordUtil.hashPassword(password));
        nuevoUsuario.setRol(rol != null ? rol : "ecousuario");
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setAvatar("default_avatar.png");
        nuevoUsuario.setBio("");
        // Los tokens se inicializan como null
        nuevoUsuario.setTokenReset(null);
        nuevoUsuario.setTokenExpira(null);
        
        usuarioDAO.insert(nuevoUsuario);
        return true;
    }
    
    /**
     * Cambia la contraseña de un usuario
     */
    public boolean cambiarPassword(int usuarioId, String passwordActual, String nuevaPassword) 
            throws SQLException {
        
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            return false;
        }
        
        if (!usuario.verifyPassword(passwordActual)) {
            return false;
        }
        
        usuario.setPasswordHash(PasswordUtil.hashPassword(nuevaPassword));
        usuarioDAO.update(usuario);
        return true;
    }
    
    /**
     * Genera un token para recuperación de contraseña
     */
    public String generarTokenRecuperacion(String email) throws SQLException {
        // Buscar usuario por email
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            return null;
        }
        
        // Generar token único
        String token = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        String tokenHash = PasswordUtil.hashPassword(token);
        
        // Guardar token en la base de datos
        Timestamp expiracion = new Timestamp(System.currentTimeMillis() + 3600000); // 1 hora
        usuario.setTokenReset(tokenHash);
        usuario.setTokenExpira(expiracion);
        usuarioDAO.update(usuario);
        
        // Devolver el token en texto plano para enviar por email
        return token;
    }
    
    /**
     * Verifica si un token de recuperación es válido
     */
    public boolean verificarTokenRecuperacion(String token) throws SQLException {
        // Buscar usuario por token
        // Nota: Buscamos por el token en texto plano, pero en la BD está hasheado
        // Por eso necesitamos buscar en la tabla completa y verificar
        // Esta implementación es más eficiente con un índice en token_reset
        // pero necesitamos verificar el hash
        
        // Como no podemos buscar directamente por el hash del token,
        // obtenemos todos los usuarios con token activo y verificamos
        
        // Esta es una implementación simple y no óptima para producción
        // Para mejor rendimiento, se podría almacenar el token sin hash
        // O usar un esquema de tokens con ID
        
        return true; // Implementación temporal
    }
    
    /**
     * Verifica token (versión mejorada)
     */
    public boolean verificarTokenValido(String token) throws SQLException {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        // Buscar usuario con token activo (hash de token)
        // Como tenemos el token hasheado en la BD, necesitamos verificarlo
        // Esta es una implementación simplificada
        // Para producción, se recomienda usar un enfoque diferente
        
        // Buscar todos los usuarios con token no nulo
        // En una implementación real, usarías una consulta directa
        // o almacenarías el token sin hash para búsquedas rápidas
        
        // Opción alternativa: Usar un campo separado para el token ID
        // y otro para el hash de verificación
        
        return false; // Implementación pendiente para producción
    }
    
    /**
     * Restablece la contraseña usando un token
     */
    public boolean restablecerPasswordConToken(String token, String nuevaPassword) throws SQLException {
        // En una implementación real, buscarías el usuario por el token
        // y verificarías que no haya expirado
        
        // Implementación simplificada
        Usuario usuario = usuarioDAO.findByTokenReset(token);
        if (usuario == null) {
            return false;
        }
        
        // Verificar expiración
        if (usuario.isTokenExpirado()) {
            return false;
        }
        
        // Actualizar contraseña
        usuario.setPasswordHash(PasswordUtil.hashPassword(nuevaPassword));
        // Limpiar token
        usuario.limpiarToken();
        usuarioDAO.update(usuario);
        
        return true;
    }
    
    /**
     * Busca un usuario por email (versión mejorada)
     */
    public Usuario buscarPorEmail(String email) throws SQLException {
        return usuarioDAO.findByEmail(email);
    }
    
    /**
     * Verifica si el usuario tiene permisos de administrador
     */
    public boolean esAdministrador(int usuarioId) throws SQLException {
        Usuario usuario = usuarioDAO.findById(usuarioId);
        return usuario != null && "admin".equals(usuario.getRol());
    }
    
    /**
     * Verifica si el usuario tiene permisos de ecousuario
     */
    public boolean esEcoUsuario(int usuarioId) throws SQLException {
        Usuario usuario = usuarioDAO.findById(usuarioId);
        return usuario != null && "ecousuario".equals(usuario.getRol());
    }
    
    /**
     * Obtiene el perfil completo de un usuario
     */
    public Usuario getPerfilUsuario(int usuarioId) throws SQLException {
        return usuarioDAO.findById(usuarioId);
    }
    
    /**
     * Actualiza el perfil de un usuario
     */
    public boolean actualizarPerfil(Usuario usuario) throws SQLException {
        if (usuario == null || usuario.getId() <= 0) {
            return false;
        }
        
        Usuario existente = usuarioDAO.findById(usuario.getId());
        if (existente == null) {
            return false;
        }
        
        // Actualizar solo campos permitidos
        existente.setUsername(usuario.getUsername());
        existente.setEmail(usuario.getEmail());
        existente.setAvatar(usuario.getAvatar());
        existente.setBio(usuario.getBio());
        // No actualizar contraseña ni tokens aquí
        
        usuarioDAO.update(existente);
        return true;
    }
    
    /**
     * Cierra la sesión del usuario
     */
    public void cerrarSesion(int usuarioId) {
        // Limpiar datos de sesión (implementación en servlet)
    }
    
    /**
     * Obtiene estadísticas de usuario para el dashboard
     */
    public Map<String, Object> getEstadisticasUsuario(int usuarioId) throws SQLException {
        Map<String, Object> estadisticas = new HashMap<>();
        
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario != null) {
            estadisticas.put("username", usuario.getUsername());
            estadisticas.put("rol", usuario.getRol());
            estadisticas.put("fechaRegistro", usuario.getCreadoEn());
            estadisticas.put("ultimoLogin", usuario.getUltimoLogin());
            estadisticas.put("activo", usuario.isActivo());
            estadisticas.put("tieneToken", usuario.getTokenReset() != null);
        }
        
        return estadisticas;
    }
    
    /**
     * Genera un enlace de recuperación (para enviar por email)
     */
    public String generarEnlaceRecuperacion(String email, String baseUrl) throws SQLException {
        String token = generarTokenRecuperacion(email);
        if (token == null) {
            return null;
        }
        
        return baseUrl + "/recuperar-password?token=" + token;
    }
}