/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.annotator;

import br.uece.lotus.project.ProjectExplorer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emerson
 */
public class AnnotatorManagerWindowController implements Initializable {
    
    @FXML
    private ListView<AnnotatorProfile> mLstProfile;
    private AnnotatorManager mAnnotatorManager;
    private ProjectExplorer mProjectExplorer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mAnnotatorManager = AnnotatorManager.getInstance();
        mAnnotatorManager.addListener((AnnotatorManager manager) -> {
            mLstProfile.getItems().clear();
            mLstProfile.getItems().addAll(manager.getProfiles());
        });
    }

    @FXML
    public void handleNew() {
        try {
            URL location = getClass().getResource("/fxml/AnnotatorProfileWindow.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());   
            AnnotatorProfileWindowController c = loader.getController();
            c.setProjectExplorer(mProjectExplorer);
            Stage ss = new Stage(StageStyle.UTILITY);
            ss.setScene(new Scene(root));
            ss.setTitle("New annotation profile");
            ss.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleRemove() {
        AnnotatorProfile profile = mLstProfile.getSelectionModel().getSelectedItem();
        mAnnotatorManager.remove(profile);
    }

    void setProjectExplorer(ProjectExplorer projectExplorer) {
        mProjectExplorer = projectExplorer;
    }
    
    @FXML
    public void handleStart() {
        mAnnotatorManager.startAll();
    }

}
