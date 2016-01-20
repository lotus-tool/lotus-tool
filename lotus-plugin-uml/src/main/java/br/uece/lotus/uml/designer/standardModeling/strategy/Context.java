/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindow;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class Context{

    private final Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategyOnMovedMouse(MouseEvent e){
        strategy.onMovedMouse(e);
    }

    public void executeStrategyOnClikedMouse(MouseEvent event) {
        strategy.onClickedMouse(event);
    }
    
    
    
    
}
