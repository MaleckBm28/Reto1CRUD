package conexion;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PoolConexion {

    private static BasicDataSource dataSource;

    static {
        try {
            dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/gestionadt?useSSL=false&serverTimezone=Europe/Madrid");
            dataSource.setUsername("root");
            dataSource.setPassword("abcd*1234");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // Configuración del pool
            dataSource.setMinIdle(2);
            dataSource.setMaxIdle(5);
            dataSource.setMaxTotal(10);
            dataSource.setMaxWaitMillis(10000);

            System.out.println("✅ Pool de conexiones inicializado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error inicializando pool: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        System.out.println("🔗 Conexión obtenida del pool. Activas: " +
                dataSource.getNumActive() + " | Inactivas: " + dataSource.getNumIdle());
        return con;
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
                System.out.println("🔙 Conexión devuelta al pool. Activas: " +
                        dataSource.getNumActive() + " | Inactivas: " + dataSource.getNumIdle());
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar conexión: " + e.getMessage());
        }
    }

    public static void cerrarPool() {
        try {
            if (dataSource != null) {
                dataSource.close();
                System.out.println("✅ Pool de conexiones cerrado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar el pool: " + e.getMessage());
        }
    }
}
