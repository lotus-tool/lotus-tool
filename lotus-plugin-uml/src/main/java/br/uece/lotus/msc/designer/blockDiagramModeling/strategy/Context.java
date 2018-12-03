/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
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


    public void executeStrategyOnMovedMouse(DesingWindowImplBlockMSC s, MouseEvent e){
        strategy.onMovedMouse(s,e);
    }

    public void executeStrategyOnClikedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {
        strategy.onClickedMouse(s,event);
    }
    
    public void executeStrategyOnPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {
        strategy.onPressedMouse(s, event);
    }
    
    public void executeStrategyOnDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {
        strategy.onDraggedMouse(s, event);
    }
    
    public void executeStrategyOnReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {
        strategy.onReleasedMouse(s, event);
    }

    public void executeStrategyOnDragDetectedMouse(DesingWindowImplBlockMSC desingWindowImplBlockDs, MouseEvent event) {
        strategy.onDragDetectedMouse(desingWindowImplBlockDs,event);
    }

    public void executeStrategyOnDragOverMouse(DesingWindowImplBlockMSC desingWindowImplBlockDs, DragEvent event) {
        strategy.onDragOverMouse(desingWindowImplBlockDs,event);
    }

    public void executeStrategyOnDragDroppedMouse(DesingWindowImplBlockMSC desingWindowImplBlockDs, DragEvent event) {
        strategy.onDragDroppedMouse(desingWindowImplBlockDs,event);

    }
}
