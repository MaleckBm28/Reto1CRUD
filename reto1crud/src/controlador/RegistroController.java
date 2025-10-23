/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;


public class RegistroController {
    
     private static Dao dao = new DaoImplementacion();

    @FXML private ComboBox<String> cbGenero;

    @FXML public void initialize() {
        cbGenero.getItems().addAll("Masculino", "Femenino", "Otro");
    }



}
