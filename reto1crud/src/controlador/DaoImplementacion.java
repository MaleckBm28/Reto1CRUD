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
        "SELECT 1 FROM usuario WHERE correo=? AND contrasena=?";

    // ✅ Obtiene todos los datos del usuario
    private static final String SQL_FIND_BY_EMAIL =
        "SELECT id_usuario, nombre, apellido, correo, contrasena, id_perfil " +
        "FROM usuario WHERE correo=?";

    @Override
    public boolean autenticar(String correo, String contrasena) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si encontró un usuario
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en autenticar(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String correo) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setCodigoUsuario(String.valueOf(rs.getInt("id_usuario"))); // puedes mapearlo aquí
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setEmail(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena"));
                    // Si quisieras más adelante, podrías buscar id_perfil → nombre_perfil con otra consulta
                    return u;
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en obtenerUsuarioPorEmail(): " + e.getMessage());
        }
        return null;
    }
}
