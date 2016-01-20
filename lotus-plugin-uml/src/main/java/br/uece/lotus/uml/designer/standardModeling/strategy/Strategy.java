/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public interface Strategy {
   
    void onClickedMouse(MouseEvent event);
    void onMovedMouse(MouseEvent event);
    
    void onDragDetectedMouse(MouseEvent event);
    void onDragOverMouse(DragEvent event);
    void onDragDroppedMouse( DragEvent event);
    void onDraggedMouse(MouseEvent event);
    
    void onPressedMouse(MouseEvent event);
    void onReleasedMouse(MouseEvent event);
    
    void onScrollMouse(ScrollEvent event);
    
    void onKeyPressed(KeyEvent event);
    void onKeyReleased(KeyEvent event);
}
