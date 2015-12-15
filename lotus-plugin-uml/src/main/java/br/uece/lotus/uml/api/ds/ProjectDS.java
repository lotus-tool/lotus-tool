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
        
        void onComponentLTSGeralCreated(ProjectDS project, ComponentBuildDS buildDS, Component component);
        void onComponentLTSGeralRemove(ProjectDS project, ComponentBuildDS buildDS, Component component);
        
        void onComponentLTSFragmentOfBuildDSCreate(ProjectDS project, ComponentBuildDS buildDS, Component componentGeralLTS, Component frag );
        void onComponentLTSFragmentOfBuildDSRemove(ProjectDS project, ComponentBuildDS buildDS, Component componentGeralLTS, Component frag );
        
        void onComponentDSCreated(ProjectDS project, ComponentDS componentDs);
        void onComponentDSRemoved(ProjectDS project, ComponentDS componentDs);

        void onComponentLTSCreated(ProjectDS project, ComponentDS cds, Component component);
        void onComponentLTSRemoved(ProjectDS project, ComponentDS cds, Component component);
    }
    
    private String mName;
    private ComponentBuildDS componentBuildDS = new ComponentBuildDS();
    private Component mComponentGeralLTS = new Component();
    private final List<Component> fragmentsBuildDS = new ArrayList<>();
    private final List<ComponentDS> mComponentsDS = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
    
    public void addFragmentsBuildDS(Component c){
        fragmentsBuildDS.add(c);
        for(Listener l : mListeners){
            l.onComponentLTSFragmentOfBuildDSCreate(this, componentBuildDS, mComponentGeralLTS, c);
        }
    }
    
    public void removeFragmentsBuildDS(Component c){
        fragmentsBuildDS.remove(c);
        for(Listener l : mListeners){
            l.onComponentLTSFragmentOfBuildDSRemove(this, componentBuildDS, mComponentGeralLTS, c);
        }
    }
    
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
    
    public void addComponentLTS(ComponentDS cds,Component c){
        cds.getmComponentsLTS().add(c);
        for(Listener l : mListeners){
            l.onComponentLTSCreated(this, cds, c);
        }
    }
    
    public void removeComponentLTS(ComponentDS cds, Component c){
        cds.getmComponentsLTS().remove(c);
        for(Listener l : mListeners){
            l.onComponentLTSRemoved(this, cds, c);
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

    public Component getmComponentGeral() {
        return mComponentGeralLTS;
    }

    public void setComponentGeralLTS(Component mComponentGeral) {
        this.mComponentGeralLTS = mComponentGeral;
        for(Listener l : mListeners){
            l.onChange(this);
            l.onComponentLTSGeralCreated(this, componentBuildDS, mComponentGeral);
        }
    }
    
    public void removeComponentGeralLTS(Component component){
        this.mComponentGeralLTS = null;
        for(Listener l : mListeners){
            l.onComponentLTSGeralRemove(this, componentBuildDS, component);
        }
    }
    
    public ComponentDS getComponentDS(int index){
        return mComponentsDS.get(index);
    }
    
    public Component getComponentLTS(ComponentDS cds,int index){
        return cds.getmComponentsLTS().get(index);
    }
    
    public Iterable<ComponentDS> getComponentsDS(){
        return mComponentsDS;
    }
    
    public Iterable<Component> getComponentsLTS(ComponentDS cds){
        return cds.getmComponentsLTS();
    }
    
    public int getComponentDSCount(){
        return mComponentsDS.size();
    }
    
    public int getComponentLTSCount(ComponentDS cds){
        return cds.getmComponentsLTS().size();
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
    
    public int indexOfComponent(ComponentDS cds,Component component) {
        int i = 0;
        for (Component c : cds.getmComponentsLTS()) {
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
