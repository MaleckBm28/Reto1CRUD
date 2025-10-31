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

            System.out.println("✅ Pool de conexiones inicializado correctamente.");

        } catch (Exception e) {
            System.out.println("⚠️ Error al inicializar pool: " + e.getMessage());
        }
    }

    // Devuelve una conexión del pool
    public static synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Cierra todo el pool (al salir del programa)
    public static synchronized void cerrarPool() {
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
            Thread.sleep(RETENCION_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    
    /*Hacer un metodo sincronized para controlar los hilos
    son 4 hilos, C R U D*/
}
