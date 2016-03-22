/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface WindowDS {
    
    StandardModeling getComponentBuildDS();
    ComponentDS getComponentDS();
    Component getComponentLTS();
    
    void setComponentBuildDS(StandardModeling buildDS);
    void setComponentDS(ComponentDS cds);
    void setComponentLTS(Component c);
    
    String getTitle();
    Node getNode();
}
