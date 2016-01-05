/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.viewer.ComponentBuildDSView;
import br.uece.lotus.uml.api.viewer.ComponentBuildDSViewImpl;
import br.uece.lotus.uml.api.window.WindowDS;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;

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
    
    private ComponentBuildDSView mViewer;
    private Node mNode;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentBuildDSViewImpl();
        mScrollPanel.setContent((Node)mViewer);
        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());
    }
    
    @Override
    public ComponentBuildDS getComponentBuildDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComponentDS getComponentDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component getComponentLTS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setComponentBuildDS(ComponentBuildDS buildDS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setComponentDS(ComponentDS cds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setComponentLTS(Component c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node getNode() {
        return mNode;
    }
    
    public void setNode(Node node) {
        this.mNode = node;
    }
    
}
