/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.model.msc.bmsc;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;

import java.util.*;

/**
 * @author Bruno Barbosa
 */
public class BmscComponent {

    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;
    private String mName;
    private final List<Listener> mListeners = new ArrayList<>();
    private List<TransitionMSC> mTransitions = new ArrayList<>();
    private final List<BmscBlock> mBlockDSses = new ArrayList<>();
    private int id;

    public interface Listener {
        void onChange(BmscComponent cds);
        void onBlockDSCreated(BmscComponent bmscComponent, BmscBlock ds);
        void onBlockDSRemoved(BmscComponent bmscComponent, BmscBlock ds);
        void onTransitionCreate(BmscComponent buildDS, TransitionMSC t);
        void onTransitionRemove(BmscComponent buildDS, TransitionMSC t);

    }

    public BmscComponent(){
    }

    public int getID(){
        return this.id;
    }

    public void setID(int i){
        this.id = i;
    }

    public int getCountTransition() {
        return mTransitions.size();
    }

    public List<TransitionMSC> getAllTransitions(){
        return mTransitions;
    }

    public BmscBlock newBmscBlock(int id) {
        BmscBlock ds = new BmscBlock(this);
        ds.setID(id);
        add(ds);
        return ds;
    }

    public void add(BmscBlock ds) {
        mBlockDSses.add(ds);
        for (Listener l : mListeners) {
            l.onBlockDSCreated(this, ds);
        }
    }

    public void add(TransitionMSC t){
        BmscBlock src = ((BlockDSView) t.getSource()).getBlockDS();
        BmscBlock dst = ((BlockDSView) t.getDestiny()).getBlockDS();
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        mTransitions.add(t);
        for(Listener l : mListeners){
            l.onTransitionCreate(this, t);
        }
    }
    
    public void add2(TransitionMSC t){
        BmscBlock src = ((BmscBlock) t.getSource());
        BmscBlock dst = ((BmscBlock) t.getDestiny());
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
    
    public TransitionMSC.Builder buildTransition(BmscBlock src, BmscBlock dst){
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
        if(t.getSource() instanceof BmscBlock){
            ((BmscBlock)t.getSource()).removeOutgoingTransition(t);
        }else {
            ((BlockDSView) t.getSource()).getBlockDS().removeOutgoingTransition(t);
        }
        if(t.getDestiny() instanceof BmscBlock){
            ((BmscBlock)t.getDestiny()).removeIncomingTransition(t);
        }else {
            ((BlockDSView) t.getDestiny()).getBlockDS().removeIncomingTransition(t);
        }
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

    public Iterable<BmscBlock> getBmscBlockList() {return mBlockDSses;}

    public String getName() {return mName;}

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    private void copyBlockDS(BmscBlock from, BmscBlock to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
//        to.setLayoutY(from.getLayoutY());
    }
    
    private void copyTransition(TransitionMSC oldTransition, TransitionMSC newTransition) {
        System.out.println("Falta implementar classe: BmscComponent");
    }

    public int getBlockDSCount() {return mBlockDSses.size();}
    public int getBlockDSIndex(BmscBlock s) {return mBlockDSses.indexOf(s);}
    
    public BmscBlock getBMSC_ByID(int id){
        for(BmscBlock b : mBlockDSses){
            if(b.getID() == id){
                return b;
            }
        }
        return null;
    }

    public void remove(BmscBlock ds) {
        List<TransitionMSC> transitionMSC = new ArrayList<>();
        transitionMSC.addAll(ds.getOutgoingTransitionsList());
        transitionMSC.addAll(ds.getIncomingTransitionsList());
        for (TransitionMSC t : transitionMSC) {
            remove(t);
        }

        mBlockDSses.remove(ds);
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
        for (BmscBlock s : mBlockDSses) {
            s.setmVisitedBlockDSCount(0);
        }
    }

    @Override
    public BmscComponent clone() throws CloneNotSupportedException {
        BmscComponent c = new BmscComponent();
        c.mName = mName;
        c.mAutoUpdateLabels = mAutoUpdateLabels;
        for (BmscBlock oldState : mBlockDSses) {
            BmscBlock newState = c.newBmscBlock(oldState.getID());
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
        hash = 59 * hash + Objects.hashCode(this.mBlockDSses);
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
        final BmscComponent other = (BmscComponent) obj;
        if (!Objects.equals(this.mName, other.mName)) {
            return false;
        }
        return true;
    }
}
