/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import br.uece.lotus.Component;

import java.util.*;

/**
 * @author Bruno Barbosa
 */
public class ComponentDS {

    private List<Component> mComponentsLTS = new ArrayList<>();
    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;
    private String mName;

    public interface Listener {
        void onChange(ComponentDS cds);
        void onBlockDSCreated(ComponentDS componentDS, BlockDS state);
        void onBlockDSRemoved(ComponentDS componentDS, BlockDS state);
        //falta adicionar os outros listener. Quando for adicionar me avisa, pq tem que implementar o restante
        //que falta nas outras classes que dependen desses listener. se nao ja vai dar erro na compilacao ou na abertura
        //da tela
    }



    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }
    private void copyBlockDS(BlockDS from, BlockDS to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
//        to.setLayoutY(from.getLayoutY());
    }

    public List<Component> getmComponentsLTS() {return mComponentsLTS;}
    public void setmComponentsLTS(List<Component> mComponentsLTS) {
        this.mComponentsLTS = mComponentsLTS;
    }
    public void addComponentLTS(Component c) {
        mComponentsLTS.add(c);
    }
    public void removeComponentLTS(Component c) {
        mComponentsLTS.remove(c);
    }

///////////////////////////////////////////////////////////////////////////
    //    public void setName(String s) {
//        this.mName = s;
//    }
//
//    public String getName() {
//        return this.mName;
//    }
//
//    public void addListener(Listener l) {
//        mListeners.add(l);
//    }
//
//    public void removeListener(Listener l) {
//        mListeners.remove(l);
//    }
///////////////////////////////////////////////////////////////////////

    private final List<BlockDS> mBlockDSs = new ArrayList<>();
    /*  private final List<Transition> mTransitions = new ArrayList<>();*/
    private final List<Listener> mListeners = new ArrayList<>();

    public String getName() {return mName;}

    public Iterable<BlockDS> getBlockDS() {return mBlockDSs;}

    public int getBlockDSCount() {return mBlockDSs.size();}

    public int getBlockDSIndex(BlockDS s) {return mBlockDSs.indexOf(s);}

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }


   /* public Iterable<Transition> getTransitions() {
        return mTransitions;
    }*/

   /* public int getTransitionsCount() {
        return mTransitions.size();
    }*/


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

   /* public void add(Transition t) {
        t.getSource().addOutgoingTransition(t);
        t.getDestiny().addIncomingTransition(t);
        mTransitions.add(t);
        for (Listener l : mListeners) {
            l.onTransitionCreated(this, t);
        }
    }*/

    /*public Transition newTransition(State src, State dst) {
        if (src == null) {
            throw new IllegalArgumentException("src state can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst state can't be null!");
        }
        Transition t = new Transition(src, dst);
        add(t);
        return t;
    }

    public Transition newTransition(int idSrc, int idDst) {
        return newTransition(getStateByID(idSrc), getStateByID(idDst));
    }

    public Transition.Builder buildTransition(State src, State dst) {
        if (src == null) {
            throw new IllegalArgumentException("src state can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst state can't be null!");
        }
        Transition t = new Transition(src, dst);
        return new Transition.Builder(this, t);
    }

    public Transition.Builder buildTransition(int idSrc, int idDst) {
        Transition t = new Transition(getStateByID(idSrc), getStateByID(idDst));
        return new Transition.Builder(this, t);
    }*/

    public void remove(BlockDS ds) {
        /*List<Transition> transitions = new ArrayList<>();
        transitions.addAll(ds.getOutgoingTransitionsList());
        transitions.addAll(ds.getIncomingTransitionsList());
        for (Transition t : transitions) {
            remove(t);
        }*/

        mBlockDSs.remove(ds);
        for (Listener l : mListeners) {
            l.onBlockDSRemoved(this, ds);
        }
        /*if (ds.isInitial()) {
            if (mStates.size() > 0) {
                setInitialState(mStates.get(0));
            }
        }*/
//        updateStateLabels();
    }

//    private void updateStateLabels() {
//        if (!mAutoUpdateLabels) {
//            return;
//        }
//        int i = 0;
//        for (BlockDS v : mBlockDSs) {
//            v.setLabel(String.valueOf(i++));
//        }
//    }

   /* public Transition getTransitionByLabel(String label){
        for(Transition transition : mTransitions){
            if(label.equals(transition.getLabel())){
                return transition;
            }
        }
        return null;
    }*/

   /* public void remove(Transition transition) {
        transition.getSource().removeOutgoingTransition(transition);
        transition.getDestiny().removeIncomingTransition(transition);
        mTransitions.remove(transition);
        for (Listener l : mListeners) {
            l.onTransitionRemoved(this, transition);
        }
    }*/

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
       /* for (Transition oldTransition : mTransitions) {
            int src = oldTransition.getSource().getID();
            int dst = oldTransition.getDestiny().getID();
            Transition newTransition = c.newTransition(src, dst);
            copyTransition(oldTransition, newTransition);
        }*/

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
        /*if (!Objects.equals(this.mInitialState, other.mInitialState)) {
            return false;
        }
        if (!Objects.equals(this.mFinalState, other.mFinalState)) {
            return false;
        }
        if (!Objects.equals(this.mErrorState, other.mErrorState)) {
            return false;
        }
        if (!Objects.equals(this.mStates, other.mStates)) {
            return false;
        }
        if (!Objects.equals(this.mTransitions, other.mTransitions)) {
            return false;
        }*/
        return true;
    }
}
