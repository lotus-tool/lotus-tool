package br.uece.lotus.designer.Strategy;

import br.uece.lotus.designer.DesignerWindowImpl;
import javafx.scene.input.MouseEvent;

/**
 * Created by lva on 19/11/15.
 */
public interface Strategy {
    
    void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event);
    
    void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onDragOverMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onDragDroppedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event);
    
    void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event);
}
