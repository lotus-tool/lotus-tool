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
public class OnDraggedMouse implements Strategy{

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        if(s.mModoAtual == s.MODO_NENHUM){
            double offsetX = e.getSceneX() - s.dragContextMouseAnchorX;
            double offsetY = e.getSceneY() - s.dragContextMouseAnchorY;
            
            if(offsetX > 0){
                s.rectSelecao.setWidth(offsetX);
            }else{
                s.rectSelecao.setX(e.getSceneX());
                s.rectSelecao.setWidth(s.dragContextMouseAnchorX - s.rectSelecao.getX());
            }
            if(offsetY > 0){
                s.rectSelecao.setHeight(offsetY);
            }else{
                s.rectSelecao.setY(e.getSceneY());
                s.rectSelecao.setHeight(s.dragContextMouseAnchorY - s.rectSelecao.getY());
            }
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
    public void onPressedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
