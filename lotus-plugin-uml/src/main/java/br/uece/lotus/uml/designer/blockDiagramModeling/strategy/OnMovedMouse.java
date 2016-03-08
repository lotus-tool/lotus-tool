/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;

/**
 *
 * @author Bruno Barbosa
 */
public class OnMovedMouse implements Strategy {

    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
        Circle c = new Circle(5);
        c.setLayoutX(event.getX());
        c.setLayoutY(event.getY());

        Object aux = s.getComponentePelaPosicaoMouse(/*new Point2D(event.getSceneX(), event.getSceneY())*/ c);
        s.mComponentSobMouse = aux;
    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event) {}
    
}
