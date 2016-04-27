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
        
        void onComponentLTSGeralCreated(ProjectDS project, StandardModeling buildDS, Component component);
        void onComponentLTSGeralRemove(ProjectDS project, StandardModeling buildDS, Component component);
        
        void onComponentBMSCCreated(ProjectDS project, ComponentDS componentDs);
        void onComponentBMSCRemoved(ProjectDS project, ComponentDS componentDs);

        void onComponentLTSCreated(ProjectDS project, Component component);
        void onComponentLTSRemoved(ProjectDS project, Component component);
    }
    
    private String mName;
    private StandardModeling componentBuildDS = new StandardModeling();
    private Component mComponentGeralLTS = new Component();
    private final List<Component> fragmentsLTS = new ArrayList<>();
    private final List<ComponentDS> mComponentsDS = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
    
    
    public void addComponent_bMSC(ComponentDS c){
        mComponentsDS.add(c);
        for(Listener l : mListeners){
            l.onComponentBMSCCreated(this, c);
        }
    }
    
    public void removeComponent_bMSC(ComponentDS c){
        mComponentsDS.remove(c);
        for(Listener l : mListeners){
            l.onComponentBMSCRemoved(this, c);
        }
    }
    
    public void addComponentFragmentLTS(Component c){
        fragmentsLTS.add(c);
        for(Listener l : mListeners){
            l.onComponentLTSCreated(this, c);
        }
    }
    
    public void removeComponentFragmentLTS(Component c){
        fragmentsLTS.remove(c);
        for(Listener l : mListeners){
            l.onComponentLTSRemoved(this, c);
        }
    }
    
    public void addComponentGeneralLTS(Component c) {
        mComponentGeralLTS = c;
        for(Listener l : mListeners){
            l.onComponentLTSGeralCreated(this, componentBuildDS, c);
        }
    }
    
    public void removeComponentGeneralLTS(Component c){
        mComponentGeralLTS = null;
        for(Listener l : mListeners){
            l.onComponentLTSGeralRemove(this, componentBuildDS, c);
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public StandardModeling getStandardModeling() {
        return componentBuildDS;
    }

    public void setComponentBuildDS(StandardModeling componentBuildDS) {
        this.componentBuildDS = componentBuildDS;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public Component getLTS_Composed() {
        return mComponentGeralLTS;
    }

    public void setLTS_Composed(Component mComponentGeral) {
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
    
    public Iterable<ComponentDS> getComponentsDS(){
        return mComponentsDS;
    }

    public int getComponentDSCount(){
        return mComponentsDS.size();
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
    
    public Component getFragment(int index){
        return fragmentsLTS.get(index);
    }
    
    public Iterable<Component> getFragments(){
        return fragmentsLTS;
    }
    
    public int getFragmentsCount(){
        return fragmentsLTS.size();
    }
    
    public int indexOfFragment(Component c){
        int i = 0;
        for(Component comp : fragmentsLTS){
            if(comp == c){
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
