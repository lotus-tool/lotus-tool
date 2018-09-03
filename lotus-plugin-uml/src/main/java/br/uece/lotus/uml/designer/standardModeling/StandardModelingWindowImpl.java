/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.model.ParallelCompositor;
import br.uece.lotus.uml.app.LifeLTSBuilder;
import br.uece.lotus.uml.app.IndividualLTSBuilder;
import br.uece.lotus.uml.app.ProbabilitySetter;
import br.uece.lotus.uml.app.runtime.controller.PropertysPanelController;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.hMSC.StandardModelingView;
import br.uece.lotus.uml.api.viewer.hMSC.StandardModelingViewImpl;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.api.window.WindowDS;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.lotus.uml.designer.standardModeling.strategy.Context;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnClickedMouse;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnDragDetected;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnDragDropped;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnDragOver;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnDraggedMouse;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnMovedMouse;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnPressedMouse;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnReleasedMouse;

import java.io.*;
import java.util.*;

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.*;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModelingWindowImpl extends AnchorPane implements WindowDS{



    private Popup popup = new Popup();
    //Area de Projeto
    public ProjectExplorerPluginDS projectExplorerPluginDS;
    //Principais da Tela
    private final ScrollPane mScrollPanel;
    private final AnchorPane mPropriedadePanel;
    private final HBox mInfoPanel;
    public final ToolBar mToolBar;
    public StandardModelingView mViewer;
    //Botoes
    private ToggleGroup mToggleGroup;
    private ToggleButton mBtnArrow;
    private ToggleButton mBtnBlock;
    private ToggleButton mBtnTransitionLine;
    private ToggleButton mBtnTransitionArc;
    private ToggleButton mBtnEraser;
    private ToggleButton mBtnHand;
    private MenuButton mBtnZoom;
    //Controles de acoes
    public final int MODO_NENHUM = 0;
    public final int MODO_BLOCO = 1;
    public final int MODO_TRANSICAO = 2;
    public final int MODO_REMOVER = 3;
    public final int MODO_MOVER = 4;
    public int contID = -1;
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
    public Object mComponentSobMouse;
    public Object mComponentSelecionado;
    public Circle mBounds = new Circle(8);
    //Cores
    public HBox paleta;
    //Transicao
    public HmscView hMSC_inicial;
    public HmscView hMSC_final;
    public Line fakeLine;
    public double xInicial;
    public double yInicial;
    //Painel de Propriedades
    public TextField txtAction;
    public TextField txtProbability;
    private Label lblAction;
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
    
    /////////////////////////////////////////////////////////////////////////
    //                   IMPLEMENTACAO DA WINDOW_DS                        //
    /////////////////////////////////////////////////////////////////////////
    @Override
    public StandardModeling getComponentBuildDS() {
        return mViewer.getComponentBuildDS();
    }

    @Override
    public ComponentDS getComponentDS() {return null;}

    @Override
    public Component getComponentLTS() {return null;}

    @Override
    public void setComponentBuildDS(StandardModeling buildDS) {
        mViewer.setComponentBuildDS(buildDS);
    }

    @Override
    public void setComponentDS(ComponentDS cds) {}

    @Override
    public void setComponentLTS(Component c) {}

    @Override
    public String getTitle() {
        StandardModeling c = mViewer.getComponentBuildDS();
        return c.getName();
    }

    @Override
    public Node getNode() {
        return this;
    }
    
    
    public StandardModelingWindowImpl(ProjectExplorerPluginDS projectExplorerPluginDS) {
        this.projectExplorerPluginDS = projectExplorerPluginDS;
        mViewer = new StandardModelingViewImpl();
        mToolBar = new ToolBar();
        mScrollPanel = new ScrollPane((Node)mViewer);
        mPropriedadePanel = new AnchorPane();
        mInfoPanel = new HBox(20);
        mViewer.getNode().setPrefSize(1200, 600);
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
        AnchorPane.setBottomAnchor(mScrollPanel, 30D);
        
        //propriedades
        getChildren().add(mPropriedadePanel);
        AnchorPane.setTopAnchor(mPropriedadePanel, 44D);
        AnchorPane.setRightAnchor(mPropriedadePanel, 3D);
        AnchorPane.setBottomAnchor(mPropriedadePanel, 30D);
        
        //info
        getChildren().add(mInfoPanel);
        AnchorPane.setLeftAnchor(mInfoPanel, 0D);
        AnchorPane.setRightAnchor(mInfoPanel, 0D);
        AnchorPane.setBottomAnchor(mInfoPanel, 0D);



        startComponentesTela();
        
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
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnArrow.setSelected(true);
        
        mBtnBlock = new ToggleButton();
        mBtnBlock.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_hMSC.png"))));
        mBtnBlock.setOnAction((ActionEvent event) -> {
            setModo(MODO_BLOCO);
        });
        mBtnBlock.setToggleGroup(mToggleGroup);
        
        mBtnTransitionLine = new ToggleButton();
        mBtnTransitionLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_line.png"))));
        mBtnTransitionLine.setOnAction((ActionEvent event) -> {
            setModo(MODO_TRANSICAO);
            mTransitionViewType = TransitionMSCView.Geometry.LINE;
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);
        
        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent event) -> {
            setModo(MODO_TRANSICAO);
            mTransitionViewType = TransitionMSCView.Geometry.CURVE;
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);
        
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent event) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);
        
        mBtnHand = new ToggleButton();
        mBtnHand.setDisable(true);
        mBtnHand.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_hand.png"))));
        mBtnHand.setOnAction((ActionEvent event) -> {
            setModo(MODO_MOVER);
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


        propertyPanelCheckBox = new CheckBox("Property Panel");

        propertyPanelCheckBox.setOnAction((ActionEvent e) -> {
            //propertyDropDown.setVisible(propertyPanelCheckBox.isSelected());
            Stage propertyPanelState = createPropertyPanel();

            if(propertyPanelCheckBox.isSelected()){
                propertyPanelState.show();
                // showPropertyPanel();
            }else {
                propertyPanelState.hide();
              //  hidePropertyPanel();
            }

            propertyPanelState.setOnCloseRequest(event -> {
                propertyPanelCheckBox.setSelected(false);
            });

        });

        mToolBar.getItems().addAll(mBtnArrow,mBtnBlock,mBtnTransitionLine,mBtnTransitionArc,mBtnEraser,mBtnHand,mBtnZoom, propertyPanelCheckBox);
        
        //ToolTips
        Tooltip arrowInfo = new Tooltip("Selection");
        Tooltip blockinfo = new Tooltip("Block");
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
        MenuItem mSaveAsPNG = new MenuItem("Save as PNG");
        mSaveAsPNG.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as PNG");
            fileChooser.setInitialFileName(mViewer.getComponentBuildDS().getName() + ".png");
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "PNG Image successfuly saved!", ButtonType.OK);
            alert.show();
        });
        
        MenuItem creat_bMSC = new MenuItem("Create bMSC");
        creat_bMSC.setOnAction((ActionEvent event) -> {
            Hmsc h = ((HmscView)mComponentSobMouse).getHMSC();
            if(!h.isFull()){
                ComponentDS bmsc = new ComponentDS();
                if(!projectExplorerPluginDS.getAll_BMSC().isEmpty()) {
                    bmsc.setID((projectExplorerPluginDS.getAll_BMSC().get(projectExplorerPluginDS.getAll_BMSC().size() - 1).getID()) + 1);
                }else {
                    bmsc.setID(( projectExplorerPluginDS.getSelectedProjectDS().getID() * 1000 ) + 201);
                }
                bmsc.setName(h.getLabel());
                mViewer.getComponentBuildDS().set_bMSC_in_hMSC(h, bmsc);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"hMSC is Full",ButtonType.OK);
                alert.show();
            }
        });

        MenuItem set_initial = new MenuItem("Set initial");
        set_initial.setOnAction((ActionEvent event) ->{
            Hmsc h = ((HmscView)mComponentSobMouse).getHMSC();
            if(mViewer.getComponentBuildDS().getHmsc_inicial() == h){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("This HMSC is the initial!");
                alert.show();
                return;
            }
            Alert pergunta = new Alert(Alert.AlertType.CONFIRMATION);
            pergunta.setTitle("Set initial");
            pergunta.setHeaderText(null);
            pergunta.setContentText("Set this HMSC as initial?");

            Optional<ButtonType> resposta = pergunta.showAndWait();
            if(resposta.get() == ButtonType.OK){
                mViewer.getComponentBuildDS().setHmsc_inicial(h);
            }
        });

        
        mContextMenuBlockBuild.getItems().addAll(creat_bMSC, set_initial ,mSaveAsPNG);
        mViewer.setBlockBuildContextMenu(mContextMenuBlockBuild);
        
        mViewer.getNode().getTransforms().add(escala);
        
        //Guardando variaveis padrao
        mViewerScaleXPadrao = mViewer.getNode().getScaleX();
        mViewerScaleYPadrao = mViewer.getNode().getScaleY();
        mViewerTranslateXPadrao = mViewer.getNode().getTranslateX();
        mViewerTranslateYPadrao = mViewer.getNode().getTranslateY();
        
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
        
        //Retangulo da selcao
        rectSelecao = new Rectangle(0, 0, 0, 0);
        rectSelecao.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.7));
        
        //Funcoes da tela
        mViewer.getNode().setOnMouseMoved(onMovedMouse);
        mViewer.getNode().setOnMouseClicked(onMouseClicked);
        
        mViewer.getNode().setOnMousePressed(onMousePressed);
        mViewer.getNode().setOnMouseDragged(onMouseDragged);
        mViewer.getNode().setOnMouseReleased(onMouseReleased);
        
        mViewer.getNode().setOnDragDetected(aoDetectarDragSobreVertice);
        mViewer.getNode().setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        mViewer.getNode().setOnDragDropped(aoSoltarMouseSobreVertice);
        
        //Panel de Proriedades
        VBox blockPropriedade = new VBox(5);
        txtAction = new TextField();
        txtAction.setPromptText("Name/Action");
        txtAction.setOnKeyReleased((KeyEvent event) -> {
            Object obj = mComponentSelecionado;
            if(event.getCode() != KeyCode.ENTER) {
                if (obj instanceof TransitionMSCView) {
                    ((TransitionMSCView) obj).getTransition().setLabel(txtAction.getText());
                }
                if (!selecao.isEmpty()) {
                    Node node = selecao.iterator().next();
                    if (node instanceof HmscView) {
                        ((HmscView) node).getHMSC().setLabel(txtAction.getText());
                        // Se O bMSC sempre tiver que ter o mesmo nome do hMSC
                        if(((HmscView) node).getHMSC().getmDiagramSequence() != null) {
                            ((HmscView) node).getHMSC().getmDiagramSequence().setName(txtAction.getText());
                        }
                        projectExplorerPluginDS.clear2();
                    }
                }
            }else{
                if (obj instanceof TransitionMSCView) {
                    txtAction.setText(((TransitionMSCView) obj).getTransition().getLabel());
                }
                if (!selecao.isEmpty()) {
                    Node node = selecao.iterator().next();
                    if (node instanceof HmscView) {
                        txtAction.setText(((HmscView) node).getHMSC().getLabel());
                        projectExplorerPluginDS.clear2();
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
            Object obj = mComponentSelecionado;
            if (obj instanceof TransitionMSCView) {
                try {
                    if(txtProbability.getText().equals("")){
                        ((TransitionMSCView) obj).getTransition().setProbability(null);
                        mViewer.getComponentBuildDS().setProb( mViewer.getComponentBuildDS().getProb() - 1 );
                    }else{
                        String auxValor = verify_Probability(txtProbability);
                        if(!auxValor.equals("")) {
                            mViewer.getComponentBuildDS().setProb(mViewer.getComponentBuildDS().getProb() + 1);

                            ((TransitionMSCView) obj).getTransition().setProbability(Double.parseDouble(auxValor));
                        }
                    }
                } catch (Exception e) {
                    //ignora
                }
            }
        });
        lblAction = new Label("Name / Action:");
        lblProbability = new Label("Probability:");

        blockPropriedade.getChildren().addAll(lblAction,txtAction,lblProbability,txtProbability);

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
        //propertyDropDown.setLayoutY(110);

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
        
        Button buildLTSFromHMSC = new Button("Build LTS",new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_build_LTS.png"))));
        buildLTSFromHMSC.setOnAction(buildLTSFromHMSCEvent);
        
        mInfoPanel.getChildren().addAll(utilidade,buildLTSFromHMSC);
        
    }

    private Stage createPropertyPanel(){
        List<Hmsc> HMSCs = getComponentBuildDS().getBlocos();

        Stage stage = new Stage();
        stage.setTitle("Property Panel");

        PropertysPanelController propertysPanelController = new PropertysPanelController(getComponentBuildDS());
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
            scene = new Scene(propertyPanelAnchorPane, 675, 470);

        }
        stage.setScene(scene);


     //   getChildren().add(propertyPanelAnchorPane);

        propertysPanelController.onCreatedView();


        return stage;

    }

    private void hidePropertyPanel() {

    }

   // private AnchorPane showPropertyPanel() {


//        getChildren().add(mInfoPanel);
//        AnchorPane.setLeftAnchor(mInfoPanel, 0D);
//        AnchorPane.setRightAnchor(mInfoPanel, 0D);
//        AnchorPane.setBottomAnchor(mInfoPanel, 0D);


//        PropertysPanelController equationsController = new PropertysPanelController();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/propertysPanel.fxml"));
//        fxmlLoader.setController(equationsController);
//        AnchorPane propertyPanelAnchorPane = null;
//        try {
//            propertyPanelAnchorPane = fxmlLoader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        getChildren().add(propertyPanelAnchorPane);
//
//        equationsController.onCreatedView();
//
//
//        return propertyPanelAnchorPane;


 //   }

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
         mViewer.getNode().setCursor(Cursor.DEFAULT);
         if (mModoAtual == MODO_MOVER) {
            mViewer.getNode().setCursor(Cursor.OPEN_HAND);
        }
    }

    public Object getComponentePelaPosicaoMouse(Point2D point) {
        Object b = mViewer.locateBlockBuildView(point);
        if(b == null){
            b = mViewer.locateTransitionBuildView(point);
        }
        return b;
    }

    public void setComponenteSelecionado(Object mComponentSobMouse) {
         if (mComponentSelecionado != null) {
            removeSelectedStyles(mComponentSelecionado);
        }
        mComponentSelecionado = mComponentSobMouse;
        if (mComponentSobMouse != null) {
            updatePropriedades(mComponentSobMouse);
            applySelectedStyles(mComponentSelecionado);
        }
    }

    private void applySelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof HmscView){
            Hmsc b = ((HmscView) mComponentSelecionado).getHMSC();
            b.setBorderWidth(3);
            b.setBorderColor("blue");
            b.setTextColor("blue");
            b.setTextStyle(Hmsc.mTextStyleBold);
        }
        else if(mComponentSelecionado instanceof TransitionMSCView){
            TransitionMSC t = ((TransitionMSCView)mComponentSelecionado).getTransition();
            t.setWidth(2);
            t.setColor("blue");
            t.setTextColor("blue");
            t.setTextStyle(TransitionMSC.TEXTSTYLE_BOLD);
        }
    }

    private void removeSelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof HmscView){
            Hmsc b = ((HmscView) mComponentSelecionado).getHMSC();
            b.setBorderWidth(1);
            b.setBorderColor("black");
            b.setTextColor("black");
            b.setTextStyle(Hmsc.mTextStyleNormal);
        }
        else if(mComponentSelecionado instanceof TransitionMSCView){
            TransitionMSC t = ((TransitionMSCView)mComponentSelecionado).getTransition();
            t.setWidth(1);
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
            txtAction.setText(t.getLabel());
            txtAction.requestFocus();
            txtProbability.setText(t.getProbability() == null ? null : String.valueOf(t.getProbability()));
            lblProbability.setVisible(true);
            txtProbability.setVisible(true);
            
        }
    }
    
    public void updateContID() {
        int aux = -1;
        for (Hmsc b : mViewer.getComponentBuildDS().getBlocos()) {
            if (b.getID() > aux) {
                aux = b.getID();
            }
        }
        contID = aux;
        contID++;
    }
    
    public void applyDefaults(TransitionMSC t){
        
    }

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
            HmscView bview = (HmscView)b;
            Hmsc bbds = bview.getHMSC();
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
        applySelectedStyles((Object)node);
        selecao.add(node);
        selecionadoPeloRetangulo = true;
        Hmsc hmsc = ((HmscView)node).getHMSC();
        txtAction.setText(hmsc.getLabel());
        txtAction.requestFocus();
        lblProbability.setVisible(false);
        txtProbability.setVisible(false);
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

        List<Component> createdComponentsWithIndividualLTS = tryBuildIndividualLTS();

        if(createdComponentsWithIndividualLTS == null){
            return;
        }

        projectExplorerPluginDS.removeFragmetsLTS();

        mViewer.getComponentBuildDS().createListLTS(createdComponentsWithIndividualLTS);

        List<Component> createdComponentsWithLifeLTS
                = tryBuilderLifeLTS(projectExplorerPluginDS.getSelectedComponentBuildDS(),
                createdComponentsWithIndividualLTS);

        if(createdComponentsWithLifeLTS == null){
            return;
        }

        mViewer.getComponentBuildDS().createListLTS(createdComponentsWithLifeLTS);

        for(Component component: createdComponentsWithLifeLTS){
            System.out.println("nome component:"+ component.getName());

                for(Hmsc hmsc : getComponentBuildDS().getBlocos()){
                    System.out.println(hmsc.getLabel()+": "+ component.getValue(hmsc.getLabel()));
                }

        }

        Component parallelComponent =  parallelComposition(createdComponentsWithLifeLTS);

        trySetPtobabilityFromTransitionMSC(parallelComponent, getComponentBuildDS().getTransitions());


        mViewer.getComponentBuildDS().createGeneralLTS(parallelComponent);




    };

    private void trySetPtobabilityFromTransitionMSC(Component parallelComponent, List<TransitionMSC> transitions) {
        try {
            ProbabilitySetter.setProbabilityFromTransitionMSCAndObjectActions(parallelComponent, transitions);
        } catch (Exception e) {

            Alert emptyAlert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            emptyAlert.show();
            e.printStackTrace();
        }
    }

    public Component parallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
        Component a = Components.get(0);
        Component b = Components.get(1);
        Component newComponent = new ParallelCompositor().compor(a, b);
        String name = a.getName() + " || " + b.getName();
        for(int i = 2; i < tam; i++){
            b = Components.get(i);
            newComponent = new ParallelCompositor().compor(newComponent, b);
            name += " || " + b.getName();
        }
        newComponent.setName(name);
        return newComponent;
    }




    private List<Component> tryBuildIndividualLTS() {
        try {
            return IndividualLTSBuilder.buildLTS(projectExplorerPluginDS);

        } catch (Exception e) {
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            emptyAlert.show();
            e.printStackTrace();

        }
        return null;
    }

    private List<Component> tryBuilderLifeLTS(StandardModeling selectedComponentBuildDS, List<Component> createdComponentsWithIndividualLTS) {
        try {
            return LifeLTSBuilder.builderLTS(projectExplorerPluginDS, createdComponentsWithIndividualLTS);
        }catch (Exception e){
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            emptyAlert.show();

            e.printStackTrace();
        }

        return null;

    }


    //    EventHandler<ActionEvent> buildLTSFromHMSCEvent = (ActionEvent event) -> {
