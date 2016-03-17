package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Created by lva on 14/03/16.
 */
public class OnDragOver implements Strategy {
    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    private DragEvent e;
    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {
        e = event;
        double xFinal = event.getX(), yFinal = event.getY();
        if (event.getGestureSource() != event.getSource()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }


        Object v = s.getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));

        s.blockDsFinal = (v instanceof BlockDSView) ? ((BlockDSView) v) : null;

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
        linha.setStartX(xInicial+50);//75 eh a metade da largura do bMSC
        linha.setStartY(e.getY());
        linha.setEndX(xFinal);
        linha.setEndY(yFinal);
        linha.setOpacity(0.5);
        linha.getStrokeDashArray().addAll(2d);
        return linha;
    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {

    }

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event) {

    }

    @Override
    public void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event) {

    }

    @Override
    public void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event) {

    }
}
