package br.uece.lotus.uml.designer.blockDiagramModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.api.viewer.bMSC.ComponentDSView;
import br.uece.lotus.uml.api.viewer.bMSC.ComponentDSViewImpl;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.api.window.WindowDS;
import br.uece.lotus.uml.designer.blockDiagramModeling.strategy.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DesingWindowImplBlockDs extends AnchorPane implements WindowDS {
    //Principais da Tela
    private final ScrollPane mScrollPanel;
    private final AnchorPane mPropriedadePanel;
    private final HBox mInfoPanel;
    public final ToolBar mToolBar;
    public ComponentDSView mViewer;
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
    public ContextMenu mContextMenuBlockDs;
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
    public HBox paleta;
    //Variaveis gerais
    public Set<Node> selecao = new HashSet<>();
    public double dragContextMouseAnchorX, dragContextMouseAnchorY;
    public Rectangle rectSelecao;
    public boolean segundaVezAoArrastar;
    public double ultimoInstanteX, ultimoInstanteY;
    public boolean selecionadoPeloRetangulo;

    @Override
    public StandardModeling getComponentBuildDS() {
        return null;
    }

    @Override
    public ComponentDS getComponentDS() {
        return mViewer.getmComponentDS();
    }

    @Override
    public Component getComponentLTS() {
        return null;
    }

    @Override
    public void setComponentBuildDS(StandardModeling buildDS) {}

    @Override
    public void setComponentDS(ComponentDS cds) {
        mViewer.setComponentDS(cds);
    }

    @Override
    public void setComponentLTS(Component c) {

    }

    @Override
    public String getTitle() {
        return mViewer.getmComponentDS().getName();
    }

    @Override
    public Node getNode() {
        return this;
    }


    public DesingWindowImplBlockDs (){
        mViewer = new ComponentDSViewImpl();
        mToolBar = new ToolBar();
        mScrollPanel = new ScrollPane((Node)mViewer);
        mPropriedadePanel = new AnchorPane();
        mInfoPanel = new HBox(20);

        mViewer.getNode().setPrefSize(1200,600);
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
        AnchorPane.setRightAnchor(mScrollPanel, 175D);
        AnchorPane.setBottomAnchor(mScrollPanel, 30D);

        //propriedades
        getChildren().add(mPropriedadePanel);
        AnchorPane.setTopAnchor(mPropriedadePanel, 44D);
        AnchorPane.setRightAnchor(mPropriedadePanel, 0D);
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
        mContextMenuBlockDs = new ContextMenu();
        mToggleGroup = new ToggleGroup();

        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent)->{
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnArrow.setSelected(true);

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

        mToolBar.getItems().addAll(mBtnArrow,mBtnBlock,mBtnTransitionLine,mBtnTransitionArc,mBtnEraser,mBtnHand,mBtnZoom);


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
            fileChooser.setInitialFileName(mViewer.getmComponentDS().getName() + ".png");
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
        mContextMenuBlockDs.getItems().add(mSaveAsPNG);
        mViewer.setBlockDSContextMenu(mContextMenuBlockDs);

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
    public Object getComponentePelaPosicaoMouse(/*Point2D point*/Circle c) {
        Object b = mViewer.locateBlockDSView(c);
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

    private void updatePropriedades(Object mComponentSobMouse) {
    }

    public void updateContID() {
        int aux = -1;
        for (BlockDS b : mViewer.getmComponentDS().getBlockDS()) {
            if (b.getID() > aux) {
                aux = b.getID();
            }
        }
        contID = aux;
        contID++;
    }

    private void applySelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof BlockDSView){
            BlockDS b = ((BlockDSView) mComponentSelecionado).getBlockDS();
            b.setBorderWidth(3);
            b.setBorderColor("blue");
            b.setTextColor("blue");
            b.setTextStyle(BlockDS.mTextStyleBold);
        }
        else if(mComponentSelecionado instanceof TransitionMSCView){
            ///FAZER AINDA!!!
        }
    }

    private void removeSelectedStyles(Object mComponentSelecionado) {
        if(mComponentSelecionado instanceof BlockDSView){
            BlockDS b = ((BlockDSView) mComponentSelecionado).getBlockDS();
            b.setBorderWidth(1);
            b.setBorderColor("black");
            b.setTextColor("black");
            b.setTextStyle(BlockDS.mTextStyleNormal);
        }
        else if(mComponentSelecionado instanceof TransitionMSCView){

        }
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
            BlockDSView bDsV = (BlockDSView)b;
            BlockDS bDs = bDsV.getBlockDS();
            if(tipo.equals("Default")){
                bDs.setColor(null);
            }
            if(tipo.equals("MultiSelecao")){
                bDs.setColor(hexCor);
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////
    //                         Metodos de Selecao                                    //
    //////////////////////////////////////////////////////////////////////////////////
    public void addNoSelecao(Node node){
        applySelectedStyles((Object)node);
        selecao.add(node);
        selecionadoPeloRetangulo = true;
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





}