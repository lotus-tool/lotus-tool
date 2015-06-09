package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import br.uece.seed.app.UserInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by emerson on 29/03/15.
 */
public class RunnerWindow implements Window, Initializable {

    private Parent mNode;
    private Component mComponent;
    private ComponentView mViewer;

    @FXML
    private ScrollPane mScrollPanel;
    @FXML
    private TableView<Symbol> mSymbolTable;
    @FXML
    private TableColumn<String, Symbol> mSymbolNameCol;
    @FXML
    private TableColumn<String, Symbol> mSymbolValueCol;

    @Override
    public Component getComponent() {
        return mComponent;
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
        mComponent = component;
    }

    @Override
    public String getTitle() {
        return mComponent.getName() + " - [Running]";
    }

    public Node getNode() {
        return mNode;
    }

    public void setNode(Parent root) {
        mNode = root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentViewImpl();
        mScrollPanel.setContent((Node) mViewer);
        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());
    }
}
