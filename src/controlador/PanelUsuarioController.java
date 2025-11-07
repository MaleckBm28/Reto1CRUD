package controlador;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.Usuario;

public class PanelUsuarioController {

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private TextField txtContrasenaVisible;
    @FXML
    private Button btnVerContrasena;
    @FXML
    private ComboBox<String> comboGenero;
    @FXML
    private TextField txtTarjeta;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtTelefono;

    private final DaoImplementacion dao = new DaoImplementacion();
    private Usuario usuarioActual;
    private boolean mostrandoContrasena = false;

    @FXML
    private void initialize() {
        comboGenero.getItems().addAll("Masculino", "Femenino", "Otro");
        txtContrasena.textProperty().bindBidirectional(txtContrasenaVisible.textProperty());
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuarioActual != null) {
            cargarDatosUsuario(usuarioActual);
        }
    }

    private void cargarDatosUsuario(Usuario u) {
        txtCodigo.setText(u.getCodigoUsuario());
        txtNombre.setText(u.getNombre());
        txtApellido.setText(u.getApellido());
        txtEmail.setText(u.getEmail());
        txtContrasena.setText(u.getContrasena());
        txtTarjeta.setText(String.valueOf(u.getnTarjeta()));
        txtUsuario.setText(u.getNombreUsuario());
        txtTelefono.setText(String.valueOf(u.getTelefono()));
        comboGenero.setValue(u.getGenero());
    }

    @FXML
    private void onModificar(ActionEvent event) {
        if (usuarioActual == null) {
            mostrarMensaje("No hay un usuario cargado.");
            return;
        }

        try {
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setApellido(txtApellido.getText());
            usuarioActual.setEmail(txtEmail.getText());
            usuarioActual.setContrasena(txtContrasena.getText());
            usuarioActual.setGenero(comboGenero.getValue());
            usuarioActual.setnTarjeta(Long.parseLong(txtTarjeta.getText()));
            usuarioActual.setNombreUsuario(txtUsuario.getText());
            usuarioActual.setTelefono(Integer.parseInt(txtTelefono.getText()));

            dao.modificarUsuario(usuarioActual);
            mostrarMensaje("Datos modificados correctamente.");
        } catch (Exception e) {
            mostrarMensaje("Error al modificar: " + e.getMessage());
        }
    }

    @FXML
    private void onBorrar(ActionEvent event) {
        if (usuarioActual == null) {
            mostrarMensaje("No hay usuario cargado para borrar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Seguro que deseas eliminar tu cuenta?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        confirm.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                dao.borrarUsuario(usuarioActual.getCodigoUsuario());
                mostrarMensaje("🗑️ Usuario eliminado correctamente.");
                limpiarCampos();
            }
        });
    }

    @FXML
    private void onSalir(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar salida");
        alerta.setHeaderText("¿Seguro que deseas salir del panel?");
        alerta.setContentText("Se cerrará tu sesión y volverás al inicio de sesión.");

        ButtonType botonSi = new ButtonType("Sí, salir", ButtonBar.ButtonData.OK_DONE);
        ButtonType botonNo = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(botonSi, botonNo);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == botonSi) {
                try {
                    Stage stageActual = (Stage) txtCodigo.getScene().getWindow();
                    stageActual.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
                    Stage stageLogin = new Stage();
                    Scene sceneLogin = new Scene(loader.load());
                    stageLogin.setTitle("Iniciar Sesión");
                    stageLogin.setScene(sceneLogin);

                    // Animación de entrada suave
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), sceneLogin.getRoot());
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();

                    stageLogin.show();

                } catch (Exception e) {
                    System.err.println("❌ Error al volver al login: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onVerContrasena() {
        mostrandoContrasena = !mostrandoContrasena;

        if (mostrandoContrasena) {
            txtContrasenaVisible.setVisible(true);
            txtContrasenaVisible.setManaged(true);
            txtContrasena.setVisible(false);
            txtContrasena.setManaged(false);
            btnVerContrasena.setText("Ocultar");
        } else {
            txtContrasenaVisible.setVisible(false);
            txtContrasenaVisible.setManaged(false);
            txtContrasena.setVisible(true);
            txtContrasena.setManaged(true);
            btnVerContrasena.setText("Ver");
        }
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtContrasena.clear();
        comboGenero.setValue(null);
        txtTarjeta.clear();
        txtUsuario.clear();
        txtTelefono.clear();
    }

    private void mostrarMensaje(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
