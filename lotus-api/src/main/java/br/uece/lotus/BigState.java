/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.BasicComponentViewer;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import br.uece.lotus.viewer.View;
import java.util.ArrayList;
import java.util.List;

public class BigState {
    
    private State state;    
    private List<State> listaStates;
    private List<Transition> listaTransitionsDentro;
    private List<Transition> listaTransitionsForaSaindo;
    private List<Transition> listaTransitionsForaChegando;
    public static List<BigState> todosOsBigStates = new ArrayList<>();

    public BigState() {
        this.listaStates = new ArrayList<>();
        this.listaTransitionsDentro = new ArrayList<>();
        this.listaTransitionsForaSaindo = new ArrayList<>();
        this.listaTransitionsForaChegando = new ArrayList<>();
    }

    public boolean addStatesAndTransition(List<State> listaS) {
        if (listaS.size() <= 1) {
            return false;
        }
        for (State state : listaS) {
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
                    listaTransitionsForaSaindo.add(tAux);
                }
            }
            for (Transition tAux : listaChegando) {
                State origem = tAux.getSource();
                if (!listaStates.contains(origem)) {                    
                    listaTransitionsForaChegando.add(tAux);
                }
            }
        }
    }

    public boolean dismountBigState(BasicComponentViewer mviewer) {
        if (!canUnmount()) {
            return false;
        }
        List<State> statesAuxs = (List<State>) mviewer.getComponent().getStates();
        List<Transition> transitionsAux = (List<Transition>) mviewer.getComponent().getTransitions();
        for (State state : listaStates) {
            mviewer.getComponent().add(state);
        }
        for (Transition transition : listaTransitionsDentro) {
            mviewer.getComponent().add(transition);
        }
        for (Transition transition : listaTransitionsForaSaindo) {
            mviewer.getComponent().add(transition);
        }
        for (Transition transition : listaTransitionsForaChegando) {
            mviewer.getComponent().add(transition);
        }
        listaTransitionsDentro.clear();
        listaTransitionsForaSaindo.clear();
        listaTransitionsForaChegando.clear();
        listaStates.clear();
        BigState.todosOsBigStates.remove(this);
        return true;
    }

    public boolean hasStateTransitionSelectedInside(View mComponentSelecionado, List<State> listaS) {
        State sAux = null;
        Transition tAux = null;
        if (mComponentSelecionado instanceof StateView) {
            sAux = ((StateView) mComponentSelecionado).getState();
            if (listaS.contains(sAux)) {
                return true;
            }
        } else {
            if (mComponentSelecionado instanceof TransitionView) {
                tAux = ((TransitionView) mComponentSelecionado).getTransition();
                if (listaTransitionsDentro.contains(tAux) || listaTransitionsForaChegando.contains(tAux)
                        || listaTransitionsForaSaindo.contains(tAux)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canUnmount() {
        for (Transition t : listaTransitionsForaSaindo) {
            for (BigState bigState : todosOsBigStates) {
                if (bigState.contains(t.getDestiny())) {
                    return false;
                }
            }
        }
        for (Transition t : listaTransitionsForaChegando) {
            for (BigState bigState : todosOsBigStates) {
                if (bigState.contains(t.getSource())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void removeBigState(BigState bigState){
        todosOsBigStates.remove(bigState);
    }
    
    public static boolean verifyIsBigState(State state) {
        for (BigState big : BigState.todosOsBigStates) {
            if (state.equals(big.getState())) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(State destino) {
        return listaStates.contains(destino);
    }

    @Override
    public String toString() {
        String retorno = " --- States do BigState ---\n";
        for (State s : listaStates) {
            retorno += "* State Label " + s.getLabel() + "ID " + state.getID() + "\n";
        }
        retorno += "--- Transitions Dentro ---\n* Quantidade " + listaTransitionsDentro.size() + "\n";
        retorno += "--- Transitions Fora Chegando ---\n* Quantidade " + listaTransitionsForaChegando.size() + "\n";
        retorno += "--- Transitions Fora Saindo ---\n* Quantidade " + listaTransitionsForaSaindo.size() + "\n";
        return retorno;
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
    
    public State getState(){
        return this.state;
    }
    
    public void setState(State state){
        this.state = state;
    }

}
