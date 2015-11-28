/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.BigState;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.designer.DesignerWindowImpl;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import java.util.List;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnKeyPressed implements Strategy{
    
    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {
        
        if(event.getCode().equals(KeyCode.DELETE)){
            System.out.println("entrou no delete");
            if (dwi.mComponentSobMouse instanceof StateView) {
                State v = ((StateView) dwi.mComponentSobMouse).getState();
                if(v.getValue("bigstate") instanceof BigState){
                    BigState.removeBigState((BigState) v.getValue("bigstate"));
                }
                dwi.mViewer.getComponent().remove(v);
            } else if (dwi.mComponentSobMouse instanceof TransitionView) {
                Transition t = ((TransitionView) dwi.mComponentSobMouse).getTransition();
                State iniTransition = t.getSource();
                State fimTransition = t.getDestiny();
                dwi.mViewer.getComponent().remove(t);
                //Verificar Mais de uma Trasition do mesmo Source e Destiny
                List<Transition> multiplasTransicoes = iniTransition.getTransitionsTo(fimTransition);
                if(multiplasTransicoes.size() > 0){
                    //deletar da tela
                    for(Transition trans : multiplasTransicoes){
                        dwi.mViewer.getComponent().remove(trans);
                    }
                    //recriar transitions
                    for(Transition trans : multiplasTransicoes){
                        dwi.mViewer.getComponent().buildTransition(iniTransition, fimTransition)
                                .setGuard(trans.getGuard())
                                .setLabel(trans.getLabel())
                                .setProbability(trans.getProbability())
                                .setViewType(TransitionView.Geometry.CURVE)
                                .create();
                    }
                }
            }
        }
    }

    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {}

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
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
    
}
