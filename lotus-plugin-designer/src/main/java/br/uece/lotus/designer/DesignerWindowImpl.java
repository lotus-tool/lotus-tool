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
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

/**
 * @author emerson
 */
public class DesignerWindowImpl extends AnchorPane implements DesignerWindow {

    private final TextField txtLabel;
    private final TextField txtGuard;
    private final TextField txtProbability;
    Rectangle ultimoRetanguloAdicionado;
    double coordenadaInicialX, coordenadaInicialY, coordenadaFinalX, coordenadaFinalY,
            coornenadaIstanteX, coordenadaIstanteY;
    boolean shifPrecionado = false;
    boolean StatesSelecionadoPeloRetangulo = false;
    boolean ctrlPressionado = false;
    double posicaoDoEstadoXMaisRaio;
    double posicaoDoEstadoYMaisRaio;
    double posicaoDoEstadoXMenosRaio;
    double posicaoDoEstadoYMenosRaio;
    ArrayList<State> stateDentroDoRetangulo = new ArrayList<State>();
    double posCircleX;
    double posCircleY;
    double variacaoDeX;
    double variacaoDeY;
    double stateDoPrimeiroClickX, stateDoPrimeiroClickY;
    boolean caso1, caso2, caso3, caso4, retorno, aux, modoCriacaoDoRetangulo = false;
    static final int RAIO_CIRCULO = 15;

    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    public static final int MODO_MOVER = 4;
    private int contID = 0;
    private final ComponentView mViewer;
    private final ToolBar mToolbar;
    private final ToggleGroup mToggleGroup;
    private final Button mBtnBigState;
    private final ToggleButton mBtnArrow;
    private final ToggleButton mBtnState;
    private final ToggleButton mBtnTransitionLine;
    private final ToggleButton mBtnTransitionArc;
    private final ToggleButton mBtnEraser;
    private final ToggleButton mBtnHand;
    private final MenuButton mBtnZoom;

    private final ToolBar mStateToolbar;
    private final ToolBar mTransitionToolbar;
    private final ExtensibleToolbar mExtensibleStateToolbar;
    private final ExtensibleToolbar mExtensibleTransitionToolbar;

