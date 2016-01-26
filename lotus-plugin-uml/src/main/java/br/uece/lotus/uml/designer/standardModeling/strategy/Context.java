/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
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

    public void executeStrategyOnMovedMouse(StandardModelingWindowImpl s, MouseEvent e){
        strategy.onMovedMouse(s,e);
    }

    public void executeStrategyOnClikedMouse(StandardModelingWindowImpl s, MouseEvent event) {
        strategy.onClickedMouse(s,event);
    }
    
    public void executeStrategyOnPressedMouse(StandardModelingWindowImpl s, MouseEvent event) {
        strategy.onPressedMouse(s, event);
    }
    
    public void executeStrategyOnDraggedMouse(StandardModelingWindowImpl s, MouseEvent event) {
        strategy.onDraggedMouse(s, event);
    }
    
    public void executeStrategyOnReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {
        strategy.onReleasedMouse(s, event);
    }
}
