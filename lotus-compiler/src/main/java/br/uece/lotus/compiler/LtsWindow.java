package br.uece.lotus.compiler;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Yan
 */
public class LtsWindow extends AnchorPane implements Window, Initializable {

     @FXML
    private VBox mVBox;

    @FXML
    private ToolBar mToolbar;
    
    @FXML
    private ScrollPane mScrollPane;

    private Node mNode;

    private ComponentView mViewer;
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentViewImpl();
        System.out.println("\n============================================");

//      Component c = viewer.getComponent();
//        c.newState(1);
//        c.newState(2);
//        c.newTransitionMSC(1, 2);
//        c.buildTransitionMSC(1, 2);.newState(1);
//        c.newState
        
    }

    @Override
    public String getTitle() {
        return "[LTS]";
    }

    @Override
    public Node getNode() {
        return mNode;
    }

    public void setNode(Parent node) {
        this.mNode = node;
    }

    @Override
    public Component getComponent() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
    }

}
