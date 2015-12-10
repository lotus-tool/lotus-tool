/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import br.uece.lotus.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 */
public class ProjectDS {
    
    private final Map<String, Object> mValues = new HashMap<>();
    
    public interface Listener{
        void onChange(ProjectDS project);
        
        void onComponentBuildDSCreated(ProjectDS project, ComponentBuildDS componentBuildDs);
        void onComponentBuildDSRemoved(ProjectDS project, ComponentBuildDS componentBuildDs);
        
        void onComponentDSCreated(ProjectDS project, ComponentDS componentDs);
        void onComponentDSRemoved(ProjectDS project, ComponentDS componentDs);

        void onComponentLTSCreated(ProjectDS project, Component component);
        void onComponentLTSRemoved(ProjectDS project, Component component);
    }
    
    private String mName;
    private ComponentBuildDS componentBuildDS = new ComponentBuildDS();
    private final List<ComponentDS> mComponentsDS = new ArrayList<>();
    private final List<Component> mComponentsLTS = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
    
    public void addComponentDS(ComponentDS c){
        mComponentsDS.add(c);
        for(Listener l : mListeners){
            l.onComponentDSCreated(this, c);
        }
    }
    
    public void removeComponentDS(ComponentDS c){
        mComponentsDS.remove(c);
        for(Listener l : mListeners){
            l.onComponentDSRemoved(this, c);
        }
    }
    
    public void addComponentLTS(Component c){
        mComponentsLTS.add(c);
        for(Listener l : mListeners){
            l.onComponentLTSCreated(this, c);
        }
    }
    
    public void removeComponentLTS(Component c){
        mComponentsLTS.remove(c);
        for(Listener l : mListeners){
            l.onComponentLTSRemoved(this, c);
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public ComponentBuildDS getComponentBuildDS() {
        return componentBuildDS;
    }

    public void setComponentBuildDS(ComponentBuildDS componentBuildDS) {
        this.componentBuildDS = componentBuildDS;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }
    
    public ComponentDS getComponentDS(int index){
        return mComponentsDS.get(index);
    }
    
    public Component getComponentLTS(int index){
        return mComponentsLTS.get(index);
    }
    
    public Iterable<ComponentDS> getComponentsDS(){
        return mComponentsDS;
    }
    
    public Iterable<Component> getComponentsLTS(){
        return mComponentsLTS;
    }
    
    public int getComponentDSCount(){
        return mComponentsDS.size();
    }
    
    public int getComponentLTSCount(){
        return mComponentsLTS.size();
    }
    
    public int indexOfComponentDS(ComponentDS component) {
        int i = 0;
        for (ComponentDS c : mComponentsDS) {
            if (c == component) {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    public int indexOfComponent(Component component) {
        int i = 0;
        for (Component c : mComponentsLTS) {
            if (c == component) {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }
}
