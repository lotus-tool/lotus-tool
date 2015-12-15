/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.seed.app.ExtensibleTabPane;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Bruno Barbosa
 * @param <T>
 */
public abstract class DefaultWindowManagerPluginDS<T extends WindowDS> extends Plugin implements WindowManagerDS{

    
    private ExtensibleTabPane mCenterPanel;
    private  List<Listener> mListeners = new ArrayList<>();
    
    private ArrayList<T> mComponentsWindows = new ArrayList<T>();
    private Map<ComponentBuildDS, Integer> mComponentBuildDSids =new HashMap<>();
    private Map<ComponentDS, Integer> mComponentDSids = new HashMap<>();
    private Map<Component, Integer> mComponentIds = new HashMap<>();
    
    private boolean mOnStartCalled;
    protected abstract T onCreate();
    protected abstract void onShow(T window, ComponentBuildDS buildDS);
    protected abstract void onShow(T window, ComponentDS cds);
    protected abstract void onShow(T window, Component c);
    protected abstract void onHide(T window);
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mCenterPanel = ((UserInterface)extensionManager.get(UserInterface.class)).getCenterPanel();
        mOnStartCalled = true;
    }
    
    
    @Override
    public void show(ComponentBuildDS buildDS) {
        
    }

    @Override
    public void show(ComponentDS cds) {
        
    }

    @Override
    public void show(Component c) {
        
    }

    @Override
    public void hide(ComponentBuildDS buildDS) {
        
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
    
    private EventHandler<ActionEvent> onCloseTab = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            
        }
    };
    
    private void checkIfStartedProperly() throws IllegalStateException {
        if (!mOnStartCalled) {
            throw new IllegalStateException("super.onStart() not called in your plugin!");
        }
    }
}
