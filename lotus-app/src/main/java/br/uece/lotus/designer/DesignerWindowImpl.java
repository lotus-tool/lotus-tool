/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.designer;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.*;
import br.uece.seed.app.ExtensibleFXToolbar;
import br.uece.seed.app.ExtensibleToolbar;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
/**
 *
 * @author emerson
 */
public class DesignerWindowImpl extends AnchorPane implements DesignerWindow {
/////////////////////////MINHA_VARIÁVEIS///////////////////////////////////////////////////
    Rectangle ultimoRetanguloAdicionado;                                                 //
    double coordenadaInicialX, coordenadaInicialY, coordenadaFinalX, coordenadaFinalY,   //
    coornenadaIstanteX, coordenadaIstanteY;                                              //
    boolean shifPrecionado = false;                                                      //
    boolean algumSelecionadoPeloRetangulo = false;                                                       //
    boolean ctrlPressionado = false;                                                     //
    double posicaoDoEstadoXMaisRaio;                                                     //
    double posicaoDoEstadoYMaisRaio;                                                     //
    double posicaoDoEstadoXMenosRaio;                                                    //
    double posicaoDoEstadoYMenosRaio;                                                    //
    ArrayList<State> stateDentroDoRetangulo = new ArrayList<State>();                        //
    double posCircleX;                                                                   //
    double posCircleY;                                                                   //
    double variacaoDeX;                                                                  //
    double variacaoDeY;                                                                  //
    double stateDoPrimeiroClickX, stateDoPrimeiroClickY;                                 //
    boolean caso1, caso2, caso3, caso4, retorno, aux, modoCriacaoDoRetangulo = false;             //
    static final int RAIO_CIRCULO = 15;                                                  //
///////////////////////////////////////////////////////////////////////////////////////////



    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    private final BasicComponentViewer mViewer;
    private final ToolBar mToolbar;
    private final ToggleGroup mToggleGroup;
    private final ToggleButton mBtnArrow;
    private final ToggleButton mBtnState;
    private final ToggleButton mBtnTransitionLine;
    private final ToggleButton mBtnTransitionArc;
    private final ToggleButton mBtnEraser;
    //private final ToggleButton maozinha;

    private final ToolBar mStateToolbar;
    private final ToolBar mTransitionToolbar;
    private final ExtensibleToolbar mExtensibleStateToolbar;
    private final ExtensibleToolbar mExtensibleTransitionToolbar;

    private final ScrollPane mScrollPanel;
    private boolean mExibirPropriedadesTransicao;

    private BasicComponentViewer.Listener mViewerListener = new BasicComponentViewer.Listener() {
        @Override
        public void onTransitionViewCreated(BasicComponentViewer v, TransitionView tv) {
            if (mExibirPropriedadesTransicao) {
                setComponenteSelecionado(tv);
            }
            mExibirPropriedadesTransicao = false;

        }
    };
    private String mDefaultTransitionColor;
    private String mDefaultTransitionTextColor;
    private Integer mDefaultTransitionWidth;
    private String mDefaultTransitionLabel;    
    private EventHandler<ActionEvent> mSetStateAsInitial = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setAsInitial();
        }
    };
    private EventHandler<ActionEvent> mSetStateAsNormal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setFinal(false);
            s.setError(false);
        }
    };
    private EventHandler<ActionEvent> mSetStateAsError = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setError(true);
        }
    };

    private EventHandler<ActionEvent> mSetStateAsFinal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setFinal(true);
        }
    };
    private int mTransitionViewType;

    @Override
    public ExtensibleToolbar getTransitionContextToolbar() {
        return mExtensibleTransitionToolbar;
    }

    @Override
    public ExtensibleToolbar getStateContextToolbar() {
        return mExtensibleStateToolbar;
    }

    @Override
    public void setDefaultTransitionLabel(String label) {
        mDefaultTransitionLabel = label;
    }

    @Override
    public void setDefaultTransitionWidth(Integer width) {
        mDefaultTransitionWidth = width;
    }

    @Override
    public void setDefaultTransitionTextColor(String color) {
        mDefaultTransitionTextColor = color;
    }

    @Override
    public void setDefaultTransitionColor(String color) {
        mDefaultTransitionColor = color;
    }

    @Override
    public String getTitle() {
        Component c = mViewer.getComponent();
        return c.getName();
    }

    @Override
    public Node getNode() {
        return this;
    }

    public interface Listener {

        void onSelectionChange(DesignerWindowImpl v);
    }

    private int mModoAtual;
    private final ContextMenu mComponentContextMenu;
    //seleção e destaque
    private View mComponentSobMouse;
    private View mComponentSelecionado;
    private final List<Listener> mListeners = new ArrayList<>();

    public DesignerWindowImpl(BasicComponentViewer viewer) {
        mToolbar = new ToolBar();
        mToggleGroup = new ToggleGroup();
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent e) -> {
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnState = new ToggleButton();
        mBtnState.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_state.png"))));
        mBtnState.setOnAction((ActionEvent e) -> {
            setModo(MODO_VERTICE);
        });
        mBtnState.setToggleGroup(mToggleGroup);
        mBtnTransitionLine = new ToggleButton();
        mBtnTransitionLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_transition_line.png"))));
        mBtnTransitionLine.setOnAction((ActionEvent e) -> {
            mTransitionViewType = TransitionViewFactory.Type.LINEAR;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);
        
        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent e) -> {
            mTransitionViewType = TransitionViewFactory.Type.SEMI_CIRCLE;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);
        
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent e) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);

        mToolbar.getItems().addAll(mBtnArrow, mBtnState, mBtnTransitionLine, mBtnTransitionArc, mBtnEraser);

        mStateToolbar = new ToolBar();
        mStateToolbar.setVisible(false);
        mTransitionToolbar = new ToolBar();
        mTransitionToolbar.setVisible(false);
