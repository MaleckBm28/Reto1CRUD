/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calcu;

import java.net.URL;
import java.util.ResourceBundle;
import calcu.CalculadoraController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.control.LabeledMatchers.hasText;


/**
 *
 * @author 2dam
 */
public class CalculadoraControllerTest extends ApplicationTest {
    
    
    
    
    @Test
    public void test_InitialStateTest() {
        
        
        @BeforeClass
        
        
        
        verifyThat("#campo",hasText("7"));
        
    }
  
}
