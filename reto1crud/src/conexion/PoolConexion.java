package conexion;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PoolConexion {
    private static BasicDataSource dataSource;
    private static long RETENCION_MS; // en milisegundos

    static {
        try {
            ResourceBundle config = ResourceBundle.getBundle("resources.configClass");

            // Datos de conexión
            String url = config.getString("Conn");
            String user = config.getString("DBUser");
            String password = config.getString("DBPass");
            String driver = config.getString("Driver");

            // Inicializar pool
            dataSource = new BasicDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setDriverClassName(driver);

            // ⚙️ Parámetros del pool leídos del .properties
            dataSource.setInitialSize(Integer.parseInt(config.getString("initialSize")));
            dataSource.setMaxTotal(Integer.parseInt(config.getString("maxTotal")));
            dataSource.setMaxIdle(Integer.parseInt(config.getString("maxIdle")));
            dataSource.setMinIdle(Integer.parseInt(config.getString("minIdle")));
            dataSource.setMaxWaitMillis(Long.parseLong(config.getString("maxWaitMillis")));

            // 🕒 Tiempo de retención (para pruebas)
            RETENCION_MS = Long.parseLong(config.getString("holdTimeSecond")) * 1000;

<<<<<<< HEAD
            System.out.println("✅ Pool de conexiones inicializado correctamente.");
=======
            System.out.println("✅ Pool de conexiones inicializado correctamente. Tiempo de retención: " + (RETENCION_MS / 1000) + " segundos.");
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b

        } catch (Exception e) {
            System.out.println("⚠️ Error al inicializar pool: " + e.getMessage());
        }
    }

    // Devuelve una conexión del pool
<<<<<<< HEAD
    public static Connection getConnection() throws SQLException {
=======
    public static synchronized Connection getConnection() throws SQLException {
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
        return dataSource.getConnection();
    }

    // Cierra todo el pool (al salir del programa)
<<<<<<< HEAD
    public static void cerrarPool() {
=======
    public static synchronized void cerrarPool() {
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
        try {
            if (dataSource != null) {
                dataSource.close();
                System.out.println("✅ Pool de conexiones cerrado correctamente.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al cerrar el pool: " + e.getMessage());
        }
    }

    // Mantiene la conexión “retenida” para pruebas (por defecto 30 seg)
    public static void retenerConexion(Connection con) {
        if (con == null) return;
        try {
<<<<<<< HEAD
            Thread.sleep(RETENCION_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
=======
            System.out.println("⏳ Reteniendo conexión por " + (RETENCION_MS / 1000) + " segundos...");
            Thread.sleep(RETENCION_MS);
            con.close();
            System.out.println("✅ Conexión devuelta al pool.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar conexión: " + e.getMessage());
        }
    }

    /* Método synchronized para controlar hilos CRUD (si se implementan) */
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
}
