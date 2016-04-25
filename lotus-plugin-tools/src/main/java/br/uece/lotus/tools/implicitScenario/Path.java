package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.State;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.util.ArrayList;

/**
 * Created by lucas on 17/04/16.
 */
public class Path {

    private ArrayList<State> states = new ArrayList<State>();

    private  ArrayList<String> traces = new ArrayList<String>();

    public void addWay(State state, String string ){
        states.add(state);
        traces.add(string);
    }

    public ArrayList<String> getTraces() {
        return traces;
    }

    public ArrayList<State> getStates() {
        return states;
    }


}


