package modelo;

public class Usuario extends Perfil {

    private String genero;
    private long nTarjeta;

    public Usuario() {}

    public Usuario(int idPerfil, String email, String contrasena, String codigoUsuario,
                   String nombreUsuario, int telefono, String nombre, String apellido,
                   String genero, long nTarjeta) {
        super(idPerfil, email, contrasena, codigoUsuario, nombreUsuario, telefono, nombre, apellido);
        this.genero = genero;
        this.nTarjeta = nTarjeta;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public long getnTarjeta() {
        return nTarjeta;
    }

    public void setnTarjeta(long nTarjeta) {
        this.nTarjeta = nTarjeta;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email=" + getEmail() +
                ", nombre=" + getNombre() +
                ", genero=" + genero +
                ", nTarjeta=" + nTarjeta +
                ", idPerfil=" + getIdPerfil() +
                '}';
    }
}
