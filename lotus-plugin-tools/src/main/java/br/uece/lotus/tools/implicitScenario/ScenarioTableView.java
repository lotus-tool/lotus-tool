/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Bruno Barbosa
 */
public class ScenarioTableView {
    private final SimpleStringProperty implicitScenario;
        
    public ScenarioTableView(String cenario){
        this.implicitScenario = new SimpleStringProperty(cenario);
    }
    
    public StringProperty implicitScenarioProperty(){
        return implicitScenario;
    }
}