//        //verificar se tem bloco com ds vazio
//        for(Node node : mViewer.getNode().getChildren()){
//            if(node instanceof HmscView){
//                HmscView b = (HmscView)node;
//                Hmsc block = b.getHMSC();
//                if(!block.isFull()){
//                    Alert alerta = new Alert(Alert.AlertType.WARNING, "Algum bloco esta vazio", ButtonType.OK);
//                    alerta.show();
//                    return;
//                }
//            }
//        }
//
//        //Limpar arvore do projeto
//        //projectExplorerPluginDS.removeFragmentsLTS();
//        projectExplorerPluginDS.removeFragmetsLTS();
//       // projectExplorerPluginDS.removeLtsComposed();
//
//        //Gerando o LTS Geral ----------------------------------------------------------------------------------------
//
//        Layouter layout = new Layouter();
//        List<Component> ltsGerados = new ArrayList<>();
//        List<ComponentDS> listaBMSC = projectExplorerPluginDS.getAll_BMSC();
//        int id_ltsfrag = 0;
//        for(ComponentDS atualComponenteDiagramaSequencia : listaBMSC){
//            id_ltsfrag++;
//            List<TabelaReferenciaID> relativo = new ArrayList<>();
//            ArrayList<BlockDS> objetos = (ArrayList<BlockDS>) atualComponenteDiagramaSequencia.getBlockDS();
//            for(int i=0;i<objetos.size();i++){
//                TabelaReferenciaID id = new TabelaReferenciaID(i+1, objetos.get(i).getID()+"");
//                relativo.add(id);
//            }
//            List<InteractionFragments> loopsOuAlts = new ArrayList<>();
//            List<Mensagem> comunicacao = new ArrayList<>();
//            for(TransitionMSC t : atualComponenteDiagramaSequencia.getAllTransitions()){
//                Mensagem m = new Mensagem();
//                if(t.getSource() instanceof BlockDSView) {
//                    m.setEnviando(new AtorAndClasse(((BlockDSView) t.getSource()).getBlockDS().getLabel(),
//                            String.valueOf(((BlockDSView) t.getSource()).getBlockDS().getID()),
//                            "actor"));
//                }else if(t.getSource() instanceof BlockDS){
//                    m.setEnviando(new AtorAndClasse(((BlockDS) t.getSource()).getLabel(),
//                            String.valueOf(((BlockDS)t.getSource()).getID()),
//                            "actor"));
//                }
//                if(t.getDestiny() instanceof BlockDSView) {
//                    m.setRecebendo(new AtorAndClasse(((BlockDSView) t.getDestiny()).getBlockDS().getLabel(),
//                            String.valueOf(((BlockDSView) t.getDestiny()).getBlockDS().getID()),
//                            "actor"));
//                }else if(t.getDestiny() instanceof BlockDS){
//                    m.setRecebendo(new AtorAndClasse(((BlockDS)t.getDestiny()).getLabel(),
//                            String.valueOf(((BlockDS)t.getDestiny()).getID()),
//                            "actor"));
//                }
//                m.setMsg(t.getLabel());
//                m.setXmiIdMsg(t.getIdSequence()+"");
//                comunicacao.add(m);
//            }
//            Component c = new Component();
//            c.setName("LTS "+atualComponenteDiagramaSequencia.getName());
//            c.setID((projectExplorerPluginDS.getSelectedProjectDS().getID() * 1000)+ 300 + id_ltsfrag);
//            LtsParser parser = new LtsParser(comunicacao, relativo, loopsOuAlts, c);
//            c = parser.parseLTSA();
//
//            Iterator<State> it = c.getStates().iterator();
//            State fi = it.next();
//            c.setInitialState(fi);
//            while (it.hasNext()){
//                fi = it.next();
//            //    fi.clearIniFin();
//            }
//            c.setFinalState(fi);
////            c.setInitialState(c.getStates().iterator().next());
//
//            layout.layout(c);
//            ltsGerados.add(c);
//        }
//        mViewer.getComponentBuildDS().createListLTS(ltsGerados);
//
//        try {
//            Component c = new Component();
//            List<Component> ltss = new ArrayList<>();
//            for(Component component : ltsGerados){
//                ltss.add(component.clone());
//            }
//            if (ltsGerados.size() >= 2) {
//                c = buildGeneralLTS(ltss);
//            } else {
//                c = ltsGerados.get(0).clone();
//            }
//            System.out.println("Nome da Composição é: "+ c.getName());
//            c.setID((projectExplorerPluginDS.getSelectedProjectDS().getID()*1000) + 100 + 1);
//
//            Iterator<State> it2 = c.getStates().iterator();
//            c.setInitialState(it2.next());
//            while(it2.hasNext()) {
//                System.out.println("Estado é: " +it2.next().getNameState());
//            }
//
//            layout(c);
//            mViewer.getComponentBuildDS().createGeneralLTS(c);
//        } catch (CloneNotSupportedException cloneNotSupportedException) {}
//    };
    public void layout(Component component) {
        int i = 1;
        for (State state : component.getStates()) {
            state.setLayoutX(i * 200);
            state.setLayoutY(300 + (i % 10));
            i++;
        }
    }
    private Component buildGeneralLTS(List<Component> ltsGerados){
        List<Hmsc> listHmsc = mViewer.getComponentBuildDS().getBlocos();
        GeneralLTSMaker make =
                new GeneralLTSMaker(listHmsc, projectExplorerPluginDS.getAll_BMSC(),ltsGerados, mViewer, projectExplorerPluginDS);
        return make.produce();
    }


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


        for(Node node : mViewer.getNode().getChildren()){
            if(node instanceof HmscView) {
                HmscView b = (HmscView) node;
                Hmsc block = b.getHMSC();
                blocks.add(block.getLabel());
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
}
