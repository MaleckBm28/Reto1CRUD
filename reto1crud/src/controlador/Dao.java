package controlador;

import modelo.Usuario;

public interface Dao {

    boolean autenticar(String gmail, String contrasena);

    Usuario obtenerUsuarioPorEmail(String gmail);
<<<<<<< HEAD
=======
    
     boolean actualizarUsuario(Usuario usuario);
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
}
