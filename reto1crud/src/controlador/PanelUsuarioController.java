package controlador;

import dao.DaoImplementacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * Controlador del panel del usuario.
 * Muestra y permite editar los datos del usuario logueado.
 */
public class PanelUsuarioController {

    // Campos del FXML
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> cbGenero;
    @FXML private TextField txtTarjeta;

    private Usuario usuario;  
    private DaoImplementacion dao = new DaoImplementacion();

    // 🔹 Inicializa el combo de géneros
    @FXML
    public void initialize() {
        cbGenero.getItems().addAll("Femenino", "Masculino", "Otro");
    }

    // 🔹 Recibe el usuario logueado desde LoginController
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("✅ Usuario cargado en PanelUsuario: " + usuario.getEmail());
        mostrarDatosUsuario();
    }

    // 🔹 Muestra los datos en los campos
    private void mostrarDatosUsuario() {
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtApellido.setText(usuario.getApellido());
            txtEmail.setText(usuario.getEmail());
            txtNombreUsuario.setText(usuario.getNombreUsuario());
            txtTelefono.setText(String.valueOf(usuario.getTelefono()));
            txtTarjeta.setText(String.valueOf(usuario.getnTarjeta()));
            cbGenero.setValue(usuario.getGenero() != null ? usuario.getGenero() : "Otro");
        }
    }

    // 🟢 Guardar cambios
    @FXML
    private void guardarCambios(ActionEvent event) {
        try {
            usuario.setNombre(txtNombre.getText());
            usuario.setApellido(txtApellido.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setNombreUsuario(txtNombreUsuario.getText());
            usuario.setTelefono(Integer.parseInt(txtTelefono.getText()));
            usuario.setGenero(cbGenero.getValue());
            usuario.setnTarjeta(Long.parseLong(txtTarjeta.getText()));

            boolean actualizado = dao.actualizarUsuario(usuario);

            if (actualizado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización exitosa", 
                        "Tus datos se han actualizado correctamente.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                        "No se pudieron actualizar tus datos.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", 
                    "Verifica que el teléfono y la tarjeta sean números válidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", 
                    "Ocurrió un error al actualizar los datos.");
        }
    }

    // 🔴 Eliminar cuenta con confirmación
    @FXML
    private void eliminarCuenta(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Seguro que deseas eliminar tu cuenta?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        ButtonType btnEliminar = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(btnEliminar, btnCancelar);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == btnEliminar) {
                boolean eliminado = dao.eliminarUsuario(usuario.getCodigoUsuario());
                if (eliminado) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Cuenta eliminada", 
                            "Tu cuenta ha sido eliminada correctamente.");
                    volverAlLogin();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                            "No se pudo eliminar tu cuenta.");
                }
            }
        });
    }

    // 🔙 Cerrar sesión (volver al login)
    @FXML
    private void cerrarSesion(ActionEvent event) {
        volverAlLogin();
    }

    // 🔁 Volver al login
    private void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            stage.show();
            System.out.println("⬅️ Sesión cerrada: vuelta al Login.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                    "No se pudo volver a la ventana de inicio de sesión.");
        }
    }

    // 🟡 Mostrar alertas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
