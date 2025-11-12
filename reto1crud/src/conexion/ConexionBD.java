package conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {

    public static boolean holdEnabled = true;
    public static int holdTimeSecond = 5;

    public static Connection open() throws SQLException {
        System.out.println("⏳ Esperando " + holdTimeSecond + " segundos antes de abrir la conexión...");
        retencion(); // 🕒 Simulación antes de obtener la conexión real
        Connection con = PoolConexion.getConnection();
        System.out.println("🟢 Conexión abierta correctamente.");
        return con;
    }

    public static void close(Connection con) {
        try {
            if (con != null) {
                System.out.println("⏳ Esperando " + holdTimeSecond + " segundos antes de cerrar la conexión...");
                retencion(); // 🕒 Simulación antes de devolverla al pool
                PoolConexion.closeConnection(con);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void retencion() {
        try {
            if (holdEnabled) {
                System.out.println("⏱️ Reteniendo conexión por " + holdTimeSecond + " segundos...");
                Thread.sleep(holdTimeSecond * 1000);
                System.out.println("✅ Retención finalizada.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
