/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public interface Strategy {
   
    void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event);
    void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event);
    
    void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event);
    void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event);
    void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event);
    
    void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event);
    void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event);
    void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event);
    
    void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event);
    
    void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event);
    void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event);
}
