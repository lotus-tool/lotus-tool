/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.project.ProjectExplorerDS;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author Bruno Barbosa
 */
public class BasicDSPlugin extends Plugin{
    
    private UserInterface mUserInterface;
    private ProjectExplorerDS mProjectExplorerDS;
    private ObservableList<Tab> mTabsDoPainelDeProjetos;
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorerDS = extensionManager.get(ProjectExplorerDS.class);
        mTabsDoPainelDeProjetos = mUserInterface.getLeftPanel().getTabs();
        
        ExtensibleMenu mMainMenu = mUserInterface.getMainMenu();
        
        mMainMenu.newItem("File/New Project.../Sequence Diagram")
                .setWeight(Integer.MIN_VALUE)
                //.setAccelerator(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
                .setAction(mNewProject)
                .create();
        
        mProjectExplorerDS.getMenu().addItem(Integer.MIN_VALUE, "New Project", mNewProject);
        
        mProjectExplorerDS.getProjectDSMenu().addItem(Integer.MIN_VALUE, "New Sequence Diagram", mNewComponentDS);
    }
    
    private Runnable mNewProject = () ->{
        ProjectDS p = new ProjectDS();
        String pName = "";
        String prompt = "Untitled"+ (mProjectExplorerDS.getAllProjectsDS().size()+1);
        
        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("New Project");
        d.setHeaderText("New Project to Sequence Diagram");
        d.setContentText("Enter the new project's name:");
        Optional<String> resul = d.showAndWait();
        if(resul.isPresent()){
            pName = resul.get();
        }
        
        if(pName.equals("")){
            pName = prompt;
        }
        if(checkExistenceName(pName)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Existing Project", ButtonType.OK);
            alert.show();
        }
        
        p.setName(pName);
        ComponentBuildDS  cbds= new ComponentBuildDS();
        cbds.setName("Standard Modeling");
        p.setComponentBuildDS(cbds);
        mProjectExplorerDS.open(p);
        abrirFocoNaTab("UML Projects");
    };
    
    private Runnable mNewComponentDS = () -> {
        ProjectDS p = mProjectExplorerDS.getSelectedProjectDS();
        if(p == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a project!", ButtonType.OK);
            alert.show();
            return;
        }
        String mName = "";
        String prompt = "SequenceDiagram"+ (p.getComponentDSCount()+1);
        
        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("New Component Sequence Diagram");
        d.setHeaderText("New Sequence Diagram");
        d.setContentText("Enter the new diagram name:");
        Optional<String> resul = d.showAndWait();
        if(resul.isPresent()){
            mName = resul.get();
        }
        if(mName.equals("")){
            mName = prompt;
        }
        ComponentDS cds = new ComponentDS();
        cds.setName(mName);
        p.addComponentDS(cds);

    };
    
    private boolean checkExistenceName(String name) {
        for(ProjectDS p : mProjectExplorerDS.getAllProjectsDS()){
            if(p.getName().equals(name)){
                return true;
            }

        }
        return false;
    }
    
    private void abrirFocoNaTab(String name){
        for(Tab tab : mTabsDoPainelDeProjetos){
            if(tab.getText().equals(name)){
                tab.getTabPane().getSelectionModel().select(tab);
            }
        }
    }
}
