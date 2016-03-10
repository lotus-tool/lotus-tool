/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
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
public class OnDraggedMouse implements Strategy{

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        if(s.mModoAtual == s.MODO_NENHUM){
            if(e.getButton() == MouseButton.PRIMARY){
                
                double offsetX = e.getX() - s.dragContextMouseAnchorX;
                double offsetY = e.getY() - s.dragContextMouseAnchorY;
                if(!(s.mComponentSobMouse instanceof HmscView)){//ajusta o retangulo se nao for arrastar um block
                    
                    if(offsetX > 0){
                        s.rectSelecao.setWidth(offsetX);
                    }else{
                        s.rectSelecao.setX(e.getX());
                        s.rectSelecao.setWidth(s.dragContextMouseAnchorX - s.rectSelecao.getX());
                    }
                    if(offsetY > 0){
                        s.rectSelecao.setHeight(offsetY);
                    }else{
                        s.rectSelecao.setY(e.getY());
                        s.rectSelecao.setHeight(s.dragContextMouseAnchorY - s.rectSelecao.getY());
                    }
                }else{//arrastando um ou uns bloco
                    if(!s.segundaVezAoArrastar){
                        s.segundaVezAoArrastar = true;
                        s.ultimoInstanteX = e.getX();
                        s.ultimoInstanteY = e.getY();
                    }else{
                        offsetX = e.getX() - s.ultimoInstanteX;
                        offsetY = e.getY() - s.ultimoInstanteY;
                        s.ultimoInstanteX = e.getX();
                        s.ultimoInstanteY = e.getY();
                    }
                    Node node = ((HmscView)s.mComponentSobMouse).getNode();
                    if(s.selecionadoPeloRetangulo && s.selecao.contains(node)){
                        for(Node n : s.selecao){
                            HmscView view = (HmscView)n;
                            Hmsc b = view.getBlockBuildDS();
                            b.setLayoutX(b.getLayoutX()+offsetX);
                            b.setLayoutY(b.getLayoutY()+offsetY);
                        }
                    }else{
                        s.clearSelecao();
                        s.addNoSelecao(node);
                        Hmsc b = ((HmscView)s.mComponentSobMouse).getBlockBuildDS();
                        b.setLayoutX(b.getLayoutX()+offsetX);
                        b.setLayoutY(b.getLayoutY()+offsetY);
                    }
                }
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
    public void onPressedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
