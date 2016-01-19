package br.uece.lotus.uml.designer.standardModeling.blockDiagramModeling;

import br.uece.lotus.uml.api.viewer.block.ComponentDSView;
import br.uece.lotus.uml.api.viewer.block.ComponentDSViewImpl;
import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.window.WindowDS;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by lva on 15/01/16.
 */
public class DesingWindowImplBlockDs extends AnchorPane implements WindowDS,Initializable {
    @FXML
    private ScrollPane mScrollPanel;

    @FXML
    private AnchorPane mPropriedadePanel;

    @FXML
    private AnchorPane mInfoPanel;

    @FXML
    private ToolBar mToolBar;

    private ComponentDSView mViewer;
    private Node mNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer= new ComponentDSViewImpl();
        mScrollPanel.setContent((Node)mViewer);
        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());
    }

    @Override
    public ComponentBuildDS getComponentBuildDS() {
        return null;
    }

    @Override
    public ComponentDS getComponentDS() {
        return null;
    }

    @Override
    public Component getComponentLTS() {
        return null;
    }

    @Override
    public void setComponentBuildDS(ComponentBuildDS buildDS) {

    }

    @Override
    public void setComponentDS(ComponentDS cds) {
        mViewer.setComponentDS(cds);
    }

    @Override
    public void setComponentLTS(Component c) {

    }

    @Override
    public String getTitle() {
        ComponentDS ds = mViewer.getmComponentDS();
              return ds.getName();
    }

    @Override
    public Node getNode() {
        return mNode;
    }
    public void setNode(Node node) {
        this.mNode = node;
    }
    /*ESSA CLASSE É EQUIVALENTE A STANDARDMODELINGWINDOW(BRUNO) QUE É EQUIVALENTE AO DESINGWINDOWIMPL(EMERSON)
    QUE FAZ A PARTE GRÁFICA DO EDITOR*/

}
