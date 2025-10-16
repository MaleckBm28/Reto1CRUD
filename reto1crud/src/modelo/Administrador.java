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
public class Administrador extends Perfil {

    private String cuentaCorriente;

    public Administrador() {
    }

    public Administrador(String email, String contrasena, String codigoUsuario, String nombreUsuario,
            int telefono, String nombre, String apellido, String cuentaCorriente) {
        super(email, contrasena, codigoUsuario, nombreUsuario, telefono, nombre, apellido);
        this.cuentaCorriente = cuentaCorriente;
    }

    public String getCuentaCorriente() {
        return cuentaCorriente;
    }

    public void setCuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    @Override
    public String toString() {
        return "Administrador{" + "cuentaCorriente=" + cuentaCorriente + '}';
    }

}
