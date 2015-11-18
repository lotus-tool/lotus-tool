package br.uece.lotus.tools.modelcheck;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.seed.ext.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ProbabilitiesCheck extends Plugin {

    public List<State> checkProbabilities(Component a){
        List<State> inconsistentStates = new ArrayList<>();

        for(State aux : a.getStates()){
            if(isInconsistent(aux)){
                inconsistentStates.add(aux);
            }
        }

        return inconsistentStates;
    }

    public boolean isInconsistent (State s){
        List<Transition> transitions = s.getOutgoingTransitionsList();
        int transitionsCount = s.getOutgoingTransitionsCount();
        double sum = 0;

        if(!(isProbabilisticState(s))){
            return false;
        }

        for(Transition aux : transitions){
            try{
                aux.getProbability().toString();
                sum += aux.getProbability();
            } catch (NullPointerException e){
                return true;
            }
        }

        if( (sum < 0.9999 || sum > 1) && transitionsCount > 0){
            return true;
        }else{
            return false;
        }

    }

    public boolean isProbabilisticState (State s){
        int transitionsCount = 0;
        for(Transition t : s.getOutgoingTransitionsList()){
            try{
                t.getProbability().toString();
            } catch (NullPointerException e){
                transitionsCount += 1;
                if(transitionsCount == s.getOutgoingTransitionsCount()){
                    return false;
                }
            }
        }
        return true;
    }

    /*public boolean isDouble (String str){
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }*/

}
