/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus;

import java.util.ArrayList;
import java.util.List;

public class BigState {

    private State state;
    private List<State> listaStates;
    private List<Transition> listaTransitionsDentro, listaTransitionsForaSaindo, listaTransitionsForaChegando;
    private static final int IN = 1, OUTGOING = 2, INCOMING = 3;
    public static List<BigState> todosOsBigStates = new ArrayList<>();

    public BigState() {
        this.listaStates = new ArrayList<>();
        this.listaTransitionsDentro = new ArrayList<>();
        this.listaTransitionsForaSaindo = new ArrayList<>();
        this.listaTransitionsForaChegando = new ArrayList<>();
    }

    public void doSomething(String name, int id, String deptCode, String regNumber, int id, int id, int id, int id) {
    }

    public boolean addStatesAndTransition(List<State> listaS) {
        if (listaS.size() <= 1) 
            return false;        
        for (State state : listaS) {
            if (state.isInitial())
                return false;            
            listaStates.add(state);
        }
        addTransitions();
        BigState.todosOsBigStates.add(this);
        return true;
    }

    private void addTransitions() {
        for (State state : listaStates) {
            for (State stateAux : listaStates) {
                List<Transition> transitionsTo = state.getTransitionsTo(stateAux);
                //ADD TRANSICOES QUE VAO DE STATE PARA STATEAUX (DENTRO DO RETANGULO)
                for (Transition t : transitionsTo) {
                    t.setPropertyBigState(IN);
                    listaTransitionsDentro.add(t);
                }
            }
        }
        for (State state : listaStates) {
            //PRIMEIRO CASO: SAINDO DELE
            List<Transition> listaSaindo = state.getOutgoingTransitionsList();
            //SEGUNDO CASO: CHEGANDO NELE
            List<Transition> listaChegando = state.getIncomingTransitionsList();
            for (Transition tAux : listaSaindo) {
                State destino = tAux.getDestiny();
                if (!listaStates.contains(destino)) {
                    tAux.setPropertyBigState(OUTGOING);
                    listaTransitionsForaSaindo.add(tAux);
                }
            }
            for (Transition tAux : listaChegando) {
                State origem = tAux.getSource();
                if (!listaStates.contains(origem)) {
                    tAux.setPropertyBigState(INCOMING);
                    listaTransitionsForaChegando.add(tAux);
                }
            }
        }
    }

    public boolean dismountBigState(Component component) {
        if (!canUnmount(component)) {
            return false;
        }
        for (State state : listaStates) {
            component.add(state);
        }
        for (Transition transition : getAllTransitions()) {
            component.add(transition);            
        }        
        listaTransitionsDentro.clear();
        listaTransitionsForaSaindo.clear();
        listaTransitionsForaChegando.clear();
        listaStates.clear();
        BigState.todosOsBigStates.remove(this);
        return true;
    }
    
    public void addState(State state) {
        listaStates.add(state);
    }
    
    public void addTransition(Transition transition, int property) {
        transition.setPropertyBigState(property);
        if (property == IN) {            
            listaTransitionsDentro.add(transition);
        } else if (property == OUTGOING) {
            listaTransitionsForaSaindo.add(transition);
        } else {
            listaTransitionsForaChegando.add(transition);
        }
    }

    private boolean contains(State destino) {
        return listaStates.contains(destino);
    }
    
    private boolean canUnmount(Component component) {
        for (Transition t : listaTransitionsForaSaindo) {
            for (BigState bigState : todosOsBigStates) {
                if (!bigState.equals(this) && bigState.getState().getComponent().equals(component) && bigState.contains(t.getDestiny())) {
                    return false;
                }
            }
        }
        for (Transition t : listaTransitionsForaChegando) {
            for (BigState bigState : todosOsBigStates) {
                if (!bigState.equals(this) && bigState.getState().getComponent().equals(component) && bigState.contains(t.getSource())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void removeBigState(BigState bigState) {
        todosOsBigStates.remove(bigState);
    }

    public static boolean verifyIsBigState(State state) {
        for (BigState big : BigState.todosOsBigStates) {
            if (state.equals(big.getState()) && state.getComponent().equals(big.getState().getComponent())) {
                return true;
            }
        }
        return false;
    }

    public static BigState getBigStateById(int id, Component component) {
        for (BigState bigState : todosOsBigStates) {
            if (id == bigState.state.getID() && bigState.state.getComponent().equals(component)) {
                return bigState;
            }
        }
        return null;
    }
    
    public static void removeStatesComponent() {
        for (BigState bigState : todosOsBigStates) {
            for (State state : bigState.getListaStates()) {
                bigState.getState().getComponent().remove(state);
            }
        }
    }        

    @Override
    public String toString() {
        String retorno = " --- STATES ---\n";
        for (State s : listaStates) {
            retorno += "* LABEL " + s.getLabel() + "ID " + s.getID() + "\n";
        }
        retorno += "--- TRANSIT. DENTRO ---\n* " + listaTransitionsDentro.size() + "\n";
        retorno += "--- TRANSIT. CHEGANDO ---\n* " + listaTransitionsForaChegando.size() + "\n";
        retorno += "--- TRANSIT. SAINDO ---\n* " + listaTransitionsForaSaindo.size() + "\n";
        return retorno;
    }
    
    public List<Transition> getAllTransitions() {
        List<Transition> allTransitions = new ArrayList<>();
        allTransitions.addAll(listaTransitionsDentro);
        allTransitions.addAll(listaTransitionsForaChegando);
        allTransitions.addAll(listaTransitionsForaSaindo);
        return allTransitions;
    }

    public List<State> getListaStates() {
        return listaStates;
    }

    public List<Transition> getListaTransitionsDentro() {
        return listaTransitionsDentro;
    }

    public List<Transition> getListaTransitionsForaSaindo() {
        return listaTransitionsForaSaindo;
    }

    public List<Transition> getListaTransitionsForaChegando() {
        return listaTransitionsForaChegando;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
