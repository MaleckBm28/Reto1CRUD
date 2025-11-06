package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private Dao dao = new DaoImplementacion();

    @FXML
    private void handleLogin(ActionEvent event) {
        String gmail = txtCorreo.getText().trim();
        String pass = txtPassword.getText().trim();

        if (gmail.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Por favor, rellene todos los campos.");
            return;
        }

        if (dao.autenticar(gmail, pass)) {
            Usuario usuario = dao.obtenerUsuarioPorEmail(gmail);
            lblMensaje.setText("✅ Acceso correcto");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PanelAdmin.fxml"));
                Scene scene = new Scene(loader.load());

                // Pasar el usuario al PanelAdminController
                PanelAdminController controller = loader.getController();
                controller.setUsuario(usuario);

                Stage stage = (Stage) txtCorreo.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Panel de Administración");
            } catch (Exception e) {
                lblMensaje.setText("⚠️ Error al abrir PanelAdmin: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            lblMensaje.setText("❌ Gmail o contraseña incorrectos");
        }
    }
}
