package controlador;

import conexion.ConexionBD;
import conexion.PoolConexion;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoImplementacion implements Dao {

    private static final String SQL_LOGIN =
        "SELECT 1 FROM perfil WHERE email=? AND contrasena=?";

    private static final String SQL_FIND_USUARIO =
        "SELECT p.codigo_usuario, p.email, p.contrasena, p.nombre_usuario, p.telefono, " +
        "p.nombre, p.apellido, u.genero, u.n_tarjeta " +
        "FROM perfil p " +
        "JOIN usuarios u ON p.codigo_usuario = u.codigo_usuario " +
        "WHERE p.email=?";

    private static final String SQL_FIND_ADMIN =
        "SELECT p.codigo_usuario, p.email, p.contrasena, p.nombre_usuario, p.telefono, " +
        "p.nombre, p.apellido, 'Administrador' AS genero, 0 AS n_tarjeta " +
        "FROM perfil p " +
        "JOIN administrador a ON p.codigo_usuario = a.codigo_usuario " +
        "WHERE p.email=?";
    // 🟢 Consultas para registro de usuario
private static final String SQL_ULTIMO_CODIGO =
    "SELECT codigo_usuario FROM perfil WHERE codigo_usuario LIKE 'C%' ORDER BY codigo_usuario DESC LIMIT 1";

private static final String SQL_INSERT_PERFIL =
    "INSERT INTO perfil (codigo_usuario, email, contrasena, nombre_usuario, telefono, nombre, apellido) " +
    "VALUES (?, ?, ?, ?, ?, ?, ?)";

private static final String SQL_INSERT_USUARIO =
    "INSERT INTO usuarios (codigo_usuario, genero, n_tarjeta) VALUES (?, ?, ?)";


    @Override
    public boolean autenticar(String email, String contrasena) {
        Connection con = null;
        try {
            con = ConexionBD.open();
            PreparedStatement ps = con.prepareStatement(SQL_LOGIN);
            ps.setString(1, email);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();

            System.out.println("🔍 [DAO] Resultado de autenticación: " + existe);

            PoolConexion.retenerConexion(con);
            return existe;

        } catch (SQLException e) {
            System.out.println("⚠️ Error en autenticar(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        Connection con = null;
        try {
            con = ConexionBD.open();

            try (PreparedStatement ps = con.prepareStatement(SQL_FIND_USUARIO)) {
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

                        PoolConexion.retenerConexion(con);
                        return u;
                    }
                }
            }

            // Si no lo encuentra en usuarios, buscar en administrador
            try (PreparedStatement ps = con.prepareStatement(SQL_FIND_ADMIN)) {
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
                        u.setGenero("Administrador");
                        u.setnTarjeta(0);

                        PoolConexion.retenerConexion(con);
                        return u;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error en obtenerUsuarioPorEmail(): " + e.getMessage());
        }

        System.out.println("⚠️ No se encontró el usuario con email: " + email);
        return null;
    }

    @Override
    public synchronized boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre=?, apellido=?, contrasena=?, id_perfil=?, genero=?, nTarjeta=? WHERE correo=?";
        Connection con = null;

        try {
            con = ConexionBD.open();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getContrasena());
            ps.setInt(4, usuario.getIdPerfil());
            ps.setString(5, usuario.getGenero());
            ps.setLong(6, usuario.getnTarjeta());
            ps.setString(7, usuario.getEmail());

            int filas = ps.executeUpdate();
            PoolConexion.retenerConexion(con);

            if (filas > 0) {
                System.out.println("✅ Usuario modificado correctamente: " + usuario.getEmail());
                return true;
            } else {
                System.out.println("⚠️ No se encontró el usuario con correo: " + usuario.getEmail());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }
    // 🟢 Método para registrar un nuevo usuario
// 🟢 Método para registrar un nuevo usuario con validaciones
public synchronized boolean registrarUsuario(Usuario usuario) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = ConexionBD.open();

        // 1️⃣ Verificar si el correo ya existe
        String sqlCheck = "SELECT email FROM perfil WHERE email = ?";
        ps = con.prepareStatement(sqlCheck);
        ps.setString(1, usuario.getEmail());
        rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("⚠️ Error: El correo " + usuario.getEmail() + " ya está registrado.");
            return false; // correo duplicado
        }

        // 2️⃣ Obtener el último código de cliente (C000X)
        String sqlUltimo = "SELECT codigo_usuario FROM perfil WHERE codigo_usuario LIKE 'C%' ORDER BY codigo_usuario DESC LIMIT 1";
        ps = con.prepareStatement(sqlUltimo);
        rs = ps.executeQuery();

        String nuevoCodigo = "C0001"; // valor por defecto si no hay usuarios
        if (rs.next()) {
            String ultimoCodigo = rs.getString("codigo_usuario"); // ej: C0004
            int num = Integer.parseInt(ultimoCodigo.substring(1)) + 1;
            nuevoCodigo = String.format("C%04d", num);
        }

        usuario.setCodigoUsuario(nuevoCodigo);

        // 3️⃣ Insertar en tabla perfil
        String sqlInsertPerfil = "INSERT INTO perfil (codigo_usuario, email, contrasena, nombre_usuario, telefono, nombre, apellido) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
        ps = con.prepareStatement(sqlInsertPerfil);
        ps.setString(1, usuario.getCodigoUsuario());
        ps.setString(2, usuario.getEmail());
        ps.setString(3, usuario.getContrasena());
        ps.setString(4, usuario.getNombreUsuario());
        ps.setInt(5, usuario.getTelefono());
        ps.setString(6, usuario.getNombre());
        ps.setString(7, usuario.getApellido());
        ps.executeUpdate();

        // 4️⃣ Insertar en tabla usuarios
        String sqlInsertUsuario = "INSERT INTO usuarios (codigo_usuario, genero, n_tarjeta) VALUES (?, ?, ?)";
        ps = con.prepareStatement(sqlInsertUsuario);
        ps.setString(1, usuario.getCodigoUsuario());
        ps.setString(2, usuario.getGenero());
        ps.setLong(3, usuario.getnTarjeta());
        ps.executeUpdate();

        PoolConexion.retenerConexion(con);
        System.out.println("✅ Usuario registrado correctamente con código: " + nuevoCodigo);
        return true;

    } catch (SQLException e) {
        if (e.getMessage().contains("Duplicate entry")) {
            System.out.println("⚠️ Error: Ya existe un usuario con ese correo electrónico.");
        } else {
            System.out.println("⚠️ Error SQL al registrar usuario: " + e.getMessage());
        }
        return false;

    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar recursos: " + e.getMessage());
        }
    }
}


}
