/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public interface Strategy {
   
    void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    
    void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event);
    void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event);
    
    void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event);
    
    void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event);
    
    void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event);
    void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event);
}
