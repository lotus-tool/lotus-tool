package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.BigState;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_NENHUM;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_REMOVER;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_VERTICE;
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
 */
public class OnClickedMouse implements Strategy {
    
    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent e) {
        
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

        if (dwi.mModoAtual == MODO_NENHUM) {
            
            if (dwi.mComponentSobMouse != null && (dwi.mComponentSobMouse instanceof StateView)) {
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
                        if (!bigState.dismountBigState(dwi.mViewer.getComponent())){
                            JOptionPane.showMessageDialog(null, "You need another BigState before dismantling");
                            return;
                        }
                        dwi.mBtnBigState.setSelected(false);
                        dwi.mBtnBigState.setGraphic(dwi.iconBigState);
                        dwi.mViewer.getComponent().remove(state);
                    }
                }
                else {
                    dwi.mBtnBigState.setSelected(false);
                    dwi.mBtnBigState.setGraphic(dwi.iconBigState);
                }
            }else{
                dwi.mBtnBigState.setSelected(false);
                dwi.mBtnBigState.setGraphic(dwi.iconBigState);

                if(!dwi.statesSelecionadoPeloRetangulo){
                    dwi.paleta.setVisible(false);
                }
            }
            
        }else {
            if (dwi.mModoAtual == MODO_VERTICE) {
                if (!(dwi.mComponentSobMouse instanceof StateView)) {
                    if (dwi.contID == -1) {
                        dwi.updateContID();
                    }
                    int id = dwi.mViewer.getComponent().getStatesCount();
                    State s = dwi.mViewer.getComponent().newState(id);
                    s.setID(dwi.contID);
                    dwi.contID++;
                    s.setLayoutX(e.getX()-(StateViewImpl.RAIO_CIRCULO));
                    s.setLayoutY(e.getY()-(StateViewImpl.RAIO_CIRCULO));
                    s.setLabel(String.valueOf(id));

                    if (dwi.mViewer.getComponent().getStatesCount() == 0) {
                        dwi.mViewer.getComponent().setInitialState(s);
                    }
                }
            }else if (dwi.mModoAtual == MODO_REMOVER) {
                
                
                /*if there are states that are selected and the eraser button was chosen 
                so erase all selected transitions from the component*/
              
                if(!dwi.statesSelecionados.isEmpty()&& dwi.mComponentSobMouse instanceof StateView){
                    
                    
                    State v = ((StateView) dwi.mComponentSobMouse).getState();
                    if(!dwi.statesSelecionados.contains(v)){
                        return;
                    }
                    
                    for(State s : dwi.statesSelecionados){
                        dwi.mViewer.getComponent().remove(s);
                    }
                    dwi.statesSelecionados.clear();
                    
                    return;
                }
              
            
                if (dwi.mComponentSobMouse instanceof StateView) {
                    State v = ((StateView) dwi.mComponentSobMouse).getState();
                    if(v.getValue("bigstate") instanceof BigState){
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
                    if(multiplasTransicoes.size() > 0){
                        //deletar da tela
                        for(Transition trans : multiplasTransicoes){
                            dwi.mViewer.getComponent().remove(trans);
                        }
                        //recriar transitions
                        for(Transition trans : multiplasTransicoes){
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
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
}
