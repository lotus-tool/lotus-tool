/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.scene.input.DragEvent;
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

    public void executeStrategyOnMovedMouse(HmscWindowMSCImpl s, MouseEvent e){
        strategy.onMovedMouse(s,e);
    }

    public void executeStrategyOnClikedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        strategy.onClickedMouse(s,event);
    }
    
    public void executeStrategyOnPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        strategy.onPressedMouse(s, event);
    }
    
    public void executeStrategyOnDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        strategy.onDraggedMouse(s, event);
    }
    
    public void executeStrategyOnReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        strategy.onReleasedMouse(s, event);
    }
    
    public void executeStrategyOnDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent e){
        strategy.onDragDetectedMouse(s, e);
    }
    
    public void executeStrategyOnDragOverMouse(HmscWindowMSCImpl s, DragEvent e){
        strategy.onDragOverMouse(s, e);
    }
    
    public void executeStrategyOnDragDroppedMouse(HmscWindowMSCImpl s, DragEvent e){
        strategy.onDragDroppedMouse(s, e);
    }
}
