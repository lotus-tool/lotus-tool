package br.uece.lotus.tools;

import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.project.ProjectSerializer;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author emerson
 */
public class ToolsPlugin extends Plugin {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private ProjectDialogsHelper mProjectDialogsHelper;

    private ExtensibleMenu mMainMenu;
    private static final int MENU_WEIGHT = 123;
    private ProjectSerializer mLTSASerializer = new LTSASerializer();
    private ProjectSerializer mPrismSerializer = new PrismSerializer();
    
    private final Runnable mExportToLTSA = new Runnable() {
        @Override
        public void run() {
            Project p = mProjectExplorer.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "There is no project avaliable!");
                return;
            }
            mProjectDialogsHelper.saveCopy(p, mLTSASerializer, "Export project to LTSA", "LTSA model (*.lts)", "*.lts");
        }
    };
    private final Runnable mExportToPrism = new Runnable() {        
        @Override
        public void run() {
            Project p = mProjectExplorer.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "There is no project avaliable!");
                return;
            }
            mProjectDialogsHelper.saveCopy(p, mPrismSerializer, "Export project to Prism", "Prism model (*.prism)", "*.prism");
        }
    };

    private final Runnable mImportFromLTSA = new Runnable() {
        @Override
        public void run() {
            Project p = mProjectDialogsHelper.open(mLTSASerializer, "Import project from LTSA", "LTSA Files (*.lts)", "*.lts");
            if (p != null) {
                mProjectExplorer.open(p);
            }
        }
    };
    

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsHelper.class);

        mMainMenu = mUserInterface.getMainMenu();

        mMainMenu.newItem("File/-")
                .setWeight(MENU_WEIGHT)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("File/Export/To LTSA...")
                .setWeight(MENU_WEIGHT)
                .setAction(mExportToLTSA)
                .create();
        mMainMenu.newItem("File/Export/To Prism...")
                .setWeight(MENU_WEIGHT)
                .setAction(mExportToPrism)
                .create();
        mMainMenu.newItem("File/Import/From LTSA...")
                .setWeight(MENU_WEIGHT)
                .setAction(mImportFromLTSA)
                .create();
    }

}
