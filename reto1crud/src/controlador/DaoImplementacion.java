package controlador;

import conexion.ConexionBD;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoImplementacion implements Dao {

    // ✅ Verifica login (correo + contraseña)
    private static final String SQL_LOGIN =
        "SELECT 1 FROM perfil WHERE email=? AND contrasena=?";

    // ✅ Obtiene todos los datos del usuario (perfil + datos específicos)
    private static final String SQL_FIND_BY_EMAIL =
        "SELECT p.codigo_usuario, p.email, p.contrasena, p.nombre_usuario, p.telefono, " +
        "p.nombre, p.apellido, u.genero, u.n_tarjeta " +
        "FROM perfil p " +
        "JOIN usuarios u ON p.codigo_usuario = u.codigo_usuario " +
        "WHERE p.email=?";

    @Override
    public boolean autenticar(String email, String contrasena) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {

            ps.setString(1, email);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si encontró el usuario
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en autenticar(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setCodigoUsuario(rs.getString("codigo_usuario"));
                    u.setEmail(rs.getString("email"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setTelefono(rs.getInt("telefono"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setGenero(rs.getString("genero"));
                    u.setnTarjeta(rs.getLong("n_tarjeta"));
                    return u;
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en obtenerUsuarioPorEmail(): " + e.getMessage());
        }
        return null;
    }
}
