package dao;

import conexion.ConexionBD;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO con operaciones CRUD sobre la base de datos. Gestiona
 * tanto los datos de la tabla perfil como los de usuarios o administradores.
 */
public class DaoImplementacion implements Dao {

    // =====================================================
    // 🔹 CONSULTAS SQL
    // =====================================================
    private static final String SQL_LOGIN
            = "SELECT * FROM perfil WHERE email=? AND contrasena=?";

    private static final String SQL_SELECT_USUARIO
            = "SELECT p.*, u.genero, u.n_tarjeta FROM perfil p "
            + "JOIN usuarios u ON p.codigo_usuario = u.codigo_usuario "
            + "WHERE p.email=?";

    private static final String SQL_SELECT_ADMIN
            = "SELECT p.*, a.cuenta_corriente FROM perfil p "
            + "JOIN administrador a ON p.codigo_usuario = a.codigo_usuario "
            + "WHERE p.email=?";

    private static final String SQL_INSERT_PERFIL
            = "INSERT INTO perfil (codigo_usuario, email, contrasena, nombre_usuario, telefono, nombre, apellido) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_USUARIO
            = "INSERT INTO usuarios (codigo_usuario, genero, n_tarjeta) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_PERFIL
            = "UPDATE perfil SET email=?, contrasena=?, nombre_usuario=?, telefono=?, nombre=?, apellido=? "
            + "WHERE codigo_usuario=?";

    private static final String SQL_UPDATE_USUARIOS
            = "UPDATE usuarios SET genero=?, n_tarjeta=? WHERE codigo_usuario=?";

    private static final String SQL_DELETE
            = "DELETE FROM perfil WHERE codigo_usuario=?";

    private static final String SQL_ALL_EMAILS
            = "SELECT email FROM perfil WHERE codigo_usuario LIKE 'C%' ORDER BY email";

    private static final String SQL_USUARIO_EXISTE
            = "SELECT codigo_usuario FROM usuarios WHERE codigo_usuario = (SELECT codigo_usuario FROM perfil WHERE email=?)";

    private static final String SQL_ADMIN_EXISTE
            = "SELECT codigo_usuario FROM administrador WHERE codigo_usuario = (SELECT codigo_usuario FROM perfil WHERE email=?)";

