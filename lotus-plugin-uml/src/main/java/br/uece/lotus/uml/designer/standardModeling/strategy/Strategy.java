/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

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
   
    void onClickedMouse(StandardModelingWindowImpl s, MouseEvent event);
    void onMovedMouse(StandardModelingWindowImpl s, MouseEvent event);
    
    void onDragDetectedMouse(StandardModelingWindowImpl s, MouseEvent event);
    void onDragOverMouse(StandardModelingWindowImpl s, DragEvent event);
    void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event);
    
    void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent event);
    void onPressedMouse(StandardModelingWindowImpl s, MouseEvent event);
    void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event);
    
    void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event);
    
    void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event);
    void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event);
}
