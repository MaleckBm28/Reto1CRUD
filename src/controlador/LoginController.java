package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private final DaoImplementacion dao = new DaoImplementacion();

    
    @FXML
    private void onLogin(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos.");
            return;
        }

        // Verificamos credenciales
        if (dao.autenticar(email, password)) {
            Usuario usuario = dao.obtenerUsuarioPorEmail(email);

            if (usuario != null) {
                // Si el código del usuario empieza por C → Cliente
                if (usuario.getCodigoUsuario().startsWith("C")) {
                    abrirPanelUsuario(usuario);
                } 
                // Si el código empieza por A → Administrador
                else if (usuario.getCodigoUsuario().startsWith("A")) {
                    mostrarMensaje("🔒 Eres administrador. Esta parte se implementará luego.");
                } 
                else {
                    mostrarMensaje("⚠️ Tipo de usuario desconocido.");
                }
            } else {
                mostrarMensaje("⚠️ No se pudo obtener el usuario.");
            }
        } else {
            mostrarMensaje("❌ Credenciales incorrectas.");
        }
    }

    private void abrirPanelUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PanelUsuario.fxml"));
            Scene scene = new Scene(loader.load());

            // Pasar el usuario al controlador del panel
            PanelUsuarioController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Panel de Usuario - " + usuario.getNombreUsuario());
            stage.show();

        } catch (Exception e) {
            mostrarMensaje("❌ Error al abrir el panel de usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
