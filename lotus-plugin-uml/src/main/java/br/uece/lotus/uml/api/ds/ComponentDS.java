/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;

import java.util.*;

/**
 * @author Bruno Barbosa
 */
public class ComponentDS {

    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;
    private String mName;
    private final List<Listener> mListeners = new ArrayList<>();
    private List<TransitionMSC> mTransitions = new ArrayList<>();
    private final List<BlockDS> mBlockDSs = new ArrayList<>();

    public interface Listener {
        void onChange(ComponentDS cds);
        void onBlockDSCreated(ComponentDS componentDS, BlockDS ds);
        void onBlockDSRemoved(ComponentDS componentDS, BlockDS ds);
        void onTransitionCreate(ComponentDS buildDS, TransitionMSC t);
        void onTransitionRemove(ComponentDS buildDS, TransitionMSC t);

    }
    public int getCountTransition() {
        return mTransitions.size();
    }
    public List<TransitionMSC> getAllTransitions(){
        return mTransitions;
    }

    public BlockDS newBlockDS(int id) {
        BlockDS ds = new BlockDS(this);
        ds.setID(id);
        add(ds);
        return ds;
    }

    public void add(BlockDS ds) {
        mBlockDSs.add(ds);
        for (Listener l : mListeners) {
            l.onBlockDSCreated(this, ds);
        }
    }

    public void add(TransitionMSC t){
        BlockDS src = ((BlockDSView) t.getSource()).getBlockDS();
        BlockDS dst = ((BlockDSView) t.getDestiny()).getBlockDS();
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        mTransitions.add(t);
        for(Listener l : mListeners){
            l.onTransitionCreate(this, t);
        }
    }
    
    public void add2(TransitionMSC t){
        BlockDS src = ((BlockDS) t.getSource());
        BlockDS dst = ((BlockDS) t.getDestiny());
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        mTransitions.add(t);
        for(Listener l : mListeners){
            l.onTransitionCreate(this, t);
        }
    }

    public TransitionMSC newTransition(BlockDSView src, BlockDSView dst){
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

    public TransitionMSC.Builder buildTransition(BlockDSView src, BlockDSView dst){
        if (src == null) {
            throw new IllegalArgumentException("src bMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst bMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        return new TransitionMSC.Builder(this, t);
    }
    
    public TransitionMSC.Builder buildTransition(BlockDS src, BlockDS dst){
        if (src == null) {
            throw new IllegalArgumentException("src bMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst bMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        return new TransitionMSC.Builder(this, t);
    }
    
    public void remove(TransitionMSC t){
        ((BlockDSView)t.getSource()).getBlockDS().removeOutgoingTransition(t);
        ((BlockDSView)t.getDestiny()).getBlockDS().removeIncomingTransition(t);
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

    public Iterable<BlockDS> getBlockDS() {return mBlockDSs;}

    public String getName() {return mName;}

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    private void copyBlockDS(BlockDS from, BlockDS to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
//        to.setLayoutY(from.getLayoutY());
    }
    
    private void copyTransition(TransitionMSC oldTransition, TransitionMSC newTransition) {
        System.out.println("Falta implementar classe: ComponentDS");
    }

    public int getBlockDSCount() {return mBlockDSs.size();}
    public int getBlockDSIndex(BlockDS s) {return mBlockDSs.indexOf(s);}
    
    public BlockDS getBMSC_ByID(int id){
        for(BlockDS b : mBlockDSs){
            if(b.getID() == id){
                return b;
            }
        }
        return null;
    }

    public void remove(BlockDS ds) {
        List<TransitionMSC> transitionMSC = new ArrayList<>();
        transitionMSC.addAll(ds.getOutgoingTransitionsList());
        transitionMSC.addAll(ds.getIncomingTransitionsList());
        for (TransitionMSC t : transitionMSC) {
            remove(t);
        }

        mBlockDSs.remove(ds);
        for (Listener l : mListeners) {
            l.onBlockDSRemoved(this, ds);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    public void clearVisitedBlockDSCount() {
        for (BlockDS s : mBlockDSs) {
            s.setmVisitedBlockDSCount(0);
        }
    }

    @Override
    public ComponentDS clone() throws CloneNotSupportedException {
        ComponentDS c = new ComponentDS();
        c.mName = mName;
        c.mAutoUpdateLabels = mAutoUpdateLabels;
        for (BlockDS oldState : mBlockDSs) {
            BlockDS newState = c.newBlockDS(oldState.getID());
            copyBlockDS(oldState, newState);
        }
       for (TransitionMSC oldTransition : mTransitions) {
            BlockDSView src = (BlockDSView) oldTransition.getSource();
            BlockDSView dst = (BlockDSView) oldTransition.getDestiny();
            TransitionMSC newTransition = c.newTransition(src, dst);
            copyTransition(oldTransition, newTransition);
        }

        return c;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.mName);
       /* hash = 59 * hash + Objects.hashCode(this.mInitialState);
        hash = 59 * hash + Objects.hashCode(this.mFinalState);
        hash = 59 * hash + Objects.hashCode(this.mErrorState);*/
        hash = 59 * hash + Objects.hashCode(this.mBlockDSs);
      /*  hash = 59 * hash + Objects.hashCode(this.mTransitions);*/
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
        final ComponentDS other = (ComponentDS) obj;
        if (!Objects.equals(this.mName, other.mName)) {
            return false;
        }
        return true;
    }
}
