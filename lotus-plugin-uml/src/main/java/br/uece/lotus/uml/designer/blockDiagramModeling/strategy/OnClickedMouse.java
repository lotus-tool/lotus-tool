/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;


import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;

import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.scene.input.*;

/**
 *
 * @author Bruno Barbosa
 */
public class OnClickedMouse implements Strategy {


    private static final int LARGURA = 100, ALTURA = 60;

    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent e) {
        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            s.setComponenteSelecionado(s.mComponentSobMouse);
            
            if (s.mComponentSelecionado instanceof BlockDSView) {
                s.mContextMenuBlockDs.show(s.mViewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                s.mContextMenuBlockDs.hide();
            }
            return;
        }else{
           s.mContextMenuBlockDs.hide();
        }
        //resetando zoom pelo mouse
        if ((e.isControlDown() && e.getButton() == MouseButton.MIDDLE) || (e.getClickCount() == 2 && s.mModoAtual == s.MODO_MOVER)) {

            s.mViewer.getNode().setScaleX(s.mViewerScaleXPadrao);
            s.mViewer.getNode().setScaleY(s.mViewerScaleYPadrao);

            s.mViewer.getNode().setTranslateX(s.mViewerTranslateXPadrao);
            s.mViewer.getNode().setTranslateY(s.mViewerTranslateYPadrao);
        }
        //verificando por controles de butoes
        if(s.mModoAtual == s.MODO_NENHUM){
             if (s.mComponentSobMouse != null && (s.mComponentSobMouse instanceof BlockDSView) && !s.mToolBar.getItems().contains(s.paleta)) {
                 s.mToolBar.getItems().add(s.paleta);
             }
             else{
                 if(!s.selecionadoPeloRetangulo){
                    s.mToolBar.getItems().remove(s.paleta);
                 }
             }
        }
        else if(s.mModoAtual == s.MODO_BLOCO){
            if (!(s.mComponentSobMouse instanceof BlockDSView)) {
                System.out.println("contID"+ s.contID);
                    if (s.contID == -1) {
                        s.updateContID();
                    }
                    BlockDS b = s.mViewer.getmComponentDS().newBlockDS(s.contID);
                    s.contID++;
                System.out.println((e.getX()-(LARGURA/2))+"%%"+(e.getY()-(ALTURA/2)));
                b.setLayoutX(e.getX() -(LARGURA/2));
//                    b.setLayoutY(e.getY()-(ALTURA/2));
                    b.setLabel("New DS");
                }
        }
        else if(s.mModoAtual == s.MODO_REMOVER){
            if(s.mComponentSobMouse instanceof BlockDSView){
                BlockDS b = ((BlockDSView)s.mComponentSobMouse).getBlockDS();
                s.mViewer.getmComponentDS().remove(b);
            }
            //falta a transition
        }
          
    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event) {}

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
