/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.windowLTS;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.window.WindowMSC;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
import br.uece.lotus.msc.designer.windowLTS.parameterized_lts.InitializeAndRangeVariablesController;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsWindowImpl extends AnchorPane implements WindowMSC {

    private final ToolBar mToolbar;
    private final ToggleButton editVariableToogleButton;
    private final Stage actionsVariablesState = new Stage();
    private ComponentView mViewer;
    private final ScrollPane mScrollPanel;
    private MenuButton mBtnZoom;
    private CheckBox zoomReset;
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);
    private Scale escala = new Scale(1, 1);
    public double mViewerScaleXPadrao, mViewerScaleYPadrao;
    public double mViewerTranslateXPadrao, mViewerTranslateYPadrao;
    
    /////////////////////////////////////////////////////////////////////////
    //                   IMPLEMENTACAO DA WINDOW_DS                        //
    /////////////////////////////////////////////////////////////////////////

    @Override
    public HmscComponent getHmscComponent() {
        return null;
    }

    @Override
    public BmscComponent getBmscComponent() {
        return null;
    }

    @Override
    public Component getComponentLTS() {
        return mViewer.getComponent();
    }

    @Override
    public void setHmscComponent(HmscComponent hmscComponent) {}

    @Override
    public void setBmscComponent(BmscComponent bmscComponent) {}

    @Override
    public void setComponentLTS(Component componentLTS) {
        mViewer.setComponent(componentLTS);
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
    
    public LtsWindowImpl(){
        mViewer = new ComponentViewImpl();
        mScrollPanel = new ScrollPane((Node)mViewer);
        mViewer.getNode().setPrefSize(1200, 600);
        mToolbar = new ToolBar();
        mScrollPanel.viewportBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
            Node content = mScrollPanel.getContent();
            mScrollPanel.setFitToWidth(content.prefWidth(-1)<newValue.getWidth());
            mScrollPanel.setFitToHeight(content.prefHeight(-1)<newValue.getHeight());
        });

      //  actionsVariablesState = createInitializeActionsVariablesView();
        
        getChildren().add(mScrollPanel);
        AnchorPane.setTopAnchor(mScrollPanel, 0D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);

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

     //   getChildren().add(mBtnZoom);
        mViewer.getNode().getTransforms().add(escala);

        mViewerScaleXPadrao = mViewer.getNode().getScaleX();
        mViewerScaleYPadrao = mViewer.getNode().getScaleY();
        mViewerTranslateXPadrao = mViewer.getNode().getTranslateX();
        mViewerTranslateYPadrao = mViewer.getNode().getTranslateY();

        zoomReset.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (zoomReset.isSelected()) {
                zoomSlide.setValue(1);
                mViewer.getNode().setScaleX(mViewerScaleXPadrao);
                mViewer.getNode().setScaleY(mViewerScaleYPadrao);
                mViewer.getNode().setTranslateX(mViewerTranslateXPadrao);
                mViewer.getNode().setTranslateY(mViewerTranslateYPadrao);
            }
        });


        editVariableToogleButton = new ToggleButton();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/imagens/ic_operations.png")));
        imageView.setFitHeight(24);
        imageView.setFitWidth(25);
        editVariableToogleButton.setGraphic(imageView);
        editVariableToogleButton.setOnAction((ActionEvent event) -> {

            if(editVariableToogleButton.isSelected()){
                showActionsVariablesView();
            }else {
                hideActionsVariablesView();

            }
        });

        mToolbar.getItems().addAll(editVariableToogleButton, mBtnZoom , new Separator(Orientation.VERTICAL));
        mToolbar.setLayoutY(1);
        mToolbar.setOrientation(Orientation.HORIZONTAL);
        getChildren().add(mToolbar);
        Tooltip editVariableTooltip = new Tooltip("Initialize Actions Variables");
        Tooltip.install(editVariableToogleButton, editVariableTooltip);




    }




    protected Stage createInitializeActionsVariablesView(ProjectExplorerPluginMSC mProjectExplorerDS) {

        actionsVariablesState.setTitle(" Initialize Actions Variables Panel");
        actionsVariablesState.setOnCloseRequest(event -> editVariableToogleButton.setSelected(false));

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/InitializeAndRangeVariablesView.fxml"));

        InitializeAndRangeVariablesController initializeAndRangeVariablesController = new InitializeAndRangeVariablesController(mViewer.getComponent(), mProjectExplorerDS);

        fxmlLoader.setController(initializeAndRangeVariablesController);

        try {
            Scene scene = new Scene(fxmlLoader.load(), 350, 235);
            actionsVariablesState.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeAndRangeVariablesController.onCreatedView();


        return actionsVariablesState;
    }

    private void showActionsVariablesView() {
        actionsVariablesState.show();
    }

    private void hideActionsVariablesView() {
        actionsVariablesState.hide();
    }

    public Stage getActionsVariablesState() {
        return actionsVariablesState;
    }
}