    private final ScrollPane mScrollPanel;
    private boolean mExibirPropriedadesTransicao;

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
    private EventHandler<ActionEvent> mSetColor = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setColor((String) ((MenuItem) event.getSource()).getUserData());
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
    //seleÃ§Ã£o e destaque
    private Object mComponentSobMouse;
    private Object mComponentSelecionado;
    private final List<Listener> mListeners = new ArrayList<>();
    private double mViewerScaleXPadrao, mViewerScaleYPadrao, mViewerTranslateXPadrao, mViewerTranslateYPadrao;
    private double posicaoMViewerHandX = 0, posicaoMViewerHandY = 0;//posição mviewer
    private double mouseHandX = 0, mouseHandY = 0;// posiÃ§Ã£o mouse
    private CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);

    public DesignerWindowImpl(ComponentView viewer) {
        mViewer = viewer;

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

        mBtnBigState = new Button();
        mBtnBigState.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_big_state.png"))));
        mBtnBigState.setOnAction(event -> {

            //CRIANDO BIGSTATE - USERDATA            
            BigState bigState = new BigState();
            List<State> listaS = statesSelecionados;

            if (!bigState.addStatesAndTransition(listaS)){
                JOptionPane.showMessageDialog(null, "Add more States or initial State selected","Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //CRIANDO NA TELA O STATE MAIOR COM O BIGSTATE
            int id = mViewer.getComponent().getStatesCount();
            State novoState = mViewer.getComponent().newState(id);
            novoState.setValue("bigstate", bigState);
            novoState.setLayoutX(100);
            novoState.setLayoutY(100);
            novoState.setLabel(String.valueOf(id));
            novoState.setBig(true);
            bigState.setState(novoState);

            if (contID == 0) {
                updateContID();
                novoState.setID(contID);
            } else
                novoState.setID(contID);
            contID++;            

            //ADD TRANSITIONS DOS BIGSTATES
            for (Transition t : bigState.getListaTransitionsForaSaindo()) {
                if (novoState.getTransitionsTo(t.getDestiny()).size() == 0) {
                    Transition tNova = mViewer.getComponent().buildTransition(novoState, t.getDestiny()).setValue("view.type", 0).setLabel(t.getLabel() == null || t.getLabel().equals("") ? "" : t.getLabel()).create();
                } else {
                    String labelAntiga = novoState.getTransitionTo(t.getDestiny()).getLabel();
                    novoState.getTransitionTo(t.getDestiny()).setLabel(t.getLabel() == null || t.getLabel().equals("") ? labelAntiga : labelAntiga + ", " + t.getLabel());
                }
            }
            for (Transition t : bigState.getListaTransitionsForaChegando()) {
                if (t.getSource().getTransitionsTo(novoState).size() == 0) {
                    Transition tNova = mViewer.getComponent().buildTransition(t.getSource(), novoState).setValue("view.type", 0).setLabel(t.getLabel() == null || t.getLabel().equals("") ? "" : t.getLabel()).create();
                } else {
                    String labelAntiga = t.getSource().getTransitionTo(novoState).getLabel();
                    t.getSource().getTransitionTo(novoState).setLabel(t.getLabel() == null || t.getLabel().equals("") ? labelAntiga : labelAntiga + ", " + t.getLabel());
                }
            }

            BigState.removeStatesComponent();

        });

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

        Tooltip zoomInfo = new Tooltip("Ctrl + MouseScroll ↑\nCtrl + MouseScroll ↓\nCtrl + Mouse Button Middle");
        Tooltip.install(mBtnZoom, zoomInfo);
        mBtnZoom.getItems().add(zoomHBox);

        txtLabel = new TextField();
        txtLabel.setPromptText("action");
        txtLabel.setOnAction(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                ((TransitionView) obj).getTransition().setLabel(txtLabel.getText());
            }
        });
        txtGuard = new TextField();
        txtGuard.setPromptText("guard");
        txtGuard.setOnAction(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                ((TransitionView) obj).getTransition().setGuard(txtGuard.getText());
            }
        });
        txtProbability = new TextField();
        txtProbability.setPromptText("probability");
        txtProbability.setOnAction(event -> {
            Object obj = getSelectedView();
            if (obj instanceof TransitionView) {
                try {
                    ((TransitionView) obj).getTransition().setProbability(Double.parseDouble(txtProbability.getText()));
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

        mToolbar.getItems().addAll(mBtnArrow, mBtnState, mBtnTransitionLine, mBtnTransitionArc, mBtnEraser, mBtnHand, mBtnZoom, mBtnBigState); //, new Separator(), txtGuard, txtProbability, txtLabel);

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
        mPainelPropriedades.getChildren().add(new Label("Probability"));
        mPainelPropriedades.getChildren().add(txtProbability);
        mPainelPropriedades.setPadding(new Insets(5));
        mPainelPropriedades.setSpacing(5);
        AnchorPane.setTopAnchor(mPainelPropriedades, 44D);
        AnchorPane.setRightAnchor(mPainelPropriedades, 0D);
        AnchorPane.setBottomAnchor(mPainelPropriedades, 0D);
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
        mComponentContextMenu.getItems().addAll(mSetAsInitialMenuItem, new SeparatorMenuItem(), mSetAsNormalMenuItem, mSetAsFinalMenuItem, mSetAsErrorMenuItem, new SeparatorMenuItem(), mSaveAsPNG);

        Menu menuColor = new Menu("Colors");
        MenuItem defaultColor = new MenuItem("Default");
        defaultColor.setOnAction(mSetColor);

        MenuItem pinkColor = new MenuItem("Pink");
        pinkColor.setUserData("#FF0066");
        pinkColor.setOnAction(mSetColor);

        MenuItem purpleColor = new MenuItem("Purple");
        purpleColor.setUserData("#660033");
        purpleColor.setOnAction(mSetColor);

        MenuItem grayColor = new MenuItem("Gray");
        grayColor.setUserData("#999966");
        grayColor.setOnAction(mSetColor);

        MenuItem redColor = new MenuItem("Red");
        redColor.setUserData("#FF0000");
        redColor.setOnAction(mSetColor);

        MenuItem yellowColor = new MenuItem("Yellow");
        yellowColor.setUserData("#FFFF00");
        yellowColor.setOnAction(mSetColor);

        MenuItem blueColor = new MenuItem("Blue");
        blueColor.setUserData("#0000ff");
        blueColor.setOnAction(mSetColor);

        MenuItem blackColor = new MenuItem("Black");
        blackColor.setUserData("#000000");
        blackColor.setOnAction(mSetColor);

        MenuItem greenColor = new MenuItem("Green");
        greenColor.setUserData("#00ff00");
        greenColor.setOnAction(mSetColor);

        MenuItem whiteColor = new MenuItem("White");
        whiteColor.setUserData("#FFFFFF");
        whiteColor.setOnAction(mSetColor);
        menuColor.getItems().addAll(defaultColor, new SeparatorMenuItem(), greenColor, blueColor, blackColor, yellowColor, whiteColor, redColor, grayColor, pinkColor, purpleColor);
        mComponentContextMenu.getItems().addAll(new SeparatorMenuItem(), menuColor);

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
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                setComponenteSelecionado(mComponentSobMouse);
                mComponentContextMenu.show(mViewer.getNode(), e.getScreenX(), e.getScreenY());
                return;
            }
            else {
                mComponentContextMenu.hide();
            }

            if (e.isControlDown() && e.getButton() == MouseButton.MIDDLE) {
                mViewer.getNode().setScaleX(mViewerScaleXPadrao);
                mViewer.getNode().setScaleY(mViewerScaleYPadrao);
                mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
                mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
            }

            if (mModoAtual == MODO_NENHUM) {

                if (mComponentSobMouse != null && (mComponentSobMouse instanceof StateView)) {
                    //VERIFICANDO SE TEM UM BIGSTATE
                    if (((BigState)((StateView)mComponentSobMouse).getState().getValue("bigstate")) != null) {
                        System.out.println("NUMERO DE BIGSTATES = "+BigState.todosOsBigStates.size());
                        System.out.println(((BigState)((StateView)mComponentSobMouse).getState().getValue("bigstate")).toString());
                        if (e.getClickCount() == 2) {
                            StateView stateView = (StateView) mComponentSobMouse;
                            State state = stateView.getState();
                            BigState bigState = (BigState) state.getValue("bigstate");
                            if (!bigState.dismountBigState(mViewer.getComponent())){
                                JOptionPane.showMessageDialog(null, "You need another BigState before dismantling");
                                return;
                            }
                            mViewer.getComponent().remove(state);
                        }
                    }
                }

            } else {
                if (mModoAtual == MODO_VERTICE) {
                    if (!(mComponentSobMouse instanceof StateView)) {
                        int id = mViewer.getComponent().getStatesCount();
                        State s = mViewer.getComponent().newState(id);
                        s.setLayoutX(e.getX());
                        s.setLayoutY(e.getY());
                        s.setLabel(String.valueOf(id));

                        if (contID == 0) {
                            updateContID();
                            s.setID(contID);
                        } else
                            s.setID(contID);
                        contID++;

                        if (mViewer.getComponent().getStatesCount() == 0) {
                            mViewer.getComponent().setInitialState(s);
                        }
                    }
                } else if (mModoAtual == MODO_REMOVER) {
                    if (mComponentSobMouse instanceof StateView) {
                        State v = ((StateView) mComponentSobMouse).getState();
                        if(v.getValue("bigstate") instanceof BigState){
                            BigState.removeBigState((BigState) v.getValue("bigstate"));
                        }
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
            Object aux = getComponentePelaPosicaoMouse(new Point2D(t.getSceneX(), t.getSceneY()));
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
    private boolean verificacao = false, auxA = false;
    private double variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice, ultimoInstanteX;
    private double variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice, ultimoInstanteY;
    private boolean downShift, selecionadoUm, selecioneiComShift, selecaoPadrao;
    ArrayList<State> statesSelecionados = new ArrayList<State>();

    private EventHandler<? super MouseEvent> aoIniciarArrastoVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            //                         HAND MOVE                                     //
            if (mModoAtual == MODO_MOVER) {
                mViewer.getNode().setCursor(Cursor.CLOSED_HAND);
                if (e.getClickCount() == 2) {
                    mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
                    mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
                }
                //gravar cordenadas x e y do mViewer de acordo com a posição do mouse
                mouseHandX = e.getSceneX();
                mouseHandY = e.getSceneY();
                //get the x and y position measure from Left-Top
                posicaoMViewerHandX = mViewer.getNode().getTranslateX();
                posicaoMViewerHandY = mViewer.getNode().getTranslateY();
            }

            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
            if (mModoAtual == MODO_NENHUM) {
                coordenadaInicialX = e.getX();
                coordenadaInicialY = e.getY();
                ultimoInstanteX = 0;
                ultimoInstanteY = 0;
                segundaVezEmDiante = false;
                //selecioneiComShift=false;


                if (e.isShiftDown()) {
                    downShift = true;

                }

                if (downShift && mComponentSobMouse != null) {

                    StateView stateView = (StateView) mComponentSobMouse;
                    State state = stateView.getState();

                    for(State s : statesSelecionados){
                        if(s==state){
                            return;
                        }
                    }
                    state.setBorderWidth(2);
                    state.setBorderColor("blue");
                    state.setTextColor("blue");
                    state.setTextSyle(State.TEXTSTYLE_BOLD);
                    statesSelecionados.add(state);
                    selecionadoUm = true;
                    selecioneiComShift = true;

                    modoCriacaoDoRetangulo = false;
                    downShift = false;
                    selecaoPadrao = false;
                    return;
                } else {

                    if (!StatesSelecionadoPeloRetangulo && mComponentSobMouse != null && !selecioneiComShift) {


                        selecaoPadrao = true;
                        if (statesSelecionados != null) {
                            statesSelecionados.clear();
                        }
                        if (mComponentSobMouse instanceof StateView) {
                            setComponenteSelecionado(mComponentSobMouse);
                            StateView stateView = (StateView) mComponentSobMouse;
                            State state = stateView.getState();
                            statesSelecionados.add(state);
                            modoCriacaoDoRetangulo = false;
                            return;
                        } else {
                            setComponenteSelecionado(mComponentSobMouse);
                        }
                    } else {
                        if (!SeClickeiEntreSelecionados(e.getX(), e.getY()) && statesSelecionados != null) {

                            verificacao = true;
                            for (State s : statesSelecionados) {
                                s.setBorderWidth(1);
                                s.setBorderColor("black");
                                s.setTextColor("black");
                                s.setTextSyle(State.TEXTSTYLE_NORMAL);
                            }
                            if (mComponentSobMouse != null) {
                                selecaoPadrao = true;
                                if (statesSelecionados != null) {
                                    statesSelecionados.clear();
                                    modoCriacaoDoRetangulo = false;
                                }

                                StateView stateView = null;
                                setComponenteSelecionado(mComponentSobMouse);
                                try {
                                    stateView = (StateView) mComponentSobMouse;
                                } catch (ClassCastException exception){
                                    return;
                                }
                                State state = stateView.getState();
                                statesSelecionados.add(state);


                            } else {
                                selecaoPadrao = false;
                                modoCriacaoDoRetangulo = true;
                                StatesSelecionadoPeloRetangulo = false;
                                selecioneiComShift = false;
                                if (selecionadoUm) {
                                    selecionadoUm = false;
                                }
                                for (State s : statesSelecionados) {
                                    s.setBorderWidth(1);
                                    s.setBorderColor("black");
                                    s.setTextColor("black");
                                    s.setTextSyle(State.TEXTSTYLE_NORMAL);
                                }

                                statesSelecionados.clear();
                                //System.out.println("Removendo estilo selecionado state/trans");
                                Object v = getSelectedView();
                                if(v instanceof TransitionView){
                                    removeSelectedStyles(v);
                                }

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

                variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getNode().getLayoutX() - e.getX() + RAIO_CIRCULO;
                variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getNode().getLayoutY() - e.getY() + RAIO_CIRCULO;
            }
        }
    };


    private double variacaoX, variacaoY;
    private boolean segundaVezEmDiante;
    private double largura = 0, altura = 0;
    private double inicioDoRectanguloX, inicioDoRectanguloY, inicioDoRectanguloXAux, inicioDoRectanguloYAux;
    //////////////////////////////////////////////////////////////////////////////////////////
    // clickar e nÃ£o soltar e mover o mouse(precionando e movendo/dragg)
    //////////////////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            //                         HAND MOVE                                     //
            if (mModoAtual == MODO_MOVER) {
                //Pegar Moviemnto do mouse
                posicaoMViewerHandX += t.getSceneX() - mouseHandX;
                posicaoMViewerHandY += t.getSceneY() - mouseHandY;
                //setar nova posição apos calculo do movimento
                mViewer.getNode().setTranslateX(posicaoMViewerHandX);
                mViewer.getNode().setTranslateY(posicaoMViewerHandY);
                //setar nova posição do mouse no mViewer
                mouseHandX = t.getSceneX();
                mouseHandY = t.getSceneY();
            }

            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
            if (mModoAtual == MODO_NENHUM) {
                if ((StatesSelecionadoPeloRetangulo || selecionadoUm ) && !selecaoPadrao) {
//                        if(selecioneiComShift){
//                            System.out.println("retornou?");
//                            //selecioneiComShift=false;
//                            return;
//                        }

                    if (!segundaVezEmDiante //&& downShift
                            ) {
                        //System.out.println("primeira vez");
                        variacaoX = t.getX() - coordenadaInicialX;
                        variacaoY = t.getY() - coordenadaInicialY;
                        segundaVezEmDiante = true;
                        ultimoInstanteX = t.getX();
                        ultimoInstanteY = t.getY();

                    } else {
                        //System.out.println("seunda vez");
                        variacaoX = (t.getX() - ultimoInstanteX);
                        variacaoY = (t.getY() - ultimoInstanteY);
                        ultimoInstanteX = t.getX();
                        ultimoInstanteY = t.getY();

                    }

                    for (State s : statesSelecionados) {
                        posicionandoConjuntoDeStates(s, variacaoX, variacaoY);
                    }

                } else {

                    if (modoCriacaoDoRetangulo) {
                        //System.out.println("entra aqui 1");

                        auxA = true;

                        coornenadaIstanteX = t.getX();
                        coordenadaIstanteY = t.getY();


                        if (coornenadaIstanteX <= coordenadaInicialX) {

                            if (coordenadaIstanteY <= coordenadaInicialY) {
                                largura = coordenadaInicialY - coordenadaIstanteY;
                                altura = coordenadaInicialX - coornenadaIstanteX;
                                inicioDoRectanguloX = coornenadaIstanteX;
                                inicioDoRectanguloY = coordenadaIstanteY;
                                inicioDoRectanguloXAux = coordenadaInicialX;
                                inicioDoRectanguloYAux = coordenadaInicialY;
                            }
                            if (coordenadaIstanteY >= coordenadaInicialY) {
                                altura = coordenadaInicialX - coornenadaIstanteX;
                                largura = coordenadaIstanteY - coordenadaInicialY;
                                inicioDoRectanguloX = coordenadaInicialX - altura;
                                inicioDoRectanguloY = coordenadaInicialY;
                                inicioDoRectanguloXAux = coordenadaInicialX;
                                inicioDoRectanguloYAux = coordenadaInicialY;
                            }

                        } else {
                            if (coordenadaIstanteY <= coordenadaInicialY) {
                                altura = coornenadaIstanteX - coordenadaInicialX;
                                largura = coordenadaInicialY - coordenadaIstanteY;
                                inicioDoRectanguloX = coordenadaInicialX;
                                inicioDoRectanguloY = coordenadaInicialY - largura;
                                inicioDoRectanguloXAux = coordenadaInicialX;
                                inicioDoRectanguloYAux = coordenadaInicialY;
                            }
                            if (coordenadaIstanteY >= coordenadaInicialY) {
                                altura = coornenadaIstanteX - coordenadaInicialX;
                                largura = coordenadaIstanteY - coordenadaInicialY;
                                inicioDoRectanguloX = coordenadaInicialX;
                                inicioDoRectanguloY = coordenadaInicialY;
                                inicioDoRectanguloXAux = coordenadaInicialX;
                                inicioDoRectanguloYAux = coordenadaInicialY;
                            }

                        }
                        if (ultimoRetanguloAdicionado != null) {
                            mViewer.getNode().getChildren().remove(ultimoRetanguloAdicionado);
                        }

                        Rectangle retangulo = new Rectangle((int) inicioDoRectanguloX, (int) inicioDoRectanguloY, (int) altura, (int) largura);
                        ultimoRetanguloAdicionado = retangulo;
                        retangulo.setFill(Color.BLUE);
                        retangulo.setOpacity(0.4);
                        retangulo.setVisible(true);
                        mViewer.getNode().getChildren().add(retangulo);

                    } else {

                        if (!(mComponentSobMouse instanceof StateView) || !selecaoPadrao) {
                            return;
                        }
                        //System.out.println("entra aqui 2");

                        State s = ((StateView) mComponentSobMouse).getState();
                        s.setLayoutX(t.getX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice - RAIO_CIRCULO);
                        s.setLayoutY(t.getY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice - RAIO_CIRCULO);
                    }
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

            if (mModoAtual == MODO_MOVER) {
                mViewer.getNode().setCursor(Cursor.OPEN_HAND);
            }

            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
            if (mModoAtual == MODO_NENHUM) {
                variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
                variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
                if (modoCriacaoDoRetangulo) {
                    if (!auxA) {
                        inicioDoRectanguloXAux = coordenadaInicialX;
                        inicioDoRectanguloYAux = coordenadaInicialY;
                    }
                    if (!selecionadoUm) {
                        coordenadaFinalX = t.getX();
                        coordenadaFinalY = t.getY();

                        StatesSelecionadoPeloRetangulo = selecionandoComRetangulo(inicioDoRectanguloXAux, inicioDoRectanguloYAux, coordenadaFinalX, coordenadaFinalY);
                    }
                    if (ultimoRetanguloAdicionado != null) {
                        mViewer.getNode().getChildren().remove(ultimoRetanguloAdicionado);

                    }

                    auxA = false;
                }
            }
        }
    };
    ArrayList<State> todosOsStates;

    public boolean selecionandoComRetangulo(double inicioDoRectanguloX, double inicioDoRectanguloY, double finalDoRectanguloX, double finalDoRectanguloY) {
        boolean aux = false;
        /////////////////////////////////////////////////
        ////////organizando as pesiÃ§Ãµes do retangulo
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


        todosOsStates = (ArrayList<State>) mViewer.getComponent().getStates();
        int n = todosOsStates.size();
        if (stateDentroDoRetangulo != null) {
            for (State s : stateDentroDoRetangulo) {
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

            //verificando se a area do retangulo estÃ¡ pegando atÃ© o centro do state
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
    // Adicionar transiÃ§Ã£o
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

            if (BigState.verifyIsBigState(mVerticeOrigemParaAdicionarTransicao.getState())) {
                JOptionPane.showMessageDialog(null, "Impossible to create transitions in a Big State!", "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //inicia o drag'n'drop
            Dragboard db = mVerticeOrigemParaAdicionarTransicao.getNode().startDragAndDrop(TransferMode.ANY);

            //soh funciona com as trÃªs linhas a seguir. Porque? Eu nÃ£o sei.
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
            //a informaÃ§ao esta sendo solta sobre o alvo
            //aceita soltar o mouse somente se nÃ£o Ã© o mesmo nodo de origem 
            //e possui uma string            
            if (event.getGestureSource() != event.getSource()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            Object v = getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
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
                if (BigState.verifyIsBigState(mVerticeDestinoParaAdicionarTransicao.getState())) {
                    JOptionPane.showMessageDialog(null, "Impossible to create transitions for a BigState!", "Alert", JOptionPane.WARNING_MESSAGE);
                    return;
                }

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
    ///////////////////////////////////////////////////////////////////////////////
//                             ZOOM                                     //////
/////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super ScrollEvent> zoom = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            if (event.isControlDown()) {
                zoomReset.setSelected(false);
                final double SCALE_DELTA = 1.1;
                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
                zoom(mViewer.getNode(), event.getX(), event.getY(), scaleFactor);
            }
        }
    };

    private void zoom(Node node, double centerX, double centerY, double factor) {
        final Point2D center = node.localToParent(centerX, centerY);
        final Bounds bounds = node.getBoundsInParent();
        final double w = bounds.getWidth();
        final double h = bounds.getHeight();

        final double dw = w * (factor - 1);
        final double xr = 2 * (w / 2 - (center.getX() - bounds.getMinX())) / w;

        final double dh = h * (factor - 1);
        final double yr = 2 * (h / 2 - (center.getY() - bounds.getMinY())) / h;

        node.setScaleX(node.getScaleX() * factor);
        node.setScaleY(node.getScaleY() * factor);
        node.setTranslateX(node.getTranslateX() + xr * dw / 2);
        node.setTranslateY(node.getTranslateY() + yr * dh / 2);
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

    private Object getComponentePelaPosicaoMouse(Point2D point) {
        Object v = mViewer.locateStateView(point);
        if (v == null) {
            v = mViewer.locateTransitionView(point);
        }
        return v;
    }

    private void setComponenteSelecionado(Object t) {
        if (mComponentSelecionado != null) {
            removeSelectedStyles(mComponentSelecionado);
        }
        mComponentSelecionado = t;
        if (t != null) {
            updatePropriedades(t);
            applySelectedStyles(mComponentSelecionado);
        }
        //System.out.println("chegou aqui ");

//        for (Listener l : mListeners) {
//            l.onSelectionChange(this);
//        }
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

    private void removeSelectedStyles(Object v) {
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

    double posXAntes;
    double posYAntes;

    public void posicionandoConjuntoDeStates(State v, double mX, double mY) {
        double posX = v.getLayoutX();
        double posY = v.getLayoutY();
        posXAntes = posX;
        posYAntes = posY;
        double distanciaClick;
        double distanciaState;
        double deltaX, deltaY;
//        mX = mX - RAIO_CIRCULO;
//        mY = mY - RAIO_CIRCULO;
        //System.out.println("varuacaoX-raio" + mX);
        //System.out.println("varuacaoY-raio" + mY);
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
        //System.out.println(v.getLayoutX());
        //System.out.println(v.getLayoutY());
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    private void updateContID() {
        int aux = 0;
        for (State s : mViewer.getComponent().getStates()) {
            if (s.getID() > aux) {
                aux = s.getID();
            }
        }
        contID = aux;
        contID++;
    }

}