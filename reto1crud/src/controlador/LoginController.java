package controlador;

import dao.DaoImplementacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Usuario;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblMensaje;

    private DaoImplementacion dao = new DaoImplementacion();

    @FXML
    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor ingrese su correo y contraseña.");
            return;
        }

        // 🔹 Mostrar ventana de carga (loading.fxml)
        Stage loadingStage = mostrarLoading();

        // 🔐 Ejecutar la verificación en un hilo
        Thread hiloLogin = new Thread(() -> {
            System.out.println("🔐 [Hilo-Login] Verificando credenciales...");
            boolean autenticado = dao.autenticar(email, contrasena);

            Platform.runLater(() -> {
                if (autenticado) {
                    Usuario usuario = dao.obtenerUsuarioPorEmail(email);

                    if (usuario != null) {
                        String tipo = dao.obtenerTipoUsuario(email);
                        System.out.println("✅ Login correcto para: " + usuario.getEmail());
                        System.out.println("✅ Usuario autenticado correctamente. Código: " + usuario.getCodigoUsuario());
                        lblMensaje.setText("✅ Autenticado correctamente.");

                        // ✅ Cerrar ventana de carga antes de abrir el panel
                        loadingStage.close();
                        abrirPanelCorrespondiente(usuario, tipo);

                    } else {
                        loadingStage.close();
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo obtener la información del usuario.");
                    }
                } else {
                    loadingStage.close();
                    lblMensaje.setText("❌ Credenciales incorrectas.");
                    lblMensaje.setStyle("-fx-text-fill: red;");
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Correo o contraseña incorrectos.");
                }
            });
        });

        hiloLogin.setName("Hilo-Login");
        hiloLogin.setDaemon(true);
        hiloLogin.start();
    }

    // 🔹 Muestra el spinner (loading.fxml)
    private Stage mostrarLoading() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Loading.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Cargando...");
            stage.setScene(scene);
            stage.show();
            return stage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void abrirPanelCorrespondiente(Usuario usuario, String tipo) {
        try {
            Stage stage = (Stage) txtEmail.getScene().getWindow();

            if ("admin".equalsIgnoreCase(tipo)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PanelAdmin.fxml"));
                Scene scene = new Scene(loader.load());
                PanelAdminController controller = loader.getController();
                controller.setAdmin(usuario);
                stage.setScene(scene);
                stage.setTitle("Panel de Administrador");
                System.out.println("🟣 Panel ADMIN abierto para: " + usuario.getEmail());
            } else if ("usuario".equalsIgnoreCase(tipo)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PanelUsuario.fxml"));
                Scene scene = new Scene(loader.load());
                PanelUsuarioController controller = loader.getController();
                controller.setUsuario(usuario);
                stage.setScene(scene);
                stage.setTitle("Panel de Usuario");
                System.out.println("🟢 Panel USUARIO abierto para: " + usuario.getEmail());
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Tipo de usuario desconocido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el panel correspondiente.");
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registro de Usuario");
            stage.show();
            System.out.println("🟩 Ventana de registro abierta correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de registro.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
