/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentBuildDS {
    
    private final Map<String, Object> mValues = new HashMap<>();
    private String mName;
    
    public interface Listener{
        void onChange(ComponentBuildDS buildDS);
        void onBlockCreate(ComponentBuildDS buildDS, BlockBuildDS bbds);
        void onBlockRemove(ComponentBuildDS buildDS, BlockBuildDS bbds);
        void onTransitionCreate(ComponentBuildDS buildDS, TransitionBuildDS t);
        void onTransitionRemove(ComponentBuildDS buildDS, TransitionBuildDS t);
    }
    
    
    private List<BlockBuildDS> mBlocos = new ArrayList<>();
    private List<TransitionBuildDS> mTransitions = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
     
    
    public BlockBuildDS newBlock(int id){
        BlockBuildDS bbds = new BlockBuildDS(this);
        add(bbds);
        return bbds;
    }
    
    public void add(BlockBuildDS b){
        mBlocos.add(b);
        for(Listener l : mListeners){
            l.onBlockCreate(this, b);
        }
    }

    public void add(TransitionBuildDS t){
        
    }
    public void remove(BlockBuildDS b){
        
    }
    public void remove(TransitionBuildDS t){
        
    }
    
    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

    public void setName(String s) {
        this.mName = s;
    }

    public String getName() {
        return this.mName;
    }

    public List<BlockBuildDS> getBlocos() {
        return mBlocos;
    }

    public void setBlocos(List<BlockBuildDS> mBlocos) {
        this.mBlocos = mBlocos;
    }

    public List<TransitionBuildDS> getTransitions() {
        return mTransitions;
    }

    public void setTransitions(List<TransitionBuildDS> mTransitions) {
        this.mTransitions = mTransitions;
    }
    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.mValues);
        hash = 97 * hash + Objects.hashCode(this.mName);
        hash = 97 * hash + Objects.hashCode(this.mBlocos);
        hash = 97 * hash + Objects.hashCode(this.mTransitions);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComponentBuildDS other = (ComponentBuildDS) obj;
        if (!Objects.equals(this.mValues, other.mValues)) {
            return false;
        }
        if (!Objects.equals(this.mName, other.mName)) {
            return false;
        }
        if (!Objects.equals(this.mBlocos, other.mBlocos)) {
            return false;
        }
        if (!Objects.equals(this.mTransitions, other.mTransitions)) {
            return false;
        }
        return true;
    }
    
}
