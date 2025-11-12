package dao;

import modelo.Usuario;

/**
 * Interfaz DAO para operaciones sobre la tabla de usuarios.
 */
public interface Dao {

    // 🔹 Verifica si el usuario existe (login)
    boolean autenticar(String email, String contrasena);

    // 🔹 Obtiene un usuario completo por su email
    Usuario obtenerUsuarioPorEmail(String email);

    // 🔹 Inserta un nuevo usuario (registro)
    boolean registrarUsuario(Usuario usuario);

    // 🔹 Actualiza los datos de un usuario (panel usuario)
    boolean actualizarUsuario(Usuario usuario);

    // 🔹 Elimina un usuario por su código (panel admin)
    boolean eliminarUsuario(String codigoUsuario);
}
