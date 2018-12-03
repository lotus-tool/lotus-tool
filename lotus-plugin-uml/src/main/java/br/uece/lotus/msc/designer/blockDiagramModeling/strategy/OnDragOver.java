package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.shape.Line;

/**
 * Created by lva on 14/03/16.
 */
public class OnDragOver implements Strategy {
    @Override
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    private DragEvent e;
    @Override
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {
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
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {

    }

    @Override
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {

    }

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {

    }

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {

    }
}
