package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import javafx.scene.input.MouseEvent;

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
}
