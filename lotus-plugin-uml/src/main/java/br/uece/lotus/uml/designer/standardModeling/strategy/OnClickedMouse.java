/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.viewer.builder.BlockBuildDSView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnClickedMouse extends StandardModelingWindow implements Strategy{

    public OnClickedMouse() {
        super();
    }

    @Override
    public void onClickedMouse(MouseEvent e) {
        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            setComponenteSelecionado(mComponentSobMouse);
            
            if (mComponentSelecionado instanceof BlockBuildDSView) {
                mContextMenuBlockBuild.show(mViewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                mContextMenuBlockBuild.hide();
            }
            return;
        }else{
           mContextMenuBlockBuild.hide();
        }
        //resetando zoom pelo mouse
        if (e.isControlDown() && e.getButton() == MouseButton.MIDDLE) {

            mViewer.getNode().setScaleX(mViewerScaleXPadrao);
            mViewer.getNode().setScaleY(mViewerScaleYPadrao);

            mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
            mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
        }
        //verificando por controles de butoes
        if(mModoAtual == MODO_NENHUM){
            
        }
        else if(mModoAtual == MODO_BLOCO){
            if (!(mComponentSobMouse instanceof BlockBuildDSView)) {
                    if (contID == -1) {
                        updateContID();
                    }
                    int id = mViewer.getComponentBuildDS().getBlocos().size();
                    BlockBuildDS b = mViewer.getComponentBuildDS().newBlock(id);
                    b.setID(contID);
                    contID++;
                    b.setLayoutX(e.getX());
                    b.setLayoutY(e.getY());
                    b.setLabel("New Block");
                }
        }
        else if(mModoAtual == MODO_REMOVER){
            
        }
          
    }

    @Override
    public void onMovedMouse(MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(MouseEvent event) {}

    @Override
    public void onDragOverMouse(DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DragEvent event) {}

    @Override
    public void onDraggedMouse(MouseEvent event) {}

    @Override
    public void onPressedMouse(MouseEvent event) {}

    @Override
    public void onReleasedMouse(MouseEvent event) {}

    @Override
    public void onScrollMouse(ScrollEvent event) {}

    @Override
    public void onKeyPressed(KeyEvent event) {}

    @Override
    public void onKeyReleased(KeyEvent event) {}
    
}
