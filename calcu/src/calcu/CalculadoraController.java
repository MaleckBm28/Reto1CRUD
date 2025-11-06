/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calcu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author iandasal
 */
public class CalculadoraController implements Initializable {
    
    @FXML
    private Label campo;
    Double num1 = 0.0;
    String operador = "";
    Double resultado;
    
    @FXML
    private void ponerNumero(ActionEvent event) {
        if(resultado != null){
            campo.setText("");
            resultado = null;
        }
        String num = ((Button) event.getSource()).getText();
        campo.setText(campo.getText() + num);
    }
    
    @FXML
    private void ponerPunto(ActionEvent event) {
        if(!campo.getText().contains(".")){
            String num = ((Button) event.getSource()).getText();
            campo.setText(campo.getText() + num);
        }
    }
    
    @FXML
    private void operar(ActionEvent event) {
        num1 = Double.parseDouble(campo.getText());
        operador = ((Button)event.getSource()).getText();
        campo.setText("");
    }
    
    @FXML
    private void igual(ActionEvent event) {
        Double num2 = Double.parseDouble(campo.getText());
        switch(operador){
            case "/":
                resultado = num1 / num2;
                break;
            case "*":
                resultado = num1 * num2;
                break;
            case "-":
                resultado = num1 - num2;
                break;
            case "+":
                resultado = num1 + num2;
                break;
            default:
                System.err.println("Algo ha salido mal con la selección del operador");
        }
        if(resultado != null){
            campo.setText(String.valueOf(resultado));
        }
    }
    
    @FXML
    private void limpiar(ActionEvent event) {
        campo.setText("");
        num1 = 0.0;
        resultado = null;
        operador = "";
    }
    
    @FXML
    private void borrar(ActionEvent event) {
        char[] texto = campo.getText().toCharArray();
        String textoSinUltimo = "";
        for (int i = 0; i < texto.length-1; i++) {
            textoSinUltimo += texto[i];
        }
        campo.setText(textoSinUltimo);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
