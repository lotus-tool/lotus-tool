/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ranniery
 */
package br.uece.lotus.tools.modelcheck;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;


public class ParallelCompositor {       
                
    public List compare(Component cA, Component cB){
        List<Transition> sharedActions = new ArrayList<>();
        Iterable<Transition> tA = cA.getTransitions();
        Iterable<Transition> tB = cB.getTransitions();
        for(Transition t1 : tA){
            if(contemTransicao(cB, t1)){
                sharedActions.add(t1);
            }
        }
        return sharedActions;
    }
    
    public boolean contemTransicao(Component c, Transition t){
        Iterable<Transition> transicoes = c.getTransitions();
        for(Transition aux : transicoes){
            if(aux.getLabel().equals(t.getLabel())){
                return true;
            }
        }
        return false;
    }
    
    public boolean contem(List<Transition> l, Transition t){
        for(Transition aux : l){
            if(aux.getLabel().equals(t.getLabel())){
                return true;
            }
        }
        return false;
    }
    
    /*public Transition recuperarTransicao(State a, Transition t){
        for(Transition aux : a.getOutgoingTransitions()){
            if(aux.getLabel().equals(t.getLabel())){
                return aux;
            }
        }
        System.out.println("Transition " + t.getLabel() + " not found in state " + a.getLabel() + "!");
        return null;
    }*/
    
    public static void layout(Component component) {
        int i = 1;
        for (State state : component.getStates()) {
            state.setLayoutX(i * 100);
            state.setLayoutY(300 + (i % 10));
            i++;
        }
    }
    
