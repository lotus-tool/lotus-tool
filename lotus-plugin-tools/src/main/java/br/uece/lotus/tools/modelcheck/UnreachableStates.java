/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.tools.modelcheck;

/**
 *
 * @author Ranniery
 */

import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collection;
import br.uece.lotus.viewer.TransitionViewFactory;
import br.uece.lotus.tools.modelcheck.DeadlockDetection;

/**
 *
 * @author Ranniery
 */
public class UnreachableStates extends Plugin {
    
    public List<State> detectUnreachableStates (Component a) {
        List<State> unreachables = new ArrayList<>();
        
        for(State aux : a.getStates()){
            if(isUnreachable(aux)){
                unreachables.add(aux);
            }
        }
        
        return unreachables;
    }

    public void remove (List<State> unreachables, Component a){
        List<State> unreachablePath = unreachables;
        
        while(!unreachablePath.isEmpty()){
            for(State aux : unreachablePath){
                a.remove(aux);
            }
//            int allStates = a.getStatesCount();
//            int allTransitions = a.getTransitionsCount();
            unreachablePath = detectUnreachableStates(a);
        }
    }
    
    
    public boolean isUnreachable (State s){
        if(s.getIncomingTransitionsCount() == 0 && !s.isInitial()){
            return true;
        }else{
            return false;
        }
    }
}
