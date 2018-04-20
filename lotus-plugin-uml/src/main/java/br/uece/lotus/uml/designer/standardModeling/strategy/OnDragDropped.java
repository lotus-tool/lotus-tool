/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDragDropped implements Strategy{

    @Override
    public void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event) {
        if(s.mModoAtual != s.MODO_TRANSICAO){
            return;
        }
        
        if(s.fakeLine != null){
            s.mViewer.getNode().getChildren().remove(s.fakeLine);
        }
        
        if(s.hMSC_final != null){
            if(existTransition(s)){
                s.mTransitionViewType = 1;
            }

            TransitionMSC t = s.mViewer.getComponentBuildDS().buildTransition(s.hMSC_inicial, s.hMSC_final)
                    .setViewType(s.mTransitionViewType)
                    .create();

        }
        
        event.setDropCompleted(true);
        event.consume();
    }

    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(StandardModelingWindowImpl s, DragEvent event) {}
    
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

    private boolean existTransition(StandardModelingWindowImpl s){
        if( (s.hMSC_inicial.getHMSC().getTransitionTo(s.hMSC_final.getHMSC())) != null){
            return true;
        }else if( (s.hMSC_final.getHMSC().getTransitionTo(s.hMSC_inicial.getHMSC())) != null){
            return true;
        }
            return false;
    }
    
}
