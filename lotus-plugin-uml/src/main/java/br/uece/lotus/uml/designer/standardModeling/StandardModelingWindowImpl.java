/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.uml.api.viewer.builder.ComponentBuildDSView;
import br.uece.lotus.uml.api.viewer.builder.TransitionBuildDSView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModelingWindowImpl {
    
    private ScrollPane mScrollPanel;
    private AnchorPane mPropriedadePanel;
    private AnchorPane mInfoPanel;
    private ToolBar mToolBar;

    private ComponentBuildDSView mViewer;
    private Node mNode;

    public StandardModelingWindowImpl(ScrollPane mScrollPanel, AnchorPane mPropriedadePanel, 
                                        AnchorPane mInfoPanel, ToolBar mToolBar, ComponentBuildDSView mViewer, 
                                        Node mNode) {
        this.mScrollPanel = mScrollPanel;
        this.mPropriedadePanel = mPropriedadePanel;
        this.mInfoPanel = mInfoPanel;
        this.mToolBar = mToolBar;
        this.mViewer = mViewer;
        this.mNode = mNode; 
    }

    public ComponentBuildDSView getmViewer() {
        return mViewer;
    }

    public Node getNode() {
        return mNode;
    }
    
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
    public static final int MODO_NENHUM = 0;
    public static final int MODO_BLOCO = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    public static final int MODO_MOVER = 4;
    private int mTransitionViewType;
    private int mModoAtual;
    //Mover e Zoom
    private CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);
    //Selecao e Destaque
    
    //Cores
    private HBox paleta;
    
    ////////////////////////////////////////////////////////////////////
    // INICIANDO COMPONENTES DA INTERFACE
    ////////////////////////////////////////////////////////////////////
    public void start(){
        mToggleGroup = new ToggleGroup();
        
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent event) -> {
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
       
        mBtnBlock = new ToggleButton();
        //mBtnBlock.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/"))));
        mBtnBlock.setOnAction((ActionEvent event) -> {
            setModo(MODO_BLOCO);
        });
        mBtnBlock.setToggleGroup(mToggleGroup);
        
        mBtnTransitionLine = new ToggleButton();
        mBtnTransitionLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_line.png"))));
        mBtnTransitionLine.setOnAction((ActionEvent event) -> {
            setModo(MODO_TRANSICAO);
            mTransitionViewType = TransitionBuildDSView.Geometry.LINE;
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);
        
        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent event) -> {
            setModo(MODO_TRANSICAO);
            mTransitionViewType = TransitionBuildDSView.Geometry.CURVE;
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);
        
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent event) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);
        
        mBtnHand = new ToggleButton();
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
            //exclui requestLayout para ver se funciona
            if (zoomFactor.getValue() != 1)
                zoomReset.setSelected(false);
        });
        mBtnZoom.getItems().add(zoomHBox);
        
        //ToolTips
        Tooltip arrowInfo = new Tooltip("Selection");
        Tooltip blockinfo = new Tooltip("Block");
        Tooltip lineTransitionInfo = new Tooltip("Straight Transition");
        Tooltip arcTransitionInfo = new Tooltip("Curved Transition");
        Tooltip eraserInfo = new Tooltip("Eraser");
        Tooltip handInfo = new Tooltip("Move");
        Tooltip zoomInfo = new Tooltip("Ctrl + MouseScroll ↑\nCtrl + MouseScroll ↓\nCtrl + Mouse Button Middle");
        Tooltip.install(mBtnArrow, arrowInfo);
        Tooltip.install(mBtnBlock, blockinfo);
        Tooltip.install(mBtnTransitionLine, lineTransitionInfo);
        Tooltip.install(mBtnTransitionArc, arcTransitionInfo);
        Tooltip.install(mBtnEraser, eraserInfo);
        Tooltip.install(mBtnHand, handInfo);
        Tooltip.install(mBtnZoom, zoomInfo);
        
        mToolBar.getItems().addAll(mBtnArrow,mBtnBlock,mBtnTransitionLine,mBtnTransitionArc,mBtnEraser,mBtnHand,mBtnZoom);
    }

    private void setModo(int modo) {
        this.mModoAtual = modo;
         mViewer.getNode().setCursor(Cursor.DEFAULT);
         if (mModoAtual == MODO_MOVER) {
            mViewer.getNode().setCursor(Cursor.OPEN_HAND);
        }
    }
    
    
    
}
