package br.uece.seed.app;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by emerson on 16/04/15.
 */
public abstract class View implements Initializable {

    private Parent mParent;

    public View(String resource) throws IOException {
        load(resource);
    }

    private void load(String resource) throws IOException {
        URL location = getClass().getResource(resource);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(this);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        mParent = (Parent) fxmlLoader.load(location.openStream());
    }

    public Parent getParent() {
        return mParent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
