/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
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
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {
        if(s.mModoAtual != s.TRANSITION_MODE){
            return;
        }
        
        if(s.fakeLine != null){
            s.viewer.getNode().getChildren().remove(s.fakeLine);
        }
        
        if(s.finalGenericElementView != null){
            if(existTransition(s)){
              //  s.mTransitionViewType = 1;
            }

            TransitionMSC t = s.viewer.getHmscComponent().buildTransitionMSC(s.initialGenericElementView, s.finalGenericElementView)
                    .setViewType(s.mTransitionViewType)
                    .create();

        }
        
        event.setDropCompleted(true);
        event.consume();
    }

    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {}
    
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

    private boolean existTransition(HmscWindowMSCImpl s){
        if( (s.initialGenericElementView.getGenericElement().getTransitionTo(s.finalGenericElementView.getGenericElement())) != null){
            return true;
        }else if( (s.finalGenericElementView.getGenericElement().getTransitionTo(s.initialGenericElementView.getGenericElement())) != null){
            return true;
        }
            return false;
    }
    
}
