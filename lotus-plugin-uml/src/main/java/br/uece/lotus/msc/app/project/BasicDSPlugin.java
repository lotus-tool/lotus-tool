/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.app.project;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.api.project.ProjectMSCSerializer;
import br.uece.lotus.msc.api.project.ProjectDialogsMSC;
import br.uece.lotus.msc.api.project.ProjectExplorerDS;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * @author Bruno Barbosa
 */
public class BasicDSPlugin extends Plugin {

    private UserInterface mUserInterface;
    private ProjectExplorerDS mProjectExplorerDS;
    private ProjectDialogsMSC mProjectDialogsHelper;
    private ObservableList<Tab> mTabsDoPainelDeProjetos;
    private ProjectMSCSerializer mProjectSerializer = new ProjectMSCxmlSerializer();
    private static final String EXTENSION_DESCRIPTION = "LoTuS-MSC files (*.xml)";
    private static final String EXTENSION = "*.xml";

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorerDS = extensionManager.get(ProjectExplorerDS.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsMSC.class);
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

    private Runnable mNewProject = () -> {
        ProjectMSC p = new ProjectMSC();
        String pName = "";
        String prompt = "Untitled" + (mProjectExplorerDS.getAllProjectsDS().size() + 1);

        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("New Project");
        d.setHeaderText("New Project to MSC");
        d.setContentText("Enter the new project'run name:");
        Optional<String> resul = d.showAndWait();
        if (resul.isPresent()) {
            pName = resul.get();
        } else {
            return;
        }

        if (pName.equals("")) {
            pName = prompt;
        }
        if (checkExistenceName(pName)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Existing Project", ButtonType.OK);
            alert.show();
            return;
        } else {
            p.setName(pName);
            HmscComponent cbds = new HmscComponent();
            cbds.setName("Standard Modeling" + "(" + p.getName() + ")");
            p.setComponentBuildDS(cbds);
            mProjectExplorerDS.open(p);
            abrirFocoNaTab("UML Projects");
        }

    };

    private Runnable mRenameProject = () -> {
        ProjectMSC p = mProjectExplorerDS.getSelectedProjectDS();
        String pName = "";
        String prompt = p.getName();

        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("Rename Project");
        d.setHeaderText("New Name to Project");
        d.setContentText("Enter the new project'run name:");
        Optional<String> resul = d.showAndWait();
        if (resul.isPresent()) {
            pName = resul.get();
        } else {
            return;
        }

        if (pName.equals("")) {
            pName = prompt;
        }
        if (checkExistenceName(pName)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Existing Project", ButtonType.OK);
            alert.show();
            return;
        } else {
            p.setName(pName);
            mProjectExplorerDS.rename(p);
            abrirFocoNaTab("UML Projects");
        }
    };

    private Runnable mRenameBMSC = () -> {
       BmscComponent bmscComponent = mProjectExplorerDS.getSelectedBMSC();
        String pName = "";
        String prompt = bmscComponent.getName();

        TextInputDialog d = new TextInputDialog(prompt);
        d.setTitle("Rename bMSC");
        d.setHeaderText("New Name to bMSC");
        d.setContentText("Enter the new bMSC'run name:");
        Optional<String> resul = d.showAndWait();
        if (resul.isPresent()) {
            pName = resul.get();
        } else {
            return;
        }

        if (pName.equals("")) {
            pName = prompt;
        }
        if (checkExistenceName(pName)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Existing Project", ButtonType.OK);
            alert.show();
            return;
        } else {
            for(HmscBlock h : mProjectExplorerDS.getSelectedProjectDS().getStandardModeling().getHmscBlockList()){
                if(h.getBmscComponet() == bmscComponent){
                    h.setLabel(pName);
                }
            }
            bmscComponent.setName(pName);
            mProjectExplorerDS.clear2();

        }

    };

    private Runnable mRemoveBMSC = () -> {
        BmscComponent bMSC = mProjectExplorerDS.getSelectedBMSC();
        if(bMSC == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a bMSC", ButtonType.OK);
            alert.show();
            return;
        }
        mProjectExplorerDS.removeBMSC(bMSC);
    };

    private Runnable mCloseProject = () -> {
        ProjectMSC p = mProjectExplorerDS.getSelectedProjectDS();
        mProjectExplorerDS.close(p);
    };

    private Runnable mSaveProject = () -> {
        ProjectMSC p = mProjectExplorerDS.getSelectedProjectDS();
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
        ProjectMSC p = mProjectDialogsHelper.open(mProjectSerializer, "Open project", EXTENSION_DESCRIPTION, EXTENSION);
        if (p != null) {
            if (!checkExistenceName(p.getName())) {
                mProjectExplorerDS.open(p);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Information");
                alert.setContentText("This Project already exists!");
                alert.show();
            }
        }
    };

    private boolean checkExistenceName(String name) {
        for (ProjectMSC p : mProjectExplorerDS.getAllProjectsDS()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void abrirFocoNaTab(String name) {
        for (Tab tab : mTabsDoPainelDeProjetos) {
            if (tab.getText().equals(name)) {
                tab.getTabPane().getSelectionModel().select(tab);
            }
        }
    }
}
