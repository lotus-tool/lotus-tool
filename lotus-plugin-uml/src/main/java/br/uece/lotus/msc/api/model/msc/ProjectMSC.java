/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.model.msc;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 */
public class ProjectMSC {
    
    private final Map<String, Object> mValues = new HashMap<>();
    private int id;
    public interface Listener{
        void onChange(ProjectMSC project);
        
        void onComponentLTSGeralCreated(ProjectMSC project, HmscComponent buildDS, Component component);
        void onComponentLTSGeralRemove(ProjectMSC project, HmscComponent buildDS, Component component);
        
        void onComponentBMSCCreated(ProjectMSC project, BmscComponent bmscComponent);
        void onComponentBMSCRemoved(ProjectMSC project, BmscComponent bmscComponent);

        void onComponentLTSCreated(ProjectMSC project, Component component);
        void onComponentLTSRemoved(ProjectMSC project, Component component);
    }
    
    private String mName;
    private HmscComponent componentBuildDS = new HmscComponent();
    private Component mComponentGeralLTS = new Component();
    private final List<Component> fragmentsLTS = new ArrayList<>();
    private final List<BmscComponent> mComponentsDS = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();




    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }
    public void addComponent_bMSC(BmscComponent c){
        mComponentsDS.add(c);
        for(Listener l : mListeners){
            l.onComponentBMSCCreated(this, c);
        }
    }
    
    public void removeComponent_bMSC(BmscComponent c){
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

    public HmscComponent getStandardModeling() {
        return componentBuildDS;
    }

    public void setComponentBuildDS(HmscComponent componentBuildDS) {
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
    
    public BmscComponent getComponentDS(int index){
        return mComponentsDS.get(index);
    }
    
    public Iterable<BmscComponent> getComponentsDS(){
        return mComponentsDS;
    }

    public int getComponentDSCount(){
        return mComponentsDS.size();
    }
    
    public int indexOfComponentDS(BmscComponent component) {
        int i = 0;
        for (BmscComponent c : mComponentsDS) {
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

    public void putValue(String key, Object value) {
        mValues.put(key, value);
    }
}
