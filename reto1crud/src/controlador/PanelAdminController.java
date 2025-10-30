package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import modelo.Usuario;

public class PanelAdminController {

    @FXML private Label lblBienvenida;

    public void setUsuario(Usuario usuario) {
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
    }
}
