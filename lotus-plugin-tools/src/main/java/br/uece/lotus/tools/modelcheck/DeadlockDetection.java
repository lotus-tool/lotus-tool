/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.modelcheck;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ranniery
 */
public class DeadlockDetection {
    
    public List<State> detectDeadlocks (Component a) {
        List<State> deadlocks = new ArrayList<>();
        for(State aux : a.getStates()){
            if(isDeadlock(aux)){
                deadlocks.add(aux);
            }
        }
        return deadlocks;
    }
    
    public boolean isDeadlock (State s){
        if(s.getOutgoingTransitionsCount() == 0 && !s.isFinal()){
            return true;
        }else{
            return false;
        }
    }

}