    // =====================================================
    // 🔹 LOGIN
    // =====================================================
    @Override
    public boolean autenticar(String email, String contrasena) {
        boolean autenticado = false;
        try (Connection con = ConexionBD.open();
                PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {
            ps.setString(1, email);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            autenticado = rs.next();
        } catch (SQLException e) {
            System.out.println("⚠️ Error en autenticar(): " + e.getMessage());
        }
        return autenticado;
    }

    // =====================================================
    // 🔹 OBTENER USUARIO COMPLETO (perfil + usuario/admin)
    // =====================================================
    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = null;

        try (Connection con = ConexionBD.open()) {

            boolean esAdmin = false;

            // 🔹 Comprobamos si el correo pertenece a un administrador
            try (PreparedStatement psAdmin = con.prepareStatement(SQL_ADMIN_EXISTE)) {
                psAdmin.setString(1, email);
                try (ResultSet rsAdmin = psAdmin.executeQuery()) {
                    if (rsAdmin.next()) {
                        esAdmin = true;
                    }
                }
            }

            String sql;
            if (esAdmin) {
                sql = SQL_SELECT_ADMIN;
            } else {
                sql = SQL_SELECT_USUARIO;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
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

                        if (esAdmin) {
                            usuario.setGenero("N/A");
                            usuario.setnTarjeta(0);
                            System.out.println("👑 Usuario ADMIN cargado: " + email);
                        } else {
                            usuario.setGenero(rs.getString("genero"));
                            usuario.setnTarjeta(rs.getLong("n_tarjeta"));
                            System.out.println("✅ Usuario cargado en PanelUsuario: " + email);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en obtenerUsuarioPorEmail(): " + e.getMessage());
        }

        return usuario;
    }

    // =====================================================
    // 🔹 REGISTRAR USUARIO
    // =====================================================
    @Override
    public boolean registrarUsuario(Usuario usuario) {
        boolean insertado = false;
        try (Connection con = ConexionBD.open()) {
            con.setAutoCommit(false); // inicia transacción

            try (PreparedStatement psPerfil = con.prepareStatement(SQL_INSERT_PERFIL);
                    PreparedStatement psUsuario = con.prepareStatement(SQL_INSERT_USUARIO)) {

                // Insertar en perfil
                psPerfil.setString(1, usuario.getCodigoUsuario());
                psPerfil.setString(2, usuario.getEmail());
                psPerfil.setString(3, usuario.getContrasena());
                psPerfil.setString(4, usuario.getNombreUsuario());
                psPerfil.setInt(5, usuario.getTelefono());
                psPerfil.setString(6, usuario.getNombre());
                psPerfil.setString(7, usuario.getApellido());
                psPerfil.executeUpdate();

                // Insertar en usuarios
                psUsuario.setString(1, usuario.getCodigoUsuario());
                psUsuario.setString(2, usuario.getGenero());
                psUsuario.setLong(3, usuario.getnTarjeta());
                psUsuario.executeUpdate();

                con.commit();
                insertado = true;
            } catch (SQLException e) {
                con.rollback();
                System.out.println("⚠️ Error al registrar usuario (rollback): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error en registrarUsuario(): " + e.getMessage());
        }
        return insertado;
    }

    // =====================================================
    // 🔹 ACTUALIZAR USUARIO
    // =====================================================
    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        boolean actualizado = false;
        try (Connection con = ConexionBD.open()) {
            con.setAutoCommit(false);

            try (PreparedStatement psPerfil = con.prepareStatement(SQL_UPDATE_PERFIL);
                    PreparedStatement psUsuario = con.prepareStatement(SQL_UPDATE_USUARIOS)) {

                // Actualizar perfil
                psPerfil.setString(1, usuario.getEmail());
                psPerfil.setString(2, usuario.getContrasena());
                psPerfil.setString(3, usuario.getNombreUsuario());
                psPerfil.setInt(4, usuario.getTelefono());
                psPerfil.setString(5, usuario.getNombre());
                psPerfil.setString(6, usuario.getApellido());
                psPerfil.setString(7, usuario.getCodigoUsuario());
                psPerfil.executeUpdate();

                // Actualizar usuarios
                psUsuario.setString(1, usuario.getGenero());
                psUsuario.setLong(2, usuario.getnTarjeta());
                psUsuario.setString(3, usuario.getCodigoUsuario());
                psUsuario.executeUpdate();

                con.commit();
                actualizado = true;
            } catch (SQLException e) {
                con.rollback();
                System.out.println("⚠️ Error en actualizarUsuario (rollback): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error en actualizarUsuario(): " + e.getMessage());
        }
        return actualizado;
    }

    // =====================================================
    // 🔹 ELIMINAR USUARIO
    // =====================================================
    @Override
    public boolean eliminarUsuario(String codigoUsuario) {
        boolean eliminado = false;
        try (Connection con = ConexionBD.open();
                PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setString(1, codigoUsuario);
            eliminado = ps.executeUpdate() > 0;
            if (eliminado) {
                System.out.println("🗑️ Usuario eliminado correctamente (Código: " + codigoUsuario + ")");
            } else {
                System.out.println("⚠️ No se encontró el usuario con código: " + codigoUsuario);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error en eliminarUsuario(): " + e.getMessage());
        }
        return eliminado;
    }

    // =====================================================
    // 🔹 OBTENER TODOS LOS CORREOS (para PanelAdmin)
    // =====================================================
    public List<String> obtenerTodosLosCorreos() {
        List<String> lista = new ArrayList<>();
        try (Connection con = ConexionBD.open();
                PreparedStatement ps = con.prepareStatement(SQL_ALL_EMAILS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al obtener correos: " + e.getMessage());
        }
        return lista;
    }

    // =====================================================
    // 🔹 OBTENER TIPO DE USUARIO
    // =====================================================
    public String obtenerTipoUsuario(String email) {
        try (Connection con = ConexionBD.open()) {

            try (PreparedStatement psAdmin = con.prepareStatement(SQL_ADMIN_EXISTE)) {
                psAdmin.setString(1, email);
                try (ResultSet rsAdmin = psAdmin.executeQuery()) {
                    if (rsAdmin.next()) {
                        return "admin";
                    }
                }
            }

            try (PreparedStatement psUsuario = con.prepareStatement(SQL_USUARIO_EXISTE)) {
                psUsuario.setString(1, email);
                try (ResultSet rsUsuario = psUsuario.executeQuery()) {
                    if (rsUsuario.next()) {
                        return "usuario";
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en obtenerTipoUsuario(): " + e.getMessage());
        }
        return "desconocido";
    }
    // =====================================================
// 🔹 VERIFICAR DUPLICADOS (teléfono y tarjeta)
// =====================================================
public boolean existeTelefono(int telefono) {
    String sql = "SELECT telefono FROM perfil WHERE telefono = ?";
    try (Connection con = ConexionBD.open();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, telefono);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true si ya existe
    } catch (SQLException e) {
        System.out.println("⚠️ Error en existeTelefono(): " + e.getMessage());
        return false;
    }
}

public boolean existeTarjeta(long tarjeta) {
    String sql = "SELECT n_tarjeta FROM usuarios WHERE n_tarjeta = ?";
    try (Connection con = ConexionBD.open();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setLong(1, tarjeta);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true si ya existe
    } catch (SQLException e) {
        System.out.println("⚠️ Error en existeTarjeta(): " + e.getMessage());
        return false;
    }
}

}
