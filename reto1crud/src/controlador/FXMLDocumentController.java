package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * Controlador del login
 */
public class FXMLDocumentController implements Initializable {

    @FXML private Label label;            // Mensajes de error o éxito
    @FXML private TextField txtUsuario;   // Campo usuario
    @FXML private PasswordField txtPassword; 
    @FXML private TextField txtPasswordPlain; 
    @FXML private ToggleButton btnTogglePwd; 
    @FXML private Button button;          // Botón "Entrar"

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vincula texto entre campo visible y oculto
        if (txtPasswordPlain != null && txtPassword != null) {
            txtPasswordPlain.textProperty().bindBidirectional(txtPassword.textProperty());
        }

        // El label solo ocupa espacio si está visible
        if (label != null) {
            label.managedProperty().bind(label.visibleProperty());
            label.setVisible(false);
        }
    }

    @FXML
    private void togglePassword(ActionEvent event) {
        boolean show = btnTogglePwd.isSelected();
        txtPasswordPlain.setVisible(show);
        txtPasswordPlain.setManaged(show);
        txtPassword.setVisible(!show);
        txtPassword.setManaged(!show);

        if (show) {
            txtPasswordPlain.requestFocus();
            txtPasswordPlain.positionCaret(txtPasswordPlain.getText().length());
        } else {
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getText().length());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String user = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String pass = (btnTogglePwd.isSelected() ? txtPasswordPlain.getText() : txtPassword.getText());
        pass = pass == null ? "" : pass;

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Rellena usuario y contraseña.");
            return;
        }

        // Validación simple (puedes conectar con BD o API)
        boolean ok = validarCredenciales(user, pass);

        if (ok) {
            showOk("¡Bienvenida, " + user + "!");
            // Aquí podrías abrir otra escena:
            // App.setRoot("main");
        } else {
            showError("Credenciales inválidas. Inténtalo de nuevo.");
        }
    }

    private boolean validarCredenciales(String user, String pass) {
        // Ejemplo: admin / 1234
        return ("admin".equalsIgnoreCase(user) || "admin@ejemplo.com".equalsIgnoreCase(user))
                && "1234".equals(pass);
    }

    private void showError(String msg) {
        label.setText(msg);
        label.setStyle("-fx-text-fill:#d32f2f; -fx-font-size:12px;");
        label.setVisible(true);
    }

    private void showOk(String msg) {
        label.setText(msg);
        label.setStyle("-fx-text-fill:#2e7d32; -fx-font-size:12px;");
        label.setVisible(true);
    }
}
