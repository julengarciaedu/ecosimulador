// src/main/java/com/ecosimulator/model/Usuario.java
package maricomputa.ecosimulator.modelo.entidad;

import java.sql.Timestamp;
import maricomputa.ecosimulator.utils.PasswordUtil;

public class Usuario {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String rol;
    private Timestamp creadoEn;
    private Timestamp ultimoLogin;
    private String avatar;
    private String bio;
    private boolean activo;
    
    // ✅ NUEVOS CAMPOS PARA RECUPERACIÓN
    private String tokenReset;
    private Timestamp tokenExpira;
    
    // Getters y Setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return this.passwordHash; }
    public void setPasswordHash(String passwordhash) { this.passwordHash = passwordhash; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Timestamp getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Timestamp creadoEn) { this.creadoEn = creadoEn; }
    
    public Timestamp getUltimoLogin() { return ultimoLogin; }
    public void setUltimoLogin(Timestamp ultimoLogin) { this.ultimoLogin = ultimoLogin; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    // ✅ NUEVOS GETTERS Y SETTERS
    public String getTokenReset() { return tokenReset; }
    public void setTokenReset(String tokenReset) { this.tokenReset = tokenReset; }
    
    public Timestamp getTokenExpira() { return tokenExpira; }
    public void setTokenExpira(Timestamp tokenExpira) { this.tokenExpira = tokenExpira; }
    
    // Método para verificar si el token ha expirado
    public boolean isTokenExpirado() {
        if (tokenExpira == null) {
            return true;
        }
        return tokenExpira.before(new Timestamp(System.currentTimeMillis()));
    }
    
    // Método para limpiar el token (después de usarlo)
    public void limpiarToken() {
        this.tokenReset = null;
        this.tokenExpira = null;
    }
    
    // Métodos de utilidad existentes
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(rol);
    }
    
    public boolean isEcoUsuario() {
        return "ecousuario".equalsIgnoreCase(rol);
    }
    
    public boolean verifyPassword(String pass){
        return PasswordUtil.verifyPassword(pass, this.passwordHash);
    }
}