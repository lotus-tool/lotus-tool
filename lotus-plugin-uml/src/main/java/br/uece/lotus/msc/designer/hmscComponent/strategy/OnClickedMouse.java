/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;

import static br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockViewImpl.HEIGHT;
import static br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockViewImpl.WIDTH;

import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
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

import java.util.Arrays;

/**
 * @author Bruno Barbosa
 */
public class OnClickedMouse implements Strategy {

    private Popup pop = new Popup();


    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent e) {
        pop.setHideOnEscape(true);
        pop.setAnchorX(e.getSceneX() + 10);
        pop.setAnchorY(e.getSceneY() - 10);
        pop.setAutoFix(true);
        pop.setAutoHide(true);

        //mostrando menu dos blocos
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            s.setComponenteSelecionado(s.componentSobMouse);

            if (s.componentSelecionado instanceof HmscBlockView) {
                s.mContextMenuBlockBuild.show(s.viewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                s.mContextMenuBlockBuild.hide();
            }
            return;
        } else {
            s.mContextMenuBlockBuild.hide();
        }

        //resetando zoom pelo mouse
        if ((e.isControlDown() && e.getButton() == MouseButton.MIDDLE) || (e.getClickCount() == 2 && s.mModoAtual == s.MOVE_MODE)) {

            s.viewer.getNode().setScaleX(s.mViewerScaleXPadrao);
            s.viewer.getNode().setScaleY(s.mViewerScaleYPadrao);

            s.viewer.getNode().setTranslateX(s.mViewerTranslateXPadrao);
            s.viewer.getNode().setTranslateY(s.mViewerTranslateYPadrao);
        }
        //verificando por controles de butoes
        if (s.mModoAtual == s.ANY_MODE) {
            if (s.componentSobMouse != null && (s.componentSobMouse instanceof HmscBlockView) && !s.mToolBar.getItems().contains(s.paleta)) {
                s.mToolBar.getItems().add(s.paleta);
            } else {
                if (!s.selecionadoPeloRetangulo) {
                    s.mToolBar.getItems().remove(s.paleta);
                }
            }
            if (s.componentSobMouse instanceof TransitionMSCView) {
                s.setComponenteSelecionado(s.componentSobMouse);
            } else if (s.componentSobMouse instanceof InterceptionNodeView) {

                s.setComponenteSelecionado(s.componentSobMouse);
            }
            if (s.componentSobMouse == null && !s.selecionadoPeloRetangulo) {
               // Apaga os Labels e talz
                s.setComponenteSelecionado(s.componentSobMouse);
            }
            //renomeando hMSC
            if (e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.componentSobMouse instanceof HmscBlockView) {
                pop.getContent().clear();
                HmscBlock hmscBlock = ((HmscBlockView) s.componentSobMouse).getHMSC();
                //pop.getContent().add(createPopup_hMSC_rename(hmscBlock));

                if (!s.projectExplorerPluginMSC.open_BMSC(hmscBlock.getBmscComponet())) {
                    s.mContextMenuBlockBuild.getItems().get(0).fire();
                    s.projectExplorerPluginMSC.open_BMSC(hmscBlock.getBmscComponet());
                }
            }
            //renomeando TransitionMSC
            if (e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton()) && s.componentSobMouse instanceof TransitionMSCView) {
                pop.getContent().clear();
                TransitionMSC t = ((TransitionMSCView) s.componentSobMouse).getTransition();
                pop.getContent().add(createPopup_TransitionMSC(t));
                pop.show(s.viewer.getNode().getScene().getWindow());
            }
        } else if (s.mModoAtual == s.HMSC_BLOCK_MODE) {
            if (!(s.componentSobMouse instanceof HmscBlockView)) {

//                if (s.countHmscBlock == -1) {
//                    s.updateCountHmscBlock();
//                }
//
//                if(s.countIntercptionNode == -1){
//                    s.updateCountGenericElements();
//                }


                HmscBlock hmscHmscBlock = s.viewer.getHmscComponent().newHmsc(s.getHmscComponent().getGenericElementList().size());

//                s.updateCountHmscBlock();
//                s.updateCountGenericElements();

                hmscHmscBlock.setLayoutX(e.getX() /*- (WIDTH / 2)*/);
                hmscHmscBlock.setLayoutY(e.getY() /*- (HEIGHT / 2)*/);
                hmscHmscBlock.setLabel("New hMSC" + s.getHmscComponent().getHmscBlockList().size());

            }


        } else if (s.mModoAtual == s.INTERCEPTION_NODE_MODE) {

            if (!(s.componentSobMouse instanceof InterceptionNodeView)) {


//                if (s.countIntercptionNode == -1) {
//                    s.updateCountInterceptionNode();
//                }
//
//                if(s.countIntercptionNode == -1){
//                    s.updateCountGenericElements();
//                }

                InterceptionNode interceptionNode = s.viewer.getHmscComponent().newInterceptionNode(s.getHmscComponent().getGenericElementList().size());

//                s.updateCountInterceptionNode();
//                s.updateCountGenericElements();
                interceptionNode.setLayoutX(e.getX());
                interceptionNode.setLayoutY(e.getY());
            }
        } else if (s.mModoAtual == s.REMOVE_MODE) {
            if (s.componentSobMouse instanceof HmscBlockView) {

                if (s.componentSelecionado == s.componentSobMouse) {
                    s.removeSelectedStyles(s.componentSelecionado);
                    s.componentSelecionado = null;
                }

                HmscBlock b = ((HmscBlockView) s.componentSobMouse).getHMSC();
                s.viewer.getHmscComponent().remove(b);


            } else if (s.componentSobMouse instanceof InterceptionNodeView) {

                if (s.componentSelecionado == s.componentSobMouse) {
                    s.removeSelectedStyles(s.componentSelecionado);
                    s.componentSelecionado = null;
                }


                InterceptionNode interceptionNode = ((InterceptionNodeView) s.componentSobMouse).getInterceptionNode();
                s.viewer.getHmscComponent().remove(interceptionNode);
            }

            if (s.componentSobMouse instanceof TransitionMSCView) {
                TransitionMSC t = ((TransitionMSCView) s.componentSobMouse).getTransition();
                s.viewer.getHmscComponent().remove(t);
            }
        }

    }


    private AnchorPane createPopup_TransitionMSC(TransitionMSC t) {
        VBox box = new VBox(5);
        Label lblLabel = new Label("Label:");
        TextField txtLabel = new TextField(t.getLabel() != null ? t.getLabel() : "");

        Label lblGuard = new Label("Guard:");
        TextField txtGuard = new TextField(t.getGuard() != null ? t.getGuard() : "");

        Label lblAction = new Label("Actions:");


        TextField txtAction = new TextField(t.getActions().size() == 0 ? "" : String.join(",", t.getActions()));

        Label lblProb = new Label("Probability:");
        TextField txtProb = new TextField(String.valueOf(t.getProbability() != null ? t.getProbability() : ""));

        Button btnSet = new Button("Set");
        HBox b = new HBox(btnSet);
        b.setAlignment(Pos.CENTER);
        btnSet.setOnAction((ActionEvent event) -> {

            if (txtLabel.getText().equals("")) {
                t.setLabel("");
            } else {
                t.setLabel(txtLabel.getText());
            }

            if (txtProb.getText().equals("")) {
                t.setProbability(null);
            } else {
                t.setLabel(txtLabel.getText());
                if (txtProb.getText().equals("")) {
                    t.setProbability(null);
                } else {
                    String valorDoField = txtProb.getText().trim();
                    String auxValor = "";
                    if (valorDoField.contains(",")) {
                        auxValor = valorDoField.replaceAll(",", ".");
                        double teste = Double.parseDouble(auxValor);
                        if (teste < 0 || teste > 1) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Input probability between 0 and 1", ButtonType.OK);
                            a.show();
                            auxValor = "";
                            txtProb.setText("");
                        }
                    } else if (valorDoField.contains(".")) {
                        auxValor = valorDoField;
                        double teste = Double.parseDouble(auxValor);
                        if (teste < 0 || teste > 1) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Input probability need 0 to 1", ButtonType.OK);
                            a.show();
                            auxValor = "";
                            txtProb.setText("");
                        }
                    } else if (valorDoField.contains("%")) {
                        double valorEntre0e1;
                        auxValor = valorDoField.replaceAll("%", "");
                        valorEntre0e1 = (Double.parseDouble(auxValor)) / 100;
                        auxValor = String.valueOf(valorEntre0e1);
                        double teste = Double.parseDouble(auxValor);
                        if (teste < 0 || teste > 1) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Input probability need 0 to 1", ButtonType.OK);
                            a.show();
                            auxValor = "";
                            txtProb.setText("");
                        }
                    } else {
                        if (valorDoField.equals("0") || valorDoField.equals("1")) {
                            auxValor = valorDoField;
                        } else {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Input probability need 0 to 1", ButtonType.OK);
                            a.show();
                        }
                    }
                    t.setProbability(Double.parseDouble(auxValor));
                }

            }

            if (txtGuard.getText().isEmpty() || txtGuard.equals("")) {
                t.setGuard(null);
            } else {
                t.setGuard(txtGuard.getText());
            }

            if (txtAction.getText().isEmpty() || txtAction.equals("")) {
                t.clearActions();
            } else {
                t.setActions(Arrays.asList(txtAction.getText().trim().split(",")));
            }

            pop.hide();


        });
        btnSet.setDefaultButton(true);
        box.getChildren().addAll(lblLabel, txtLabel, lblGuard, txtGuard, lblAction, txtAction, lblProb, txtProb, b);
        AnchorPane panePopup = new AnchorPane(box);
        panePopup.setStyle("-fx-background-color: whitesmoke; -fx-effect: dropshadow( gaussian , gray , 5 , 0.0 , 0 , 1);");
        return panePopup;
    }

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {
    }

    @Override
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {
    }

    @Override
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {
    }

    @Override
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {
    }

    @Override
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {
    }

    @Override
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {
    }

    @Override
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {
    }

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {
    }

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {
    }

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {
    }

}
