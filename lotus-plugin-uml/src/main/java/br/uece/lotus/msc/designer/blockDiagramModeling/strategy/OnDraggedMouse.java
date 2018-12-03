/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;


import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.scene.Node;
import javafx.scene.input.*;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDraggedMouse implements Strategy {

    @Override
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent e) {
        if(s.mModoAtual == s.MODO_NENHUM){
            if(e.getButton() == MouseButton.PRIMARY){
                
                double offsetX = e.getX() - s.dragContextMouseAnchorX;
                double offsetY = e.getY() - s.dragContextMouseAnchorY;

                if(s.mComponentSobMouse instanceof TransitionMSCView){
                    TransitionMSC t = ((TransitionMSCView)s.mComponentSobMouse).getTransition();

                }
                if(!(s.mComponentSobMouse instanceof BlockDSView) ){//ajusta o retangulo se nao for arrastar um block
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
                }else{//arrastando um block
                    if(!s.segundaVezAoArrastar){
                        s.segundaVezAoArrastar = true;
                        s.ultimoInstanteX = e.getX();
                        s.ultimoInstanteY = e.getY();
                    }else{
                        offsetX = e.getX() - s.ultimoInstanteX;
                        //offsetY = e.getY() - run.ultimoInstanteY;
                        s.ultimoInstanteX = e.getX();
                        s.ultimoInstanteY = e.getY();
                    }
                    Node node = ((BlockDSView) s.mComponentSobMouse).getNode();
                    if(s.selecionadoPeloRetangulo && s.selecao.contains(node)){
                        for(Node n : s.selecao){
                            BlockDSView view = (BlockDSView)n;
                            BmscBlock b = view.getBlockDS();
                            b.setLayoutX(b.getLayoutX()+offsetX);
                            //b.setLayoutY(b.getLayoutY()+offsetY);
                        }
                    }else{
                       s.clearSelecao();
                       s.addNoSelecao(node);
                        BmscBlock b = ((BlockDSView)s.mComponentSobMouse).getBlockDS();
                        b.setLayoutX(b.getLayoutX()+offsetX);
                        //b.setLayoutY(b.getLayoutY()+offsetY);
                    }
                }
            }
        }
        
        e.consume();
    }

    @Override
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {}
    
    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {}
    
}
