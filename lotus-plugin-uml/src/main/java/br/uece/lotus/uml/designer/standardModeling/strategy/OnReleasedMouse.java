/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnReleasedMouse implements Strategy{

    @Override
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
            if(s.mModoAtual == s.MODO_NENHUM){

                if(!e.isShiftDown() && !e.isControlDown()){
                    s.clearSelecao();
                }
                for(Node node : s.mViewer.getNode().getChildren()){
                    if(node instanceof HmscView){
                        if(node.getBoundsInParent().intersects(s.rectSelecao.getBoundsInParent())){
                            if(e.isShiftDown()){
                                s.addNoSelecao(node);
                            }
                            else if(e.isControlDown()){
                                if(s.containsNoSelecao(node)){
                                    s.removeNoSelecao(node);
                                }else{
                                    s.addNoSelecao(node);
                                }
                            }
                            else{
                                s.addNoSelecao(node);
                            }
                        }
                    }
                }
                s.rectSelecao.setX(0);
                s.rectSelecao.setY(0);
                s.rectSelecao.setWidth(0);
                s.rectSelecao.setHeight(0);
                s.mViewer.getNode().getChildren().remove(s.rectSelecao);
            
                if(s.selecionadoPeloRetangulo && !s.mToolBar.getItems().contains(s.paleta)){
                    s.mToolBar.getItems().add(s.paleta);
                }
                
                if(s.mComponentSobMouse == null){
                    // EXECUTA COM NADA SELECIONADO
                }
            }
            
            if (s.mModoAtual == s.MODO_MOVER) {
                s.mViewer.getNode().setCursor(Cursor.OPEN_HAND);
            }
        }
        e.consume();
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
    public void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onPressedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
