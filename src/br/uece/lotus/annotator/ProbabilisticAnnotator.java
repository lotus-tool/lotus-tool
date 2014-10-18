/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.annotator;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emerson
 */
public class ProbabilisticAnnotator {
    private State mCurrentState;
    private static final String VISIT_COUNT = "visit.count";

    public void annotate(Component c, InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] trace = line.split(",");
                mCurrentState = c.getInitialState();
                for (String event: trace) {                    
                    step(event.trim());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProbabilisticAnnotator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void step(String event) {        
        Transition ct = mCurrentState.getTransitionByLabel(event);
        if (ct == null) {
            System.out.println("invalid event " + event + " at state " + mCurrentState.getLabel());
            return;
        }
        int updatedStateVisitCount = getStateVisitCount(mCurrentState) + 1;
        setStateVisitCount(mCurrentState, updatedStateVisitCount);
        for (Transition t: mCurrentState.getOutgoingTransitions()) {                        
            int transitionCount = getTransitionCount(t);    
            if (t == ct) {
                transitionCount += 1;
                setTransitionCount(t, transitionCount);
            }
            t.setProbability(((double) transitionCount) / updatedStateVisitCount);
        }
        mCurrentState = ct.getDestiny();
        
    }

    private int getStateVisitCount(State s) {
        Object obj = s.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setStateVisitCount(State s, int value) {
        s.setValue(VISIT_COUNT, value);
    }

    private int getTransitionCount(Transition t) {
        Object obj = t.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setTransitionCount(Transition t, int v) {
        t.setValue(VISIT_COUNT, v);
    }

}
