/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Line;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDragOver implements Strategy{

    @Override
    public void onDragOverMouse(StandardModelingWindowImpl s, DragEvent event) {
         
        double xFinal = event.getX(), yFinal = event.getY();
        if (event.getGestureSource() != event.getSource()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        
        Object v = s.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        s.hMSC_final = (v instanceof HmscView) ? ((HmscView) v) : null;
        
        if(s.fakeLine != null){
            s.mViewer.getNode().getChildren().remove(s.fakeLine);
        }
        Line l = createViewFakeTransitionLine(s.xInicial, s.yInicial, xFinal, yFinal);
        s.mViewer.getNode().getChildren().add(l);
        l.toBack();
        s.fakeLine = l;
        
        event.consume();
    }
    
    private Line createViewFakeTransitionLine(double xInicial, double yInicial, double xFinal, double yFinal) {
        Line linha = new Line();
        linha.setStartX(xInicial+75);//75 eh a metade da largura do hMSC
        linha.setStartY(yInicial+35);//35 eh a metade da altura do hMSC
        linha.setEndX(xFinal);
        linha.setEndY(yFinal);
        linha.setOpacity(0.5);
        linha.getStrokeDashArray().addAll(2d);
        return linha;
    }
    
    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

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
