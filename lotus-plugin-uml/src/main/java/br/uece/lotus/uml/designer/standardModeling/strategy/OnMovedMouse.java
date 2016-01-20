/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindow;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnMovedMouse extends StandardModelingWindow implements Strategy{

    public OnMovedMouse() {
        super();
    }

    @Override
    public void onClickedMouse(MouseEvent event) {}

    @Override
    public void onMovedMouse(MouseEvent event) {
        Object aux = getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        mComponentSobMouse = aux;
    }

    @Override
    public void onDragDetectedMouse(MouseEvent event) {}

    @Override
    public void onDragOverMouse(DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DragEvent event) {}

    @Override
    public void onDraggedMouse(MouseEvent event) {}

    @Override
    public void onPressedMouse(MouseEvent event) {}

    @Override
    public void onReleasedMouse(MouseEvent event) {}

    @Override
    public void onScrollMouse(ScrollEvent event) {}

    @Override
    public void onKeyPressed(KeyEvent event) {}

    @Override
    public void onKeyReleased(KeyEvent event) {}
    
}
