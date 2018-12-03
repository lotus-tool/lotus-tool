/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnPressedMouse implements Strategy{

    
    @Override
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent e) {
        
        if(s.mModoAtual == s.ANY_MODE && MouseButton.PRIMARY.equals(e.getButton())){
            
            s.dragContextMouseAnchorX = e.getX();
            s.dragContextMouseAnchorY = e.getY();
            s.segundaVezAoArrastar = false;
            s.ultimoInstanteX = 0;
            s.ultimoInstanteY = 0;
            
            s.rectSelecao.setX(s.dragContextMouseAnchorX);
            s.rectSelecao.setY(s.dragContextMouseAnchorY);
            s.rectSelecao.setWidth(0);
            s.rectSelecao.setHeight(0);

            if(s.viewer.getNode() != null){
                s.viewer.getNode().getChildren().add(s.rectSelecao);
            }


        }
        
        if(s.mModoAtual == s.MOVE_MODE){
            s.viewer.getNode().setCursor(Cursor.CLOSED_HAND);
        }
        
        e.consume();
    }
    
    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {}
    
    @Override
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {}
    
}
