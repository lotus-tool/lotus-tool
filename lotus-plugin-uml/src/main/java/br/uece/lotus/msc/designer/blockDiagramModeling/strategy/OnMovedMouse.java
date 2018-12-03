/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnMovedMouse implements Strategy {


    @Override
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

        s.c.setLayoutX(event.getX());
        s.c.setLayoutY(event.getY());

        Object aux = s.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        s.mComponentSobMouse = aux;
    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {}
    
}
