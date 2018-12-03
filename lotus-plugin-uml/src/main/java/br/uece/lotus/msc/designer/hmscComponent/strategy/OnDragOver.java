/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
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
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {

        double xFinal = event.getX(), yFinal = event.getY();
        if (event.getGestureSource() != event.getSource()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        
        Object v = s.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        s.finalGenericElementView = (v instanceof GenericElementView) ? ((GenericElementView) v) : null;
        
        if(s.fakeLine != null){
            s.viewer.getNode().getChildren().remove(s.fakeLine);
        }
        Line l = createViewFakeTransitionLine(s.xInicial, s.yInicial, xFinal, yFinal);
        s.viewer.getNode().getChildren().add(l);
        l.toBack();
        s.fakeLine = l;
        
        event.consume();
    }
    
    private Line createViewFakeTransitionLine(double xInicial, double yInicial, double xFinal, double yFinal) {
        Line linha = new Line();
      //  linha.setStartX(xInicial+75);//75 eh a metade da largura do hMSC
       // linha.setStartY(yInicial+35);//35 eh a metade da altura do hMSC
        linha.setStartX(xInicial);
        linha.setStartY(yInicial);
        linha.setEndX(xFinal);
        linha.setEndY(yFinal);
        linha.setOpacity(0.5);
        linha.getStrokeDashArray().addAll(2d);
        return linha;
    }
    
    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

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
