/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.project.ProjectExplorerDS;
import br.uece.seed.app.ExtensibleFXContextMenu;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeView;

/**
 *
 * @author Bruno Barbosa
 */
public final class ProjectExplorerPluginDS extends Plugin implements ProjectExplorerDS{

    private UserInterface mUserInterface;
    
    private final ContextMenu mMnuWorkspace;
    private final ContextMenu mMnuProjectDS;
    private final ContextMenu mMnuComponentBuildDS;
    private final ContextMenu mMnuComponentDS;
    private final ContextMenu mMnuComponentLTS;
    
    private final ExtensibleMenu mExtMnuWorkspace;
    private final ExtensibleMenu mExtMnuProjectDS;
    private final ExtensibleMenu mExtMnuComponentBuildDS;
    private final ExtensibleMenu mExtMnuComponentDS;
    private final ExtensibleMenu mExtMnuComponentLTS;
    
    private final TreeView<WrapperDS> mProjectView;
    private final List<Listener> mListeners;
    
    private final ProjectDS.Listener mProjectDSListener = new ProjectDS.Listener() {

        @Override
        public void onChange(ProjectDS project) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentBuildDSCreated(ProjectDS project, ComponentBuildDS componentBuildDs) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentBuildDSRemoved(ProjectDS project, ComponentBuildDS componentBuildDs) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentDSCreated(ProjectDS project, ComponentDS componentDs) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentDSRemoved(ProjectDS project, ComponentDS componentDs) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentLTSCreated(ProjectDS project, Component component) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComponentLTSRemoved(ProjectDS project, Component component) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    //Falta os listeners do componentBuidDS, componentDS e ComponentLTS <- (o normal)
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception{
        mUserInterface = extensionManager.get(UserInterface.class);
    }
    
    public ProjectExplorerPluginDS(){
        mListeners = new ArrayList<>();
        mMnuWorkspace = new ContextMenu();
        mMnuProjectDS = new ContextMenu();
        mMnuComponentBuildDS = new ContextMenu();
        mMnuComponentDS = new ContextMenu();
        mMnuComponentLTS = new ContextMenu();
        
        mExtMnuWorkspace = new ExtensibleFXContextMenu(mMnuWorkspace);
        mExtMnuProjectDS = new ExtensibleFXContextMenu(mMnuProjectDS);
        mExtMnuComponentBuildDS = new ExtensibleFXContextMenu(mMnuComponentBuildDS);
        mExtMnuComponentDS = new ExtensibleFXContextMenu(mMnuComponentDS);
        mExtMnuComponentLTS = new ExtensibleFXContextMenu(mMnuComponentLTS);
        
        mProjectView = new TreeView<>();
        //continua...
    }
    
    @Override
    public ExtensibleMenu getMenu() {
        return mExtMnuWorkspace;
    }

    @Override
    public ExtensibleMenu getProjectDSMenu() {
        return mExtMnuProjectDS;
    }

    @Override
    public ExtensibleMenu getComponentBuildDSMenu() {
        return mExtMnuComponentBuildDS;
    }

    @Override
    public ExtensibleMenu getComponentDSMenu() {
        return mExtMnuComponentDS;
    }

    @Override
    public ExtensibleMenu getComponentLTSMenu() {
        return mExtMnuComponentLTS;
    }

    @Override
    public void open(ProjectDS p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close(ProjectDS p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProjectDS getSelectedProjectDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComponentBuildDS getSelectedComponentBuildDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComponentDS getSelectedComponentDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component getSelectedComponentLTS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProjectDS> getSelectedProjectsDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ComponentDS> getSelectedComponentsDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Component> getSelectedComponentsLTS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    @Override
    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
}

class WrapperDS {
    
    private final Object mObj;
    
    public WrapperDS(Object obj) {
        this.mObj = obj;
    }
    
    Object getObject() {
        return mObj;
    }
    
    void set(String s) {
        if (mObj instanceof ProjectDS) {
            ((ProjectDS) mObj).setName(s);
        } 
        else if(mObj instanceof ComponentBuildDS){
            ((ComponentBuildDS) mObj).setName(s);
        }
        else if(mObj instanceof ComponentDS){
            ((ComponentDS) mObj).setName(s);
        }
        else if(mObj instanceof Component){
            ((Component) mObj).setName(s);
        }
    }
    
    @Override
    public String toString(){
        String s = "";
        if(mObj instanceof ProjectDS){
            s = ((ProjectDS) mObj).getName();
        }
        else if(mObj instanceof ComponentBuildDS){
            s = ((ComponentBuildDS) mObj).getName();
        }
        else if(mObj instanceof ComponentDS){
            s = ((ComponentDS) mObj).getName();
        }
        else if(mObj instanceof Component){
            s = ((Component) mObj).getName();
        }
        
        return s;
    }
}
