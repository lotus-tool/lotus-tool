package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.BigState;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_NENHUM;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_REMOVER;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_VERTICE;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_ALTERAR;
import br.uece.lotus.viewer.TransitionView;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.StateViewImpl;
import br.uece.lotus.viewer.TransitionView;
import java.util.List;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javax.swing.JOptionPane;
 
/**
 * Created by lva on 19/11/15.
 * Adaptation by Yan Gurgel 12/2016
 */
public class OnClickedMouse implements Strategy {

    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent e) {

        try {
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                dwi.setComponenteSelecionado(dwi.mComponentSobMouse);

                if (dwi.mComponentSelecionado instanceof StateView) {

                    dwi.mComponentContextMenu.show(dwi.mViewer.getNode(), e.getScreenX(), e.getScreenY());
                } else {
                    dwi.mComponentContextMenu.hide();
                }
                return;
            } else {
                dwi.mComponentContextMenu.hide();
            }
            if (e.isControlDown() && e.getButton() == MouseButton.MIDDLE) {

                dwi.mViewer.getNode().setScaleX(dwi.mViewerScaleXPadrao);
                dwi.mViewer.getNode().setScaleY(dwi.mViewerScaleYPadrao);

                dwi.mViewer.getNode().setTranslateX(dwi.mViewerTranslateXPadrao);
                dwi.mViewer.getNode().setTranslateY(dwi.mViewerTranslateYPadrao);
            }
        } catch (Exception ex) {
        }

        if (dwi.mModoAtual == MODO_NENHUM) {
            if (dwi.mComponentSobMouse != null && (dwi.mComponentSobMouse instanceof TransitionView)) {
                //Foco alterado para o txtaction
                dwi.setComponenteSelecionado(dwi.mComponentSobMouse);
            } else if (dwi.mComponentSobMouse != null && (dwi.mComponentSobMouse instanceof StateView)) {
                dwi.paleta.setVisible(true);
                //VERIFICANDO SE TEM UM BIGSTATE
                StateView stateView = (StateView) dwi.mComponentSobMouse;
                State state = stateView.getState();
                BigState bigState = (BigState) state.getValue("bigstate");
                if (bigState != null) {
                    dwi.mBtnBigState.setSelected(true);
                    dwi.mBtnBigState.setGraphic(dwi.iconBigStateDismount);
                    /*System.out.println("NUMERO DE BIGSTATES = "+BigState.todosOsBigStates.size());
                    System.out.println(((BigState)((StateView)mComponentSobMouse).getState().getValue("bigstate")).toString());*/
                    if (e.getClickCount() == 2) {
                        if (!bigState.dismountBigState(dwi.mViewer.getComponent())) {
                            JOptionPane.showMessageDialog(null, "You need another BigState before dismantling");
                            return;
                        }
                        dwi.mBtnBigState.setSelected(false);
                        dwi.mBtnBigState.setGraphic(dwi.iconBigState);
                        dwi.mViewer.getComponent().remove(state);
                    }
                } else {
                    dwi.mBtnBigState.setSelected(false);
                    dwi.mBtnBigState.setGraphic(dwi.iconBigState);
                }
            } else {
                dwi.mBtnBigState.setSelected(false);
                dwi.mBtnBigState.setGraphic(dwi.iconBigState);
                if (!dwi.statesSelecionadoPeloRetangulo) {
                    dwi.paleta.setVisible(false);
                }
                dwi.setComponenteSelecionado(null);
            }

        } else {
            if (dwi.mModoAtual == MODO_VERTICE) {
                if (!(dwi.mComponentSobMouse instanceof StateView)) {
                    if (dwi.contID == -1) {
                        dwi.updateContID();
                    }
                    int id = dwi.mViewer.getComponent().getStatesCount();
                    State s = dwi.mViewer.getComponent().newState(id);
                    s.setID(dwi.contID);
                    dwi.contID++;
                    s.setLayoutX(e.getX() - (StateViewImpl.RAIO_CIRCULO));
                    s.setLayoutY(e.getY() - (StateViewImpl.RAIO_CIRCULO));
                    s.setLabel(String.valueOf(id));

                    if (dwi.mViewer.getComponent().getStatesCount() == 0) {
                        dwi.mViewer.getComponent().setInitialState(s);
                    }
                }
            }else if (dwi.mModoAtual == MODO_ALTERAR) {
                if (dwi.mComponentSelecionado != null && dwi.getSelectedView() instanceof TransitionView) {
                    System.out.println("transição selecionada");

                    Transition t = ((TransitionView) dwi.getSelectedView()).getTransition();
                    State origem = t.getSource();
                    State destino = t.getDestiny();
                    String label = t.getLabel();
                    Double probabilidade = t.getProbability();
                    
                    //t.setValue("view.type", t.getValue("view.type"));
                    System.out.println("view type " + t.getValue("view.type"));

                    Component c = dwi.mViewer.getComponent();

                    if (t.getValue("view.type").equals(1)) {
                        c.buildTransition(origem, destino)
                                .setLabel(label)
                                .setProbability(probabilidade)
                                .setViewType(TransitionView.Geometry.LINE)
                                .create();
                    } else {
                        c.buildTransition(origem, destino)
                                .setLabel(label)
                                .setProbability(probabilidade)
                                .setViewType(TransitionView.Geometry.CURVE)
                                .create();
                    }
                    dwi.mViewer.getComponent().remove(t);
                    
                    dwi.mModoAtual = MODO_NENHUM;
                }
            }  else if (dwi.mModoAtual == MODO_REMOVER) {
                /*
                *Verifica se existe algum estado selecionado após clicar na borracha se sim ele(s)
                *são apagados
                */
                if (!dwi.statesSelecionados.isEmpty()) {
                    int r = JOptionPane.showConfirmDialog(null,
                            new StringBuilder("Do you really want to remove the all selection?"),
                            "Remove",
                            JOptionPane.YES_NO_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        for (State s : dwi.statesSelecionados) {
                            dwi.mViewer.getComponent().remove(s);
                        }
                        dwi.statesSelecionados.clear();
                    }
                    else{
                        for (State s : dwi.statesSelecionados) {
                            //dwi.removeSelectedStyles(s);
                             s.setBorderWidth(1);
                             s.setBorderColor("black");
                             s.setTextColor("black");
                             s.setTextSyle(State.TEXTSTYLE_NORMAL);
                        }
                        dwi.statesSelecionados.clear();
                    }
                } 
                /*
                *Verifica se existe alguma Transition selecionada após clicar na borracha 
                *se sim ela é apagada
                */ 
                else if (dwi.mComponentSelecionado != null && dwi.getSelectedView() instanceof TransitionView) {
                    int r = JOptionPane.showConfirmDialog(null,
                            new StringBuilder("Do you really want to remove the Transition?"), "Remove",
                            JOptionPane.YES_NO_OPTION);

                    if (r == JOptionPane.YES_OPTION) {

                        Transition t = ((TransitionView) dwi.getSelectedView()).getTransition();
                        State iniTransition = t.getSource();
                        State fimTransition = t.getDestiny();
                        dwi.mViewer.getComponent().remove(t);
                        //Verificar Mais de uma Trasition do mesmo Source e Destiny
                        List<Transition> multiplasTransicoes = iniTransition.getTransitionsTo(fimTransition);
                        if (multiplasTransicoes.size() > 0) {
                            //deletar da tela
                            for (Transition trans : multiplasTransicoes) {
                                dwi.mViewer.getComponent().remove(trans);
                            }
                            //recriar transitions
                            for (Transition trans : multiplasTransicoes) {
                                dwi.mViewer.getComponent().buildTransition(iniTransition, fimTransition)
                                        .setGuard(trans.getGuard())
                                        .setLabel(trans.getLabel())
                                        .setProbability(trans.getProbability())
                                        .setViewType(TransitionView.Geometry.CURVE)
                                        .create();
                            }
                        }
                        dwi.mComponentSelecionado = null;
                    }
                    else{
                    dwi.setComponenteSelecionado(null);
                    dwi.mComponentSelecionado = null;
                    }
                }
                /*
                *Deleção caso não existe uma seleção (caso 1)
                */ 
                else if (dwi.mComponentSelecionado != null && dwi.mComponentSobMouse != dwi.getSelectedView() && (dwi.mComponentSobMouse instanceof StateView || dwi.mComponentSobMouse instanceof TransitionView)) {
                    int r = JOptionPane.showConfirmDialog(null,
                            new StringBuilder("Do you really want to remove the ").append(dwi.mComponentSobMouse instanceof StateView ? "State" : dwi.mComponentSobMouse instanceof TransitionView ? "Transition" : "").append("?"),
                            "Remove",
                            JOptionPane.YES_NO_OPTION);

                    if (r == JOptionPane.YES_OPTION) {
                        if (!dwi.statesSelecionados.isEmpty() && dwi.mComponentSobMouse instanceof StateView) {

                            State v = ((StateView) dwi.mComponentSobMouse).getState();
                            if (!dwi.statesSelecionados.contains(v)) {
                                JOptionPane.showMessageDialog(null, "State fora da seleção");
                                return;
                            }

                            for (State s : dwi.statesSelecionados) {
                                dwi.mViewer.getComponent().remove(s);
                            }
                            dwi.statesSelecionados.clear();

                            return;
                        }

                        if (dwi.mComponentSobMouse instanceof StateView) {
                            State v = ((StateView) dwi.mComponentSobMouse).getState();
                            if (v.getValue("bigstate") instanceof BigState) {
                                BigState.removeBigState((BigState) v.getValue("bigstate"));
                            }
                            dwi.mViewer.getComponent().remove(v);
                        } else if (dwi.mComponentSobMouse instanceof TransitionView) {
                            Transition t = ((TransitionView) dwi.mComponentSobMouse).getTransition();
                            State iniTransition = t.getSource();
                            State fimTransition = t.getDestiny();
                            dwi.mViewer.getComponent().remove(t);
                            //Verificar Mais de uma Trasition do mesmo Source e Destiny
                            List<Transition> multiplasTransicoes = iniTransition.getTransitionsTo(fimTransition);
                            if (multiplasTransicoes.size() > 0) {
                                //deletar da tela
                                for (Transition trans : multiplasTransicoes) {
                                    dwi.mViewer.getComponent().remove(trans);
                                }
                                //recriar transitions
                                for (Transition trans : multiplasTransicoes) {
                                    dwi.mViewer.getComponent().buildTransition(iniTransition, fimTransition)
                                            .setGuard(trans.getGuard())
                                            .setLabel(trans.getLabel())
                                            .setProbability(trans.getProbability())
                                            .setViewType(TransitionView.Geometry.CURVE)
                                            .create();
                                }
                            }
                        }

                    }
                } 
//====================================================================================================================================
                /*
                *Deleção caso não existe uma seleção (caso 2)
                */ 
                 else if (dwi.mComponentSobMouse != null && dwi.mComponentSelecionado == null && (dwi.mComponentSobMouse instanceof StateView || dwi.mComponentSobMouse instanceof TransitionView)) {
                    dwi.getComponent().getName();
                    int r = JOptionPane.showConfirmDialog(null,
                            new StringBuilder("Do you really want to remove the ").append(dwi.mComponentSobMouse instanceof StateView ? "State" : dwi.mComponentSobMouse instanceof TransitionView ? "Transition" : "").append("?"),
                            "Remove",
                            JOptionPane.YES_NO_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        if (!dwi.statesSelecionados.isEmpty() && dwi.mComponentSobMouse instanceof StateView) {

                            State v = ((StateView) dwi.mComponentSobMouse).getState();
                            if (!dwi.statesSelecionados.contains(v)) {
                                return;
                            }

                            for (State s : dwi.statesSelecionados) {
                                dwi.mViewer.getComponent().remove(s);
                            }
                            dwi.statesSelecionados.clear();

                            return;
                        }

                        if (dwi.mComponentSobMouse instanceof StateView) {
                            State v = ((StateView) dwi.mComponentSobMouse).getState();
                            if (v.getValue("bigstate") instanceof BigState) {
                                BigState.removeBigState((BigState) v.getValue("bigstate"));
                            }
                            dwi.mViewer.getComponent().remove(v);
                        } else if (dwi.mComponentSobMouse instanceof TransitionView) {
                            Transition t = ((TransitionView) dwi.mComponentSobMouse).getTransition();
                            State iniTransition = t.getSource();
                            State fimTransition = t.getDestiny();
                            dwi.mViewer.getComponent().remove(t);
                            //Verificar Mais de uma Trasition do mesmo Source e Destiny
                            List<Transition> multiplasTransicoes = iniTransition.getTransitionsTo(fimTransition);
                            if (multiplasTransicoes.size() > 0) {
                                //deletar da tela
                                for (Transition trans : multiplasTransicoes) {
                                    dwi.mViewer.getComponent().remove(trans);
                                }
                                //recriar transitions
                                for (Transition trans : multiplasTransicoes) {
                                    dwi.mViewer.getComponent().buildTransition(iniTransition, fimTransition)
                                            .setGuard(trans.getGuard())
                                            .setLabel(trans.getLabel())
                                            .setProbability(trans.getProbability())
                                            .setViewType(TransitionView.Geometry.CURVE)
                                            .create();
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    @Override
    public void onMovedMouse(DesignerWindowImpl Dwi, MouseEvent event) {
    }

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl Dwi, MouseEvent event) {
    }

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl Dwi, DragEvent event) {
    }

    @Override
    public void onDraggedMouse(DesignerWindowImpl Dwi, MouseEvent event) {
    }

    @Override
    public void onPressedMouse(DesignerWindowImpl Dwi, MouseEvent event) {
    }

    @Override
    public void onReleasedMouse(DesignerWindowImpl Dwi, MouseEvent event) {
    }

    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {
    }

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {
    }

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {
    }

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {
    }

}
