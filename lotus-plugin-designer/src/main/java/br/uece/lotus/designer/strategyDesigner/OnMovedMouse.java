/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import br.uece.lotus.viewer.ComponentView;
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

    private ComponentView mViewer;

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {
        
        Object aux = dwi.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        dwi.mComponentSobMouse = aux;
    }

    
    @Override
    public void onDragDetectedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl Dwi, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl Dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onClickedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
    
}

