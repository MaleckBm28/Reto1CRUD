package controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import manejoHilos.HiloLeer;
import modelo.Usuario;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;

    private DaoImplementacion dao = new DaoImplementacion();

    @FXML
    private void handleLogin(ActionEvent event) {
        String gmail = txtCorreo.getText().trim();
        String pass = txtPassword.getText().trim();

        // 🟡 Validar campos vacíos
        if (gmail.isEmpty() || pass.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }

        // 🌀 Mostrar ventana de carga (Loading.fxml)
        Stage loadingStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Loading.fxml"));
            Scene scene = new Scene(loader.load());
            loadingStage.setScene(scene);
            loadingStage.setTitle("Conectando...");
            loadingStage.setResizable(false);

            // 🚫 Quitar la X y los bordes
            loadingStage.initStyle(StageStyle.UNDECORATED);

            // 🔒 Bloquear la ventana principal mientras carga
            loadingStage.initOwner(((Stage) txtCorreo.getScene().getWindow()));
            loadingStage.initModality(Modality.WINDOW_MODAL);

            loadingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔵 Ejecutar el hilo de login
        HiloLeer hiloLogin = new HiloLeer(dao, gmail, pass);
        Thread t = new Thread(hiloLogin, "Hilo-Login");
        t.start();

        // 🔸 Hilo auxiliar que espera el tiempo del pool (30s) y luego cierra el loading
        new Thread(() -> {
            try {
                t.join(); // espera a que termine el hilo de login
                Thread.sleep(30000); // coincide con el retardo del pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                loadingStage.close(); // cerrar el spinner

                // ❌ Fallo de autenticación
                if (!hiloLogin.isAutenticado()) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Credenciales incorrectas", "Gmail o contraseña incorrectos.");
                    return;
                }

                // ✅ Login correcto
                Usuario usuario = hiloLogin.getUsuario();
                if (usuario == null) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de datos", "No se pudieron obtener los datos del usuario.");
                    return;
                }

                mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido", "Acceso correcto, " + usuario.getNombre() + "!");

                // 🧭 Determinar si es admin o usuario
                try {
                    FXMLLoader loader;
                    String codigo = usuario.getCodigoUsuario().trim().toUpperCase();

                    if (codigo.startsWith("A")) {
                        loader = new FXMLLoader(getClass().getResource("/vista/PanelAdmin.fxml"));
                    } else {
                        loader = new FXMLLoader(getClass().getResource("/vista/PanelUsuario.fxml"));
                    }

                    // Cargar escena
                    Scene scene = new Scene(loader.load());

                    // Pasar usuario al panel
                    Object controller = loader.getController();
                    if (controller instanceof PanelAdminController) {
                        ((PanelAdminController) controller).setUsuario(usuario);
                    } else if (controller instanceof PanelUsuarioController) {
                        System.out.println("👤 Usuario cargado en PanelUsuario: " + usuario.getEmail());
                    }

                    // Cambiar ventana
                    Stage stage = (Stage) txtCorreo.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle(codigo.startsWith("A") ? "Panel de Administración" : "Panel de Usuario");

                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar", "No se pudo abrir el panel.\n" + e.getMessage());
                }
            });
        }).start();
    }

    // 🔸 Método auxiliar para mostrar popups
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
