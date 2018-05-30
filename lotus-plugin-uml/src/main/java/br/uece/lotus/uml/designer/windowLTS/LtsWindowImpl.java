/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.windowLTS;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.window.WindowDS;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsWindowImpl extends AnchorPane implements WindowDS{
    
    private ComponentView mViewer;
    private final ScrollPane mScrollPanel;
    
    /////////////////////////////////////////////////////////////////////////
    //                   IMPLEMENTACAO DA WINDOW_DS                        //
    /////////////////////////////////////////////////////////////////////////

    @Override
    public StandardModeling getComponentBuildDS() {
        return null;
    }

    @Override
    public ComponentDS getComponentDS() {
        return null;
    }

    @Override
    public Component getComponentLTS() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponentBuildDS(StandardModeling buildDS) {}

    @Override
    public void setComponentDS(ComponentDS cds) {}

    @Override
    public void setComponentLTS(Component c) {
        mViewer.setComponent(c);
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
        mScrollPanel.viewportBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
            Node content = mScrollPanel.getContent();
            mScrollPanel.setFitToWidth(content.prefWidth(-1)<newValue.getWidth());
            mScrollPanel.setFitToHeight(content.prefHeight(-1)<newValue.getHeight());
        });
        
        getChildren().add(mScrollPanel);
        AnchorPane.setTopAnchor(mScrollPanel, 0D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);
    }
    
}
