package controlador;

import dao.DaoImplementacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;

public class RegistroController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private ComboBox<String> cbGenero;
    @FXML private TextField txtTarjeta;

    private DaoImplementacion dao = new DaoImplementacion();

    @FXML
    public void initialize() {
        cbGenero.getItems().addAll("Femenino", "Masculino");
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        try {
            if (txtEmail.getText().trim().isEmpty() ||
                txtContrasena.getText().trim().isEmpty() ||
                txtNombreUsuario.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() ||
                txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                cbGenero.getValue() == null ||
                txtTarjeta.getText().trim().isEmpty()) {

                mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setCodigoUsuario(generarCodigoUsuario());
            nuevo.setEmail(txtEmail.getText().trim());
            nuevo.setContrasena(txtContrasena.getText().trim());
            nuevo.setNombreUsuario(txtNombreUsuario.getText().trim());
            nuevo.setTelefono(Integer.parseInt(txtTelefono.getText().trim()));
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setApellido(txtApellido.getText().trim());
            nuevo.setGenero(cbGenero.getValue());
            nuevo.setnTarjeta(Long.parseLong(txtTarjeta.getText().trim()));

            boolean exito = dao.registrarUsuario(nuevo);

            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente.");
                limpiarCampos();
                volverAlLogin(event);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "El correo ya está registrado o ocurrió un error.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "El teléfono o número de tarjeta no es válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", e.getMessage());
        }
    }

    private String generarCodigoUsuario() {
        int numero = (int) (Math.random() * 9000) + 1000;
        return "C" + numero;
    }

    @FXML
    private void volverAlLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            System.out.println("🔙 Volviendo a la pantalla de login...");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo volver al login.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos() {
        txtEmail.clear();
        txtContrasena.clear();
        txtNombreUsuario.clear();
        txtTelefono.clear();
        txtNombre.clear();
        txtApellido.clear();
        cbGenero.setValue(null);
        txtTarjeta.clear();
    }
}
