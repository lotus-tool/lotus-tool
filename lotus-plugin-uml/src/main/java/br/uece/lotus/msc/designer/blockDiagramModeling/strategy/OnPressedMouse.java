/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnPressedMouse implements Strategy {

    
    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent e) {
        
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
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}
    
    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {}
    
}
