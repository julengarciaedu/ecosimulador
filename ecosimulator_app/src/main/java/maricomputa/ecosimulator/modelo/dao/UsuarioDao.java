// src/main/java/com/ecosimulator/dao/UsuarioDAO.java
package maricomputa.ecosimulator.modelo.dao;

import maricomputa.ecosimulator.modelo.entidad.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao extends BaseDao {

    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY username";
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            return usuarios;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

    
    public void insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, email, password_hash, rol, avatar, bio, activo, token_reset, token_expira) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getAvatar());
            ps.setString(6, usuario.getBio());
            ps.setBoolean(7, usuario.isActivo());
            ps.setString(8, usuario.getTokenReset());
            ps.setTimestamp(9, usuario.getTokenExpira());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        } finally {
            closeResources(ps, conn);
        }
    }

// ACTUALIZAR el método update
    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET username=?, email=?, password_hash=?, rol=?, "
                + "avatar=?, bio=?, activo=?, ultimo_login=?, "
                + "token_reset=?, token_expira=? "
                + "WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getAvatar());
            ps.setString(6, usuario.getBio());
            ps.setBoolean(7, usuario.isActivo());
            ps.setTimestamp(8, usuario.getUltimoLogin());
            ps.setString(9, usuario.getTokenReset());
            ps.setTimestamp(10, usuario.getTokenExpira());
            ps.setInt(11, usuario.getId());

            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }

// ✅ NUEVO MÉTODO: Buscar usuario por token de recuperación
    public Usuario findByTokenReset(String token) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE token_reset = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

// ✅ NUEVO MÉTODO: Buscar usuario por email
    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

// ✅ NUEVO MÉTODO: Limpiar token de un usuario
    public void limpiarToken(int usuarioId) throws SQLException {
        String sql = "UPDATE usuarios SET token_reset = NULL, token_expira = NULL WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } finally {
            closeResources(ps, conn);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
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

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        usuario.setRol(rs.getString("rol"));
        usuario.setCreadoEn(rs.getTimestamp("creado_en"));
        usuario.setUltimoLogin(rs.getTimestamp("ultimo_login"));
        usuario.setAvatar(rs.getString("avatar"));
        usuario.setBio(rs.getString("bio"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setTokenReset(rs.getString("token_reset"));
        usuario.setTokenExpira(rs.getTimestamp("token_expira"));

        return usuario;
    }
}
