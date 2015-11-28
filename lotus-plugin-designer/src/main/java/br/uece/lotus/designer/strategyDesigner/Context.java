package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Created by lva on 19/11/15.
 */
public class Context {
    private final Strategy strategy;

    public Context(Strategy strategy){
        this.strategy=strategy;
    }
    
    public void executeStrategyOnClikedMouse(DesignerWindowImpl dwi, MouseEvent e){
         strategy.onClickedMouse(dwi, e);
    }
    
    public void executeStrategyOnPressedMouse(DesignerWindowImpl dwi, MouseEvent e){
        strategy.onPressedMouse(dwi, e);
    }
    
    public void executeStrategyOnMovedMouse(DesignerWindowImpl dwi, MouseEvent e){
        strategy.onMovedMouse(dwi, e);
    }
    
    public void executeStrategyOnReleasedMouse(DesignerWindowImpl dwi, MouseEvent e){
        strategy.onReleasedMouse(dwi, e);
    }
    
    public void executeStrategyOnDraggedMouse(DesignerWindowImpl dwi, MouseEvent e){
        strategy.onDraggedMouse(dwi, e);
    }
    
    public void executeStrategyOnDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent e){
        strategy.onDraggedMouse(dwi, e);
    }
    
    public void executeStrategyOnDragOverMouse(DesignerWindowImpl dwi, DragEvent e){
        strategy.onDragOverMouse(dwi, e);
    }
    
    public void executeStrategyOnDragDroppedMouse(DesignerWindowImpl dwi, DragEvent e){
        strategy.onDragDroppedMouse(dwi, e);
    }
    
    public void executeStrategyOnScrollMouse(DesignerWindowImpl dwi, ScrollEvent e){
        strategy.onScrollMouse(dwi, e);
    }
    
    public void executeStrategyOnKeyPressed(DesignerWindowImpl dwi, KeyEvent e){
        strategy.onKeyPressed(dwi, e);
    }
    
    public void executeStrategyOnKeyReleased(DesignerWindowImpl dwi, KeyEvent e){
        strategy.onKeyReleased(dwi, e);
    }
}
