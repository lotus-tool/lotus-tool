/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do CearÃ¡.
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

import br.uece.lotus.BigState;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.designer.strategyDesigner.Context;
import br.uece.lotus.designer.strategyDesigner.OnClickedMouse;
import br.uece.lotus.designer.strategyDesigner.OnDragDetectedMouse;
import br.uece.lotus.designer.strategyDesigner.OnDragDropped;
import br.uece.lotus.designer.strategyDesigner.OnDragOverMouse;
import br.uece.lotus.designer.strategyDesigner.OnMovedMouse;
import br.uece.lotus.designer.strategyDesigner.OnPressedMouse;
import br.uece.lotus.designer.strategyDesigner.OnReleasedMouse;
import br.uece.lotus.designer.strategyDesigner.OnDraggedMouse;
import br.uece.lotus.designer.strategyDesigner.OnKeyPressed;
import br.uece.lotus.designer.strategyDesigner.OnScrollMouse;
import br.uece.lotus.properties.TransitionsPropertiesController;
import br.uece.lotus.viewer.*;
import br.uece.seed.app.ExtensibleFXToolbar;
import br.uece.seed.app.ExtensibleToolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

/**
 * @author emerson
 */
public class DesignerWindowImpl extends AnchorPane implements DesignerWindow {

    public final TextField txtLabel;
    public final TextField txtGuard;
    public final TextField txtProbability;
    public Rectangle ultimoRetanguloAdicionado;
    public double coordenadaInicialX;
    public double coordenadaInicialY;
    public double coordenadaFinalX;
    public double coordenadaFinalY;
    public double coornenadaIstanteX;
    public double coordenadaIstanteY;
    boolean shifPrecionado = false;
    public boolean statesSelecionadoPeloRetangulo = false;
    boolean ctrlPressionado = false;
    public double posicaoDoEstadoXMaisRaio;
    public double posicaoDoEstadoYMaisRaio;
    public double posicaoDoEstadoXMenosRaio;
    public double posicaoDoEstadoYMenosRaio;
    public ArrayList<State> stateDentroDoRetangulo = new ArrayList<State>();
    public double posCircleX;
    public double posCircleY;
    double variacaoDeX;
    double variacaoDeY;
    double stateDoPrimeiroClickX, stateDoPrimeiroClickY;
    boolean caso1;
    boolean caso2;
    boolean caso3;
    boolean caso4;
    boolean retorno;
    boolean aux;
    public boolean modoCriacaoDoRetangulo = false;
    public static final int RAIO_CIRCULO = 15;

    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    public static final int MODO_MOVER = 4;
    public int contID = -1;
    public ComponentView mViewer;
    private final ToolBar mToolbar;
    private final ToggleGroup mToggleGroup;
    public final ToggleButton mBtnBigState;
    private final ToggleButton mBtnArrow;
    private final ToggleButton mBtnState;
    private final ToggleButton mBtnTransitionLine;
    private final ToggleButton mBtnTransitionArc;
    private final ToggleButton mBtnEraser;
    private final ToggleButton mBtnHand;
    private final MenuButton mBtnZoom;
    private final Button mBtnUndo;
    private final Button mBtnRedo;
    private ComponentView[] mUndoRedo;
    private final int tamHistorico = 14;
    public final ImageView iconBigState = new ImageView(new Image(getClass().getResourceAsStream("/images/ic_big_state.png")));
    public final ImageView iconBigStateDismount = new ImageView(new Image(getClass().getResourceAsStream("/images/ic_big_state_dismount.png")));

    private final ToolBar mStateToolbar;
    private final ToolBar mTransitionToolbar;
    private final ExtensibleToolbar mExtensibleStateToolbar;
    private final ExtensibleToolbar mExtensibleTransitionToolbar;

    private final ScrollPane mScrollPanel;
    public boolean mExibirPropriedadesTransicao;

    public ComponentView getMViewer() {
        return this.mViewer;
    }

