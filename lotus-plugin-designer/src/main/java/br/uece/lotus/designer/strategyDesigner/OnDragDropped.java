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
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_TRANSICAO;
import br.uece.lotus.viewer.TransitionView;
import java.util.List;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDragDropped implements Strategy{

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event) {
        if (dwi.mModoAtual != MODO_TRANSICAO) {
            return;
        }
        
        if(dwi.ultimaLinha!=null){
            dwi.mViewer.getNode().getChildren().remove(dwi.ultimaLinha);
        }

        if (dwi.mVerticeDestinoParaAdicionarTransicao != null) {
            if (BigState.verifyIsBigState(dwi.mVerticeDestinoParaAdicionarTransicao.getState())) {
                JOptionPane.showMessageDialog(null, "Impossible to create transitions for a BigState!", "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }

            State o = dwi.mVerticeOrigemParaAdicionarTransicao.getState();
            State d = dwi.mVerticeDestinoParaAdicionarTransicao.getState();
            dwi.mExibirPropriedadesTransicao = true;

            int qtdeTransitionOD = o.getTransitionsTo(d).size();
            int qtdeTransitionDO = d.getTransitionsTo(o).size();
            List<Transition> transitionsOD = o.getTransitionsTo(d);
            List<Transition> transitionsDO = d.getTransitionsTo(o);
            boolean temLineOD = verificarSeExisteTransitionLine(transitionsOD);
            boolean temLineDO = verificarSeExisteTransitionLine(transitionsDO);

            if(dwi.mTransitionViewType == 1){ // curve
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(dwi.mTransitionViewType)
                        .create();
                applyDefaults(t,dwi);
            }
            //line  com auto ajuste
            else if(qtdeTransitionOD == 0 && qtdeTransitionDO == 0){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(dwi.mTransitionViewType)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD == 0 && qtdeTransitionDO > 0 && !temLineDO){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.LINE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD == 0 && qtdeTransitionDO > 0 && temLineDO){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD > 0 && !temLineOD && qtdeTransitionDO == 0){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.LINE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD > 0 && temLineOD && qtdeTransitionDO == 0){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD > 0 && !temLineOD && qtdeTransitionDO > 0 && !temLineDO){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.LINE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD > 0 && temLineOD && qtdeTransitionDO > 0 && !temLineDO){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
                applyDefaults(t,dwi);
            }
            else if(qtdeTransitionOD > 0 && !temLineOD &&qtdeTransitionDO > 0 && temLineDO){
                Transition t = dwi.mViewer.getComponent().buildTransition(o, d)
                        .setViewType(TransitionView.Geometry.CURVE)
                        .create();
                applyDefaults(t,dwi);
            }
        }

        event.setDropCompleted(true);
        event.consume();
    }
    
    private boolean verificarSeExisteTransitionLine(List<Transition> transitions){
        boolean line = false;
        for(Transition t : transitions){
            if((int)t.getValue("view.type") == 0){
                line = true;
            }
        }
        return line;
    }
    
    private void applyDefaults(Transition t, DesignerWindowImpl dwi) {
        if (dwi.mDefaultTransitionLabel != null) {
            t.setLabel(dwi.mDefaultTransitionLabel);
        }
        if (dwi.mDefaultTransitionColor != null) {
            t.setColor(dwi.mDefaultTransitionColor);
        }
        if (dwi.mDefaultTransitionTextColor != null) {
            t.setTextColor(dwi.mDefaultTransitionTextColor);
        }
        if (dwi.mDefaultTransitionWidth != null) {
            t.setWidth(dwi.mDefaultTransitionWidth);
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
