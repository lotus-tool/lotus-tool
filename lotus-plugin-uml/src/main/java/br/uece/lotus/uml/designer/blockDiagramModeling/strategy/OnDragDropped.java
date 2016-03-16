package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Line;

/**
 * Created by lva on 14/03/16.
 */
public class OnDragDropped implements Strategy {
    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {

    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {
        if(s.mModoAtual != s.MODO_TRANSICAO){
            return;
        }

        if(s.fakeLine != null){
            s.mViewer.getNode().getChildren().remove(s.fakeLine);
        }

        if(s.blockDsFinal != null){
            if(s.mTransitionViewType == 0){
                TransitionMSC t = s.mViewer.getmComponentDS().buildTransition(s.blockDsInicial, s.blockDsFinal)
                        .setViewType(s.mTransitionViewType)
                        .createDS();
                TransitionMSCView tview = (TransitionMSCView)t.getValue("view");
                Line l = tview.getLineTransition();
                l.setStartY(event.getY());
                l.setEndY(event.getY());
                s.updateSequenceTransition();
            }
        }

        event.setDropCompleted(true);
        event.consume();
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
