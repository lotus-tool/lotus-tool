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

public class Component {

    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;

    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
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
    private State mFinalState;
    private State mErrorState;
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

    public Iterable<State> getStates() {
        return mStates;
    }

    public int getStatesCount() {
        return mStates.size();
    }

    public int getStateIndex(State s) {
        return mStates.indexOf(s);
    }

    public Iterable<Transition> getTransitions() {
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
        for (Transition t : state.getOutgoingTransitions()) {
            mTransitions.remove(t);
        }
        for (Transition t : state.getIncomingTransitions()) {
            mTransitions.remove(t);
        }
        mStates.remove(state);
        for (Listener l : mListeners) {
            l.onStateRemoved(this, state);
        }
        if (state.isInitial()) {
            if (mStates.size() > 0) {
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
            v.setLabel(String.valueOf(i++));
        }
    }

    public void remove(Transition transition) {
        transition.getSource().removeIncomingTransition(transition);
        transition.getDestiny().removeOutgoingTransition(transition);
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

    @Override
    public Component clone() throws CloneNotSupportedException {
        Component c = new Component();
        c.mName = mName;

        List<State> unvisitedStates = new ArrayList<>(mStates);
        List<State> stack = new ArrayList<>();

        while (!unvisitedStates.isEmpty()) {
            stack.add(unvisitedStates.get(0));
            while (!stack.isEmpty()) {
                State s = stack.remove(0);
                State ss = c.getStateByID(s.getID());
                if (ss == null) {
                    ss = c.newState(s.getID());
                    ss.copy(s);
                    c.mStates.add(ss);
                }
                unvisitedStates.remove(s);
                for (Transition t : s.getOutgoingTransitions()) {
                    State ts = t.getDestiny();
                    State tss;
                    if (unvisitedStates.contains(ts)) {
                        tss = c.getStateByID(ts.getID());
                        if (tss == null) {
                            tss = c.newState(ts.getID());
                            tss.copy(ts);
                            c.mStates.add(tss);
                        }
                        unvisitedStates.remove(ts);
                        stack.add(0, ts);
                    } else {
                        tss = c.getStateByID(ts.getID());
                    }
                    Transition tt = c.newTransition(ss, tss);
                    tt.setLabel(t.getLabel());
                    tt.setProbability(t.getProbability());
                    tt.setGuard(t.getGuard());
                    tt.setValue("view.type", t.getValue("view.type"));
                }
            }
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

}
