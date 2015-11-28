
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.State;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_MOVER;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_NENHUM;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_VERTICE;
import static br.uece.lotus.designer.DesignerWindowImpl.RAIO_CIRCULO;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class OnPressedMouse implements Strategy {

    @Override
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent e) {
       
        if (dwi.mModoAtual == MODO_MOVER) {
            dwi.mViewer.getNode().setCursor(Cursor.CLOSED_HAND);
            if (e.getClickCount() == 2) {
                dwi.mViewer.getNode().setTranslateX(dwi.mViewerTranslateXPadrao);
                dwi.mViewer.getNode().setTranslateY(dwi.mViewerTranslateYPadrao);
            }
            //gravar cordenadas x e y do mViewer de acordo com a posiÃ§Ã£o do mouse

            dwi.mouseHandX=e.getSceneX();
            dwi.mouseHandY=e.getSceneY();
            //get the x and y position measure from Left-Top
            dwi.posicaoMViewerHandX=dwi.mViewer.getNode().getTranslateX();
            dwi.posicaoMViewerHandY=dwi.mViewer.getNode().getTranslateY();


        }
        if (dwi.mModoAtual != MODO_VERTICE && dwi.mModoAtual != MODO_NENHUM) {
            return;
        }
        if (dwi.mModoAtual == MODO_NENHUM) {
            dwi.coordenadaInicialX=e.getX();
            dwi.coordenadaInicialY=e.getY();
            dwi.ultimoInstanteX=0;
            dwi.ultimoInstanteY=0;
            dwi.segundaVezEmDiante=false;

            if (e.isShiftDown()) {
                    dwi.downShift=true;

            }

            if (dwi.downShift && dwi.mComponentSobMouse != null) {

                StateView stateView = (StateView) dwi.mComponentSobMouse;
                State state = stateView.getState();

                for(State s : dwi.statesSelecionados){
                    if(s==state){
                        return;
                    }
                }
                state.setBorderWidth(2);
                state.setBorderColor("blue");
                state.setTextColor("blue");
                state.setTextSyle(State.TEXTSTYLE_BOLD);
                dwi.statesSelecionados.add(state);
                dwi.selecionadoUm=true;
                dwi.selecioneiComShift=true;

                dwi.modoCriacaoDoRetangulo=false;
                dwi.selecaoPadrao=false;
                return;
            }
            else{

                if (!dwi.statesSelecionadoPeloRetangulo && dwi.mComponentSobMouse != null && !dwi.selecioneiComShift) {


                    dwi.selecaoPadrao=true;
                    if (dwi.statesSelecionados != null) {
                        dwi.statesSelecionados.clear();
                    }
                    if (dwi.mComponentSobMouse instanceof StateView) {
                        dwi.setComponenteSelecionado(dwi.mComponentSobMouse);
                        StateView stateView = (StateView) dwi.mComponentSobMouse;
                        State state = stateView.getState();
                        dwi.statesSelecionados.add(state);
                        dwi.modoCriacaoDoRetangulo=false;
                        return;
                    } else {
                        dwi.setComponenteSelecionado(dwi.mComponentSobMouse);
                    }
                } else {

                    if (!SeClickeiEntreSelecionados(e.getX(), e.getY(), dwi) && dwi.statesSelecionados != null) {

                        dwi.verificacao = true;
                        for (State s : dwi.statesSelecionados) {
                            s.setBorderWidth(1);
                            s.setBorderColor("black");
                            s.setTextColor("black");
                            s.setTextSyle(State.TEXTSTYLE_NORMAL);
                        }
                        if (dwi.mComponentSobMouse != null) {
                            dwi.selecaoPadrao=true;
                            if (dwi.statesSelecionados != null) {
                                dwi.statesSelecionados.clear();
                               dwi.modoCriacaoDoRetangulo=false;
                            }

                            StateView stateView = null;
                            dwi.setComponenteSelecionado(dwi.mComponentSobMouse);
                            try {
                                stateView = (StateView) dwi.mComponentSobMouse;
                            } catch (ClassCastException exception){
                                return;
                            }
                            State state = stateView.getState();
                            dwi.statesSelecionados.add(state);


                        } else {
                            dwi.selecaoPadrao=false;
                            dwi.modoCriacaoDoRetangulo=true;
                            dwi.statesSelecionadoPeloRetangulo=false;
                            dwi.selecioneiComShift=false;
                            if (dwi.selecionadoUm) {
                               dwi.selecionadoUm=false;
                            }
                            for (State s : dwi.statesSelecionados) {
                                s.setBorderWidth(1);
                                s.setBorderColor("black");
                                s.setTextColor("black");
                                s.setTextSyle(State.TEXTSTYLE_NORMAL);
                            }

                            dwi.statesSelecionados.clear();
                            //System.out.println("Removendo estilo selecionado state/trans");
                            Object v = dwi.getSelectedView();
                            if(v instanceof TransitionView){
                                dwi.removeSelectedStyles(v);
                            }

                        }

                    } else {
                        dwi.verificacao = false;
                        dwi.modoCriacaoDoRetangulo=false;
                    }

                }
            }
            if (!(dwi.mComponentSobMouse instanceof StateView)) {

                return;
            }
            StateView v = (StateView) dwi.mComponentSobMouse;

            dwi.variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice=v.getNode().getLayoutX() - e.getX() + RAIO_CIRCULO;
            dwi.variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice=v.getNode().getLayoutY() - e.getY() + RAIO_CIRCULO;
        }
    }


    public boolean SeClickeiEntreSelecionados(double x, double y, DesignerWindowImpl dwi) {
        boolean aux = false;
        if (dwi.statesSelecionados != null) {

            for (State s : dwi.statesSelecionados) {
                double posCircleX = s.getLayoutX() + RAIO_CIRCULO;
                double posCircleY = s.getLayoutY() + RAIO_CIRCULO;

                double posicaoDoEstadoXMaisRaio = posCircleX + RAIO_CIRCULO;
                double posicaoDoEstadoYMaisRaio = posCircleY + RAIO_CIRCULO;
                double posicaoDoEstadoXMenosRaio = posCircleX - RAIO_CIRCULO;
                double posicaoDoEstadoYMenosRaio = posCircleY - RAIO_CIRCULO;

                if (x == posicaoDoEstadoXMenosRaio || x > posicaoDoEstadoXMenosRaio) {
                    if (x == posicaoDoEstadoXMaisRaio || x < posicaoDoEstadoXMaisRaio) {
                        if (y == posicaoDoEstadoYMenosRaio || y > posicaoDoEstadoYMenosRaio) {
                            if (y == posicaoDoEstadoYMaisRaio || y < posicaoDoEstadoYMaisRaio) {
                                return true;
                            }

                        }
                    }
                }
            }
        }

        return aux;
    }  

    @Override
    public void onClickedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}
    
    @Override
    public void onMovedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl Dwi, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl Dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl Dwi, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
}



