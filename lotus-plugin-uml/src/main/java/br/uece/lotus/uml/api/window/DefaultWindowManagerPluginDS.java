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
import br.uece.seed.app.ExtensibleTabPane;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
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
    protected abstract void onShow(E window, StandardModeling buildDS);
    protected abstract void onShow(E window, ComponentDS cds);
    protected abstract void onShow(E window, Component c);
    protected abstract void onHide(E window);
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mCenterPanel = ((UserInterface)extensionManager.get(UserInterface.class)).getCenterPanel();
        mOnStartCalled = true;
    }
    
    
    @Override
    public void show(StandardModeling buildDS) {
        checkIfStartedProperly();
        E window = null;
        for(E w : mComponentsWindows){
            if(w.getComponentBuildDS() != null){
                if(w.getComponentBuildDS() == buildDS){
                    window= w;
                }
            }
        }
        if(window == null){
            window = onCreate();
            window.setComponentBuildDS(buildDS);
            mComponentsWindows.add(window);
            for(Listener l : mListeners){
                l.onCreateWindow(window);
            }
            buildDS.addListener(mComponentBuildDSListener);
            onShow(window, buildDS);
            Integer id = mComponentBuildDSids.get(buildDS);
            boolean visivel = id != null && mCenterPanel.isShowing(id);
            if(!visivel || id == null){
                id = mCenterPanel.newTab(window.getTitle(), window.getNode(), true);
                mComponentBuildDSids.put(buildDS, id);
            }
            mCenterPanel.showTab(id);
        }
    }

    @Override
    public void show(ComponentDS cds) {
        checkIfStartedProperly();
        E window = null;
        for(E w : mComponentsWindows){
            if(w.getComponentDS() != null){
                if(w.getComponentDS() == cds){
                    window= w;
                }
            }
        }
        if(window == null){
            window = onCreate();
            window.setComponentDS(cds);
            mComponentsWindows.add(window);
            for(Listener l : mListeners){
                l.onCreateWindow(window);
            }
            cds.addListener(mComponentDSListener);
            onShow(window, cds);
            Integer id = mComponentDSids.get(cds);
            boolean visivel = id != null && mCenterPanel.isShowing(id);
            if(!visivel || id == null){
                id = mCenterPanel.newTab(window.getTitle(), window.getNode(), true);
                mComponentDSids.put(cds, id);
            }
            mCenterPanel.showTab(id);
        }
    }

    @Override
    public void show(Component c) {
        checkIfStartedProperly();
        E window = null;
        for(E w : mComponentsWindows){
            if(w.getComponentLTS()!= null){
                if(w.getComponentLTS() == c){
                    window= w;
                }
            }
        }
        if(window == null){
            window = onCreate();
            window.setComponentLTS(c);
            mComponentsWindows.add(window);
            for(Listener l : mListeners){
                l.onCreateWindow(window);
            }
            c.addListener(mComponentLTSListener);
            onShow(window, c);
            Integer id = mComponentLTSids.get(c);
            boolean visivel = id != null && mCenterPanel.isShowing(id);
            if(!visivel || id == null){
                id = mCenterPanel.newTab(window.getTitle(), window.getNode(), true);
                mComponentLTSids.put(c, id);
            }
            mCenterPanel.showTab(id);
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
            Integer id = mComponentBuildDSids.get(buildDS);
            if(id!=null){
                mCenterPanel.renameTab(id, buildDS.getName());
            }
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
    };
    
    private final ComponentDS.Listener mComponentDSListener = new ComponentDS.Listener() {
        @Override
        public void onChange(ComponentDS c) {
            Integer id = mComponentDSids.get(c);
            if(id!=null){
                mCenterPanel.renameTab(id, c.getName());
            }
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
            Integer id = mComponentLTSids.get(component);
            if(id!=null){
                mCenterPanel.renameTab(id, component.getName());
            }
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
