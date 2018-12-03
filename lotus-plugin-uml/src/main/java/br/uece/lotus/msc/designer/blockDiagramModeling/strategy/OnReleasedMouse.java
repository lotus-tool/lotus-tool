/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.*;

/**
 *
 * @author Bruno Barbosa
 */
public class OnReleasedMouse implements Strategy {

    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent e) {

        if(e.getButton() == MouseButton.PRIMARY){
            if(s.mModoAtual == s.MODO_NENHUM){

                if(!e.isShiftDown() && !e.isControlDown()){
                    s.clearSelecao();
                }
                for(Node node : s.mViewer.getNode().getChildren()){
                    if(node instanceof BlockDSView){
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
                                System.out.println("selecionou um model");
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
            }
            
            if (s.mModoAtual == s.MODO_MOVER) {
                s.mViewer.getNode().setCursor(Cursor.OPEN_HAND);
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
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {}
    
}
