/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockViewImpl;
import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeView;
import br.uece.lotus.msc.app.ParallelComponentController;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
import br.uece.lotus.msc.app.runtime.controller.PropertysPanelController;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_component.HmscComponentView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_component.HmscComponentViewImpl;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.api.window.WindowMSC;
import br.uece.lotus.msc.designer.hmscComponent.strategy.Context;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnClickedMouse;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnDragDetected;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnDragDropped;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnDragOver;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnDraggedMouse;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnMovedMouse;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnPressedMouse;
import br.uece.lotus.msc.designer.hmscComponent.strategy.OnReleasedMouse;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import br.uece.lotus.viewer.TransitionView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javax.swing.*;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscWindowMSCImpl extends AnchorPane implements WindowMSC {



    private Popup popup = new Popup();
    //Area de Projeto
    public ProjectExplorerPluginMSC projectExplorerPluginMSC;
    //Principais da Tela
    private final ScrollPane mScrollPanel;
    private final AnchorPane mPropriedadePanel;
    private final HBox mInfoPanel;
    public final ToolBar mToolBar;
    public HmscComponentView viewer;
    //Botoes
    private ToggleGroup mToggleGroup;
    private ToggleButton mBtnArrow;
    private ToggleButton mBtnBlock;
    private ToggleButton interceptionNodeButton;
    private ToggleButton mBtnTransitionLine;
    private ToggleButton mBtnTransitionArc;
    private ToggleButton mBtnEraser;
    private ToggleButton mBtnHand;
    private MenuButton mBtnZoom;

    //Controles de acoes
    public final int ANY_MODE = 0;
    public final int HMSC_BLOCK_MODE = 1;
    public final int INTERCEPTION_NODE_MODE = 2;
    public final int TRANSITION_MODE = 3;
    public final int REMOVE_MODE = 4;
    public final int MOVE_MODE = 5;

//    public int countHmscBlock = 0;
//    public int countIntercptionNode = 0;
//    public int countGenericElement = 0;


    public int mTransitionViewType;
    public int mModoAtual;
    public ContextMenu mContextMenuBlockBuild;
    //Mover e Zoom
    private CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);
    public double mViewerScaleXPadrao, mViewerScaleYPadrao;
    public double mViewerTranslateXPadrao, mViewerTranslateYPadrao;
    //Selecao e Destaque
    public Object componentSobMouse;
    public Object componentSelecionado;
    public Circle mBounds = new Circle(8);
    //Cores
    public HBox paleta;
    //Transicao
    public GenericElementView initialGenericElementView;
    public GenericElementView finalGenericElementView;
    public Line fakeLine;
    public double xInicial;
    public double yInicial;

    //Painel de Propriedades
    public TextField txtLabel;
    public TextField txtAction;
    private TextField txtGuard;
    public TextField txtProbability;

    private Label lblLabel;
    private Label lblAction;
    private Label lblGuard;
    private Label lblProbability;

    private Button btnAddProperty;
    private TitledPane propertyDropDown;
    private CheckBox propertyPanelCheckBox;
    private TableView<Entry> tableViewProperty;
    private TableColumn<Entry, String> mSourceCol;
    private TableColumn<Entry, String> mTargetCol;
    private TableColumn<Entry, String> mInequationCol;
    private TableColumn<Entry, String> mConditionCol;
    private TableColumn<Entry, String> mExcSttCol;
    private TableColumn<Entry, String> mResultCol;
    private ObservableList<Entry> mEntries = FXCollections.observableArrayList();


    //Variaveis gerais
    public Set<Node> selecao = new HashSet<>();
    public double dragContextMouseAnchorX, dragContextMouseAnchorY;
    public Rectangle rectSelecao;
    public boolean segundaVezAoArrastar;
    public double ultimoInstanteX, ultimoInstanteY;
    private Component parallelComponet;
    private MenuItem setAsDecisionNode;

    /////////////////////////////////////////////////////////////////////////
    //                   IMPLEMENTACAO DA WINDOW_DS                        //
    /////////////////////////////////////////////////////////////////////////
    @Override
    public HmscComponent getHmscComponent() {
        return viewer.getHmscComponent();
    }

    @Override
    public BmscComponent getBmscComponent() {return null;}

    @Override
    public Component getComponentLTS() {return parallelComponet;}

    @Override
    public void setHmscComponent(HmscComponent hmscComponent) {
        viewer.setComponentBuildDS(hmscComponent);
    }

    @Override
    public void setBmscComponent(BmscComponent bmscComponent) {}

    @Override
    public void setComponentLTS(Component componentLTS) {
         parallelComponet = componentLTS;
    }

    @Override
    public String getTitle() {
        HmscComponent c = viewer.getHmscComponent();
        return c.getName();
    }

    @Override
    public Node getNode() {
        return this;
    }
    
    
    public HmscWindowMSCImpl(ProjectExplorerPluginMSC projectExplorerPluginMSC) {
        this.projectExplorerPluginMSC = projectExplorerPluginMSC;
        viewer = new HmscComponentViewImpl();
        mToolBar = new ToolBar();
        mScrollPanel = new ScrollPane((Node) viewer);
        mPropriedadePanel = new AnchorPane();
        mInfoPanel = new HBox(20);
        viewer.getNode().setPrefSize(1200, 400);
        mScrollPanel.viewportBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
            Node content = mScrollPanel.getContent();
            mScrollPanel.setFitToWidth(content.prefWidth(-1)<newValue.getWidth());
            mScrollPanel.setFitToHeight(content.prefHeight(-1)<newValue.getHeight());
        });


        //Posicoes dos elementos
        //toolbar
        AnchorPane.setTopAnchor(mToolBar, 0D);
        AnchorPane.setLeftAnchor(mToolBar, 0D);
        AnchorPane.setRightAnchor(mToolBar, 0D);
        getChildren().add(mToolBar);
       
        //scrollPane
        getChildren().add(mScrollPanel);
        AnchorPane.setTopAnchor(mScrollPanel, 44D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 180D/*0D*/);
        AnchorPane.setBottomAnchor(mScrollPanel, 70D);
        
        //propriedades
        getChildren().add(mPropriedadePanel);
        AnchorPane.setTopAnchor(mPropriedadePanel, 44D);
        AnchorPane.setRightAnchor(mPropriedadePanel, 3D);
        AnchorPane.setBottomAnchor(mPropriedadePanel, 30D);

        //info
        getChildren().add(mInfoPanel);
        AnchorPane.setLeftAnchor(mInfoPanel, 0D);
        AnchorPane.setRightAnchor(mInfoPanel, 0D);
        AnchorPane.setBottomAnchor(mInfoPanel, 43D);



        startComponentesTela();

      //  setAsDecisionNode.fire();
        
    }

    private void startComponentesTela() {
        ////////////////////////////////////////////////////////////////////
        // INICIANDO COMPONENTES DA INTERFACE
        ////////////////////////////////////////////////////////////////////
        mContextMenuBlockBuild = new ContextMenu();
        mToggleGroup = new ToggleGroup();
        
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent event) -> {
            setModo(ANY_MODE);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnArrow.setSelected(true);
        
        mBtnBlock = new ToggleButton();
        mBtnBlock.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_hMSC.png"))));
        mBtnBlock.setOnAction((ActionEvent event) -> {
            setModo(HMSC_BLOCK_MODE);
        });
        mBtnBlock.setToggleGroup(mToggleGroup);

        interceptionNodeButton = new ToggleButton();
        interceptionNodeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_interception_node.png"))));
        interceptionNodeButton.setOnAction((ActionEvent event) -> {
            setModo(INTERCEPTION_NODE_MODE);
        });

        interceptionNodeButton.setToggleGroup(mToggleGroup);

        
        mBtnTransitionLine = new ToggleButton();
        mBtnTransitionLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_line.png"))));
        mBtnTransitionLine.setOnAction((ActionEvent event) -> {
            setModo(TRANSITION_MODE);
            mTransitionViewType = TransitionMSCView.Geometry.LINE;
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);
        
        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent event) -> {
            setModo(TRANSITION_MODE);
            mTransitionViewType = TransitionMSCView.Geometry.CURVE;
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);
        
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent event) -> {
            setModo(REMOVE_MODE);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);
        
        mBtnHand = new ToggleButton();
        mBtnHand.setDisable(true);
        mBtnHand.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_hand.png"))));
        mBtnHand.setOnAction((ActionEvent event) -> {
            setModo(MOVE_MODE);
        });
        mBtnHand.setToggleGroup(mToggleGroup);
        
        mBtnZoom = new MenuButton();
        HBox menuSlideZoom = new HBox();
        menuSlideZoom.setSpacing(5);
        Slider zoomSlide = new Slider(0.5, 2, 1);
        zoomSlide.setShowTickMarks(true);
        ImageView zoomMoree = new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_zoom_mais.png")));
        ImageView zoomLess = new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_zoom_menos.png")));
        zoomReset = new CheckBox("Reset");
        menuSlideZoom.getChildren().addAll(zoomLess, zoomSlide, zoomMoree, zoomReset);
        mBtnZoom.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_zoom.png"))));
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
        
        //Set Colors-----------------------------------------------------------------------------------
        ColorPicker cores = new ColorPicker();
        MenuButton complementoColors = new MenuButton("");
        cores.setOnAction((ActionEvent event) -> {
            if(selecao.isEmpty()){
                changeColorsBlock(cores, "");
            }else{
                changeColorsBlock(cores, "MultiSelecao");
            }
        });
        MenuItem defaultColor = new MenuItem("Default Color");
        defaultColor.setOnAction((ActionEvent event) -> {
            changeColorsBlock(cores, "Default");
        });
        complementoColors.getItems().add(defaultColor);
        paleta = new HBox();
        paleta.setAlignment(Pos.CENTER);
        paleta.getChildren().addAll(cores,complementoColors);

        final boolean[] forceChecked = {false};
        propertyPanelCheckBox = new CheckBox("Property Panel");
        AtomicReference<Stage> propertyPanelState = new AtomicReference<>();
        propertyPanelCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {


            if(newValue && !forceChecked[0]){
                forceChecked[0] = false;
                propertyPanelState.set(createPropertyPanel());
                propertyPanelState.get().show();

            }else if(oldValue && !newValue){
                Alert alert = new Alert(Alert.AlertType.NONE, "Really close the Propertys Panel?", ButtonType.YES, ButtonType.NO);
                if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                    // you may need to close other windows or replace this with Platform.exit();
                    propertyPanelState.get().close();
                    forceChecked[0] = false;
                }else {
                    forceChecked[0] = true;
                    propertyPanelCheckBox.setSelected(true);

                }
            }
        });

        mToolBar.getItems().addAll(mBtnArrow,mBtnBlock,interceptionNodeButton,mBtnTransitionLine,mBtnTransitionArc,mBtnEraser,mBtnHand,mBtnZoom /*,propertyPanelCheckBox*/);
        
        //ToolTips
        Tooltip arrowInfo = new Tooltip("Selection");
        Tooltip blockinfo = new Tooltip("HmscBlock");
        Tooltip lineTransitionInfo = new Tooltip("Straight Transition");
        Tooltip arcTransitionInfo = new Tooltip("Curved Transition");
        Tooltip eraserInfo = new Tooltip("Eraser");
        Tooltip handInfo = new Tooltip("Move");
        Tooltip zoomInfo = new Tooltip("Ctrl + MouseScroll-Up \nCtrl + MouseScroll-Down \nCtrl + Mouse Button Middle");
        Tooltip.install(mBtnArrow, arrowInfo);
        Tooltip.install(mBtnBlock, blockinfo);
        Tooltip.install(mBtnTransitionLine, lineTransitionInfo);
        Tooltip.install(mBtnTransitionArc, arcTransitionInfo);
        Tooltip.install(mBtnEraser, eraserInfo);
        Tooltip.install(mBtnHand, handInfo);
        Tooltip.install(mBtnZoom, zoomInfo);
        
        //Context Menu
        MenuItem saveAsPNG = new MenuItem("Save as PNG");
        saveAsPNG.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as PNG");
            fileChooser.setInitialFileName(viewer.getHmscComponent().getName() + ".png");
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
            viewer.saveAsPng(arq);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "PNG Image successfuly saved!", ButtonType.OK);
            alert.show();
        });
        
        MenuItem creatHMSC = new MenuItem("Create bMSC");
        creatHMSC.setOnAction((ActionEvent event) -> {
            HmscBlock h = ((HmscBlockView) componentSobMouse).getHMSC();
            if(!h.isFull()){
                BmscComponent bmsc = new BmscComponent();
                if(!projectExplorerPluginMSC.getAll_BMSC().isEmpty()) {
                    bmsc.setID((projectExplorerPluginMSC.getAll_BMSC().get(projectExplorerPluginMSC.getAll_BMSC().size() - 1).getID()) + 1);
                }else {
                    bmsc.setID(( projectExplorerPluginMSC.getSelectedProjectDS().getID() * 1000 ) + 201);
                }
                bmsc.setName(h.getLabel());
                viewer.getHmscComponent().setBmscIntoHmsc(h, bmsc);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"hMSC is Full",ButtonType.OK);
                alert.show();
            }
        });

        MenuItem setAsInitial = new MenuItem("Set initial");
        setAsInitial.setOnAction((ActionEvent event) ->{
            HmscBlock h = ((HmscBlockView) componentSobMouse).getHMSC();
            if(viewer.getHmscComponent().getInitialHmscBlock() == h){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("This HMSC is the initial!");
                alert.show();
                return;
            }

            Alert pergunta = new Alert(Alert.AlertType.CONFIRMATION);
            pergunta.setTitle("Set as Initial");
            pergunta.setHeaderText(null);
            pergunta.setContentText("Set this HMSC as initial?");

            Optional<ButtonType> resposta = pergunta.showAndWait();
            if(resposta.get() == ButtonType.OK){
                viewer.getHmscComponent().setInitialHmscBlock(h);
            }
        });

        setAsDecisionNode = new MenuItem("Set as Exceptional/Normal");
        setAsDecisionNode.setOnAction(event ->{
            HmscBlock selectedHmscBlock = ((HmscBlockView)componentSelecionado).getHMSC();
            if(selectedHmscBlock.isExceptional()){
                removeStyleExceptional(((HmscBlockView) componentSelecionado));
                selectedHmscBlock.setExceptional(false);
            }else {
                ((HmscBlockView)componentSelecionado).getHMSC().setExceptional(true);
                setStyleExceptional(((HmscBlockView) componentSelecionado));
            }

        });





        
        mContextMenuBlockBuild.getItems().addAll(creatHMSC, setAsInitial, setAsDecisionNode ,saveAsPNG);
        viewer.setBlockBuildContextMenu(mContextMenuBlockBuild);
        
        viewer.getNode().getTransforms().add(escala);
        
        //Guardando variaveis padrao
        mViewerScaleXPadrao = viewer.getNode().getScaleX();
        mViewerScaleYPadrao = viewer.getNode().getScaleY();
        mViewerTranslateXPadrao = viewer.getNode().getTranslateX();
        mViewerTranslateYPadrao = viewer.getNode().getTranslateY();
        
        //Resetando Zoom
        zoomReset.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (zoomReset.isSelected()) {
                zoomSlide.setValue(1);
                viewer.getNode().setScaleX(mViewerScaleXPadrao);
                viewer.getNode().setScaleY(mViewerScaleYPadrao);
                viewer.getNode().setTranslateX(mViewerTranslateXPadrao);
                viewer.getNode().setTranslateY(mViewerTranslateYPadrao);
            }
        });
        
        //Retangulo da selcao
        rectSelecao = new Rectangle(0, 0, 0, 0);
        rectSelecao.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.7));
        
        //Funcoes da tela
        viewer.getNode().setOnMouseMoved(onMovedMouse);
        viewer.getNode().setOnMouseClicked(onMouseClicked);
        
        viewer.getNode().setOnMousePressed(onMousePressed);
        viewer.getNode().setOnMouseDragged(onMouseDragged);
        viewer.getNode().setOnMouseReleased(onMouseReleased);
        
        viewer.getNode().setOnDragDetected(aoDetectarDragSobreVertice);
        viewer.getNode().setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        viewer.getNode().setOnDragDropped(aoSoltarMouseSobreVertice);
        
        //Panel de Proriedades
        VBox blockPropriedade = new VBox(5);
        txtLabel = new TextField();
        txtLabel.setPromptText("Name/Label");
        txtLabel.setOnKeyReleased((KeyEvent event) -> {
            Object obj = componentSelecionado;
            if(event.getCode() != KeyCode.ENTER) {
                if (obj instanceof TransitionMSCView) {
                    ((TransitionMSCView) obj).getTransition().setLabel(txtLabel.getText());
                }
                if (!selecao.isEmpty()) {
                    Node node = selecao.iterator().next();
                    if (node instanceof HmscBlockView) {
                        ((HmscBlockView) node).getHMSC().setLabel(txtLabel.getText());
                        // Se O bMSC sempre tiver que ter o mesmo nome do hMSC
                        if(((HmscBlockView) node).getHMSC().getBmscComponet() != null) {
                            ((HmscBlockView) node).getHMSC().getBmscComponet().setName(txtLabel.getText());
                        }
                        projectExplorerPluginMSC.clear2();
                    }
                }
            }else{
                if (obj instanceof TransitionMSCView) {
                    txtLabel.setText(((TransitionMSCView) obj).getTransition().getLabel());
                }
                if (!selecao.isEmpty()) {
                    Node node = selecao.iterator().next();
                    if (node instanceof HmscBlockView) {
                        txtLabel.setText(((HmscBlockView) node).getHMSC().getLabel());
                        projectExplorerPluginMSC.clear2();
                    }
                }
            }
        });
        txtProbability = new TextField();
        txtProbability.setPrefWidth(50);
        txtProbability.setAlignment(Pos.CENTER);
        campoProbability(txtProbability);
        txtProbability.setPromptText("%");
        txtProbability.setOnAction(event -> {
            Object obj = componentSelecionado;
            if (obj instanceof TransitionMSCView) {
                try {
                    if(txtProbability.getText().equals("")){
                        ((TransitionMSCView) obj).getTransition().setProbability(null);
                        viewer.getHmscComponent().setProb( viewer.getHmscComponent().getProb() - 1 );
                    }else{
                        String auxValor = verify_Probability(txtProbability);
                        if(!auxValor.equals("")) {
                            viewer.getHmscComponent().setProb(viewer.getHmscComponent().getProb() + 1);

                            ((TransitionMSCView) obj).getTransition().setProbability(Double.parseDouble(auxValor));
                        }
                    }
                } catch (Exception e) {
                    //ignora
                }
            }
        });


        txtGuard = new TextField();
        txtGuard.setPrefWidth(50);
        txtGuard.setAlignment(Pos.CENTER);
        txtGuard.setPromptText("( Guard )");
        txtGuard.setOnAction(event -> {
            Object obj = componentSelecionado;
            if (obj instanceof TransitionMSCView) {
                if(txtGuard.getText().isEmpty() || txtGuard.getText().equals("")){
                    ((TransitionMSCView) obj).getTransition().setGuard(null);
                }else {
                    ((TransitionMSCView) obj).getTransition().setGuard(txtGuard.getText());
                }
            }
        });


        txtAction = new TextField();
        txtAction.setPrefWidth(50);
        txtAction.setAlignment(Pos.CENTER);
        txtAction.setPromptText("{ Action }");
        txtAction.setOnAction(event -> {
            Object obj = componentSelecionado;
            if (obj instanceof TransitionMSCView) {
                TransitionMSC transitionMSC = ((TransitionMSCView) obj).getTransition();
                if(txtAction.getText().isEmpty() || txtAction.getText().equals("")){
                   if( transitionMSC.getActions() != null){
                       transitionMSC.clearActions();
                   }
                }else {
                    if( transitionMSC.getActions() != null){
                        transitionMSC.clearActions();
                    }
                  for(String action: txtAction.getText().split(",")){
                      action= action.trim();
                      transitionMSC.addAction(action);
                  }
                }
            }
        });

        lblLabel = new Label("Name / Label:");
        lblGuard = new Label("Guard:");
        lblAction = new Label("Actions:");
        lblProbability = new Label("Probability:");

        blockPropriedade.getChildren().addAll(lblLabel, txtLabel,lblGuard, txtGuard, lblAction, txtAction, lblProbability,txtProbability);

        ////////////////// Propriedades para Runtime ///////////////////
        popup.setHideOnEscape(false);
        popup.setAutoFix(true);
        popup.setAutoHide(false);

        mSourceCol = new TableColumn<>("Source");
        mSourceCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("source"));
        mSourceCol.setPrefWidth(100);
        mTargetCol = new TableColumn<>("Target");
        mTargetCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("target"));
        mTargetCol.setPrefWidth(100);
        mInequationCol = new TableColumn<>("Inequation");
        mInequationCol.setPrefWidth(100);
        mInequationCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("inequation"));
        mConditionCol = new TableColumn<>("Condition");
        mConditionCol.setPrefWidth(100);
        mConditionCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("condition"));
        mExcSttCol = new TableColumn<>("Excluding States");
        mExcSttCol.setPrefWidth(160);
        mExcSttCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("excstt"));
        mResultCol = new TableColumn<>("Result");
        mResultCol.setPrefWidth(100);
        mResultCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("result"));

        tableViewProperty = new TableView<>();
        tableViewProperty.setPrefWidth(150);
        tableViewProperty.getColumns().addAll(mSourceCol, mTargetCol, mInequationCol, mConditionCol, mExcSttCol, mResultCol);
        tableViewProperty.setItems(mEntries);


        btnAddProperty = new Button("Add new property");
        btnAddProperty.setOnAction(btnAddPropertyAction);

        propertyDropDown = new TitledPane("Property Panel",propertyPanel()); // Trocar btnAdd por metodo que gera um Panel com todas as informações do Runtime
        propertyDropDown.setVisible(false);

        VBox PropertyPanelBox = new VBox(5);
        PropertyPanelBox.getChildren().addAll(blockPropriedade, propertyDropDown);

        mPropriedadePanel.getChildren().add(PropertyPanelBox);





       //Panel de info / utilidade
        HBox utilidade = new HBox(10); 
        utilidade.setPrefSize(mInfoPanel.getPrefWidth(), mInfoPanel.getPrefHeight());
        HBox infoEmpyt = new HBox(2);
        infoEmpyt.setPrefSize(utilidade.getPrefWidth(), utilidade.getPrefHeight());
        infoEmpyt.setAlignment(Pos.CENTER);
        HBox infoFull = new HBox(2);
        infoFull.setPrefSize(utilidade.getPrefWidth(), utilidade.getPrefHeight());
        infoFull.setAlignment(Pos.CENTER);
        HBox infoInitial = new HBox(2);
        infoInitial.setPrefSize(utilidade.getPrefWidth(), utilidade.getPrefHeight());
        infoInitial.setAlignment(Pos.CENTER);

        Circle green = new Circle(7); green.setFill(Color.GREEN);
        Label full = new Label("Full");
        Circle red = new Circle(7); red.setFill(Color.RED);
        Label empyt = new Label("Empyt");
        Polygon mInitial = new Polygon();
        mInitial.getPoints().addAll(new Double[]{
                0.0, 0.0,
                10.0, 0.0,
                5.0, 10.0
        });
        Label initial = new Label("Initial State");

        infoFull.getChildren().addAll(green,full);
        infoEmpyt.getChildren().addAll(red,empyt);
        infoInitial.getChildren().addAll(mInitial,initial);
        utilidade.getChildren().addAll(infoEmpyt,infoFull, infoInitial);
        
        Button buildLTSFromHMSC = new Button("Build LTS", new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_build_LTS.png"))));
        buildLTSFromHMSC.setOnAction(buildLTSFromHMSCEvent );

        
        mInfoPanel.getChildren().addAll(utilidade,buildLTSFromHMSC);
        
    }

    private void removeStyleExceptional(HmscBlockView hMSCView) {
        hMSCView.getHMSC().setBorderWidth(HmscBlockViewImpl.DEFAULT_BORDER_WIDTH);
        hMSCView.getHMSC().setColor(HmscBlockViewImpl.DEFAULT_COLOR);
    }

    private void setStyleExceptional(HmscBlockView hMSCView) {
       hMSCView.getHMSC().setBorderWidth(HmscBlockViewImpl.EXCEPTIONAL_BORDER_WIDTH);
       hMSCView.getHMSC().setColor(HmscBlockViewImpl.EXCEPTIONAL_COLOR);



    }

    private Stage createPropertyPanel(){
        Stage stage = new Stage();
        stage.setTitle("Property Panel");

        PropertysPanelController propertysPanelController = new PropertysPanelController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/propertysPanel.fxml"));
        fxmlLoader.setController(propertysPanelController);
        AnchorPane propertyPanelAnchorPane = null;
        try {
            propertyPanelAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = null;
        if (propertyPanelAnchorPane != null) {
            scene = new Scene(propertyPanelAnchorPane, 692, 470);

        }
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            propertyPanelCheckBox.setSelected(false);
        });


        propertysPanelController.onCreatedView();

        return stage;

    }



    // Ao Mover o mouse----------------------------------------------------------------------
    private final EventHandler<? super MouseEvent> onMovedMouse = (MouseEvent event) -> {
        Context context = new Context(new OnMovedMouse());
        context.executeStrategyOnMovedMouse(this,event);
    };
    // Ao Clicar o mouse(clickar e soltar)---------------------------------------------------
    private final EventHandler<? super MouseEvent> onMouseClicked = (MouseEvent event) -> {
        Context context = new Context(new OnClickedMouse());
        context.executeStrategyOnClikedMouse(this,event);
    };
    //Ao Clicar sem soltar
    private final EventHandler<? super MouseEvent> onMousePressed = (MouseEvent event) -> {
        Context context = new Context(new OnPressedMouse());
        context.executeStrategyOnPressedMouse(this,event);
    };
    //Ao Arrastar o mouse
    private final EventHandler<? super MouseEvent> onMouseDragged = (MouseEvent event) -> {
        Context context = new Context(new OnDraggedMouse());
        context.executeStrategyOnDraggedMouse(this,event);
    };
    //Ao Soltar o mouse
    private final EventHandler<? super MouseEvent> onMouseReleased = (MouseEvent event) -> {
        Context context = new Context(new OnReleasedMouse());
        context.executeStrategyOnReleasedMouse(this,event);
    };
    //Preparar Transicao
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = (MouseEvent event) -> {
        Context context = new Context(new OnDragDetected());
        context.executeStrategyOnDragDetectedMouse(this, event);
    };
    //Possivel alvo para Transicao
    private EventHandler<DragEvent> aoDetectarPossivelAlvoParaSoltarODrag = (DragEvent event) -> {
        Context context = new Context(new OnDragOver());
        context.executeStrategyOnDragOverMouse(this, event);
    };
    //Criar Transicao
    private EventHandler<DragEvent> aoSoltarMouseSobreVertice = (DragEvent event) -> {
        Context context = new Context(new OnDragDropped());
        context.executeStrategyOnDragDroppedMouse(this, event);
    };

    ///////////////////////////////////////////////////////////////////////////////////
    //                         Metodos de Controle da View                           //
    //////////////////////////////////////////////////////////////////////////////////
    private void setModo(int modo) {
        this.mModoAtual = modo;
         viewer.getNode().setCursor(Cursor.DEFAULT);
         if (mModoAtual == MOVE_MODE) {
            viewer.getNode().setCursor(Cursor.OPEN_HAND);
        }
    }

    public Object getComponentePelaPosicaoMouse(Point2D point) {
        Object b = viewer.locateGenericElementView(point);
        if(b == null){
            b = viewer.locateTransitionBuildView(point);
        }
        return b;
    }

    public void setComponenteSelecionado(Object mComponentSobMouse) {
         if (componentSelecionado != null) {
            removeSelectedStyles(componentSelecionado);
        }
        componentSelecionado = mComponentSobMouse;
        if (mComponentSobMouse != null) {
            updatePropriedades(mComponentSobMouse);
            applySelectedStyles(componentSelecionado);
        }else {
            hideProppriedades();
        }
    }

    private void hideProppriedades() {
        lblLabel.setVisible(false);
        txtLabel.setVisible(false);


        txtGuard.setVisible(false);
        lblGuard.setVisible(false);

        txtAction.setVisible(false);
        lblAction.setVisible(false);


        lblProbability.setVisible(false);
        txtProbability.setVisible(false);
    }

    private void applySelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof HmscBlockView){
            HmscBlock b = ((HmscBlockView) mComponentSelecionado).getHMSC();
            b.setBorderWidth(3);
            b.setBorderColor("blue");
            b.setTextColor("blue");
            b.setTextStyle(HmscBlock.mTextStyleBold);
        }
        else if(mComponentSelecionado instanceof TransitionMSCView){
            TransitionMSC t = ((TransitionMSCView)mComponentSelecionado).getTransition();
            t.setBorderWidth(2);
            t.setColor("blue");
            t.setTextColor("blue");
            t.setTextStyle(TransitionMSC.TEXTSTYLE_BOLD);

        }else if(mComponentSelecionado instanceof InterceptionNodeView){
            InterceptionNode interceptionNode = ((InterceptionNodeView)mComponentSelecionado).getInterceptionNode();
            interceptionNode.setBorderWidth(5);
            interceptionNode.setColor("blue");
            interceptionNode.setTextColor("blue");
            interceptionNode.setTextStyle(HmscBlock.mTextStyleBold);



        }
    }

    public void removeSelectedStyles(Object mComponentSelecionado) {


        if(mComponentSelecionado instanceof HmscBlockView){

            if(((HmscBlockView) mComponentSelecionado).getHMSC()==null){
                return;
            }


            HmscBlock b = ((HmscBlockView) mComponentSelecionado).getHMSC();
            b.setBorderWidth(1);
            b.setBorderColor("black");
            b.setTextColor("black");
            b.setTextStyle(HmscBlock.mTextStyleNormal);
        }else if(mComponentSelecionado instanceof TransitionMSCView){

            if(((TransitionMSCView)mComponentSelecionado).getTransition()==null){
                return;
            }

                TransitionMSC t = ((TransitionMSCView)mComponentSelecionado).getTransition();
                t.setBorderWidth(1);
                t.setColor("black");
                t.setTextColor("black");
                t.setTextStyle(TransitionMSC.TEXTSTYLE_NORMAL);
            }

        }

    
    // Mascara para Probabilidade
    private void campoProbability(final TextField textField) {
        textField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                char ch = textField.getText().charAt(oldValue.intValue());
                if (!(ch >= '0' && ch <= '9' || ch == '.' || ch == ',' || ch == '%')) {
                    textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                }
            }
        });
    }

    private void updatePropriedades(Object obj) {
        if(obj instanceof TransitionMSCView){
            TransitionMSC t = ((TransitionMSCView)obj).getTransition();
            lblLabel.setVisible(true);
            txtLabel.setVisible(true);
            txtLabel.setText(t.getLabel());
            txtLabel.requestFocus();

            txtGuard.setText(t.getGuard()== null ? "" : t.getGuard());
            txtGuard.setVisible(true);
            lblGuard.setVisible(true);

            txtAction.setText(t.getActions().size() ==  0 ? "" : String.join(",", t.getActions()));
            txtAction.setVisible(true);
            lblAction.setVisible(true);

            /*txt.setText(t.getActions().size() ==  0 ? "" : String.join(",", t.getParameters()));
            txtAction.setVisible(true);*/

            txtProbability.setText(t.getProbability() == null ? null : String.valueOf(t.getProbability()));
            lblProbability.setVisible(true);
            txtProbability.setVisible(true);
            
        }
    }
