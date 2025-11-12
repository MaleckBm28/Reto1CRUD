package controlador;

import dao.DaoImplementacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;

import java.util.List;

public class PanelAdminController {

    @FXML private ComboBox<String> cbUsuarios;
    @FXML private TextField txtNombre, txtApellido, txtEmail, txtNombreUsuario, txtTelefono, txtTarjeta;
    @FXML private ComboBox<String> cbGenero;
    @FXML private Label lblAdmin;

    private DaoImplementacion dao = new DaoImplementacion();
    private Usuario usuarioSeleccionado;
    private Usuario adminActual;

    public void setAdmin(Usuario admin) {
        this.adminActual = admin;
        lblAdmin.setText("Administrador: " + admin.getNombreUsuario());
        cargarCorreosUsuarios();
    }

    @FXML
    public void initialize() {
        cbGenero.getItems().addAll("Femenino", "Masculino");
    }

    private void cargarCorreosUsuarios() {
        List<String> correos = dao.obtenerTodosLosCorreos();
        cbUsuarios.getItems().setAll(correos);
    }

    @FXML
    private void cargarDatosUsuario() {
        String email = cbUsuarios.getValue();
        if (email == null) return;

        usuarioSeleccionado = dao.obtenerUsuarioPorEmail(email);
        if (usuarioSeleccionado != null) {
            txtNombre.setText(usuarioSeleccionado.getNombre());
            txtApellido.setText(usuarioSeleccionado.getApellido());
            txtEmail.setText(usuarioSeleccionado.getEmail());
            txtNombreUsuario.setText(usuarioSeleccionado.getNombreUsuario());
            txtTelefono.setText(String.valueOf(usuarioSeleccionado.getTelefono()));
            cbGenero.setValue(usuarioSeleccionado.getGenero());
            txtTarjeta.setText(String.valueOf(usuarioSeleccionado.getnTarjeta()));
        }
    }

    @FXML
    private void guardarCambios() {
        if (usuarioSeleccionado == null) return;

        try {
            usuarioSeleccionado.setNombre(txtNombre.getText());
            usuarioSeleccionado.setApellido(txtApellido.getText());
            usuarioSeleccionado.setEmail(txtEmail.getText());
            usuarioSeleccionado.setNombreUsuario(txtNombreUsuario.getText());
            usuarioSeleccionado.setTelefono(Integer.parseInt(txtTelefono.getText()));
            usuarioSeleccionado.setGenero(cbGenero.getValue());
            usuarioSeleccionado.setnTarjeta(Long.parseLong(txtTarjeta.getText()));

            if (dao.actualizarUsuario(usuarioSeleccionado)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Datos modificados correctamente.");
                cargarCorreosUsuarios();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el usuario.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Verifica los datos ingresados.");
        }
    }

    @FXML
    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Eliminar usuario?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dao.eliminarUsuario(usuarioSeleccionado.getCodigoUsuario())) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Usuario eliminado correctamente.");
                    cbUsuarios.getItems().remove(usuarioSeleccionado.getEmail());
                    limpiarCampos();
                    usuarioSeleccionado = null;
                }
            }
        });
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) cbUsuarios.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtNombreUsuario.clear();
        txtTelefono.clear();
        txtTarjeta.clear();
        cbGenero.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
