/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.seed.app.ExtensibleTabPane;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 * @param <E>
 */
public abstract class DefaultWindowManagerPluginDS<E extends WindowDS> extends Plugin implements WindowManagerDS{

    
    private ExtensibleTabPane mCenterPanel;
    private List<Listener> mListeners = new ArrayList<>();
    
    private ArrayList<E> mComponentsWindows = new ArrayList<E>();
    private Map<StandardModeling, Integer> mComponentBuildDSids = new HashMap<>();
    private Map<ComponentDS, Integer> mComponentDSids = new HashMap<>();
    private Map<Component, Integer> mComponentLTSids = new HashMap<>();
    
    private boolean mOnStartCalled;
    protected abstract E onCreate();
    protected abstract E onCreateStandadM(ProjectExplorerPluginDS pep);
    protected abstract void onShow(E window, StandardModeling buildDS);
    protected abstract void onShow(E window, ComponentDS cds);
    protected abstract void onShow(E window, Component c);
    protected abstract void onHide(E window);
    
    private ProjectExplorerPluginDS mProjectExplorerDS;
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mCenterPanel = ((UserInterface)extensionManager.get(UserInterface.class)).getCenterPanel();
        mOnStartCalled = true;
        mProjectExplorerDS = extensionManager.get(ProjectExplorerPluginDS.class);
    }
    
    
    @Override
    public void show(StandardModeling buildDS) {
        checkIfStartedProperly();
        Integer id = buildDS.getID();
        boolean visivel = mCenterPanel.isShowing(id);

        E window = null;
        for (E w : mComponentsWindows){
            if(w.getComponentBuildDS() != null){
                if(w.getComponentBuildDS() == buildDS){
                    window = w;
                }
            }
        }
        if(!visivel){
            if(window == null) {
                window = onCreateStandadM(mProjectExplorerDS);
                window.setComponentBuildDS(buildDS);
                mComponentsWindows.add(window);
                for (Listener l : mListeners) {
                    l.onCreateWindow(window);
                }
                buildDS.addListener(mComponentBuildDSListener);
                onShow(window, buildDS);
            }
            id = mCenterPanel.newTab(window.getTitle(), window.getNode(),buildDS.getID() ,true);
            mComponentBuildDSids.put(buildDS, id);
            mCenterPanel.showTab(id);
        }else {
            for(Tab t : mCenterPanel.getTabs()){
                if((Integer)t.getUserData() == buildDS.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    @Override
    public void show(ComponentDS cds) {
        checkIfStartedProperly();
        Integer id = cds.getID();
        boolean visivel =  mCenterPanel.isShowing(id);
        E window = null;
        for(E w : mComponentsWindows){
            if(w.getComponentDS() != null){
                if(w.getComponentDS() == cds){
                    window= w;
                }
            }
        }

        if(!visivel){
            if(window == null) {
                window = onCreate();
                window.setComponentDS(cds);
                mComponentsWindows.add(window);
                for (Listener l : mListeners) {
                    l.onCreateWindow(window);
                }
                cds.addListener(mComponentDSListener);
                onShow(window, cds);
            }
            id = mCenterPanel.newTab(window.getTitle(), window.getNode(),cds.getID(), true);
            mComponentDSids.put(cds, id);
            mCenterPanel.showTab(id);
        }else {
            for(Tab t : mCenterPanel.getTabs()){
                if((Integer)t.getUserData() == cds.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    @Override
    public void show(Component c) {
        checkIfStartedProperly();
        Integer id = c.getID();
        boolean visivel = mCenterPanel.isShowing(id);
        E window = null;
        for(E w : mComponentsWindows){
            if(w.getComponentLTS()!= null){
                if(w.getComponentLTS() == c){
                    window= w;
                }
            }
        }
        if(!visivel){
            if(window == null) {
                window = onCreate();
                window.setComponentLTS(c);
                mComponentsWindows.add(window);
                for (Listener l : mListeners) {
                    l.onCreateWindow(window);
                }
                c.addListener(mComponentLTSListener);
                onShow(window, c);
            }
            id = mCenterPanel.newTab(window.getTitle(), window.getNode(),c.getID(), true);
            mComponentLTSids.put(c, id);
            mCenterPanel.showTab(id);
        }else {
            for(Tab t : mCenterPanel.getTabs()){
                if((Integer)t.getUserData() == c.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    public void close(StandardModeling buildDS){
        if(mCenterPanel != null) {
            mCenterPanel.closeTab(buildDS.getID());
        }
    }

    public void close(ComponentDS cds) {
        if (mCenterPanel != null){
            mCenterPanel.closeTab(cds.getID());
            StandardModeling stdm = mProjectExplorerDS.getSelectedProjectDS().getStandardModeling();
            for(Hmsc hmsc : stdm.getBlocos()){
                if(hmsc.getmDiagramSequence() == cds){
                    hmsc.setColorStatus("red");
                    mProjectExplorerDS.getAll_BMSC().remove(cds);
                }
            }
        }
    }

    public void close(Component c){
        if(mCenterPanel != null) {
            mCenterPanel.closeTab(c.getID());
        }

    }


    @Override
    public void hide(StandardModeling buildDS) {
    }

    @Override
    public void hide(ComponentDS cds) {
        
    }

    @Override
    public void hide(Component c) {
        
    }

    @Override
    public void hideAll() {
        
    }

    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }
    
    private void checkIfStartedProperly() throws IllegalStateException {
        if (!mOnStartCalled) {
            throw new IllegalStateException("super.onStart() not called in your plugin!");
        }
    }
    
    private final StandardModeling.Listener mComponentBuildDSListener = new StandardModeling.Listener() {
        @Override
        public void onChange(StandardModeling buildDS) {
                mCenterPanel.renameTab(buildDS.getID(), buildDS.getName());
        }

        @Override
        public void onBlockCreate(StandardModeling buildDS, Hmsc bbds) {}
        @Override
        public void onBlockRemove(StandardModeling buildDS, Hmsc bbds) {}
        @Override
        public void onTransitionCreate(StandardModeling buildDS, TransitionMSC t) {}
        @Override
        public void onTransitionRemove(StandardModeling buildDS, TransitionMSC t) {}
        @Override
        public void onBlockCreateBMSC(StandardModeling sm, Hmsc hmsc, ComponentDS bmsc) {}
        @Override
        public void onComponentLTSCreate(Component c) {}
        @Override
        public void onComponentLTSGeneralCreate(Component c) {}
    };
    
    private final ComponentDS.Listener mComponentDSListener = new ComponentDS.Listener() {
        @Override
        public void onChange(ComponentDS c) {
                mCenterPanel.renameTab(c.getID(), c.getName());
        }

        @Override
        public void onBlockDSCreated(ComponentDS componentDS, BlockDS state) {}
        @Override
        public void onBlockDSRemoved(ComponentDS componentDS, BlockDS state) {}

        @Override
        public void onTransitionCreate(ComponentDS componentDSDS, TransitionMSC t) {}

        @Override
        public void onTransitionRemove(ComponentDS componentDSDS, TransitionMSC t) {}
    };
    
    private final Component.Listener mComponentLTSListener = new Component.Listener() {
        @Override
        public void onChange(Component component) {
                mCenterPanel.renameTab(component.getID(), component.getName());
        }
        @Override
        public void onStateCreated(Component component, State state) {}
        @Override
        public void onStateRemoved(Component component, State state) {}
        @Override
        public void onTransitionCreated(Component component, Transition state) {}
        @Override
        public void onTransitionRemoved(Component component, Transition state) {}
    };
}
