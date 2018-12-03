/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
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
public abstract class DefaultWindowManagerPluginMSC<E extends WindowMSC> extends Plugin implements WindowManagerMSC {

    
    private ExtensibleTabPane centerPanel;
    private List<Listener> listeners = new ArrayList<>();
    private List<E> componentsWindowsList = new ArrayList<E>();
    private Map<HmscComponent, Integer> hmscComponentMap = new HashMap<>();
    private Map<BmscComponent, Integer> bmscComponentMap = new HashMap<>();
    private Map<Component, Integer> componentLTSMap = new HashMap<>();
    private boolean onStartCalled;
    private ProjectExplorerPluginMSC projectExplorerPluginMSC;

    protected abstract E onCreate();
    protected abstract E onCreateStandadM(ProjectExplorerPluginMSC pep);
    protected abstract void onShow(E window, HmscComponent buildDS);
    protected abstract void onShow(E window, BmscComponent cds);
    protected abstract void onShow(E window, Component c, ProjectExplorerPluginMSC mProjectExplorerDS);
    protected abstract void onHide(E window);
    

    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        centerPanel = ((UserInterface)extensionManager.get(UserInterface.class)).getCenterPanel();
        onStartCalled = true;
        projectExplorerPluginMSC = extensionManager.get(ProjectExplorerPluginMSC.class);


    }
    
    
    @Override
    public void show(HmscComponent hmscComponent) {
        checkIfStartedProperly();
        Integer id = hmscComponent.getID();
        boolean visivel = centerPanel.isShowing(id);

        E window = null;
        for (E w : componentsWindowsList){
            if(w.getHmscComponent() != null){
                if(w.getHmscComponent() == hmscComponent){
                    window = w;
                }
            }
        }
        if(!visivel){
            if(window == null) {
                window = onCreateStandadM(projectExplorerPluginMSC);
                window.setHmscComponent(hmscComponent);
                componentsWindowsList.add(window);
                for (Listener l : listeners) {
                    l.onCreateWindow(window);
                }
                hmscComponent.addListener(hmscComponentListener);
                onShow(window, hmscComponent);
            }
            id = centerPanel.newTab(window.getTitle(), window.getNode(), hmscComponent.getID() ,true);
            hmscComponentMap.put(hmscComponent, id);
            centerPanel.showTab(id);
        }else {
            for(Tab t : centerPanel.getTabs()){
                if((Integer)t.getUserData() == hmscComponent.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    @Override
    public void show(BmscComponent bmscComponent) {
        checkIfStartedProperly();
        Integer id = bmscComponent.getID();
        boolean visivel =  centerPanel.isShowing(id);
        E window = null;
        for(E w : componentsWindowsList){
            if(w.getBmscComponent() != null){
                if(w.getBmscComponent() == bmscComponent){
                    window= w;
                }
            }
        }

        if(!visivel){
            if(window == null) {
                window = onCreate();
                window.setBmscComponent(bmscComponent);
                componentsWindowsList.add(window);
                for (Listener l : listeners) {
                    l.onCreateWindow(window);
                }
                bmscComponent.addListener(bmscComponentListener);
                onShow(window, bmscComponent);
            }
            id = centerPanel.newTab(window.getTitle(), window.getNode(), bmscComponent.getID(), true);
            bmscComponentMap.put(bmscComponent, id);
            centerPanel.showTab(id);
        }else {
            for(Tab t : centerPanel.getTabs()){
                if((Integer)t.getUserData() == bmscComponent.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    @Override
    public void show(Component component) {
        checkIfStartedProperly();
        Integer id = component.getID();
        boolean visivel = centerPanel.isShowing(id);
        E window = null;
        for(E w : componentsWindowsList){
            if(w.getComponentLTS()!= null){
                if(w.getComponentLTS() == component){
                    window= w;
                }
            }
        }
        if(!visivel){
            if(window == null) {
                window = onCreate();
                window.setComponentLTS(component);
                componentsWindowsList.add(window);
                for (Listener l : listeners) {
                    l.onCreateWindow(window);
                }
                component.addListener(componentLTSListener);
                onShow(window, component, projectExplorerPluginMSC);
            }
            id = centerPanel.newTab(window.getTitle(), window.getNode(), component.getID(), true);
            componentLTSMap.put(component, id);
            centerPanel.showTab(id);
        }else {
            for(Tab t : centerPanel.getTabs()){
                if((Integer)t.getUserData() == component.getID()){
                    t.getTabPane().getSelectionModel().select(t);
                }
            }
        }
    }

    public void close(HmscComponent hmscComponent){
        if(centerPanel != null) {
            centerPanel.closeTab(hmscComponent.getID());
        }
    }

    public void close(BmscComponent bmscComponent) {
        if (centerPanel != null){
            centerPanel.closeTab(bmscComponent.getID());
            HmscComponent stdm = projectExplorerPluginMSC.getSelectedProjectDS().getStandardModeling();
            for(HmscBlock hmscHmscBlock : stdm.getHmscBlockList()){
                if(hmscHmscBlock.getBmscComponet() == bmscComponent){
                    hmscHmscBlock.setColorStatus("red");
                    projectExplorerPluginMSC.getAll_BMSC().remove(bmscComponent);
                }
            }
        }
    }

    public void close(Component component){
        if(centerPanel != null) {
            centerPanel.closeTab(component.getID());
        }

    }


    @Override
    public void hide(HmscComponent hmscComponent) {
    }

    @Override
    public void hide(BmscComponent bmscComponent) {
        
    }

    @Override
    public void hide(Component component) {
        
    }

    @Override
    public void hideAll() {
        
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    private void checkIfStartedProperly() throws IllegalStateException {
        if (!onStartCalled) {
            throw new IllegalStateException("super.onStart() not called in your plugin!");
        }
    }
    
    private final HmscComponent.Listener hmscComponentListener = new HmscComponent.Listener() {
        @Override
        public void onChange(HmscComponent hmscComponent) {
                centerPanel.renameTab(hmscComponent.getID(), hmscComponent.getName());
        }

        @Override
        public void onCreateGenericElement(HmscComponent hmscComponent, GenericElement genericElement) {

        }

        @Override
        public void onRemoveGenericElement(HmscComponent hmscComponent, GenericElement genericElement) {

        }

        @Override
        public void onCreateTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC) {}
        @Override
        public void onRemoveTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC) {}
        @Override
        public void onCreateBmscBlock(HmscComponent hmscComponent, HmscBlock hmscHmscBlock, BmscComponent bmsc) {}
        @Override
        public void onCreateComponentLTS(Component component) {}
        @Override
        public void onCreateGeneralComponentLTS(Component component) {}
    };
    
    private final BmscComponent.Listener bmscComponentListener = new BmscComponent.Listener() {
        @Override
        public void onChange(BmscComponent c) {
                centerPanel.renameTab(c.getID(), c.getName());
        }

        @Override
        public void onBlockDSCreated(BmscComponent bmscComponent, BmscBlock state) {}
        @Override
        public void onBlockDSRemoved(BmscComponent bmscComponent, BmscBlock state) {}

        @Override
        public void onTransitionCreate(BmscComponent bmscComponentDSDS, TransitionMSC t) {}

        @Override
        public void onTransitionRemove(BmscComponent bmscComponentDSDS, TransitionMSC t) {}
    };
    
    private final Component.Listener componentLTSListener = new Component.Listener() {
        @Override
        public void onChange(Component component) {
                centerPanel.renameTab(component.getID(), component.getName());
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
