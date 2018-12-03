/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnMovedMouse implements Strategy{

    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        Object aux = s.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        s.componentSobMouse = aux;
        //para a transicao
        s.mBounds.setLayoutX(event.getX());
        s.mBounds.setLayoutY(event.getY());
    }

    @Override
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {}
    
}
