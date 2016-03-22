/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public interface Strategy {
   
    void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    
    void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event);
    void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event);
    
    void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event);
    
    void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event);
    
    void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event);
    void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event);
}
