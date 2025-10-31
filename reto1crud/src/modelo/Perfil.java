/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author 2dam
 */
public abstract class Perfil {

    private String email;
    private String contrasena;
    private String codigoUsuario;
    private String nombreUsuario;
    private int telefono;
    private String nombre;
    private String apellido;
    private int idPerfil;

    public Perfil() {
    }

    public Perfil(int idPerfil, String email, String contrasena, String codigoUsuario,
            String nombreUsuario, int telefono, String nombre, String apellido) {
        this.idPerfil = idPerfil;
        this.email = email;
        this.contrasena = contrasena;
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Perfil{" + "email=" + email + ", contrasena=" + contrasena + ", codigoUsuario=" + codigoUsuario + ", nombreUsuario=" + nombreUsuario + ", telefono=" + telefono + ", nombre=" + nombre + ", apellido=" + apellido + '}';
    }

}
