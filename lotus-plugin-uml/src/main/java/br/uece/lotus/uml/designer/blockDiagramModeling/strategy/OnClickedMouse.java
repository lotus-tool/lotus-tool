/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;


import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;

import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 * @author Bruno Barbosa
 */
public class OnClickedMouse implements Strategy {


    private static final int LARGURA = 100, ALTURA = 60;
    private  final  static double LAYOUT_Y =50;
    private Popup pop = new Popup();

    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent e) {
        pop.setHideOnEscape(true);
        pop.setAnchorX(e.getSceneX()+10);
        pop.setAnchorY(e.getSceneY()-10);
        pop.setAutoFix(true);
        pop.setAutoHide(true);

        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            s.setComponenteSelecionado(s.mComponentSobMouse);

            if (s.mComponentSelecionado instanceof BlockDSView) {
                s.mContextMenuBlockDs.show(s.mViewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                s.mContextMenuBlockDs.hide();
            }
            return;
        } else {
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
        if (s.mModoAtual == s.MODO_NENHUM) {
            if (s.mComponentSobMouse != null && (s.mComponentSobMouse instanceof BlockDSView) && !s.mToolBar.getItems().contains(s.paleta)) {
                s.mToolBar.getItems().add(s.paleta);
            } else {
                if (!s.selecionadoPeloRetangulo) {
                    s.mToolBar.getItems().remove(s.paleta);
                }
            }
            if(s.mComponentSobMouse instanceof TransitionMSCView){
                s.setComponenteSelecionado(s.mComponentSobMouse);
            }else{
                s.setComponenteSelecionado(null);
            }
            //renomeando bMSC
            if(e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.mComponentSobMouse instanceof BlockDSView){
                pop.getContent().clear();
                BlockDS block = ((BlockDSView)s.mComponentSobMouse).getBlockDS();
                pop.getContent().add(createPopup_bMSC_rename(block));
                pop.show(s.mViewer.getNode().getScene().getWindow());
            }
            //renomeando TransitionMSC
            if(e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.mComponentSobMouse instanceof TransitionMSCView){
                pop.getContent().clear();
                TransitionMSC t = ((TransitionMSCView)s.mComponentSobMouse).getTransition();
                pop.getContent().add(createPopup_TransitionMSC(t));
                pop.show(s.mViewer.getNode().getScene().getWindow());
            }
        } else if (s.mModoAtual == s.MODO_BLOCO) {
            if (!(s.mComponentSobMouse instanceof BlockDSView)) {
                if (s.contID == -1) {
                    s.updateContID();
                }
                BlockDS b = s.mViewer.getmComponentDS().newBlockDS(s.contID);
                s.contID++;
                b.setLayoutX(e.getX() - (LARGURA / 2));
                b.setLayoutY(LAYOUT_Y);
                b.setLabel("Object" + s.contID);
            }
        } else if (s.mModoAtual == s.MODO_REMOVER) {
            if (s.mComponentSobMouse instanceof BlockDSView) {
                BlockDS b = ((BlockDSView) s.mComponentSobMouse).getBlockDS();
                s.mViewer.getmComponentDS().remove(b);
            }
            if (s.mComponentSobMouse instanceof TransitionMSCView) {
                TransitionMSC t = ((TransitionMSCView) s.mComponentSobMouse).getTransition();
                s.mViewer.getmComponentDS().remove(t);
                s.updateSequenceTransition();

            }
        }

    }

    private AnchorPane createPopup_TransitionMSC(TransitionMSC t) {
        VBox box = new VBox(5);
        Label lblAction = new Label("Action:");
        TextField txtAction = new TextField(t.getLabel() != null ? t.getLabel() : "");
        Label lblGuard = new Label("Guard:");
        TextField txtGuard = new TextField(t.getGuard() != null ? t.getGuard() : "");
        Button btnSet = new Button("Set");
        HBox b = new HBox(btnSet);
        b.setAlignment(Pos.CENTER);
        btnSet.setOnAction((ActionEvent event) -> {
           if(txtAction.getText().equals("")){
               pop.hide();
           }else{
               t.setLabel(txtAction.getText());
               if(txtGuard.getText().equals("")){
                   pop.hide();
               }else{
                   t.setGuard(txtGuard.getText());
               }
               pop.hide();
           }
        });
        btnSet.setDefaultButton(true);
        box.getChildren().addAll(lblAction,txtAction,lblGuard,txtGuard,b);
        AnchorPane panePopup = new AnchorPane(box);
        panePopup.setStyle("-fx-background-color: whitesmoke; -fx-effect: dropshadow( gaussian , gray , 5 , 0.0 , 0 , 1);");
        return panePopup;
    }

    private AnchorPane createPopup_bMSC_rename(BlockDS block) {
        VBox vbox = new VBox(5);
        HBox hbox = new HBox(3);
        Label lblnome = new Label("Name:");
        TextField name = new TextField(block.getLabel() != null ? block.getLabel() : "");
        Button rename = new Button("Rename");
        rename.setOnAction((ActionEvent event) -> {
            if(name.getText().equals("")){
                pop.hide();
            }else{
                block.setLabel(name.getText());
                pop.hide();
            }
        });
        rename.setDefaultButton(true);
        hbox.getChildren().addAll(name,rename);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(lblnome,hbox);
        AnchorPane panePopup = new AnchorPane(vbox);
        panePopup.setStyle("-fx-background-color: whitesmoke; -fx-effect: dropshadow( gaussian , gray , 5 , 0.0 , 0 , 1);");
        return panePopup;
    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
    }

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {
    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {
    }

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
    }

    @Override
    public void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
    }

    @Override
    public void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
    }

    @Override
    public void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event) {
    }

    @Override
    public void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event) {
    }

    @Override
    public void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event) {
    }

}