//        getChildren().add(mStateToolbar);
//        getChildren().add(mTransitionToolbar);
        mExtensibleStateToolbar = new ExtensibleFXToolbar(mStateToolbar);
        mExtensibleTransitionToolbar = new ExtensibleFXToolbar(mTransitionToolbar);

        HBox aux = new HBox(mToolbar, mStateToolbar, mTransitionToolbar);
        getChildren().add(aux);
        AnchorPane.setTopAnchor(aux, 0D);
        AnchorPane.setLeftAnchor(aux, 0D);
        AnchorPane.setRightAnchor(aux, 0D);

        mComponentContextMenu = new ContextMenu();

        mViewer = viewer;
        mViewer.addListener(mViewerListener);
        mViewer.setStateContextMenu(mComponentContextMenu);
        mViewer.setOnMouseClicked(aoClicarMouse);
        mViewer.setOnMouseMoved(aoMoverMouse);


        mViewer.setOnDragDetected(aoDetectarDragSobreVertice);
        mViewer.setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        mViewer.setOnDragDropped(aoSoltarMouseSobreVertice);

        mViewer.setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        mViewer.setOnMouseDragged(aoArrastarVerticeComOMouse);
        mViewer.setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);
//        mViewer.setPrefSize(500, 500);        
        mScrollPanel = new ScrollPane(mViewer);
        AnchorPane.setTopAnchor(mScrollPanel, 44D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);
        getChildren().add(mScrollPanel);
        mViewer.minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.minWidthProperty().bind(mScrollPanel.widthProperty());
        MenuItem mSetAsInitialMenuItem = new MenuItem("Set as initial");        
        mSetAsInitialMenuItem.setOnAction(mSetStateAsInitial);
        MenuItem mSetAsNormalMenuItem = new MenuItem("Set as normal");
        mSetAsNormalMenuItem.setOnAction(mSetStateAsNormal);
        MenuItem mSetAsFinalMenuItem = new MenuItem("Set as error");
        mSetAsFinalMenuItem.setOnAction(mSetStateAsError);
        MenuItem mSetAsErrorMenuItem = new MenuItem("Set as final");
        mSetAsErrorMenuItem.setOnAction(mSetStateAsFinal);
        
        MenuItem mSaveAsPNG = new MenuItem("Save as PNG");
        mSaveAsPNG.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as PNG");
            fileChooser.setInitialFileName(mViewer.getComponent().getName() + ".png");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png")
            );
            File arq = fileChooser.showSaveDialog(null);
            if (arq == null) {
                return;
            }
            mViewer.saveAsPng(arq);
            JOptionPane.showMessageDialog(null, "PNG Image successfuly saved!");
        });
        mComponentContextMenu.getItems().addAll(mSetAsInitialMenuItem, new SeparatorMenuItem(), mSetAsNormalMenuItem, mSetAsFinalMenuItem, mSetAsErrorMenuItem, new SeparatorMenuItem(), mSaveAsPNG);

    }

    ////////////////////////////////////////////////////////////////////////////
    // Ao Clicar o mouse(clickar e soltar)
    ////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                setComponenteSelecionado(mComponentSobMouse);
                mComponentContextMenu.show(mViewer, e.getScreenX(), e.getScreenY());
                return;
            }

            if (mModoAtual == MODO_NENHUM) {


            } else {
                if (mModoAtual == MODO_VERTICE) {
                    if (!(mComponentSobMouse instanceof StateView)) {
                        int id = mViewer.getComponent().getStatesCount();
                        State s = mViewer.getComponent().newState(id);
                        s.setLayoutX(e.getX());
                        s.setLayoutY(e.getY());
                        s.setLabel(String.valueOf(id));
                        if (mViewer.getComponent().getStatesCount() == 0) {
                            mViewer.getComponent().setInitialState(s);
                        }
                    }
                } else if (mModoAtual == MODO_REMOVER) {
                    if (mComponentSobMouse instanceof StateView) {
                        State v = ((StateView) mComponentSobMouse).getState();
                        mViewer.getComponent().remove(v);
                    } else if (mComponentSobMouse instanceof TransitionView) {
                        Transition t = ((TransitionView) mComponentSobMouse).getTransition();
                        mViewer.getComponent().remove(t);
                    }
                }
            }
        }
    };
