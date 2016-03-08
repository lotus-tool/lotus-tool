/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.viewer.builder.BlockBuildDSView;
import static br.uece.lotus.uml.api.viewer.builder.BlockBuildDSViewImpl.ALTURA;
import static br.uece.lotus.uml.api.viewer.builder.BlockBuildDSViewImpl.LARGURA;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnClickedMouse implements Strategy {

    
    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            s.setComponenteSelecionado(s.mComponentSobMouse);
            
            if (s.mComponentSelecionado instanceof BlockBuildDSView) {
                s.mContextMenuBlockBuild.show(s.mViewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                s.mContextMenuBlockBuild.hide();
            }
            return;
        }else{
           s.mContextMenuBlockBuild.hide();
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
             if (s.mComponentSobMouse != null && (s.mComponentSobMouse instanceof BlockBuildDSView) && !s.mToolBar.getItems().contains(s.paleta)) {
                 s.mToolBar.getItems().add(s.paleta);
             }
             else{
                 if(!s.selecionadoPeloRetangulo){
                    s.mToolBar.getItems().remove(s.paleta);
                 }
             }
        }
        else if(s.mModoAtual == s.MODO_BLOCO){
            if (!(s.mComponentSobMouse instanceof BlockBuildDSView)) {
                    if (s.contID == -1) {
                        s.updateContID();
                    }
                    BlockBuildDS b = s.mViewer.getComponentBuildDS().newBlock(s.contID);
                    s.contID++;
                    b.setLayoutX(e.getX()-(LARGURA/2));
                    b.setLayoutY(e.getY()-(ALTURA/2));
                    b.setLabel("New hMSC");
                }
        }
        else if(s.mModoAtual == s.MODO_REMOVER){
            if(s.mComponentSobMouse instanceof BlockBuildDSView){
                BlockBuildDS b = ((BlockBuildDSView)s.mComponentSobMouse).getBlockBuildDS();
                s.mViewer.getComponentBuildDS().remove(b);
            }
            //falta a transition
        }
          
    }

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
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
