/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.project.ProjectDSSerializer;
import br.uece.lotus.uml.api.project.ProjectDialogsDS;
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
    private ProjectDialogsDS mProjectDialogsHelper;
    private ObservableList<Tab> mTabsDoPainelDeProjetos;
    private ProjectDSSerializer mProjectSerializer = new ProjectDSxmlSerializer();
    private static final String EXTENSION_DESCRIPTION = "LoTuS-MSC files (*.xml)";
    private static final String EXTENSION = "*.xml";
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorerDS = extensionManager.get(ProjectExplorerDS.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsDS.class);
        mTabsDoPainelDeProjetos = mUserInterface.getLeftPanel().getTabs();
        
        ExtensibleMenu mMainMenu = mUserInterface.getMainMenu();
        
        mMainMenu.newItem("MSC/New Project...Lotus-MSC")
                .setWeight(Integer.MIN_VALUE)
                //.setAccelerator(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
                .setAction(mNewProject)
                .create();
        mMainMenu.newItem("MSC/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("MSC/Open...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mOpenProject)
                .create();
        mMainMenu.newItem("MSC/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("MSC/Close Project...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mCloseProject)
                .create();
        mMainMenu.newItem("MSC/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("MSC/Save Project...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mSaveProject)
                .create();
        
        mProjectExplorerDS.getMenu().addItem(Integer.MIN_VALUE, "New Project", mNewProject);
        mProjectExplorerDS.getMenu().addItem(Integer.MIN_VALUE, "Open Project", mOpenProject);
        mProjectExplorerDS.getProjectMSCMenu().addItem(Integer.MIN_VALUE, "Close Project", mCloseProject);
        mProjectExplorerDS.getProjectMSCMenu().addItem(Integer.MIN_VALUE, "Save Project", mSaveProject);
        mProjectExplorerDS.getProjectMSCMenu().addItem(Integer.MIN_VALUE, "Rename Project", mRenameProject);
        mProjectExplorerDS.getComponentBMSCMenu().addItem(Integer.MIN_VALUE, "Rename bMSC", mRenameBMSC);
        mProjectExplorerDS.getComponentBMSCMenu().addItem(Integer.MIN_VALUE, "Remove bMSC", mRemoveBMSC);
        
        //mProjectExplorerDS.getProjectMSCMenu().addItem(Integer.MIN_VALUE, "New Sequence Diagram", mNewComponentDS);
    }
    
    private Runnable mNewProject = () ->{
        ProjectDS p = new ProjectDS();
        String pName = "";
        String prompt = "Untitled"+ (mProjectExplorerDS.getAllProjectsDS().size()+1);
        
        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("New Project");
        d.setHeaderText("New Project to MSC");
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
        StandardModeling  cbds = new StandardModeling();
        cbds.setName("Standard Modeling" + "(" + p.getName() + ")");
        p.setComponentBuildDS(cbds);
        mProjectExplorerDS.open(p);
        abrirFocoNaTab("UML Projects");
    };
    
    private Runnable mRenameProject = () -> {
        System.out.println("falta implementar  Classe: BasicDSPlugin");
    };
    
    private Runnable mRenameBMSC = () -> {
        System.out.println("falta implementar  Classe: BasicDSPlugin");
    };
    
    private Runnable mRemoveBMSC = () -> {
        System.out.println("falta implementar  Classe: BasicDSPlugin");
    };
    
    private Runnable mCloseProject = () -> {
        System.out.println("falta implementar  Classe: BasicDSPlugin");
    };
    
    private Runnable mSaveProject = () -> {
        ProjectDS p = mProjectExplorerDS.getSelectedProjectDS();
        if (p == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information");
            alert.setContentText("Please select a project!");
            alert.show();
            return;
        }
        mProjectDialogsHelper.save(p, mProjectSerializer, "Save project", EXTENSION_DESCRIPTION, EXTENSION/*,false*/);
    };
    
    private Runnable mOpenProject = () -> {
        ProjectDS p = mProjectDialogsHelper.open(mProjectSerializer, "Open project", EXTENSION_DESCRIPTION, EXTENSION);
        if (p != null) {
            if(!checkExistenceName(p.getName())){
                mProjectExplorerDS.open(p);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Information");
                alert.setContentText("This Project already exists!");
                alert.show();
            }
        }
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
