/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package maricomputa.ecosimulator.modelo.entidad;

/**
 *
 * @author Julen Profe
 */
public class ErrorM {
    
    private String error;
    private String descripcion;
    private String codigo;

    public ErrorM(String error, String descripcion, String codigo) {
        this.error = error;
        this.descripcion = descripcion;
        this.codigo = codigo;
    }

    public ErrorM(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
 
    
    
}
