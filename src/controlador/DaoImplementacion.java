package controlador;

import conexion.ConexionBD;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoImplementacion implements Dao {

    // ===================== CONSULTAS SQL =====================

    // Login (verifica usuario y contraseña)
    private static final String SQL_LOGIN =
            "SELECT codigo_usuario FROM perfil WHERE email=? AND contrasena=?";

    // Obtener usuario por email
    private static final String SQL_FIND_BY_EMAIL =
            "SELECT codigo_usuario, nombre, apellido, email, contrasena, nombre_usuario, telefono " +
            "FROM perfil WHERE email=?";

    // Obtener usuario por código (perfil + datos usuario)
    private static final String GET_USUARIO =
            "SELECT p.codigo_usuario, p.email, p.contrasena, p.nombre_usuario, p.telefono, " +
            "p.nombre, p.apellido, u.genero, u.n_tarjeta " +
            "FROM perfil p JOIN usuarios u ON p.codigo_usuario = u.codigo_usuario " +
            "WHERE p.codigo_usuario = ?";

    // Actualizar datos
    private static final String UPDATE_PERFIL =
            "UPDATE perfil SET nombre=?, apellido=?, email=?, contrasena=?, nombre_usuario=?, telefono=? " +
            "WHERE codigo_usuario=?";
    private static final String UPDATE_USUARIOS =
            "UPDATE usuarios SET genero=?, n_tarjeta=? WHERE codigo_usuario=?";

    // Eliminar usuario
    private static final String DELETE_USUARIOS =
            "DELETE FROM usuarios WHERE codigo_usuario=?";
    private static final String DELETE_PERFIL =
            "DELETE FROM perfil WHERE codigo_usuario=?";

    // Listar usuarios
    private static final String LISTAR_USUARIOS =
            "SELECT p.codigo_usuario, p.email, p.contrasena, p.nombre_usuario, p.telefono, " +
            "p.nombre, p.apellido, u.genero, u.n_tarjeta " +
            "FROM perfil p JOIN usuarios u ON p.codigo_usuario = u.codigo_usuario";

    // ===================== MÉTODOS =====================

    @Override
    public boolean autenticar(String correo, String contrasena) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // True si existe usuario
            }

        } catch (SQLException e) {
            System.out.println("Error en autenticar(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String correo) {
        Usuario u = null;
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setCodigoUsuario(rs.getString("codigo_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setEmail(rs.getString("email"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setTelefono(rs.getInt("telefono"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en obtenerUsuarioPorEmail(): " + e.getMessage());
        }
        return u;
    }

    @Override
    public Usuario obtenerUsuarioPorCodigo(String codigo) {
        Usuario usuario = null;
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(GET_USUARIO)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setCodigoUsuario(rs.getString("codigo_usuario"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setTelefono(rs.getInt("telefono"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setGenero(rs.getString("genero"));
                    usuario.setnTarjeta(rs.getLong("n_tarjeta"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public void modificarUsuario(Usuario u) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps1 = con.prepareStatement(UPDATE_PERFIL);
             PreparedStatement ps2 = con.prepareStatement(UPDATE_USUARIOS)) {

            // Actualiza tabla perfil
            ps1.setString(1, u.getNombre());
            ps1.setString(2, u.getApellido());
            ps1.setString(3, u.getEmail());
            ps1.setString(4, u.getContrasena());
            ps1.setString(5, u.getNombreUsuario());
            ps1.setInt(6, u.getTelefono());
            ps1.setString(7, u.getCodigoUsuario());
            ps1.executeUpdate();

            // Actualiza tabla usuarios
            ps2.setString(1, u.getGenero());
            ps2.setLong(2, u.getnTarjeta());
            ps2.setString(3, u.getCodigoUsuario());
            ps2.executeUpdate();

            System.out.println("Usuario modificado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error en modificarUsuario(): " + e.getMessage());
        }
    }

    @Override
    public void borrarUsuario(String codigo) {
        try (Connection con = ConexionBD.open();
             PreparedStatement ps1 = con.prepareStatement(DELETE_USUARIOS);
             PreparedStatement ps2 = con.prepareStatement(DELETE_PERFIL)) {

            ps1.setString(1, codigo);
            ps1.executeUpdate();

            ps2.setString(1, codigo);
            ps2.executeUpdate();

            System.out.println("Usuario eliminado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al borrar usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.open();
             PreparedStatement ps = con.prepareStatement(LISTAR_USUARIOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
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
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println(" Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
