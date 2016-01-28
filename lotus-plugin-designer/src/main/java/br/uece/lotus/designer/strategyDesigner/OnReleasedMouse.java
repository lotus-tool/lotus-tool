/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.BigState;
import br.uece.lotus.State;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_MOVER;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_NENHUM;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_VERTICE;
import static br.uece.lotus.designer.DesignerWindowImpl.RAIO_CIRCULO;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnReleasedMouse implements Strategy{
    
    
    @Override
    public void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event) {
        
        
            if (dwi.mModoAtual == MODO_MOVER) {
                dwi.mViewer.getNode().setCursor(Cursor.OPEN_HAND);
            }
            
            if (dwi.mModoAtual != MODO_VERTICE && dwi.mModoAtual != MODO_NENHUM) {
                return;
            }
            if (dwi.mModoAtual == MODO_NENHUM) {
                dwi.variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
                dwi.variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
                if (dwi.modoCriacaoDoRetangulo) {
                    if (!dwi.auxA) {
                        dwi.inicioDoRectanguloXAux = dwi.coordenadaInicialX;
                        dwi.inicioDoRectanguloYAux = dwi.coordenadaInicialY;
                    }
                    if (!dwi.selecionadoUm) {
                        dwi.coordenadaFinalX = event.getX();
                        dwi.coordenadaFinalY = event.getY();

                        dwi.statesSelecionadoPeloRetangulo = selecionandoComRetangulo(dwi.inicioDoRectanguloXAux, dwi.inicioDoRectanguloYAux, dwi.coordenadaFinalX, dwi.coordenadaFinalY, dwi);
                        if(dwi.statesSelecionadoPeloRetangulo){
                            dwi.paleta.setVisible(true);
                        }
                    }
                    if (dwi.ultimoRetanguloAdicionado != null) {
                        dwi.mViewer.getNode().getChildren().remove(dwi.ultimoRetanguloAdicionado);
                    }
                    dwi.auxA = false;
                }
            }
    }
    
    private boolean selecionandoComRetangulo(double inicioDoRectanguloX, double inicioDoRectanguloY, double finalDoRectanguloX, double finalDoRectanguloY, DesignerWindowImpl dwi) {
        boolean aux = false;
        /////////////////////////////////////////////////
        ////////ORGANIZANDO AS POSICOES DO RETANGULO DE SELECAO
        /////////////////////////////////////////////////
        if (inicioDoRectanguloX > finalDoRectanguloX) {
            double ajuda = finalDoRectanguloX;
            finalDoRectanguloX = inicioDoRectanguloX;
            inicioDoRectanguloX = ajuda;

        }
        if (inicioDoRectanguloY > finalDoRectanguloY) {
            double ajuda = finalDoRectanguloY;
            finalDoRectanguloY = inicioDoRectanguloY;
            inicioDoRectanguloY = ajuda;
        }

        dwi.todosOsStates = (ArrayList<State>) dwi.mViewer.getComponent().getStates();
        int n = dwi.todosOsStates.size();
        if (dwi.stateDentroDoRetangulo != null) {
            for (State s : dwi.stateDentroDoRetangulo) {
                dwi.statesSelecionados.remove(s);
            }
            dwi.stateDentroDoRetangulo.clear();
        }

        for (int i = 0; i < n; i++) {
            State s = dwi.todosOsStates.get(i);
            dwi.posCircleX = s.getLayoutX() + RAIO_CIRCULO;
            dwi.posCircleY = s.getLayoutY() + RAIO_CIRCULO;

            dwi.posicaoDoEstadoXMaisRaio = dwi.posCircleX + RAIO_CIRCULO;
            dwi.posicaoDoEstadoYMaisRaio = dwi.posCircleY + RAIO_CIRCULO;
            dwi.posicaoDoEstadoXMenosRaio = dwi.posCircleX - RAIO_CIRCULO;
            dwi.posicaoDoEstadoYMenosRaio = dwi.posCircleY - RAIO_CIRCULO;

            //verificando se a area do retangulo estÃ¡ pegando atÃ© o centro do state
            if (dwi.posicaoDoEstadoXMenosRaio == inicioDoRectanguloX || dwi.posicaoDoEstadoXMenosRaio > inicioDoRectanguloX) {
                if (dwi.posicaoDoEstadoXMaisRaio == finalDoRectanguloX || dwi.posicaoDoEstadoXMaisRaio < finalDoRectanguloX) {
                    if (dwi.posicaoDoEstadoYMenosRaio == inicioDoRectanguloY || dwi.posicaoDoEstadoYMenosRaio > inicioDoRectanguloY) {
                        if (dwi.posicaoDoEstadoYMaisRaio == finalDoRectanguloY || dwi.posicaoDoEstadoYMaisRaio < finalDoRectanguloY) {
                            dwi.stateDentroDoRetangulo.add(s);
                            s.setBorderWidth(2);
                            s.setBorderColor("blue");
                            s.setTextColor("blue");
                            s.setTextSyle(State.TEXTSTYLE_BOLD);

                            aux = true;
                        } else {
                            s.setBorderWidth(1);
                            s.setBorderColor("black");
                            s.setTextColor("black");
                            s.setTextSyle(State.TEXTSTYLE_NORMAL);
                        }
                    } else {
                        s.setBorderWidth(1);
                        s.setBorderColor("black");
                        s.setTextColor("black");
                        s.setTextSyle(State.TEXTSTYLE_NORMAL);
                    }
                } else {
                    s.setBorderWidth(1);
                    s.setBorderColor("black");
                    s.setTextColor("black");
                    s.setTextSyle(State.TEXTSTYLE_NORMAL);
                }
            } else {
                s.setBorderWidth(1);
                s.setBorderColor("black");
                s.setTextColor("black");
                s.setTextSyle(State.TEXTSTYLE_NORMAL);
            }
        }
        dwi.statesSelecionados.addAll(dwi.stateDentroDoRetangulo);

        //MUDANDO ICONE E SELECAO DO TOGGLEBUTON BIGSTATE
        changeIconToggleBigState(dwi);
        
        return aux;
    }
    
    private void changeIconToggleBigState(DesignerWindowImpl dwi){
        if(dwi.statesSelecionados.size()==1){
            BigState bigState = (BigState) dwi.statesSelecionados.get(0).getValue("bigstate");
            if (bigState != null){
                dwi.mBtnBigState.setSelected(true);
                dwi.mBtnBigState.setGraphic(dwi.iconBigStateDismount);
            }
            else {
                dwi.mBtnBigState.setSelected(false);
                dwi.mBtnBigState.setGraphic(dwi.iconBigState);
            }
        }
        else {
            dwi.mBtnBigState.setSelected(false);
            dwi.mBtnBigState.setGraphic(dwi.iconBigState);
        }
    }

    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}

    
}
