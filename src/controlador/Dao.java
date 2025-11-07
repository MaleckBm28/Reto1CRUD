package controlador;

import java.util.List;
import modelo.Usuario;

public interface Dao {
    boolean autenticar(String correo, String contrasena);
    Usuario obtenerUsuarioPorEmail(String correo);
    Usuario obtenerUsuarioPorCodigo(String codigo);
    void borrarUsuario(String codigo);
    List<Usuario> listarUsuarios();
    void modificarUsuario(Usuario u);
}
