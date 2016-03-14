/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import static br.uece.lotus.uml.api.viewer.hMSC.HmscViewImpl.ALTURA;
import static br.uece.lotus.uml.api.viewer.hMSC.HmscViewImpl.LARGURA;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 *
 * @author Bruno Barbosa
 */
public class OnClickedMouse implements Strategy{

    private Popup pop = new Popup();
    
    
    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent e) {
        pop.setHideOnEscape(true);
        pop.setAnchorX(e.getSceneX()+10);
        pop.setAnchorY(e.getSceneY()-10);
        pop.setAutoFix(true);
        pop.setAutoHide(true);
        
        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            s.setComponenteSelecionado(s.mComponentSobMouse);
            
            if (s.mComponentSelecionado instanceof HmscView) {
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
            if (s.mComponentSobMouse != null && (s.mComponentSobMouse instanceof HmscView) && !s.mToolBar.getItems().contains(s.paleta)) {
                s.mToolBar.getItems().add(s.paleta);
            }
            else{
                if(!s.selecionadoPeloRetangulo){
                   s.mToolBar.getItems().remove(s.paleta);
                }
            }
            if(s.mComponentSobMouse instanceof TransitionMSCView){
                s.setComponenteSelecionado(s.mComponentSobMouse);
            }else{
                s.setComponenteSelecionado(null);
            } 
            //renomeando hMSC
            if(e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.mComponentSobMouse instanceof HmscView){
                pop.getContent().clear();
                Hmsc block = ((HmscView)s.mComponentSobMouse).getHMSC();
                pop.getContent().add(createPopup_hMSC_rename(block));
                pop.show(s.mViewer.getNode().getScene().getWindow());
            }
            //renomeando TransitionMSC
            if(e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.mComponentSobMouse instanceof TransitionMSCView){
                pop.getContent().clear();
                TransitionMSC t = ((TransitionMSCView)s.mComponentSobMouse).getTransition();
                pop.getContent().add(createPopup_TransitionMSC(t));
                pop.show(s.mViewer.getNode().getScene().getWindow());
            }
        }
        else if(s.mModoAtual == s.MODO_BLOCO){
            if (!(s.mComponentSobMouse instanceof HmscView)) {
                    if (s.contID == -1) {
                        s.updateContID();
                    }
                    Hmsc b = s.mViewer.getComponentBuildDS().newBlock(s.contID);
                    s.contID++;
                    b.setLayoutX(e.getX()-(LARGURA/2));
                    b.setLayoutY(e.getY()-(ALTURA/2));
                    b.setLabel("New hMSC");
                }
        }
        else if(s.mModoAtual == s.MODO_REMOVER){
            if(s.mComponentSobMouse instanceof HmscView){
                Hmsc b = ((HmscView)s.mComponentSobMouse).getHMSC();
                s.mViewer.getComponentBuildDS().remove(b);
            }
            if(s.mComponentSobMouse instanceof TransitionMSCView){
                TransitionMSC t = ((TransitionMSCView)s.mComponentSobMouse).getTransition();
                s.mViewer.getComponentBuildDS().remove(t);
            }
        }
          
    }
    
    private AnchorPane createPopup_hMSC_rename(Hmsc block){
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
    
    private AnchorPane createPopup_TransitionMSC(TransitionMSC t){
        VBox box = new VBox(5);       
        Label lblAcao = new Label("Action:");
        TextField txtAcao = new TextField(t.getLabel() != null ? t.getLabel() : "");
        Label lblProb = new Label("Probability:");
        TextField txtProb = new TextField(String.valueOf(t.getProbability() != null ? t.getProbability() : ""));
        Button btnSet = new Button("Set");
        HBox b = new HBox(btnSet);
        b.setAlignment(Pos.CENTER);
        btnSet.setOnAction((ActionEvent event) -> {
            if(txtAcao.getText().equals("")){
                pop.hide();
            }else{
                t.setLabel(txtAcao.getText());
                if(txtProb.getText().equals("")){
                    t.setProbability(null);
                }else{
                    String valorDoField = txtProb.getText().trim();
                    String auxValor = "";
                    if(valorDoField.contains(",")){
                        auxValor = valorDoField.replaceAll(",", ".");
                        double teste = Double.parseDouble(auxValor);
                        if(teste<0 || teste >1){
                            Alert a = new Alert(Alert.AlertType.ERROR,"Input probability between 0 and 1",ButtonType.OK);
                            a.show();
                            auxValor="";
                            txtProb.setText("");
                        }
                    }
                    else if(valorDoField.contains(".")){
                        auxValor = valorDoField;
                        double teste = Double.parseDouble(auxValor);
                        if(teste<0 || teste >1){
                            Alert a = new Alert(Alert.AlertType.ERROR,"Input probability need 0 to 1",ButtonType.OK);
                            a.show();
                            auxValor="";
                            txtProb.setText("");
                        }
                    }
                    else if(valorDoField.contains("%")){
                        double valorEntre0e1;
                        auxValor = valorDoField.replaceAll("%", "");
                        valorEntre0e1 = (Double.parseDouble(auxValor))/100;
                        auxValor = String.valueOf(valorEntre0e1);
                        double teste = Double.parseDouble(auxValor);
                        if(teste<0 || teste >1){
                            Alert a = new Alert(Alert.AlertType.ERROR,"Input probability need 0 to 1",ButtonType.OK);
                            a.show();
                            auxValor="";
                            txtProb.setText("");
                        }
                    }
                    else{
                        if(valorDoField.equals("0") || valorDoField.equals("1")){
                            auxValor = valorDoField;
                        }else{
                            Alert a = new Alert(Alert.AlertType.ERROR,"Input probability need 0 to 1",ButtonType.OK);
                            a.show();
                        }
                    }
                    t.setProbability(Double.parseDouble(auxValor));
                }
                pop.hide();
            }
        });
        btnSet.setDefaultButton(true);
        box.getChildren().addAll(lblAcao,txtAcao,lblProb,txtProb,b);
        AnchorPane panePopup = new AnchorPane(box);
        panePopup.setStyle("-fx-background-color: whitesmoke; -fx-effect: dropshadow( gaussian , gray , 5 , 0.0 , 0 , 1);");
        return panePopup;        
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
