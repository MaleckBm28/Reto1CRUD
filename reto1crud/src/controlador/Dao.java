package controlador;

import modelo.Usuario;

public interface Dao {

    boolean autenticar(String gmail, String contrasena);

    Usuario obtenerUsuarioPorEmail(String gmail);
}
