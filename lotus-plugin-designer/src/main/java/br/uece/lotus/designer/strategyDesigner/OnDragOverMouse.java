/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import br.uece.lotus.viewer.StateView;
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
public class OnDragOverMouse implements Strategy{

    private final double AJUSTE_X=20,AJUSTE_y=20;
    
    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {
        
        double xFinal=event.getX(),yFinal=event.getY();
        //a informaÃ§ao esta sendo solta sobre o alvo
        //aceita soltar o mouse somente se nÃ£o Ã© o mesmo nodo de origem 
        //e possui uma string            
        if (event.getGestureSource() != event.getSource()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        Object v = dwi.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        dwi.mVerticeDestinoParaAdicionarTransicao = (v instanceof StateView) ? ((StateView) v) : null;
        /*<><><><><><><><><><><><><><><><><><><><><><<><><><><><><><><><><><><><><><><><><><><><><><><><>*/
        if(dwi.ultimaLinha!=null){
            dwi.mViewer.getNode().getChildren().remove(dwi.ultimaLinha);
        }

        Line linha= createViewFakeTransitionLine(dwi.xInicial, dwi.yInicial, xFinal, yFinal);
        dwi.mViewer.getNode().getChildren().add(linha);
        linha.toBack();///<-coloca a linha por trás do state
        //System.out.println("ADICIONOU");
        dwi.ultimaLinha=linha;

        /*<><><><><><><><><><><><><><><><><><><><><><<><><><><><><><><><><><><><><><><><><><><><><><><><>*/
        event.consume();
    }
    
    private Line createViewFakeTransitionLine(double xInicial, double yInicial, double xFinal, double yFinal) {
        Line linha = new Line();
        linha.setStartX(xInicial+AJUSTE_X);
        linha.setStartY(yInicial+AJUSTE_y);
        linha.setEndX(xFinal);
        linha.setEndY(yFinal);
        linha.setOpacity(0.5);
        linha.getStrokeDashArray().addAll(2d);
        return linha;
    }
    
    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
    
}
