/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Component {

    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;

    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

    private void copyState(State from, State to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
        to.setLayoutY(from.getLayoutY());

        if (from.isError()) {
            to.setError(true);
        } else if (from.isFinal()) {
            to.setFinal(true);
        } else if (from.isInitial()) {
            to.setAsInitial();
        }
        if (from.isBig()) {
            to.setBig(true);
        }
    }

    private void copyTransition(Transition from, Transition to) {
        to.setLabel(from.getLabel());
        to.setProbability(from.getProbability());
        to.setGuard(from.getGuard());
        to.setValue("view.type", from.getValue("view.type"));
    }

    public interface Listener {

        void onChange(Component component);

        void onStateCreated(Component component, State state);

        void onStateRemoved(Component component, State state);

        void onTransitionCreated(Component component, Transition state);

        void onTransitionRemoved(Component component, Transition state);
    }

    private String mName;
    private State mInitialState;
    //This attributes should be lists os states.
    private State mFinalState;
    private State mErrorState;
    private int id;

    private final List<State> mStates = new ArrayList<>();
    private final List<Transition> mTransitions = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public int getID(){
        return this.id;
    }
    public void setID(int id ){
        this.id = id;
    }

    public Iterable<State> getStates() {
        return mStates;
    }

    public List<State> getStatesList(){ return mStates;}

    public int getStatesCount() {
        return mStates.size();
    }

    public int getBigStatesCount() {
        int count = 0;
        for (State s : mStates) {
            if (s.getValue("bigstate") != null) {
                count++;
            }
        }
        return count;
    }

    public int getStateIndex(State s) {
        return mStates.indexOf(s);
    }

    public Iterable<Transition> getTransitions() {
        return mTransitions;
    }

    public List<Transition> getTransitionsList(){
        return mTransitions;
    }

    public int getTransitionsCount() {
        return mTransitions.size();
    }

    public State getStateByID(int id) {
        for (State v : mStates) {
            if (v.getID() == id) {
                return v;
            }
        }
        return null;
    }

    public State newState(int id) {
        State v = new State(this);
        v.setID(id);
        add(v);
        if (mInitialState == null) {
            setInitialState(v);
        }
        return v;
    }

    public void add(State v) {
        mStates.add(v);
        updateStateLabels();
        for (Listener l : mListeners) {
            l.onStateCreated(this, v);
        }
    }

    public void add(Transition t) {
        t.getSource().addOutgoingTransition(t);
        t.getDestiny().addIncomingTransition(t);
        mTransitions.add(t);
        for (Listener l : mListeners) {
            l.onTransitionCreated(this, t);
        }
    }

    public Transition newTransition(State src, State dst) {
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
    }

    public void remove(State state) {
        List<Transition> transitions = new ArrayList<>();
        transitions.addAll(state.getOutgoingTransitionsList());
        transitions.addAll(state.getIncomingTransitionsList());
        for (Transition t : transitions) {
            remove(t);
        }

        mStates.remove(state);
        for (Listener l : mListeners) {
            l.onStateRemoved(this, state);
        }
        if (state.isInitial()) {
            if (!mStates.isEmpty() ) {
                setInitialState(mStates.get(0));
            }
        }
        updateStateLabels();
    }

    private void updateStateLabels() {
        if (!mAutoUpdateLabels) {
            return;
        }
        int i = 0;
        for (State v : mStates) {
            v.setID(i);
            v.setLabel(String.valueOf(i++));
        }
    }

    public Transition getTransitionByLabel(String label) {
        for (Transition transition : mTransitions) {
            if (label.equals(transition.getLabel())) {
                return transition;
            }
        }
        return null;
    }

    public Transition getTransitionByAction(String action){
        for (Transition t : mTransitions){
            String [] t_action = t.getLabel().split("[.]");
            if(action.equals(t_action[2])){
                return t;
            }
        }
        return null;
    }

    public void remove(Transition transition) {
        transition.getSource().removeOutgoingTransition(transition);
        transition.getDestiny().removeIncomingTransition(transition);
        mTransitions.remove(transition);
        for (Listener l : mListeners) {
            l.onTransitionRemoved(this, transition);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    public State getInitialState() {
        return mInitialState;
    }

    public void setInitialState(State initialState) {
        if (mInitialState != null) {
            mInitialState.markInitial(false);
        }
        mInitialState = initialState;
        if (mInitialState != null) {
            mInitialState.markInitial(true);
        }
    }

    public State getErrorState() {
        return mErrorState;
    }

    public void setErrorState(State errorState) {
        if (mErrorState != null) {
            mErrorState.setError(false);
        }
        mErrorState = errorState;
        if (mErrorState != null) {
            mErrorState.setError(true);
        }
    }

    public State getFinalState() {
        return mFinalState;
    }

    public void setFinalState(State finalState) {
        if (mFinalState != null) {
            mFinalState.setFinal(false);
        }
        mFinalState = finalState;
        if (mFinalState != null) {
            mFinalState.setFinal(true);
        }
    }

    public void clearVisitedStatesCount() {
        for (State s : mStates) {
            s.setmVisitedStatesCount(0);
        }
    }

    @Override
    public Component clone() throws CloneNotSupportedException {
        Component c = new Component();
        c.mName = mName;
        c.mAutoUpdateLabels = mAutoUpdateLabels;
        for (State oldState : mStates) {
            State newState = c.newState(oldState.getID());
            copyState(oldState, newState);
        }
        for (Transition oldTransition : mTransitions) {
            int src = oldTransition.getSource().getID();
            int dst = oldTransition.getDestiny().getID();
            Transition newTransition = c.newTransition(src, dst);
            copyTransition(oldTransition, newTransition);
        }

        if (mInitialState != null) {
            c.setInitialState(c.getStateByID(mInitialState.getID()));
        }
        if (mFinalState != null) {
            c.setFinalState(c.getStateByID(mFinalState.getID()));
        }
        if (mErrorState != null) {
            c.setErrorState(c.getStateByID(mErrorState.getID()));
        }
        return c;
    }

    public void setAutoUpdateLabels(boolean value) {
        mAutoUpdateLabels = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.mName);
        hash = 59 * hash + Objects.hashCode(this.mInitialState);
        hash = 59 * hash + Objects.hashCode(this.mFinalState);
        hash = 59 * hash + Objects.hashCode(this.mErrorState);
        hash = 59 * hash + Objects.hashCode(this.mStates);
        hash = 59 * hash + Objects.hashCode(this.mTransitions);
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
        final Component other = (Component) obj;
        if (!Objects.equals(this.mName, other.mName)) {
            return false;
        }
        if (!Objects.equals(this.mInitialState, other.mInitialState)) {
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
        }
        return true;
    }

}