    private ComponentView.Listener mViewerListener = new ComponentView.Listener() {
        @Override
        public void onStateViewCreated(ComponentView cv, StateView v) {

        }

        @Override
        public void onTransitionViewCreated(ComponentView v, TransitionView tv) {
            if (mExibirPropriedadesTransicao) {
                setComponenteSelecionado(tv);
            }
            mExibirPropriedadesTransicao = false;

        }
    };
    public String mDefaultTransitionColor;
    public String mDefaultTransitionTextColor;
    public Integer mDefaultTransitionWidth;
    public String mDefaultTransitionLabel;
    private EventHandler<ActionEvent> mSetStateAsInitial = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setError(false);
            s.setFinal(false);
            s.setAsInitial();
            s.setColor(null);
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
            s.setColor(null);
        }
    };

    private EventHandler<ActionEvent> mSetStateAsError = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            if(s.isInitial()){
                JOptionPane.showMessageDialog(null, "Impossible to change an Initial state to Error.", "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Component c = mViewer.getComponent();

            s.setFinal(false);
            c.setErrorState(s);
            s.setColor(null);
        }
    };

    private EventHandler<ActionEvent> mSetStateAsFinal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            if(s.isInitial()){
                JOptionPane.showMessageDialog(null, "Impossible to change an Initial state to Final.", "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Component c = mViewer.getComponent();

            s.setError(false);
            c.setFinalState(s);
            s.setColor(null);
        }
    };

    private EventHandler<ActionEvent> mCreateDismountBigState = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null && statesSelecionados.size()==0) {
                return;
            }
            //CRIANDO BIGSTATE - USERDATA            
            BigState bigState = new BigState();
            List<State> listaS = statesSelecionados;

            //TEST DISMOUNT BIGSTATE
            if(statesSelecionados.size()==1){
                BigState bigS = (BigState) statesSelecionados.get(0).getValue("bigstate");
                if (bigS!= null){
                    if (!bigS.dismountBigState(mViewer.getComponent())){
                        JOptionPane.showMessageDialog(null, "You need another BigState before dismantling");
                        return;
                    }
                    mBtnBigState.setSelected(false);
                    mBtnBigState.setGraphic(iconBigState);
                    mViewer.getComponent().remove(statesSelecionados.get(0));
                    return;
                }
            }

            if (!bigState.addStatesAndTransition(listaS)){
                JOptionPane.showMessageDialog(null, "Add more States or initial State selected","Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //CRIANDO NA TELA O STATE MAIOR COM O BIGSTATE
            if (contID == -1) {
                updateContID();
            }
            int id = mViewer.getComponent().getStatesCount();
            State novoState = mViewer.getComponent().newState(id);
            novoState.setID(contID);
            contID++;
            novoState.setValue("bigstate", bigState);
            novoState.setLayoutX(statesSelecionados.get(0).getLayoutX()+20);
            novoState.setLayoutY(statesSelecionados.get(0).getLayoutY()+20);
            novoState.setLabel(String.valueOf(id));
            novoState.setBig(true);
            bigState.setState(novoState);

            //ADD TRANSITIONS DOS BIGSTATES
            int type = 0;
            for (Transition t : bigState.getListaTransitionsForaSaindo()) {
                if (novoState.getTransitionsTo(t.getDestiny()).size() == 0) {
                    Transition tNova = mViewer.getComponent().buildTransition(novoState, t.getDestiny())
                            .setValue("view.type", type)
                            .setLabel(t.getLabel() == null || t.getLabel().equals("") ? "" : t.getLabel())
                            .create();
                } else {
                    String labelAntiga = novoState.getTransitionTo(t.getDestiny()).getLabel();
                    novoState.getTransitionTo(t.getDestiny())
                            .setLabel(t.getLabel() == null || t.getLabel().equals("") ? labelAntiga : labelAntiga + ", " + t.getLabel());

                }
            }
            for (Transition t : bigState.getListaTransitionsForaChegando()) {
                if(novoState.getTransitionsTo(t.getSource()).size()!=0)
                    type = 1;
                if (t.getSource().getTransitionsTo(novoState).size() == 0) {
                    Transition tNova = mViewer.getComponent().buildTransition(t.getSource(), novoState)
                            .setValue("view.type", type)
                            .setLabel(t.getLabel() == null || t.getLabel().equals("") ? "" : t.getLabel())
                            .create();
                } else {
                    String labelAntiga = t.getSource().getTransitionTo(novoState).getLabel();
                    t.getSource().getTransitionTo(novoState)
                            .setLabel(t.getLabel() == null || t.getLabel().equals("") ? labelAntiga : labelAntiga + ", " + t.getLabel());
                }
            }

            mBtnBigState.setSelected(true);
            mBtnBigState.setGraphic(iconBigStateDismount);

            statesSelecionados.clear();
            
            BigState.removeStatesComponent();
        }
    };

    public int mTransitionViewType;

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

    public int mModoAtual;
    public final ContextMenu mComponentContextMenu;
    //seleÃ§Ã£o e destaque
    public Object mComponentSobMouse;
    public Object mComponentSelecionado;
    private final List<Listener> mListeners = new ArrayList<>();
    //zoom e mover
    public double mViewerScaleXPadrao;
    public double mViewerScaleYPadrao;
    public double mViewerTranslateXPadrao;
    public double mViewerTranslateYPadrao;
    public double posicaoMViewerHandX = 0;
    public double posicaoMViewerHandY = 0;//posição mviewer
    public double mouseHandX = 0;
    public double mouseHandY = 0;// posiÃ§Ã£o mouse
    public CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);
    public HBox paleta;

    public DesignerWindowImpl(ComponentView viewer) {
        mViewer = viewer;
        mUndoRedo = new ComponentView[14];

        mToolbar = new ToolBar();
        mToggleGroup = new ToggleGroup();
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent e) -> {
            // ComponentViewImpl v = new ComponentViewImpl();
            // viewer.reajuste();
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
            mTransitionViewType = TransitionView.Geometry.LINE;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);

        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent e) -> {
            mTransitionViewType = TransitionView.Geometry.CURVE;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);

        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent e) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);

        mBtnHand = new ToggleButton();
        mBtnHand.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_hand.png"))));
        mBtnHand.setToggleGroup(mToggleGroup);
        mBtnHand.setOnAction((ActionEvent e) -> {
            setModo(MODO_MOVER);
        });

        mBtnBigState = new ToggleButton();
        mBtnBigState.setGraphic(iconBigState);
        mBtnBigState.setOnAction(mCreateDismountBigState);

        mBtnZoom = new MenuButton();
        HBox menuSlideZoom = new HBox();
        menuSlideZoom.setSpacing(5);
        Slider zoomSlide = new Slider(0.5, 2, 1);
        zoomSlide.setShowTickMarks(true);
        ImageView zoomMoree = new ImageView(new Image(getClass().getResourceAsStream("/images/ic_zoom_mais.png")));
        ImageView zoomLess = new ImageView(new Image(getClass().getResourceAsStream("/images/ic_zoom_menos.png")));
        zoomReset = new CheckBox("Reset");
        menuSlideZoom.getChildren().addAll(zoomLess, zoomSlide, zoomMoree, zoomReset);
        mBtnZoom.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_zoom.png"))));
        MenuItem zoomHBox = new MenuItem();
        zoomHBox.setGraphic(menuSlideZoom);
        zoomFactor.bind(zoomSlide.valueProperty());
        zoomFactor.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            escala.setX(newValue.doubleValue());
            escala.setY(newValue.doubleValue());
            requestLayout();
            if (zoomFactor.getValue() != 1)
                zoomReset.setSelected(false);
        });
        mBtnZoom.getItems().add(zoomHBox);

        mBtnUndo = new Button();
        mBtnUndo.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_undo.png"))));
        mBtnUndo.setOnAction((ActionEvent event) -> {
            //historicoViewer("Desfazer");
        });

        mBtnRedo = new Button();
        mBtnRedo.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_redo.png"))));
        mBtnRedo.setOnAction((ActionEvent event) -> {
            //historicoViewer("Refazer");
        });

        //Set Colors-----------------------------------------------------------------------------------
        ColorPicker cores = new ColorPicker();
        MenuButton complementoColors = new MenuButton("");
        cores.setOnAction((ActionEvent event) -> {
            if(statesSelecionados.isEmpty()){
                System.out.println("é nulo o statesSelecionados");
                changeColorsState(cores, "");
            }else{
                changeColorsState(cores, "MultiSelecao");
            }
        });
        MenuItem defaultColor = new MenuItem("Default Color");
        defaultColor.setOnAction((ActionEvent event) -> {
            changeColorsState(cores, "Default");
        });
        complementoColors.getItems().add(defaultColor);
        paleta = new HBox();
        paleta.setAlignment(Pos.CENTER);
        paleta.getChildren().addAll(cores,complementoColors);
        paleta.setVisible(false);


        txtLabel = new TextField();
        txtLabel.setPromptText("Action");
        txtLabel.setOnKeyReleased(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                ((TransitionView) obj).getTransition().setLabel(txtLabel.getText());
            }
        });
        txtGuard = new TextField();
        txtGuard.setPromptText("Guard");
        // txtGuard.setOnAction(event -> {
        txtGuard.setOnKeyReleased(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                ((TransitionView) obj).getTransition().setGuard(txtGuard.getText());
            }
        });
        txtProbability = new TextField();
        txtProbability.setPrefWidth(50);
        txtProbability.setAlignment(Pos.CENTER);
        TransitionsPropertiesController.campoProbability(txtProbability);
        txtProbability.setPromptText("%");
        txtProbability.setOnAction(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                try {
                    if(txtProbability.getText().equals("")){
                        ((TransitionView) obj).getTransition().setProbability(null);
                    }else{
                        String valorDoField = txtProbability.getText().trim();
                        String auxValor = "";
                        if(valorDoField.contains(",")){
                            auxValor = valorDoField.replaceAll(",", ".");
                            double teste = Double.parseDouble(auxValor);
                            if(teste<0 || teste >1){
                                JOptionPane.showMessageDialog(null, "Input probability between 0 and 1", "Erro", JOptionPane.ERROR_MESSAGE);
                                auxValor="";
                                txtProbability.setText("");
                            }
                        }
                        else if(valorDoField.contains(".")){
                            auxValor = valorDoField;
                            double teste = Double.parseDouble(auxValor);
                            if(teste<0 || teste >1){
                                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro", JOptionPane.ERROR_MESSAGE);
                                auxValor="";
                                txtProbability.setText("");
                            }
                        }
                        else if(valorDoField.contains("%")){
                            double valorEntre0e1;
                            auxValor = valorDoField.replaceAll("%", "");
                            valorEntre0e1 = (Double.parseDouble(auxValor))/100;
                            auxValor = String.valueOf(valorEntre0e1);
                            double teste = Double.parseDouble(auxValor);
                            if(teste<0 || teste >1){
                                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro", JOptionPane.ERROR_MESSAGE);
                                auxValor="";
                                txtProbability.setText("");
                            }
                        }
                        else{
                            if(valorDoField.equals("0") || valorDoField.equals("1")){
                                auxValor = valorDoField;
                            }else{
                                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        ((TransitionView) obj).getTransition().setProbability(Double.parseDouble(auxValor));
                    }
                } catch (Exception e) {
                    //ignora
                }
            }
        });

        addListener(v -> {
            Object obj = v.getSelectedView();
            if (obj instanceof TransitionView) {
                Transition t = ((TransitionView) obj).getTransition();
                txtGuard.setText(t.getGuard());
                txtLabel.setText(t.getLabel());
                txtProbability.setText(t.getProbability() == null ? "" : t.getProbability().toString());
            }
        });

        //ToolTips
        Tooltip arrowInfo = new Tooltip("Selection");
        Tooltip stateInfo = new Tooltip("State");
        Tooltip lineTransitionInfo = new Tooltip("Straight Transition");
        Tooltip arcTransitionInfo = new Tooltip("Curved Transition");
        Tooltip eraserInfo = new Tooltip("Eraser");
        Tooltip handInfo = new Tooltip("Move");
        Tooltip zoomInfo = new Tooltip("Ctrl + MouseScroll ↑\nCtrl + MouseScroll ↓\nCtrl + Mouse Button Middle");
        Tooltip bigStateInfo = new Tooltip("Composed State");
        Tooltip undoInfo = new Tooltip("Undo (CTRL+Z");
        Tooltip redoInfo = new Tooltip("Redo (CTRL+Y");
        Tooltip.install(mBtnArrow, arrowInfo);
        Tooltip.install(mBtnState, stateInfo);
        Tooltip.install(mBtnTransitionLine, lineTransitionInfo);
        Tooltip.install(mBtnTransitionArc, arcTransitionInfo);
        Tooltip.install(mBtnEraser, eraserInfo);
        Tooltip.install(mBtnHand, handInfo);
        Tooltip.install(mBtnZoom, zoomInfo);
        Tooltip.install(mBtnBigState, bigStateInfo);
        Tooltip.install(mBtnUndo, undoInfo);
        Tooltip.install(mBtnRedo, redoInfo);

        mToolbar.getItems().addAll(mBtnArrow, mBtnState, mBtnTransitionLine, mBtnTransitionArc, mBtnEraser, mBtnHand, mBtnZoom, mBtnBigState,
                new Separator(Orientation.VERTICAL),paleta);//mBtnUndo, mBtnRedo); //, new Separator(), txtGuard, txtProbability, txtLabel);

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

        mViewer.getNode().getTransforms().add(escala);

        mViewer.addListener(mViewerListener);
        mViewer.setStateContextMenu(mComponentContextMenu);
        mViewer.getNode().setOnMouseClicked(aoClicarMouse);
        mViewer.getNode().setOnMouseMoved(aoMoverMouse);


        mViewer.getNode().setOnDragDetected(aoDetectarDragSobreVertice);
        mViewer.getNode().setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        mViewer.getNode().setOnDragDropped(aoSoltarMouseSobreVertice);

        mViewer.getNode().setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        mViewer.getNode().setOnMouseDragged(aoArrastarVerticeComOMouse);
        mViewer.getNode().setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);

        mViewer.getNode().addEventHandler(KeyEvent.ANY, teclaPressionada);

        //////////////fiz isso/////
        mViewer.tamalhoPadrao();
        mScrollPanel = new ScrollPane(mViewer.getNode());
        AnchorPane.setTopAnchor(mScrollPanel, 44D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 175D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);
        getChildren().add(mScrollPanel);

        //Propriedades
        VBox mPainelPropriedades = new VBox();
        mPainelPropriedades.getChildren().add(new Label("Action"));
        mPainelPropriedades.getChildren().add(txtLabel);
        mPainelPropriedades.getChildren().add(new Label("Guard"));
        mPainelPropriedades.getChildren().add(txtGuard);

        HBox mPainelPropriedadeProbability = new HBox(2);
        mPainelPropriedadeProbability.setAlignment(Pos.CENTER);
        mPainelPropriedades.getChildren().add(new Label("Probability"));
        mPainelPropriedadeProbability.getChildren().add(txtProbability);
        Label exemplo = new Label("Ex: 0,5 OR 0.5 OR 50%\nPress Enter to validate");
        exemplo.setFont(new Font(10));
        mPainelPropriedadeProbability.getChildren().add(exemplo);

        mPainelPropriedades.getChildren().add(mPainelPropriedadeProbability);
        mPainelPropriedades.setPadding(new Insets(5));
        mPainelPropriedades.setSpacing(5);
        AnchorPane.setTopAnchor(mPainelPropriedades, 44D);
        AnchorPane.setRightAnchor(mPainelPropriedades, 0D);
        AnchorPane.setBottomAnchor(mPainelPropriedades, 0D);
        //AnchorPane.setLeftAnchor(mPainelPropriedades, 0D);
        getChildren().add(mPainelPropriedades);

