/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnPressedMouse implements Strategy{

    
    @Override
    public void onPressedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        
        if(s.mModoAtual == s.MODO_NENHUM){
            
            s.dragContextMouseAnchorX = e.getX();
            s.dragContextMouseAnchorY = e.getY();
            s.segundaVezAoArrastar = false;
            s.ultimoInstanteX = 0;
            s.ultimoInstanteY = 0;
            
            s.rectSelecao.setX(s.dragContextMouseAnchorX);
            s.rectSelecao.setY(s.dragContextMouseAnchorY);
            s.rectSelecao.setWidth(0);
            s.rectSelecao.setHeight(0);
            s.mViewer.getNode().getChildren().add(s.rectSelecao);

        }
        
        if(s.mModoAtual == s.MODO_MOVER){
            s.mViewer.getNode().setCursor(Cursor.CLOSED_HAND);
        }
        
        e.consume();
    }
    
    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent event) {}
    
    @Override
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
