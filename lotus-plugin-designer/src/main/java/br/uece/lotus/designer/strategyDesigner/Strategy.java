package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Created by lva on 19/11/15.
 */
public interface Strategy {
    
    void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event);
    
    void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event);
    void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event);
    void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event);
    
    void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event);
    void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event);
    
    void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event);
    
    void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event);
    void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event);
}
