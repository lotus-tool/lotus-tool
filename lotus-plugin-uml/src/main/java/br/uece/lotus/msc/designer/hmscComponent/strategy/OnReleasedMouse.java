/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.geometry.Point2D;
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
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
            if(s.mModoAtual == s.ANY_MODE){

                if(!e.isShiftDown() && !e.isControlDown()){
                    s.clearSelecao();
                }
                for(Node node : s.viewer.getNode().getChildren()){
                    if(node instanceof GenericElementView){
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
                    } else if(node instanceof TransitionMSCView){
                        if(s.selecionadoPeloRetangulo){
                            if(node.getBoundsInParent().intersects(s.rectSelecao.getBoundsInParent())){
                                s.addNoSelecao(node);


                            }
                        }else if(((TransitionMSCView)node).isInsideBoundshMSC(new Point2D(e.getX(),e.getY()))){

                            s.addNoSelecao(node);

                        }


                    }
                }
                s.rectSelecao.setX(0);
                s.rectSelecao.setY(0);
                s.rectSelecao.setWidth(0);
                s.rectSelecao.setHeight(0);
                s.viewer.getNode().getChildren().remove(s.rectSelecao);
            
                if(s.selecionadoPeloRetangulo && !s.mToolBar.getItems().contains(s.paleta)){
                    s.mToolBar.getItems().add(s.paleta);
                }
                
                if(s.componentSobMouse == null){
                    // EXECUTA COM NADA SELECIONADO
                }
            }
            
            if (s.mModoAtual == s.MOVE_MODE) {
                s.viewer.getNode().setCursor(Cursor.OPEN_HAND);
            }
        }
        e.consume();
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
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {}
    
}
