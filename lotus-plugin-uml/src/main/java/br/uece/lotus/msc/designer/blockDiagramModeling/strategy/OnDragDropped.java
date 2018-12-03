package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
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
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {

    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {
        if(s.mModoAtual != s.MODO_TRANSICAO){
            return;
        }

        if(s.fakeLine != null){
            s.mViewer.getNode().getChildren().remove(s.fakeLine);
        }

        if(s.blockDsFinal != null){
            if(s.mTransitionViewType == 0){
                TransitionMSC t = s.mViewer.getBmscComponent().buildTransition(s.blockDsInicial, s.blockDsFinal)
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