//       mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
//       mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());


        mViewerScaleXPadrao = mViewer.getNode().getScaleX();
        mViewerScaleYPadrao = mViewer.getNode().getScaleY();
        mViewerTranslateXPadrao = mViewer.getNode().getTranslateX();
        mViewerTranslateYPadrao = mViewer.getNode().getTranslateY();

        MenuItem mSetAsInitialMenuItem = new MenuItem("Set as initial");
        mSetAsInitialMenuItem.setOnAction(mSetStateAsInitial);
        MenuItem mSetAsNormalMenuItem = new MenuItem("Set as normal");
        mSetAsNormalMenuItem.setOnAction(mSetStateAsNormal);
        MenuItem mSetAsFinalMenuItem = new MenuItem("Set as error");
        mSetAsFinalMenuItem.setOnAction(mSetStateAsError);
        MenuItem mSetAsErrorMenuItem = new MenuItem("Set as final");
        mSetAsErrorMenuItem.setOnAction(mSetStateAsFinal);

        MenuItem mCreateDismountBigStateMenuItem = new MenuItem("Create/Dismount Composed State");
        mCreateDismountBigStateMenuItem.setOnAction(mCreateDismountBigState);

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
        
        mViewer.getNode().setOnScroll(zoom);
        
        mComponentContextMenu.getItems().addAll(mSetAsInitialMenuItem, new SeparatorMenuItem(), mCreateDismountBigStateMenuItem, new SeparatorMenuItem(), mSetAsNormalMenuItem, mSetAsFinalMenuItem, mSetAsErrorMenuItem, new SeparatorMenuItem(), mSaveAsPNG);

        //Resetando Zoom
        zoomReset.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (zoomReset.isSelected()) {
                zoomSlide.setValue(1);
                mViewer.getNode().setScaleX(mViewerScaleXPadrao);
                mViewer.getNode().setScaleY(mViewerScaleYPadrao);
                mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
                mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
            }
        });

    }

    ////////////////////////////////////////////////////////////////////////////
    // Ao Clicar o mouse(clickar e soltar)
    ////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Context context = new Context(new OnClickedMouse());
            context.executeStrategyOnClikedMouse((DesignerWindowImpl) getNode(),e);
       
        }
    };

    ////////////////////////////////////////////////////////////////////////////////
