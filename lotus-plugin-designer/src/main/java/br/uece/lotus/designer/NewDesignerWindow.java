package br.uece.lotus.designer;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.properties.PropertiesEditorController;
import br.uece.lotus.viewer.ComponentViewImpl;
import br.uece.seed.app.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by emerson on 16/04/15.
 */
public class NewDesignerWindow extends View implements Window {

    @FXML
    private AnchorPane mPropertiesContainer;
    @FXML
    private ComponentViewImpl mComponentView;
    @FXML
    private ScrollPane mComponentViewWrapper;


    private PropertiesEditorController mPropertiesPane;

    public NewDesignerWindow() throws IOException {
        super("/fxml/designer.fxml");

        mComponentView.minHeightProperty().bind(mComponentViewWrapper.heightProperty());
        mComponentView.minWidthProperty().bind(mComponentViewWrapper.widthProperty());

        mPropertiesPane = new PropertiesEditorController();
        Node propertiesNode = mPropertiesPane.getParent();
        AnchorPane.setTopAnchor(propertiesNode, 0D);
        AnchorPane.setLeftAnchor(propertiesNode, 0D);
        AnchorPane.setBottomAnchor(propertiesNode, 0D);
        AnchorPane.setRightAnchor(propertiesNode, 0D);
        mPropertiesContainer.getChildren().add(propertiesNode);
    }

    @Override
    public Component getComponent() {
        return mComponentView.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        mComponentView.setComponent(component);
    }

    @Override
    public String getTitle() {
        Component c = getComponent();
        return c == null ? "" : getComponent().getName();
    }

    @Override
    public Node getNode() {
        return getParent();
    }
}