////////////////////////////////////////////////////////////////////////////////
// Mover o mouse(mover o cursor do mouse)
////////////////////////////////////////////////////////////////////////////
    private double coordenadaIniX = 0;
    private double coordenadaIniY = 0;
    private EventHandler<? super MouseEvent> aoMoverMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            View aux = getComponentePelaPosicaoMouse(t.getX(), t.getY());
//            if (mComponentSobMouse != null) {
//                mComponentSobMouse.setHighlighted(false);
//            }
//            if (aux != null) {
//                aux.setHighlighted(true);
//            }
            mComponentSobMouse = aux;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Mover state (clickar sem soltar)
    ////////////////////////////////////////////////////////////////////////////
    private boolean verificacao = false,auxA=false;
    private double variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice,ultimoInstanteX;
    private double variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice,ultimoInstanteY;
    private boolean downShift, selecionadoUm,selecioneiComShift,selecaoDoEmerson;
    ArrayList<State>statesSelecionados=new ArrayList<State>();

    private EventHandler<? super MouseEvent> aoIniciarArrastoVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
            coordenadaInicialX = e.getX();
            coordenadaInicialY = e.getY();
            ultimoInstanteX = 0;
            ultimoInstanteY = 0;
            segundaVezEmDiante = false;

            
            if(e.isShiftDown()){
                downShift=true;

            }

            if(downShift&&mComponentSobMouse!=null){
                StateView stateView= (StateView)mComponentSobMouse;
                State state=stateView.getState();
                state.setBorderWidth(2);
                state.setBorderColor("blue");
                state.setTextColor("blue");
                state.setTextSyle(State.TEXTSTYLE_BOLD);
                statesSelecionados.add(state);
                selecionadoUm =true;
                selecioneiComShift=true;

                modoCriacaoDoRetangulo=false;
                downShift=false;
                selecaoDoEmerson=false;
                return;
            }else{
            
            if (!algumSelecionadoPeloRetangulo&&mComponentSobMouse!=null && !selecioneiComShift ) {


                selecaoDoEmerson=true;
                if (statesSelecionados != null) {
                    statesSelecionados.clear();
                }
                setComponenteSelecionado(mComponentSobMouse);
                StateView stateView= (StateView)mComponentSobMouse;
                State state=stateView.getState();
                statesSelecionados.add(state);
                modoCriacaoDoRetangulo=false;
                return;
            }

            else{
                if (!SeClickeiEntreSelecionados(e.getX(), e.getY()) && statesSelecionados != null) {

                    verificacao = true;
                    for (State s : statesSelecionados) {
                        s.setBorderWidth(1);
                        s.setBorderColor("black");
                        s.setTextColor("black");
                        s.setTextSyle(State.TEXTSTYLE_NORMAL);
                    }
                    if (mComponentSobMouse != null) {
                        selecaoDoEmerson=true;
                        if (statesSelecionados != null) {
                            statesSelecionados.clear();
                            modoCriacaoDoRetangulo = false;
                        }

                        setComponenteSelecionado(mComponentSobMouse);
                        StateView stateView= (StateView)mComponentSobMouse;
                        State state=stateView.getState();
                        statesSelecionados.add(state);


                    }
                    else{
                        selecaoDoEmerson=false;
                        modoCriacaoDoRetangulo = true;
                        algumSelecionadoPeloRetangulo = false;
                        selecioneiComShift=false;
                        if(selecionadoUm){
                        selecionadoUm=false;
                    }
                        for (State s : statesSelecionados) {
                            s.setBorderWidth(1);
                            s.setBorderColor("black");
                            s.setTextColor("black");
                            s.setTextSyle(State.TEXTSTYLE_NORMAL);
                        }
                        statesSelecionados.clear();

                    }

            } else {
                    verificacao = false;
                    modoCriacaoDoRetangulo = false;
                }

            }
            }

            if (!(mComponentSobMouse instanceof StateView)) {

                return;
            }

            StateView v = (StateView) mComponentSobMouse;

            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getLayoutX() - e.getX()+RAIO_CIRCULO;
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getLayoutY() - e.getY()+RAIO_CIRCULO;

        }
    };



    private double variacaoX, variacaoY;
    private boolean segundaVezEmDiante;
    private double largura = 0, altura = 0;
    private double inicioDoRectanguloX, inicioDoRectanguloY,inicioDoRectanguloXAux,inicioDoRectanguloYAux;
    //////////////////////////////////////////////////////////////////////////////////////////
    // clickar e não soltar e mover o mouse(precionando e movendo/dragg)
    //////////////////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
             if ((algumSelecionadoPeloRetangulo || selecionadoUm) && !selecaoDoEmerson) {

                if (!segundaVezEmDiante ) {
                    variacaoX = t.getX() - coordenadaInicialX;
                    variacaoY = t.getY() - coordenadaInicialY;
                    segundaVezEmDiante = true;
                    ultimoInstanteX = t.getX();
                    ultimoInstanteY = t.getY();

                } else {
                    variacaoX = (t.getX() - ultimoInstanteX);
                    variacaoY = (t.getY() - ultimoInstanteY);
                    ultimoInstanteX = t.getX();
                    ultimoInstanteY = t.getY();

                }

                for (State s : statesSelecionados) {
                    setandoStateEmConjunto(s, variacaoX, variacaoY);
                }

            } else {

                if (modoCriacaoDoRetangulo) {
                    auxA=true;

                    coornenadaIstanteX = t.getX();
                    coordenadaIstanteY = t.getY();



                    if (coornenadaIstanteX <= coordenadaInicialX) {

                        if (coordenadaIstanteY <= coordenadaInicialY) {
                            largura = coordenadaInicialY - coordenadaIstanteY;
                            altura = coordenadaInicialX - coornenadaIstanteX;
                            inicioDoRectanguloX = coornenadaIstanteX;
                            inicioDoRectanguloY = coordenadaIstanteY;
                            inicioDoRectanguloXAux=coordenadaInicialX;
                            inicioDoRectanguloYAux=coordenadaInicialY;
                        }
                        if(coordenadaIstanteY >= coordenadaInicialY){
                            altura = coordenadaInicialX  - coornenadaIstanteX  ;
                            largura = coordenadaIstanteY - coordenadaInicialY ;
                            inicioDoRectanguloX = coordenadaInicialX-altura;
                            inicioDoRectanguloY = coordenadaInicialY;
                            inicioDoRectanguloXAux=coordenadaInicialX;
                            inicioDoRectanguloYAux=coordenadaInicialY;
                        }

                    } else {
                        if (coordenadaIstanteY <= coordenadaInicialY) {
                            altura = coornenadaIstanteX - coordenadaInicialX ;
                            largura = coordenadaInicialY - coordenadaIstanteY;
                            inicioDoRectanguloX = coordenadaInicialX;
                            inicioDoRectanguloY = coordenadaInicialY-largura;
                            inicioDoRectanguloXAux=coordenadaInicialX;
                            inicioDoRectanguloYAux=coordenadaInicialY;
                        }
                        if(coordenadaIstanteY >= coordenadaInicialY){
                            altura = coornenadaIstanteX - coordenadaInicialX;
                            largura=coordenadaIstanteY-coordenadaInicialY;
                            inicioDoRectanguloX = coordenadaInicialX;
                            inicioDoRectanguloY = coordenadaInicialY;
                            inicioDoRectanguloXAux=coordenadaInicialX;
                            inicioDoRectanguloYAux=coordenadaInicialY;
                        }

                    }
                    if (ultimoRetanguloAdicionado != null) {
                        mViewer.getChildren().remove(ultimoRetanguloAdicionado);
                    }

                    Rectangle retangulo = new Rectangle((int) inicioDoRectanguloX, (int) inicioDoRectanguloY, (int) altura, (int) largura);
                    ultimoRetanguloAdicionado = retangulo;
                    retangulo.setFill(Color.BLUE);
                    retangulo.setOpacity(0.4);
                    retangulo.setVisible(true);
                    mViewer.getChildren().add(retangulo);

                } else {

                    if (!(mComponentSobMouse instanceof StateView)||!selecaoDoEmerson) {
                        return;
                    }

                    State s = ((StateView) mComponentSobMouse).getState();
                    s.setLayoutX(t.getX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice-RAIO_CIRCULO);
                    s.setLayoutY(t.getY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice-RAIO_CIRCULO);
                }
            }
        }
    };
    ////////////////////////////////////////////////////////////////////
    //Soltar
    ///////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoLiberarVerticeArrastadoComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
           variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
            if (modoCriacaoDoRetangulo) {
                if(!auxA){
                    inicioDoRectanguloXAux=coordenadaInicialX;
                    inicioDoRectanguloYAux=coordenadaInicialY;
                }
                if(!selecionadoUm) {
                    coordenadaFinalX = t.getX();
                    coordenadaFinalY = t.getY();

                    algumSelecionadoPeloRetangulo = selecionandoComRetangulo(inicioDoRectanguloXAux, inicioDoRectanguloYAux, coordenadaFinalX, coordenadaFinalY);
                }
                if (ultimoRetanguloAdicionado != null) {
                    mViewer.getChildren().remove(ultimoRetanguloAdicionado);

                }

                auxA=false;
            }
        }
    };
    ArrayList<State> todosOsStates;

    public boolean selecionandoComRetangulo(double inicioDoRectanguloX, double inicioDoRectanguloY, double finalDoRectanguloX, double finalDoRectanguloY) {
        boolean aux = false;
        /////////////////////////////////////////////////
        ////////organizando as pesições do retangulo
        /////////////////////////////////////////////////
        if(inicioDoRectanguloX>finalDoRectanguloX){
            double ajuda = finalDoRectanguloX;
            finalDoRectanguloX=inicioDoRectanguloX;
            inicioDoRectanguloX=ajuda;

        }
        if(inicioDoRectanguloY>finalDoRectanguloY){
            double ajuda = finalDoRectanguloY;
            finalDoRectanguloY=inicioDoRectanguloY;
            inicioDoRectanguloY=ajuda;


        }


        todosOsStates = (ArrayList<State>) mViewer.getComponent().getStates();
        int n = todosOsStates.size();
        if (stateDentroDoRetangulo != null) {
            for(State s: stateDentroDoRetangulo){
                statesSelecionados.remove(s);
            }
            stateDentroDoRetangulo.clear();
        }

        for (int i = 0; i < n; i++) {
            State s = todosOsStates.get(i);
            posCircleX = s.getLayoutX() + RAIO_CIRCULO;
            posCircleY = s.getLayoutY() + RAIO_CIRCULO;

            posicaoDoEstadoXMaisRaio = posCircleX + RAIO_CIRCULO;
            posicaoDoEstadoYMaisRaio = posCircleY + RAIO_CIRCULO;
            posicaoDoEstadoXMenosRaio = posCircleX - RAIO_CIRCULO;
            posicaoDoEstadoYMenosRaio = posCircleY - RAIO_CIRCULO;

            //verificando se a area do retangulo está pegando até o centro do state
            if (posicaoDoEstadoXMenosRaio == inicioDoRectanguloX || posicaoDoEstadoXMenosRaio > inicioDoRectanguloX) {
                if (posicaoDoEstadoXMaisRaio == finalDoRectanguloX || posicaoDoEstadoXMaisRaio < finalDoRectanguloX) {
                    if (posicaoDoEstadoYMenosRaio == inicioDoRectanguloY || posicaoDoEstadoYMenosRaio > inicioDoRectanguloY) {
                        if (posicaoDoEstadoYMaisRaio == finalDoRectanguloY || posicaoDoEstadoYMaisRaio < finalDoRectanguloY) {
                            stateDentroDoRetangulo.add(s);
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
        statesSelecionados.addAll(stateDentroDoRetangulo);
        return aux;
    }

    public boolean SeClickeiEntreSelecionados(double x, double y) {
        boolean aux = false;
        if (statesSelecionados != null) {

            for (State s : statesSelecionados) {
                posCircleX = s.getLayoutX() + RAIO_CIRCULO;
                posCircleY = s.getLayoutY() + RAIO_CIRCULO;

                posicaoDoEstadoXMaisRaio = posCircleX + RAIO_CIRCULO;
                posicaoDoEstadoYMaisRaio = posCircleY + RAIO_CIRCULO;
                posicaoDoEstadoXMenosRaio = posCircleX - RAIO_CIRCULO;
                posicaoDoEstadoYMenosRaio = posCircleY - RAIO_CIRCULO;

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


    ////////////////////////////////////////////////////////////////////////////
    // Adicionar transição
    ////////////////////////////////////////////////////////////////////////////
    private StateView mVerticeOrigemParaAdicionarTransicao;
    private StateView mVerticeDestinoParaAdicionarTransicao;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_TRANSICAO) {
                return;
            }

            if (!(mComponentSobMouse instanceof StateView)) {
                return;
            }
            StateView v = (StateView) mComponentSobMouse;

            //guarda o objeto no qual iniciamos o drag            
            mVerticeOrigemParaAdicionarTransicao = v;

            //inicia o drag'n'drop
            Dragboard db = mVerticeOrigemParaAdicionarTransicao.startDragAndDrop(TransferMode.ANY);

            //soh funciona com as três linhas a seguir. Porque? Eu não sei.
            ClipboardContent content = new ClipboardContent();
            content.putString("gambiarra");
            db.setContent(content);

            //indica que este evento foi realizado
            t.consume();
        }
    };
    private EventHandler<DragEvent> aoDetectarPossivelAlvoParaSoltarODrag = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            //a informaçao esta sendo solta sobre o alvo
            //aceita soltar o mouse somente se não é o mesmo nodo de origem 
            //e possui uma string            
            if (event.getGestureSource() != event.getSource()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            View v = getComponentePelaPosicaoMouse(event.getX(), event.getY());
            mVerticeDestinoParaAdicionarTransicao = (v instanceof StateView) ? ((StateView) v) : null;
            event.consume();
        }
    };
    private final EventHandler<DragEvent> aoSoltarMouseSobreVertice = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            if (mModoAtual != MODO_TRANSICAO) {
                return;
            }

            if (mVerticeDestinoParaAdicionarTransicao != null) {
                State o = mVerticeOrigemParaAdicionarTransicao.getState();
                State d = mVerticeDestinoParaAdicionarTransicao.getState();
                mExibirPropriedadesTransicao = true;
                Transition t = mViewer.getComponent().buildTransition(o, d)
                    .setValue("view.type", mTransitionViewType)
                    .create();
                applyDefaults(t);
            }

            event.setDropCompleted(true);
            event.consume();
        }

        private void applyDefaults(Transition t) {
            if (mDefaultTransitionLabel != null) {
                t.setLabel(mDefaultTransitionLabel);
            }
            if (mDefaultTransitionColor != null) {
                t.setColor(mDefaultTransitionColor);
            }
            if (mDefaultTransitionTextColor != null) {
                t.setTextColor(mDefaultTransitionTextColor);
            }
            if (mDefaultTransitionWidth != null) {
                t.setWidth(mDefaultTransitionWidth);
            }
        }

    };

    @Override
    public Component getComponent() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
    }

    public void setModo(int modo) {
        this.mModoAtual = modo;
        mViewer.setCursor(Cursor.DEFAULT);
        mStateToolbar.setVisible(mModoAtual == MODO_VERTICE);
        mTransitionToolbar.setVisible(mModoAtual == MODO_TRANSICAO);
    }

    private View getComponentePelaPosicaoMouse(double x, double y) {
        for (State s : mViewer.getComponent().getStates()) {
            View v = (View) s.getValue("view");
            if (v.isInsideBounds(x, y)) {
                return v;
            }
        }
        for (Transition t : mViewer.getComponent().getTransitions()) {
            View v = (View) t.getValue("view");
            if (v.isInsideBounds(x, y)) {
                return v;
            }
        }
        return null;
    }

    private void setComponenteSelecionado(View t) {
        if (mComponentSelecionado != null) {
            removeSelectedStyles(mComponentSelecionado);
        }
        mComponentSelecionado = t;
        if (t != null) {
            applySelectedStyles(mComponentSelecionado);
        }
        for (Listener l : mListeners) {
            l.onSelectionChange(this);
        }
    }

    public View getSelectedView() {
        return mComponentSelecionado;
    }

    private void applySelectedStyles(View v) { 
        System.out.println("applyselectedstyles " + v);       
        if (v instanceof StateView) {
            State s = ((StateView) v).getState();
            s.setBorderWidth(2);
            s.setBorderColor("blue");
            s.setTextColor("blue");
            s.setTextSyle(State.TEXTSTYLE_BOLD);
        } else if (v instanceof TransitionView) {
            Transition t = ((TransitionView) v).getTransition();
            t.setWidth(1);
            t.setColor("blue");
            t.setTextColor("blue");
            t.setTextSyle(State.TEXTSTYLE_BOLD);
        }
    }

    private void removeSelectedStyles(View v) {
        System.out.println("removeselectedstyles " + v);        
        if (v instanceof StateView) {
            State s = ((StateView) v).getState();
            s.setBorderWidth(1);
            s.setBorderColor("black");
            s.setTextColor("black");
            s.setTextSyle(State.TEXTSTYLE_NORMAL);
        } else if (v instanceof TransitionView) {
            Transition t = ((TransitionView) v).getTransition();
            if (t == null) {
                return;
            }
            t.setWidth(1);
            t.setColor("black");
            t.setTextColor("black");
            t.setTextSyle(State.TEXTSTYLE_NORMAL);
        }
    }
    double posXAntes;
    double posYAntes;

    public void setandoStateEmConjunto(State v, double mX, double mY) {
        double posX = v.getLayoutX();
        double posY = v.getLayoutY();
        posXAntes = posX;
        posYAntes = posY;
        double distanciaClick;
        double distanciaState;
        double deltaX, deltaY;
//        mX = mX - RAIO_CIRCULO;
//        mY = mY - RAIO_CIRCULO;
        System.out.println("varuacaoX-raio" + mX);
        System.out.println("varuacaoY-raio" + mY);
//        System.out.println("mX" + mX);
//        System.out.println("mY" + mY);
//        System.out.println("posX" + posX);
//        System.out.println("posY" + posY);
//        distanciaClick = Math.floor(Math.sqrt((mX * mX) + (mY * mY)));
//        distanciaState = Math.floor(Math.sqrt((posX * posX) + (posY * posY)));
//        System.out.println("distanciaClick" + distanciaClick);
//        System.out.println("distanciaState" + distanciaState);
//        if (distanciaClick > distanciaState) {
//            deltaX = mX - posX;
//            deltaY = mY - posY;
//            v.setLayoutX(posX + deltaX);
//            v.setLayoutY(posY + deltaY);
//
//        }
//        if (distanciaClick < distanciaState) {
//            deltaX = posX - mX;
//            deltaY = posY - mY;
//            v.setLayoutX(posX + deltaX);
//            v.setLayoutY(posY + deltaY);
//
//        }

        v.setLayoutX(v.getLayoutX() + mX);
        v.setLayoutY(v.getLayoutY() + mY);
        System.out.println(v.getLayoutX());
        System.out.println(v.getLayoutY());
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

}
