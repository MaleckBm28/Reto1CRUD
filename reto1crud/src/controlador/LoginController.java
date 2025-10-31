package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import manejoHilos.HiloLeer;
import modelo.Usuario;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private DaoImplementacion dao = new DaoImplementacion();

    @FXML
    private void handleLogin(ActionEvent event) {
        String gmail = txtCorreo.getText().trim();
        String pass = txtPassword.getText().trim();

        if (gmail.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Por favor, rellene todos los campos.");
            return;
        }

        // 👉 Aquí usamos tu hilo HiloLeer para manejar el login
        HiloLeer hiloLogin = new HiloLeer(dao, gmail, pass);
        Thread t = new Thread(hiloLogin, "Hilo-Login");

        t.start();

        try {
            t.join(); // Espera a que el hilo termine
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (hiloLogin.isAutenticado()) {
            Usuario usuario = hiloLogin.getUsuario();
            lblMensaje.setText("✅ Acceso correcto");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PanelAdmin.fxml"));
                Scene scene = new Scene(loader.load());

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
