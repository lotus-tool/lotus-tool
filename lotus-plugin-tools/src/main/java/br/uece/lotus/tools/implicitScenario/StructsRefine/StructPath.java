package br.uece.lotus.tools.implicitScenario.StructsRefine;

import br.uece.lotus.State;
import br.uece.lotus.Transition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lucas on 12/04/16.
 */
public class StructPath {


    private ArrayList<Transition> path = new ArrayList<>();
    private State lastState;
    private HashMap<String, State> creator = new HashMap<>();

    public ArrayList<Transition> getPath() {
        return path;
    }

    public void add(Transition t) {
        path.add(t);
    }

    public State getLastState() {
        if (path.size() == 0) {
            return lastState;
        } else {
            Transition lastTransitionOnPath = path.get(path.size() - 1);
            lastState = lastTransitionOnPath.getDestiny();
            return lastState;
        }
    }

    public boolean containState(State s) {
        for (Transition t : path) {
            if (t.getSource().equals(s) || t.getDestiny().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void setLastStateOnPath(State s) {
        lastState = s;

    }

    public boolean containStateRepeated() {
        State initialState;
        if (path.size() == 0) {
            initialState = lastState;
        } else {
            Transition fistTransitionOnPath = path.get(0);
            initialState = fistTransitionOnPath.getSource();
        }
        for (Transition t : path) {
            State dst = t.getDestiny();
            if (initialState.equals(dst)) {
                return true;
            }
        }

        for (Transition t : path) {
            State dst = t.getDestiny();
            if (containState(dst)) {
                return true;
            }

        }
        return false;

    }


}
