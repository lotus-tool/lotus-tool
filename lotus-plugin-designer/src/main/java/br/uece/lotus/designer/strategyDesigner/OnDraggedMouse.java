package br.uece.lotus.designer.strategyDesigner;




import br.uece.lotus.State;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_MOVER;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_NENHUM;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_VERTICE;
import static br.uece.lotus.designer.DesignerWindowImpl.RAIO_CIRCULO;
import br.uece.lotus.viewer.StateView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDraggedMouse implements Strategy{

    @Override
    public void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent t) {
    
        //                         HAND MOVE                                     //
       if (dwi.mModoAtual == MODO_MOVER) {
           //Pegar Moviemnto do mouse
           dwi.posicaoMViewerHandX += t.getSceneX() - dwi.mouseHandX;
           dwi.posicaoMViewerHandY += t.getSceneY() - dwi.mouseHandY;
           //setar nova posição apos calculo do movimento
           dwi.mViewer.getNode().setTranslateX(dwi.posicaoMViewerHandX);
           dwi.mViewer.getNode().setTranslateY(dwi.posicaoMViewerHandY);
           //setar nova posição do mouse no mViewer
           dwi.mouseHandX = t.getSceneX();
           dwi.mouseHandY = t.getSceneY();
       }

       if (dwi.mModoAtual != MODO_VERTICE && dwi.mModoAtual != MODO_NENHUM) {
           return;
       }
       if (dwi.mModoAtual == MODO_NENHUM) {
           if ((dwi.statesSelecionadoPeloRetangulo || dwi.selecionadoUm ) && !dwi.selecaoPadrao) {

               if (!dwi.segundaVezEmDiante) {
                   dwi.variacaoX = t.getX() - dwi.coordenadaInicialX;
                   dwi.variacaoY = t.getY() - dwi.coordenadaInicialY;
                   dwi.segundaVezEmDiante = true;
                   dwi.ultimoInstanteX = t.getX();
                   dwi.ultimoInstanteY = t.getY();

               } else {
                   //System.out.println("seunda vez");
                   dwi.variacaoX = (t.getX() - dwi.ultimoInstanteX);
                   dwi.variacaoY = (t.getY() - dwi.ultimoInstanteY);
                   dwi.ultimoInstanteX = t.getX();
                   dwi.ultimoInstanteY = t.getY();
               }

               for (State s : dwi.statesSelecionados) {
                   posicionandoConjuntoDeStates(s, dwi.variacaoX, dwi.variacaoY);
               }

           } else {

               if (dwi.modoCriacaoDoRetangulo) {
                   dwi.auxA = true;

                   dwi.coornenadaIstanteX = t.getX();
                   dwi.coordenadaIstanteY = t.getY();

                   if (dwi.coornenadaIstanteX <= dwi.coordenadaInicialX) {

                       if (dwi.coordenadaIstanteY <= dwi.coordenadaInicialY) {
                           dwi.largura = dwi.coordenadaInicialY - dwi.coordenadaIstanteY;
                           dwi.altura = dwi.coordenadaInicialX - dwi.coornenadaIstanteX;
                           dwi.inicioDoRectanguloX = dwi.coornenadaIstanteX;
                           dwi.inicioDoRectanguloY = dwi.coordenadaIstanteY;
                           dwi.inicioDoRectanguloXAux = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloYAux = dwi.coordenadaInicialY;
                       }
                       if (dwi.coordenadaIstanteY >= dwi.coordenadaInicialY) {
                           dwi.altura = dwi.coordenadaInicialX - dwi.coornenadaIstanteX;
                           dwi.largura = dwi.coordenadaIstanteY - dwi.coordenadaInicialY;
                           dwi.inicioDoRectanguloX = dwi.coordenadaInicialX - dwi.altura;
                           dwi.inicioDoRectanguloY = dwi.coordenadaInicialY;
                           dwi.inicioDoRectanguloXAux = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloYAux = dwi.coordenadaInicialY;
                       }

                   } else {
                       if (dwi.coordenadaIstanteY <= dwi.coordenadaInicialY) {
                           dwi.altura = dwi.coornenadaIstanteX - dwi.coordenadaInicialX;
                           dwi.largura = dwi.coordenadaInicialY - dwi.coordenadaIstanteY;
                           dwi.inicioDoRectanguloX = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloY = dwi.coordenadaInicialY - dwi.largura;
                           dwi.inicioDoRectanguloXAux = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloYAux = dwi.coordenadaInicialY;
                       }
                       if (dwi.coordenadaIstanteY >= dwi.coordenadaInicialY) {
                           dwi.altura = dwi.coornenadaIstanteX - dwi.coordenadaInicialX;
                           dwi.largura = dwi.coordenadaIstanteY - dwi.coordenadaInicialY;
                           dwi.inicioDoRectanguloX = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloY = dwi.coordenadaInicialY;
                           dwi.inicioDoRectanguloXAux = dwi.coordenadaInicialX;
                           dwi.inicioDoRectanguloYAux = dwi.coordenadaInicialY;
                       }

                   }
                   if (dwi.ultimoRetanguloAdicionado != null) {
                       dwi.mViewer.getNode().getChildren().remove(dwi.ultimoRetanguloAdicionado);
                   }

                   Rectangle retangulo = new Rectangle((int) dwi.inicioDoRectanguloX, (int) dwi.inicioDoRectanguloY, (int) dwi.altura, (int) dwi.largura);
                   dwi.ultimoRetanguloAdicionado = retangulo;
                   retangulo.setFill(Color.BLUE);
                   retangulo.setOpacity(0.4);
                   retangulo.setVisible(true);
                   dwi.mViewer.getNode().getChildren().add(retangulo);

               } else {

                   if (!(dwi.mComponentSobMouse instanceof StateView) || !dwi.selecaoPadrao) {
                       return;
                   }

                   State s = ((StateView) dwi.mComponentSobMouse).getState();
                   s.setLayoutX(t.getX() + dwi.variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice - RAIO_CIRCULO);
                   s.setLayoutY(t.getY() + dwi.variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice - RAIO_CIRCULO);
               }
           }
       }
    }
    
    
    private void posicionandoConjuntoDeStates(State v, double mX, double mY) {
        v.setLayoutX(v.getLayoutX() + mX);
        v.setLayoutY(v.getLayoutY() + mY);
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
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
    
    
    
}
