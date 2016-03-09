/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;


import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.viewer.block.BlockDSView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.scene.Node;
import javafx.scene.input.*;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDraggedMouse implements Strategy {

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent e) {
        if(s.mModoAtual == s.MODO_NENHUM){
            if(e.getButton() == MouseButton.PRIMARY){
                
                double offsetX = e.getX() - s.dragContextMouseAnchorX;
                double offsetY = e.getY() - s.dragContextMouseAnchorY;
                if(!(s.mComponentSobMouse instanceof BlockDSView)){//ajusta o retangulo se nao for arrastar um block
                    
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
                        offsetY = e.getY() - s.ultimoInstanteY;
                        s.ultimoInstanteX = e.getX();
                        s.ultimoInstanteY = e.getY();
                    }
                    if(s.selecionadoPeloRetangulo){
                        for(Node n : s.selecao){
                            BlockDSView view = (BlockDSView)n;
                            BlockDS b = view.getBlockDS();
                            b.setLayoutX(b.getLayoutX()+offsetX);
                            b.setLayoutY(b.getLayoutY()+offsetY);
                        }
                    }else{
                        BlockDS b = ((BlockDSView)s.mComponentSobMouse).getBlockDS();
                        b.setLayoutX(b.getLayoutX()+offsetX);
                        b.setLayoutY(b.getLayoutY()+offsetY);
                    }
                }
            }
        }
        
        e.consume();
    }

    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {}
    
    @Override
    public void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event) {}
    
}
