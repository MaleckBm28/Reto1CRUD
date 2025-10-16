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
public class Usuario extends Perfil {

    private String genero;
    private long nTarjeta;

    public Usuario() {}

    public Usuario(String email, String contrasena, String codigoUsuario, String nombreUsuario,
                   int telefono, String nombre, String apellido, String genero, long nTarjeta) {
        super(email, contrasena, codigoUsuario, nombreUsuario, telefono, nombre, apellido);
        this.genero = genero;
        this.nTarjeta = nTarjeta;
    }

    public String getGenero() {
        return genero;
    }

    public long getnTarjeta() {
        return nTarjeta;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setnTarjeta(long nTarjeta) {
        this.nTarjeta = nTarjeta;
    }

    @Override
    public String toString() {
        return "Usuario{" + "genero=" + genero + ", nTarjeta=" + nTarjeta + '}';
    }

   
}

