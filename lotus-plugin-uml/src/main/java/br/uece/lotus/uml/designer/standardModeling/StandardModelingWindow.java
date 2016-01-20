/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.viewer.builder.BlockBuildDSView;
import br.uece.lotus.uml.api.viewer.builder.ComponentBuildDSView;
import br.uece.lotus.uml.api.viewer.builder.ComponentBuildDSViewImpl;
import br.uece.lotus.uml.api.viewer.builder.TransitionBuildDSView;
import br.uece.lotus.uml.api.window.WindowDS;
import br.uece.lotus.uml.designer.standardModeling.strategy.Context;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnClickedMouse;
import br.uece.lotus.uml.designer.standardModeling.strategy.OnMovedMouse;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModelingWindow extends AnchorPane implements WindowDS, Initializable{

    @FXML
    private ScrollPane mScrollPanel;
    
    @FXML
    private AnchorPane mPropriedadePanel;

    @FXML
    private AnchorPane mInfoPanel;

    @FXML
    private ToolBar mToolBar;
    
    public ComponentBuildDSView mViewer;
    private Node mNode;
    
    
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
    private int mTransitionViewType;
    public int mModoAtual;
    protected ContextMenu mContextMenuBlockBuild;
    //Mover e Zoom
    private CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);
    public double mViewerScaleXPadrao, mViewerScaleYPadrao;
    public double mViewerTranslateXPadrao, mViewerTranslateYPadrao;
    //Selecao e Destaque
    public Object mComponentSobMouse;
    public Object mComponentSelecionado;
    //Cores
    private HBox paleta;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentBuildDSViewImpl();
        mScrollPanel.setContent((Node)mViewer);
        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());
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
        //mBtnArrow.setSelected(true);
        
        mBtnBlock = new ToggleButton("Bloco");
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
            requestLayout();
            if (zoomFactor.getValue() != 1)
                zoomReset.setSelected(false);
        });
        mBtnZoom.getItems().add(zoomHBox);
        
        mToolBar.getItems().addAll(mBtnTransitionLine,mBtnTransitionArc,mBtnEraser,mBtnHand,mBtnZoom,mBtnArrow,mBtnBlock);
        
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
        mContextMenuBlockBuild.getItems().add(mSaveAsPNG);
        mViewer.setBlockBuildContextMenu(mContextMenuBlockBuild);
        
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
        
        //Funcoes da tela
        mViewer.getNode().setOnMouseMoved(onMovedMouse);
        mViewer.getNode().setOnMouseClicked(onMouseClicked);
    }
    
    // Ao Mover o mouse----------------------------------------------------------------------
    private final EventHandler<? super MouseEvent> onMovedMouse = (MouseEvent event) -> {
        //Context context = new Context(new OnMovedMouse());
        //context.executeStrategyOnMovedMouse(event);
        Object aux = getComponentePelaPosicaoMouse(new Point2D(event.getSceneX(), event.getSceneY()));
        mComponentSobMouse = aux;
    };
    // Ao Clicar o mouse(clickar e soltar)---------------------------------------------------
    private final EventHandler<? super MouseEvent> onMouseClicked = (MouseEvent e) -> {
        //Context context = new Context(new OnClickedMouse());
        //context.executeStrategyOnClikedMouse(event);
        //mostrando menu dos blocos
        
        if (MouseButton.SECONDARY.equals(e.getButton())) {
            setComponenteSelecionado(mComponentSobMouse);
            
            if (mComponentSelecionado instanceof BlockBuildDSView) {
                mContextMenuBlockBuild.show(mViewer.getNode(), e.getScreenX(), e.getScreenY());
            } else {
                mContextMenuBlockBuild.hide();
            }
            return;
        }else{
           mContextMenuBlockBuild.hide();
        }
        //resetando zoom pelo mouse
        if (e.isControlDown() && e.getButton() == MouseButton.MIDDLE) {

            mViewer.getNode().setScaleX(mViewerScaleXPadrao);
            mViewer.getNode().setScaleY(mViewerScaleYPadrao);

            mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
            mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
        }
        //verificando por controles de butoes
        if(mModoAtual == MODO_NENHUM){
            
        }
        else if(mModoAtual == MODO_BLOCO){
            if (!(mComponentSobMouse instanceof BlockBuildDSView)) {
                    if (contID == -1) {
                        updateContID();
                    }
                    int id = mViewer.getComponentBuildDS().getBlocos().size();
                    BlockBuildDS b = mViewer.getComponentBuildDS().newBlock(id);
                    b.setID(contID);
                    contID++;
                    b.setLayoutX(e.getX());
                    b.setLayoutY(e.getY());
                    b.setLabel("New Block");
                }
        }
        else if(mModoAtual == MODO_REMOVER){
            
        }
          
    };

    private void setModo(int modo) {
        this.mModoAtual = modo;
         mViewer.getNode().setCursor(Cursor.DEFAULT);
         if (mModoAtual == MODO_MOVER) {
            mViewer.getNode().setCursor(Cursor.OPEN_HAND);
        }
    }

    public Object getComponentePelaPosicaoMouse(Point2D point2D) {
        Object b = mViewer.locateBlockBuildView(point2D);
        if(b == null){
            //b = mViewer.locateTransitionBuildView(point2D);
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
        if(mComponentSelecionado instanceof BlockBuildDSView){
            BlockBuildDS b = ((BlockBuildDSView) mComponentSelecionado).getBlockBuildDS();
            b.setBorderWidth(15);
            b.setBorderColor("blue");
            b.setTextColor("blue");
            b.setTextStyle(BlockBuildDS.mTextStyleBold);
        }
        else if(mComponentSelecionado instanceof TransitionBuildDSView){
            
        }
    }

    private void removeSelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof BlockBuildDSView){
            BlockBuildDS b = ((BlockBuildDSView) mComponentSelecionado).getBlockBuildDS();
            b.setBorderWidth(10);
            b.setBorderColor("black");
            b.setTextColor("black");
            b.setTextStyle(BlockBuildDS.mTextStyleNormal);
        }
        else if(mComponentSelecionado instanceof TransitionBuildDSView){
            
        }
    }

    private void updatePropriedades(Object mComponentSobMouse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void updateContID() {
        int aux = -1;
        for (BlockBuildDS b : mViewer.getComponentBuildDS().getBlocos()) {
            if (b.getID() > aux) {
                aux = b.getID();
            }
        }
        contID = aux;
        contID++;
    }
    
    /////////////////////////////////////////////////////////////////////////
    //                   IMPLEMENTACAO DA WINDOW_DS                        //
    /////////////////////////////////////////////////////////////////////////
    @Override
    public ComponentBuildDS getComponentBuildDS() {
        return mViewer.getComponentBuildDS();
    }

    @Override
    public ComponentDS getComponentDS() {return null;}

    @Override
    public Component getComponentLTS() {return null;}

    @Override
    public void setComponentBuildDS(ComponentBuildDS buildDS) {
        mViewer.setComponentBuildDS(buildDS);
    }

    @Override
    public void setComponentDS(ComponentDS cds) {}

    @Override
    public void setComponentLTS(Component c) {}

    @Override
    public String getTitle() {
        ComponentBuildDS c = mViewer.getComponentBuildDS();
        return c.getName();
    }

    @Override
    public Node getNode() {
        return mNode;
    }
    
    public void setNode(Node node) {
        this.mNode = node;
    }
    
}
