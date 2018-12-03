/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
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
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent e) {
        if(s.mModoAtual == s.ANY_MODE){
            if(e.getButton() == MouseButton.PRIMARY){
                
                double offsetX = e.getX() - s.dragContextMouseAnchorX;
                double offsetY = e.getY() - s.dragContextMouseAnchorY;
                if(!(s.componentSobMouse instanceof GenericElementView)){//ajusta o retangulo se nao for arrastar um block
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


                    Node node = ((GenericElementView)s.componentSobMouse).getNode();

                    if(s.selecionadoPeloRetangulo && s.selecao.contains(node)){
                        for(Node n : s.selecao){

                            GenericElementView view = (GenericElementView) n;
                            GenericElement genericElement = view.getGenericElement();
                            genericElement.setLayoutX(genericElement.getLayoutX()+offsetX);
                            genericElement.setLayoutY(genericElement.getLayoutY()+offsetY);

                        }
                    }else{
                        s.clearSelecao();
                        s.addNoSelecao(node);

                        GenericElement genericElement = ((GenericElementView)s.componentSobMouse).getGenericElement();
                        genericElement.setLayoutX(genericElement.getLayoutX()+offsetX);
                        genericElement.setLayoutY(genericElement.getLayoutY()+offsetY);
                    }
                }
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
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {}
    
}