//
//    public void updateCountHmscBlock() {
//        int aux = -1;
//        for (HmscBlock b : viewer.getHmscComponent().getHmscBlockList()) {
//            if (b.getID() > aux) {
//                aux = b.getID();
//            }
//        }
//        countHmscBlock = aux;
//        countHmscBlock++;
//    }
//
//    public void updateCountInterceptionNode() {
//        int aux = -1;
//        for(InterceptionNode interceptionNode : viewer.getHmscComponent().getInterceptionNodeList()){
//            if (interceptionNode.getID() > aux) {
//                aux = interceptionNode.getID();
//            }
//        }
//
//        countIntercptionNode = aux;
//        countIntercptionNode++;
//
//
//    }
//
//    public void updateCountGenericElements() {
//        int aux = -1;
//        for(GenericElement genericElement : viewer.getHmscComponent().getGenericElementList()){
//            if (genericElement.getID() > aux) {
//                aux = genericElement.getID();
//            }
//        }
//
//        countGenericElement = aux;
//        countGenericElement++;
//
//
//    }
//
//    public int getCountHmscBlock() {
//        return countHmscBlock;
//    }
//
//    public void setCountHmscBlock(int countHmscBlock) {
//        this.countHmscBlock = countHmscBlock;
//    }
//
//    public int getCountIntercptionNode() {
//        return countIntercptionNode;
//    }
//
//    public void setCountIntercptionNode(int countIntercptionNode) {
//        this.countIntercptionNode = countIntercptionNode;
//    }
//
//    public int getCountGenericElement() {
//        return countGenericElement;
//    }
//
//    public void setCountGenericElement(int countGenericElement) {
//        this.countGenericElement = countGenericElement;
//    }

    private void changeColorsBlock(ColorPicker cores, String tipo) {
        if(selecao==null){
            return;
        }
        String hexCor = "";
        if(cores.getValue().toString().equals("0x000000ff")){
            hexCor = "black";
        }else{
            hexCor = "#"+ Integer.toHexString(cores.getValue().hashCode()).substring(0, 6).toUpperCase();
        }
        for(Node b : selecao){
            HmscBlockView bview = (HmscBlockView)b;
            HmscBlock bbds = bview.getHMSC();
            if(tipo.equals("Default")){
                bbds.setColor(null);
            }
            if(tipo.equals("MultiSelecao")){
                bbds.setColor(hexCor);
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //                         Metodos de Selecao                                    //
    //////////////////////////////////////////////////////////////////////////////////
    
    public boolean selecionadoPeloRetangulo = false;
    
    public void addNoSelecao(Node node){
        if(componentSelecionado instanceof TransitionMSCView){
            removeSelectedStyles(componentSelecionado);
        }

        applySelectedStyles((Object)node);
        selecao.add(node);
        selecionadoPeloRetangulo = true;



        if(node instanceof GenericElementView) {
            GenericElement genericElement = ((GenericElementView) node).getGenericElement();

            if (genericElement instanceof HmscBlock) {
                lblLabel.setVisible(true);
                txtLabel.setVisible(true);
                txtLabel.setText(((HmscBlock) genericElement).getLabel());
            }
        }

        txtLabel.requestFocus();
        lblProbability.setVisible(false);
        txtProbability.setVisible(false);
        txtGuard.setVisible(false);
        lblGuard.setVisible(false);
        txtAction.setVisible(false);
        lblAction.setVisible(false);
    }
    
    public void removeNoSelecao(Node node){
        removeSelectedStyles((Object)node);
        selecao.remove(node);
    }
    
    public void clearSelecao(){
        while(!selecao.isEmpty()){
            removeNoSelecao(selecao.iterator().next());
        }
        selecionadoPeloRetangulo = false;
    }
    
    public boolean containsNoSelecao(Node node){
        return selecao.contains(node);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //                         Metodos de Botoes                                    //
    //////////////////////////////////////////////////////////////////////////////////

    EventHandler<ActionEvent> buildLTSFromHMSCEvent = (ActionEvent event) -> {

     //   setAsDecisionNode.fire();
        HmscComponent standardModeling = this.getHmscComponent();


        ParallelComponentController parallelComponentController =  new ParallelComponentController(standardModeling);


        try {
            List<Component> createdComponentsWithIndividualLTS = parallelComponentController.buildIndividualComponents();

             createdComponentsWithIndividualLTS =  removeInterceptionNodeComponent(createdComponentsWithIndividualLTS);

            parallelComponentController.addIndividualComponentsInLeftPanel(this, createdComponentsWithIndividualLTS);

            List<Component> createdComponentsWithLifeLTS = parallelComponentController.buildLifeComponents();
             parallelComponentController.addLifeComponentsInLeftPanel(this, createdComponentsWithLifeLTS);

            Component parallelComponent = parallelComponentController.buildParallelComponent();


            //remover labals//
            for(Transition transition : parallelComponent.getTransitionsList()){
                transition.setProbability(null);
                if( transition.getLabel().split("\\.")[1].isEmpty()){
                    transition.setLabel(null);
                }else {
                    transition.setLabel(transition.getLabel().split("\\.")[2]);
                }

            }

            removeTransictionWithoutLabelsAndActionAndGuard(parallelComponent);

            changeStructureLabelState(parallelComponent);

            for(State state : parallelComponent.getStatesList()){
                Boolean isExceptional = false;
                Boolean isInterceptionNode = false;

               if(state.getValue("isExceptional")!= null){
                   isExceptional = (Boolean) state.getValue("isExceptional");
               }

                if(state.getValue("isInterceptionNode")!= null){
                    isInterceptionNode = (Boolean) state.getValue("isInterceptionNode");
                }


                System.out.println("state"+state.getLabel()+" isExceptional: "+isExceptional +" isInterceptionNode: "+isInterceptionNode);
            }






            setComponentLTS(parallelComponent);

          /*  //new project lts
            Project p = new Project();
            String namePrompt = "Untitled" + (projectExplorerPluginMSC.projectExplorer.getAllProjects().size() + 1);
            String name = JOptionPane.showInputDialog(null, "Enter the new project'run name", namePrompt);

            p.setName(name);

            p.addComponent(parallelComponent);
            projectExplorerPluginMSC.projectExplorer.open(p);*/
            parallelComponentController.addParallelComponentInLeftPanel(this, parallelComponent);

        } catch (Exception e) {
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            emptyAlert.show();
            e.printStackTrace();
        }

    };

    private void changeStructureLabelState(Component parallelComponent) {
        int newLabel = 0;
        for(State state : parallelComponent.getStates()){
            state.setLabel(state.getLabel().split(",")[0].replace("<"," ").trim());
            state.setLabel(String.valueOf(newLabel++));
        }
    }

    private void removeTransictionWithoutLabelsAndActionAndGuard(Component parallelComponent) {

        List<State> statesToRemove = new ArrayList<>();
        List<Transition> transitionsToAdd = new ArrayList<>();

        for(Transition currentTransition : parallelComponent.getTransitionsList()){
            if(currentTransition.getLabel() == null || currentTransition.getLabel().equals("")){
                if(currentTransition.getGuard() == null || currentTransition.getGuard().equals("") ){
                    if(currentTransition.getActions().size() == 0){
                        if(currentTransition.getParameters().size()==0){

                            join(currentTransition.getSource(), currentTransition.getDestiny(), transitionsToAdd, currentTransition);

                            statesToRemove.add(currentTransition.getSource());
                        }
                    }

                }
            }
        }




        for(Transition transitionToAdd : transitionsToAdd){
           Transition newTransition = parallelComponent.buildTransition(transitionToAdd.getSource(), transitionToAdd.getDestiny()).setViewType(TransitionView.Geometry.LINE).create();

            newTransition.setLabel(transitionToAdd.getLabel());
            newTransition.setProbability(transitionToAdd.getProbability());
            newTransition.setActions(transitionToAdd.getActions());
            newTransition.setGuard(transitionToAdd.getGuard());
            newTransition.setParameters(transitionToAdd.getParameters());
            newTransition.setValues(transitionToAdd.getValues());

        }

        for(State stateToRemove : statesToRemove){
            parallelComponent.remove(stateToRemove);
        }

    }

    private void join(State source, State destiny, List<Transition> transitionsToAdd, Transition currentTransition) {

        Transition transitionToJump = currentTransition;

        for(Transition inTransition :source.getIncomingTransitionsList()){

            if(inTransition == transitionToJump){
                continue;
            }

            buildTransitionIn(destiny, inTransition, transitionsToAdd);

        }

        for(Transition outTransition :source.getOutgoingTransitionsList()){

            if(outTransition == transitionToJump){
                continue;
            }

            buildTransitionOut(destiny,outTransition, transitionsToAdd);

        }




    }

    private void buildTransitionIn(State destState, Transition inTransition, List<Transition> transitionsToAdd) {


        State sourceState =  inTransition.getSource();

        State currentState = inTransition.getDestiny();

        Transition newTransition = new Transition(sourceState, destState);

        newTransition.setLabel(inTransition.getLabel());
        newTransition.setProbability(inTransition.getProbability());
        newTransition.setActions(inTransition.getActions());
        newTransition.setGuard(inTransition.getGuard());
        newTransition.setParameters(inTransition.getParameters());
        newTransition.setValues(inTransition.getValues());


        Boolean isExceptional = false;
        Boolean isInterceptionNode = false;

        if(currentState.getValue("isExceptional")!= null){
            isExceptional = (Boolean) currentState.getValue("isExceptional");

            if(isExceptional){
                destState.setValue("isExceptional", true);
            }
        }

        if(currentState.getValue("isInterceptionNode")!= null){
            isInterceptionNode = (Boolean) currentState.getValue("isInterceptionNode");
        }

        if(isInterceptionNode){
            destState.setValue("isInterceptionNode", true);
        }

        transitionsToAdd.add(newTransition);

    }

    private void buildTransitionOut(State sourceState, Transition outTransition, List<Transition> transitionsToAdd) {



        State destState =  outTransition.getDestiny();
        State currentState = outTransition.getSource();

        Transition newTransition = new Transition(sourceState, destState);
        newTransition.setLabel(outTransition.getLabel());
        newTransition.setProbability(outTransition.getProbability());
        newTransition.setActions(outTransition.getActions());
        newTransition.setGuard(outTransition.getGuard());
        newTransition.setParameters(outTransition.getParameters());
        newTransition.setValues(outTransition.getValues());

        Boolean isExceptional = false;
        Boolean isInterceptionNode = false;

        if(currentState.getValue("isExceptional")!= null){
            isExceptional = (Boolean) currentState.getValue("isExceptional");

            if(isExceptional){
                sourceState.setValue("isExceptional", true);
            }
        }

        if(currentState.getValue("isInterceptionNode")!= null){
            isInterceptionNode = (Boolean) currentState.getValue("isInterceptionNode");
        }

        if(isInterceptionNode){
            sourceState.setValue("isInterceptionNode", true);
        }

        transitionsToAdd.add(newTransition);

    }



    private List<Component> removeInterceptionNodeComponent(List<Component> createdComponentsWithIndividualLTS) {
        List<Component> componentList = new ArrayList<>();

        for(Component c : createdComponentsWithIndividualLTS){

            if(!c.getName().contains(InterceptionNode.class.getSimpleName())){
                componentList.add(c);
            }
        }
        return componentList;
    }


    //    EventHandler<ActionEvent> buildLTSFromHMSCEvent = (ActionEvent event) -> {
//        //verificar se tem bloco com model vazio
//        for(Node node : viewer.getNode().getChildren()){
//            if(node instanceof HmscBlockView){
//                HmscBlockView b = (HmscBlockView)node;
//                HmscBlock block = b.getHMSC();
//                if(!block.isFull()){
//                    Alert alerta = new Alert(Alert.AlertType.WARNING, "Algum bloco esta vazio", ButtonType.OK);
//                    alerta.show();
//                    return;
//                }
//            }
//        }
//
//        //Limpar arvore do projeto
//        //projectExplorerPluginMSC.removeFragmentsLTS();
//        projectExplorerPluginMSC.removeFragmetsLTS();
//       // projectExplorerPluginMSC.removeLtsComposed();
//
//        //Gerando o LTS Geral ----------------------------------------------------------------------------------------
//
//        Layouter layout = new Layouter();
//        List<Component> ltsGerados = new ArrayList<>();
//        List<BmscComponent> listaBMSC = projectExplorerPluginMSC.getAll_BMSC();
//        int id_ltsfrag = 0;
//        for(BmscComponent atualComponenteDiagramaSequencia : listaBMSC){
//            id_ltsfrag++;
//            List<TabelaReferenciaID> relativo = new ArrayList<>();
//            ArrayList<BmscBlock> objetos = (ArrayList<BmscBlock>) atualComponenteDiagramaSequencia.getBmscBlockList();
//            for(int i=0;i<objetos.size();i++){
//                TabelaReferenciaID id = new TabelaReferenciaID(i+1, objetos.get(i).getID()+"");
//                relativo.add(id);
//            }
//            List<InteractionFragments> loopsOuAlts = new ArrayList<>();
//            List<Mensagem> comunicacao = new ArrayList<>();
//            for(TransitionMSC t : atualComponenteDiagramaSequencia.getAllTransitions()){
//                Mensagem m = new Mensagem();
//                if(t.getSource() instanceof BlockDSView) {
//                    m.setEnviando(new AtorAndClasse(((BlockDSView) t.getSource()).getBmscBlockList().getLabel(),
//                            String.valueOf(((BlockDSView) t.getSource()).getBmscBlockList().getID()),
//                            "actor"));
//                }else if(t.getSource() instanceof BmscBlock){
//                    m.setEnviando(new AtorAndClasse(((BmscBlock) t.getSource()).getLabel(),
//                            String.valueOf(((BmscBlock)t.getSource()).getID()),
//                            "actor"));
//                }
//                if(t.getDestiny() instanceof BlockDSView) {
//                    m.setRecebendo(new AtorAndClasse(((BlockDSView) t.getDestiny()).getBmscBlockList().getLabel(),
//                            String.valueOf(((BlockDSView) t.getDestiny()).getBmscBlockList().getID()),
//                            "actor"));
//                }else if(t.getDestiny() instanceof BmscBlock){
//                    m.setRecebendo(new AtorAndClasse(((BmscBlock)t.getDestiny()).getLabel(),
//                            String.valueOf(((BmscBlock)t.getDestiny()).getID()),
//                            "actor"));
//                }
//                m.setMsg(t.getLabel());
//                m.setXmiIdMsg(t.getIdSequence()+"");
//                comunicacao.add(m);
//            }
//            Component componentLTS = new Component();
//            componentLTS.setName("LTS "+atualComponenteDiagramaSequencia.getName());
//            componentLTS.setID((projectExplorerPluginMSC.getSelectedProjectDS().getID() * 1000)+ 300 + id_ltsfrag);
//            LtsParser parser = new LtsParser(comunicacao, relativo, loopsOuAlts, componentLTS);
//            componentLTS = parser.parseLTSA();
//
//            Iterator<State> it = componentLTS.getStates().iterator();
//            State fi = it.next();
//            componentLTS.setInitialState(fi);
//            while (it.hasNext()){
//                fi = it.next();
//            //    fi.clearIniFin();
//            }
//            componentLTS.setFinalState(fi);
////            componentLTS.setInitialState(componentLTS.getStates().iterator().next());
//
//            layout.layout(componentLTS);
//            ltsGerados.add(componentLTS);
//        }
//        viewer.getHmscComponent().createListLTS(ltsGerados);
//
//        try {
//            Component componentLTS = new Component();
//            List<Component> ltss = new ArrayList<>();
//            for(Component component : ltsGerados){
//                ltss.add(component.clone());
//            }
//            if (ltsGerados.size() >= 2) {
//                componentLTS = buildGeneralLTS(ltss);
//            } else {
//                componentLTS = ltsGerados.get(0).clone();
//            }
//            System.out.println("Nome da Composição é: "+ componentLTS.getName());
//            componentLTS.setID((projectExplorerPluginMSC.getSelectedProjectDS().getID()*1000) + 100 + 1);
//
//            Iterator<State> it2 = componentLTS.getStates().iterator();
//            componentLTS.setInitialState(it2.next());
//            while(it2.hasNext()) {
//                System.out.println("Estado é: " +it2.next().getNameState());
//            }
//
//            layout(componentLTS);
//            viewer.getHmscComponent().createGeneralLTS(componentLTS);
//        } catch (CloneNotSupportedException cloneNotSupportedException) {}
//    };
//    public void layout(Component component) {
//        int i = 1;
//        for (State stateInBase : component.getStates()) {
//            stateInBase.setLayoutX(i * 200);
//            stateInBase.setLayoutY(300 + (i % 10));
//            i++;
//        }
//    }
//    private Component buildGeneralLTS(List<Component> ltsGerados){
//        List<HmscBlock> listHmsc = viewer.getHmscComponent().getHmscBlockList();
//        GeneralLTSMaker make =
//                new GeneralLTSMaker(listHmsc, projectExplorerPluginMSC.getAll_BMSC(),ltsGerados, viewer, projectExplorerPluginMSC);
//        return make.produce();
//    }
//

    ///////////////////////////////////////////////////////////////////////////////////
    //                         Metodos das Propriedades                             //
    //////////////////////////////////////////////////////////////////////////////////
    public void changeToProperty(){
        ( (VBox) mPropriedadePanel.getChildren().get(0) ).getChildren().get(0).setVisible(false);
        /*
        ( (VBox) mPropriedadePanel.getChildren().get(0) ).getChildren().get(1).setVisible(false);
        ( (VBox) mPropriedadePanel.getChildren().get(0) ).getChildren().get(1).setLayoutY(5);
        ( (VBox) mPropriedadePanel.getChildren().get(0) ).getChildren().get(1).setVisible(true);
        */
    }


    EventHandler<ActionEvent> btnAddPropertyAction = (ActionEvent) -> {
        if(!popup.isShowing()) {
            popup.getContent().add(add_property());
            popup.setAnchorX(btnAddProperty.getLocalToSceneTransform().getTx() + 10);
            popup.setAnchorY(btnAddProperty.getLocalToSceneTransform().getTy());
            popup.show(getNode().getScene().getWindow());
        }
    };
    private AnchorPane add_property(){
        VBox box = new VBox(5);

        Label lblSour = new Label("Source:");
        ComboBox stats_source = new ComboBox();

        Label lblDest = new Label("Destiny:");
        ComboBox stats_destiny = new ComboBox();

        List<String> blocks = new ArrayList<>();


        for(Node node : viewer.getNode().getChildren()){
            if(node instanceof HmscBlockView) {
                HmscBlockView b = (HmscBlockView) node;
                HmscBlock hmscBlock = b.getHMSC();
                blocks.add(hmscBlock.getLabel());
            }
        }

        stats_source.getItems().addAll(blocks);
        stats_destiny.getItems().addAll(blocks);

        Label lblEquation = new Label("Equation");
        ComboBox equation_Box = new ComboBox();
        equation_Box.getItems().addAll("==", ">", ">=", "<", "<=", "!=");

        Label lblProbability = new Label("Probability");
        TextField prob_input = new TextField();
        prob_input.setPromptText("0.x or x% or 0,x");
        campoProbability(prob_input);


        Button save = new Button("Save");

        save.setOnAction((ActionEvent) -> {
            if(stats_source.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Source can not be Null", ButtonType.OK);
                alert.show();
                return;
            }

            if(stats_destiny.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Destiny can not be Null", ButtonType.OK);
                alert.show();
                return;
            }

            if(equation_Box.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Equation can not be Null", ButtonType.OK);
                alert.show();
                return;
            }

            if(prob_input.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Probability can not be Null", ButtonType.OK);
                alert.show();
                return;
            }
            String prob_value = verify_Probability(prob_input);
            if(!prob_value.equals("")) {
                Entry newEntry = new Entry(
                        stats_source.getValue().toString(),
                        stats_destiny.getValue().toString(),
                        equation_Box.getValue().toString(), null, null,
                        prob_value
                );
                mEntries.add(newEntry);
                popup.hide();
            }


        });

        Button close = new Button("close");
        close.setOnAction((ActionEvent) -> {
            popup.hide();
        });
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(save, close);
        box.getChildren().addAll(lblSour, stats_source, lblDest, stats_destiny,lblEquation,equation_Box,lblProbability,prob_input,hBox);


        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setStyle("-fx-background-color: whitesmoke; -fx-effect: dropshadow( gaussian , gray , 5 , 0.0 , 0 , 1);");
        return anchorPane;
    }

    private AnchorPane propertyPanel(){
        AnchorPane panel = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableViewProperty);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(300);

        VBox vBox = new VBox(5);
        vBox.getChildren().add(scrollPane);

        vBox.getChildren().add(btnAddProperty);



        panel.getChildren().add(vBox);
        return panel;
    }

    private String verify_Probability(TextField textField){
        String valorDoField = textField.getText().trim();
        String auxValor = "";
        if(valorDoField.contains(",")){
            auxValor = valorDoField.replaceAll(",", ".");
            double teste = Double.parseDouble(auxValor);
            if(teste<0 || teste >1){
                JOptionPane.showMessageDialog(null, "Input probability between 0 and 1", "Erro", JOptionPane.ERROR_MESSAGE);
                auxValor="";
            }
        }
        else if(valorDoField.contains(".")){
            auxValor = valorDoField;
            double teste = Double.parseDouble(auxValor);
            if(teste<0 || teste >1){
                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro", JOptionPane.ERROR_MESSAGE);
                auxValor="";
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
            }
        }
        else{
            if(valorDoField.equals("0") || valorDoField.equals("1")){
                auxValor = "";
            }else{
                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        return auxValor;
    }



    public class Entry {

        private SimpleStringProperty  mSource;
        private SimpleStringProperty mTarget;
        private SimpleStringProperty mInequation;
        private SimpleStringProperty mCondition;
        private SimpleStringProperty mExcStt;
        private SimpleStringProperty mResult;

        Entry(String source, String target, String inequation, String condition, String excstt, String result) {
            this.mSource = new SimpleStringProperty(source);
            this.mTarget = new SimpleStringProperty(target);
            this.mInequation = new SimpleStringProperty(inequation);
            this.mCondition = new SimpleStringProperty(condition);
            this.mExcStt = new SimpleStringProperty(excstt);
            this.mResult = new SimpleStringProperty(result);
        }

        public String getSource() { return mSource.get(); }
        public void setSource(String source) {
            mSource.set(source);
        }

        public String getTarget() {
            return mTarget.get();
        }
        public void setTarget(String target) {
            mTarget.set(target);
        }

        public String getInequation() {
            return mInequation.get();
        }
        public void setInequation(String inequation) {
            mInequation.set(inequation);
        }

        public String getCondition() {
            return mCondition.get();
        }
        public void setCondition(String condition) {
            mCondition.set(condition);
        }

        public String getExcstt(){ return mExcStt.get(); }
        public void setExcstt(String excstt){ mExcStt.set(excstt); }

        public String getResult() {
            return mResult.get();
        }
        public void setResult(String result) {
            mResult.set(result);
        }



    }

    public HmscComponentView getViewer() {
        return viewer;
    }

    public void setViewer(HmscComponentView viewer) {
        this.viewer = viewer;
    }
}