    public Component compor(Component cA, Component cB) {
        Component PC = new Component();
        PC.setAutoUpdateLabels(false);
        List<Transition> sharedActions = new ArrayList<>();
        Queue<ParallelState> Q = new LinkedList<>();
        List<ParallelState> mVisitedStates = new ArrayList<>();

        //System.out.println("Component A: " + cA.getName()+ " Component B: " + cB.getName());
        
        sharedActions = compare(cA, cB);
        /*System.out.println("Shared actions: ");
        for(Transition action : sharedActions){
            System.out.print(action.getLabel() + " ");
        }
        System.out.println();*/
        ParallelState firstPrlState = new ParallelState(cA.getInitialState(), cB.getInitialState());
        firstPrlState.setCompositeState(criarOuRecuperarEstadoParaOEstadoParalelo(PC, firstPrlState));
        Q.add(firstPrlState);
        
        while (!Q.isEmpty()) {
            ParallelState aux = Q.remove();
            if (mVisitedStates.contains(aux)) {
                continue;
            }
            for (Transition t : aux.getA().getOutgoingTransitions()) {
                //System.out.println("Visiting state " + aux.a.getLabel() + " of LTS A.");
                ParallelState newPrlState = null;
                Transition t2 = aux.getB().getTransitionByLabel(t.getLabel());
                if(!contem(sharedActions, t)){
                    newPrlState = new ParallelState(t.getDestiny(), aux.getB());
                }else{
                    if(t2 != null){
                        newPrlState = new ParallelState(t.getDestiny(), t2.getDestiny());
                    }else{
                        continue;
                    }
                }
                newPrlState.setCompositeState(criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState));

                List<Transition> transitionsTo = aux.getCompositeState().getTransitionsTo(newPrlState.getCompositeState());
                Transition.Builder tt = null;
                if(transitionsTo.size() == 0){
                    tt = PC.buildTransition(aux.getCompositeState(), newPrlState.getCompositeState());
                }else{
                    continue;
                }

                tt.setLabel(t.getLabel());
                tt.setViewType(1);
                Transition transicao = tt.create();
                //System.out.println("Added transition " + t.getLabel() + " .");

                if (!Q.contains(newPrlState)) {
                    Q.add(newPrlState);
                }
                /*if(!contem(sharedActions, t)){
                    ParallelState newPrlState = new ParallelState(t.getDestiny(), aux.b);
                    newPrlState.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState);

                    Transition.Builder tt = PC.buildTransition(aux.compositeState, newPrlState.compositeState);
                    tt.setLabel(t.getLabel()); 
                    tt.setViewType(1);
                    Transition transicao = tt.create();
                    //System.out.println("Added transition " + t.getLabel() + " .");
                
                    if (!Q.contains(newPrlState)) {
                        Q.add(newPrlState);
                    }
                }else{
                    Transition t2 = recuperarTransicao(aux.b, t);
                    if(t2 != null){
                        ParallelState newPrlState = new ParallelState(t.getDestiny(), t2.getDestiny());
                        newPrlState.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState);

                        Transition.Builder tt = PC.buildTransition(aux.compositeState, newPrlState.compositeState);
                        tt.setLabel(t.getLabel()); 
                        tt.setViewType(1);
                        Transition transicao = tt.create();
                        //System.out.println("Added transition " + t.getLabel() + ".");
                
                        if (!Q.contains(newPrlState)) {
                            Q.add(newPrlState);
                        }
                    }
                }*/
                
            }

            for (Transition t : aux.getB().getOutgoingTransitions()) {
                //System.out.println("Visiting state " + aux.b.getLabel() + " of LTS B.");
                ParallelState newPrlState = null;
                Transition t2 = aux.getA().getTransitionByLabel(t.getLabel());

                if(!contem(sharedActions, t)){
                    newPrlState = new ParallelState(aux.getA(), t.getDestiny());
                }else{
                    if(t2 != null){
                        newPrlState = new ParallelState(t2.getDestiny(), t.getDestiny());
                    }else{
                        continue;
                    }
                }

                newPrlState.setCompositeState(criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState));

                List<Transition> transitionsTo = aux.getCompositeState().getTransitionsTo(newPrlState.getCompositeState());
                Transition.Builder tt = null;
                if(transitionsTo.size() == 0){
                    tt = PC.buildTransition(aux.getCompositeState(), newPrlState.getCompositeState());
                }else{
                    continue;
                }
                tt.setLabel(t.getLabel());
                tt.setViewType(1);
                Transition transicao = tt.create();
                //System.out.println("Added transition " + t.getLabel() + ".");

                if (!Q.contains(newPrlState)) {
                    Q.add(newPrlState);
                }
                /*if(!contem(sharedActions, t)){
                    ParallelState newPrlState = new ParallelState(aux.a, t.getDestiny());
                    newPrlState.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState);
                    Transition.Builder tt = PC.buildTransition(aux.compositeState, newPrlState.compositeState);
                    tt.setLabel(t.getLabel());
                    tt.setViewType(1);
                    Transition transicao = tt.create();
                    //System.out.println("Added transition " + t.getLabel() + ".");

                    if (!Q.contains(newPrlState)) {
                        Q.add(newPrlState);
                    }
                }else{
                    Transition t2 = recuperarTransicao(aux.a, t);
                    if(t2 != null){
                        ParallelState newPrlState = new ParallelState(t2.getDestiny(), t.getDestiny());
                        newPrlState.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, newPrlState); 
                        Transition.Builder tt = PC.buildTransition(aux.compositeState, newPrlState.compositeState);
                        tt.setLabel(t.getLabel()); 
                        tt.setViewType(1);
                        Transition transicao = tt.create();
                        //System.out.println("Added transition " + t.getLabel() + ".");
                
                        if (!Q.contains(newPrlState)) {
                            Q.add(newPrlState);
                        }
                    }
                }*/
            }
            mVisitedStates.add(aux);
        }
        layout(PC);
        return PC;
    }

    private State criarOuRecuperarEstadoParaOEstadoParalelo(Component p, ParallelState PrlState) {
        int id = PrlState.getA().getID() * 10 + PrlState.getB().getID();
        State s = p.getStateByID(id);
        if (s == null) {
            s = p.newState(id);
            if (PrlState.getA().isInitial() && PrlState.getB().isInitial()) {
                p.setInitialState(s);
            } else if (PrlState.getA().isFinal() && PrlState.getB().isFinal()) {
                p.setFinalState(s);
            } else if (PrlState.getA().isError() || PrlState.getB().isError()) {
                p.setErrorState(s);
            }
            s.setLabel("<" + PrlState.getA().getID() + ", " + PrlState.getB().getID() + ">");
            //System.out.println("Added state " + s.getLabel() + ".");
        }
        return s;
    }
}