// Mover o mouse(mover o cursor do mouse)
////////////////////////////////////////////////////////////////////////////
    
    private EventHandler<? super MouseEvent> aoMoverMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            Context context = new Context(new OnMovedMouse());
            context.executeStrategyOnMovedMouse((DesignerWindowImpl) getNode(),t);
           
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Mover state (clickar sem soltar)
    ////////////////////////////////////////////////////////////////////////////
    public boolean verificacao = false, auxA = false;
    public double variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice;
    public double ultimoInstanteX;
    public double variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice;
    public double ultimoInstanteY;
    public boolean downShift;
    public boolean selecionadoUm;
    public boolean selecioneiComShift;
    public boolean selecaoPadrao;
    public ArrayList<State> statesSelecionados = new ArrayList<State>();

    private EventHandler<? super MouseEvent> aoIniciarArrastoVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            
            Context context = new Context(new OnPressedMouse());
            context.executeStrategyOnPressedMouse((DesignerWindowImpl) getNode(), e);

        }
    };


    public double variacaoX, variacaoY;
    public boolean segundaVezEmDiante;
    public double largura = 0, altura = 0;
    public double inicioDoRectanguloX, inicioDoRectanguloY, inicioDoRectanguloXAux, inicioDoRectanguloYAux;
    //////////////////////////////////////////////////////////////////////////////////////////
    // clickar e nÃƒÂ£o soltar e mover o mouse(precionando e movendo/dragg)
    //////////////////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            
            Context context = new Context(new OnDraggedMouse());
            context.executeStrategyOnDraggedMouse((DesignerWindowImpl)getNode(), t);
        }
    };
    ////////////////////////////////////////////////////////////////////
    //Soltar
    ///////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoLiberarVerticeArrastadoComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            Context context = new Context(new OnReleasedMouse());
            context.executeStrategyOnReleasedMouse((DesignerWindowImpl) getNode(), t);
        }
    };
    public ArrayList<State> todosOsStates;

    ////////////////////////////////////////////////////////////////////////////
    // Adicionar transiÃ§Ã£o
    ////////////////////////////////////////////////////////////////////////////
    public StateView mVerticeOrigemParaAdicionarTransicao;
    public StateView mVerticeDestinoParaAdicionarTransicao;
    public Line ultimaLinha;
    public Circle ultimoCircle;
    public double xInicial,yInicial;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            
            Context context = new Context(new OnDragDetectedMouse());
            context.executeStrategyOnDragDetectedMouse((DesignerWindowImpl)getNode(), t);
        }
    };
    
    private EventHandler<DragEvent> aoDetectarPossivelAlvoParaSoltarODrag = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            
            Context context = new Context(new OnDragOverMouse());
            context.executeStrategyOnDragOverMouse((DesignerWindowImpl)getNode(), event);
            
        }
    };

    private final EventHandler<DragEvent> aoSoltarMouseSobreVertice = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            
            Context context = new Context(new OnDragDropped());
            context.executeStrategyOnDragDroppedMouse((DesignerWindowImpl)getNode(), event);
        }

    };
