/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModeling {
    
    private final Map<String, Object> mValues = new HashMap<>();
    private String mName;
    
    public interface Listener{
        void onChange(StandardModeling buildDS);
        void onBlockCreate(StandardModeling buildDS, Hmsc bbds);
        void onBlockRemove(StandardModeling buildDS, Hmsc bbds);
        void onBlockCreateBMSC(StandardModeling sm, Hmsc hmsc, ComponentDS bmsc);
        void onTransitionCreate(StandardModeling buildDS, TransitionMSC t);
        void onTransitionRemove(StandardModeling buildDS, TransitionMSC t);
    }
    
    
    private List<Hmsc> mBlocos = new ArrayList<>();
    private List<TransitionMSC> mTransitions = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
     
    
    public Hmsc newBlock(int id){
        Hmsc bbds = new Hmsc(this);
        bbds.setID(id);
        add(bbds);
        return bbds;
    }
    
    public void add(Hmsc b){
        mBlocos.add(b);
        for(Listener l : mListeners){
            l.onBlockCreate(this, b);
        }
    }
    
    public void set_bMSC_in_hMSC(Hmsc h, ComponentDS b){
        h.setmDiagramSequence(b);
        for(Listener l : mListeners){
            l.onBlockCreateBMSC(this, h, b);
        }
    }

    public TransitionMSC newTransition(HmscView src, HmscView dst){
        if (src == null) {
            throw new IllegalArgumentException("src hMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst hMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        add(t);
        return t;
    }
    
    public TransitionMSC.Builder buildTransition(HmscView src, HmscView dst){
        if (src == null) {
            throw new IllegalArgumentException("src hMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst hMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        return new TransitionMSC.Builder(this, t);
    }
    
    public void add(TransitionMSC t){
        Hmsc src = ((HmscView) t.getSource()).getHMSC();
        Hmsc dst = ((HmscView) t.getDestiny()).getHMSC();
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        mTransitions.add(t);
        for(Listener l : mListeners){
            l.onTransitionCreate(this, t);
        }
    }
    
    public void remove(Hmsc b){
        List<TransitionMSC> transition = new ArrayList<>();
        transition.addAll(b.getIncomingTransitionsList());
        transition.addAll(b.getOutgoingTransitionsList());
        for(TransitionMSC t : transition){
            remove(t);
        }
        mBlocos.remove(b);
        for(Listener l : mListeners){
            l.onBlockRemove(this, b);
        }
    }
    
    public void remove(TransitionMSC t){
        ((HmscView)t.getSource()).getHMSC().removeOutgoingTransition(t);
        ((HmscView)t.getDestiny()).getHMSC().removeIncomingTransition(t);
        mTransitions.remove(t);
        for(Listener l : mListeners){
            l.onTransitionRemove(this, t);
        }
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

    public List<Hmsc> getBlocos() {
        return mBlocos;
    }

    public void setBlocos(List<Hmsc> mBlocos) {
        this.mBlocos = mBlocos;
    }

    public List<TransitionMSC> getTransitions() {
        return mTransitions;
    }

    public void setTransitions(List<TransitionMSC> mTransitions) {
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
        final StandardModeling other = (StandardModeling) obj;
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
