package controlador;

<<<<<<< HEAD
import javafx.fxml.FXML;
import javafx.scene.control.Label;
=======
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
import modelo.Usuario;

public class PanelAdminController {

<<<<<<< HEAD
    @FXML private Label lblBienvenida;

    public void setUsuario(Usuario usuario) {
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
=======
    private Usuario usuario; // el usuario que vino del login

    /**
     * Método para recibir el usuario desde LoginController
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("👤 Usuario cargado en PanelAdmin: " + usuario.getEmail());
    }

    /**
     * Método vinculado al botón Modificar Usuario
     * (corresponde al onAction="#modificarUsuario" del FXML)
     */
    @FXML
    private void modificarUsuario(ActionEvent event) {
        if (usuario != null) {
            System.out.println("🟠 Modificando usuario: " + usuario.getEmail());

            // 🔹 Aquí podrías lanzar tu hilo HiloModificar:
            // HiloModificar hilo = new HiloModificar(usuario);
            // Thread t = new Thread(hilo, "Hilo-Modificar");
            // t.start();

        } else {
            System.out.println("⚠️ No hay usuario cargado en el panel.");
        }
    }

    // (Opcional) puedes ir preparando otros métodos del panel:
    @FXML
    private void insertarUsuario(ActionEvent event) {
        System.out.println("🟢 Insertar usuario (pendiente de implementación)");
    }

    @FXML
    private void eliminarUsuario(ActionEvent event) {
        System.out.println("🔴 Eliminar usuario (pendiente de implementación)");
    }

    @FXML
    private void leerUsuarios(ActionEvent event) {
        System.out.println("📘 Leer usuarios (pendiente de implementación)");
>>>>>>> 1c47af27f8e65d46621e7fedaa60d700259cf29b
    }
}