///////////////////////////////////////////////////////////////////////////////
//                       TECLAS PRECIONADAS                             //////
/////////////////////////////////////////////////////////////////////////////    

    private EventHandler<KeyEvent> teclaPressionada = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {
            
            Context context = new Context(new OnKeyPressed());
            context.executeStrategyOnKeyPressed((DesignerWindowImpl)getNode(), event);
            
        }
    };
    ///////////////////////////////////////////////////////////////////////////////
//                             ZOOM                                     //////
/////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super ScrollEvent> zoom = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            
            Context context = new Context(new OnScrollMouse());
            context.executeStrategyOnScrollMouse((DesignerWindowImpl)getNode(), event);
        }
    };

    private void changeColorsState(ColorPicker cores, String tipo){
        if(statesSelecionados==null){
            return;
        }
        String hexCor = "";
        if(cores.getValue().toString().equals("0x000000ff")){
            hexCor = "black";
        }else{
            hexCor = "#"+ Integer.toHexString(cores.getValue().hashCode()).substring(0, 6).toUpperCase();
        }
        for(State s : statesSelecionados){
            if(s.isInitial() || s.isFinal() || s.isError()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "-Initial\n-Final\n-Error", ButtonType.OK);
                alert.setHeaderText("Impossible to change color of States:");
                alert.show();
                return;
            }
            if(tipo.equals("Default")){
                s.setColor(null);
            }
            if(tipo.equals("MultiSelecao")){
                s.setColor(hexCor);
            }
        }
    }

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
        mViewer.getNode().setCursor(Cursor.DEFAULT);
        mStateToolbar.setVisible(mModoAtual == MODO_VERTICE);
        mTransitionToolbar.setVisible(mModoAtual == MODO_TRANSICAO);
        if (mModoAtual == MODO_MOVER) {
            mViewer.getNode().setCursor(Cursor.OPEN_HAND);
        }
    }

    public Object getComponentePelaPosicaoMouse(Point2D point) {
        Object v = mViewer.locateStateView(point);
        if (v == null) {
            v = mViewer.locateTransitionView(point);
        }
        return v;
    }

    public void setComponenteSelecionado(Object t) {
        if (mComponentSelecionado != null) {
            removeSelectedStyles(mComponentSelecionado);
        }
        mComponentSelecionado = t;
        if (t != null) {
            updatePropriedades(t);
            applySelectedStyles(mComponentSelecionado);
        }
    }

    private void updatePropriedades(Object t) {
        if (t instanceof TransitionView) {
            Transition tt = ((TransitionView) t).getTransition();
            txtGuard.setText(tt.getGuard());
            txtProbability.setText(tt.getProbability() == null ? null : String.valueOf(tt.getProbability()));
            txtLabel.setText(tt.getLabel());
            txtLabel.requestFocus();
        }
    }

    public Object getSelectedView() {
        return mComponentSelecionado;
    }

    private void applySelectedStyles(Object v) {
        //System.out.println("applyselectedstyles " + v);
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

    public void removeSelectedStyles(Object v) {
        // System.out.println("removeselectedstyles " + v);
        if (v instanceof StateView) {
            State s = ((StateView) v).getState();
            if (s == null){
                return;
            }
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

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    public void updateContID() {
        int aux = -1;
        for (State s : mViewer.getComponent().getStates()) {
            if (s.getID() > aux) {
                aux = s.getID();
            }
        }
        contID = aux;
        contID++;
    }

    //Historico para Desfazer e Refazer
    /*private void historicoViewer(String opcao) {
        switch(opcao){
            case "Desfazer":{
                if(contPosHistoricoCheia>0){
                    mScrollPanel.getChildrenUnmodifiable().clear();
                    mScrollPanel.getChildrenUnmodifiable().add(mUndoRedo[contPosHistoricoCheia-1].getNode());
                }
            }break;

            case "Refazer":{

            }break;
        }
    }
    private int contPosHistoricoCheia = 0;
    private void addHistorico(ComponentView viewer){
        if(contPosHistoricoCheia <= tamHistorico){
            mUndoRedo[contPosHistoricoCheia] = viewer;
            contPosHistoricoCheia+=1;
        }else{
            for(int i=1;i<=tamHistorico;i++){
                mUndoRedo[i-1] = mUndoRedo[i];
            }
            contPosHistoricoCheia-=1;
            mUndoRedo[contPosHistoricoCheia] = viewer;
            contPosHistoricoCheia+=1;
        }
    }*/

}