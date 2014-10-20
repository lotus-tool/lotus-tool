/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.annotator;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectExplorer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emerson
 */
public class AnnotatorProfileWindowController implements Initializable {    
    @FXML
    private TextField mEdtSource;
    @FXML
    private ChoiceBox<String> mCbxTarget;    
    private Map<String, Component> mTargets = new HashMap<>();        
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {                
        
    }

    @FXML
    public void handleSave() {        
        AnnotatorProfile p = new AnnotatorProfile();
        p.setSource(mEdtSource.getText());
        String targetString = mCbxTarget.getSelectionModel().getSelectedItem();
        p.setTarget(targetString);
        p.setTargetComponent(mTargets.get(targetString));
        p.setStatus("STOPPED");
        AnnotatorManager.getInstance().add(p);
        close();                
    }
    
    @FXML
    public void handleCancel() {
        close();
    }

    private void close() {
        ((Stage) mEdtSource.getScene().getWindow()).close();
    }

    void setProjectExplorer(ProjectExplorer projectExplorer) {        
        mTargets.clear();
        for (Project p: projectExplorer.getAllProjects()) {
            for (Component c: p.getComponents()) {
                mTargets.put(p.getName() + "." + c.getName(), c);
            }
        }
        mCbxTarget.getItems().clear();
        mCbxTarget.getItems().addAll(mTargets.keySet());
    }

}
