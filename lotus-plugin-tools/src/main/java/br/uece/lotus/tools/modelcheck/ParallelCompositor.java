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
    
    public Transition recuperarTransicao(State a, Transition t){
        for(Transition aux : a.getOutgoingTransitions()){
            if(aux.getLabel().equals(t.getLabel())){
                return aux;
            }
        }
        System.out.println("Transition " + t.getLabel() + " not found in state " + a.getLabel() + "!");
        return null;
    }
    
    public void layout(Component component) {
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

        System.out.println("Component A: " + cA.getName()+ " Component B: " + cB.getName());
        
        sharedActions = compare(cA, cB);
        System.out.println("Shared actions: ");
        for(Transition action : sharedActions){
            System.out.print(action.getLabel() + " ");
        }
        System.out.println();
        ParallelState ps = new ParallelState(cA.getInitialState(), cB.getInitialState());
        ps.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, ps);
        Q.add(ps);
        
        while (!Q.isEmpty()) {
            ParallelState aux = Q.remove();
            if (mVisitedStates.contains(aux)) {
                continue;
            }
            for (Transition t : aux.a.getOutgoingTransitions()) {
                System.out.println("Visiting state " + aux.a.getLabel() + " of LTS A.");
                if(!contem(sharedActions, t)){
                    ParallelState foo = new ParallelState(t.getDestiny(), aux.b);
                    foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, foo);

                    Transition.Builder tt = PC.buildTransition(aux.compositeState, foo.compositeState);
                    tt.setLabel(t.getLabel()); 
                    tt.setViewType(1);
                    Transition transicao = tt.create();
                    System.out.println("Added transition " + t.getLabel() + " .");
                
                    if (!Q.contains(foo)) {
                        Q.add(foo);
                    }
                }else{
                    Transition t2 = recuperarTransicao(aux.b, t);
                    if(t2 != null){
                        ParallelState foo = new ParallelState(t.getDestiny(), t2.getDestiny());
                        foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, foo);

                        Transition.Builder tt = PC.buildTransition(aux.compositeState, foo.compositeState);
                        tt.setLabel(t.getLabel()); 
                        tt.setViewType(1);
                        Transition transicao = tt.create();
                        System.out.println("Added transition " + t.getLabel() + ".");
                
                        if (!Q.contains(foo)) {
                            Q.add(foo);
                        }
                    }
                }
                
            }

            for (Transition t : aux.b.getOutgoingTransitions()) {
                System.out.println("Visiting state " + aux.b.getLabel() + " of LTS B.");
                if(!contem(sharedActions, t)){
                    ParallelState foo = new ParallelState(aux.a, t.getDestiny());
                    foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, foo);
                    Transition.Builder tt = PC.buildTransition(aux.compositeState, foo.compositeState);
                    tt.setLabel(t.getLabel()); 
                    tt.setViewType(1);
                    Transition transicao = tt.create();
                    System.out.println("Added transition " + t.getLabel() + ".");
                
                    if (!Q.contains(foo)) {
                        Q.add(foo);
                    }
                }else{
                    Transition t2 = recuperarTransicao(aux.a, t);
                    if(t2 != null){
                        ParallelState foo = new ParallelState(t2.getDestiny(), t.getDestiny());
                        foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(PC, foo); 
                        Transition.Builder tt = PC.buildTransition(aux.compositeState, foo.compositeState);
                        tt.setLabel(t.getLabel()); 
                        tt.setViewType(1);
                        Transition transicao = tt.create();
                        System.out.println("Added transition " + t.getLabel() + ".");
                
                        if (!Q.contains(foo)) {
                            Q.add(foo);
                        }
                    }
                }
            }
            mVisitedStates.add(aux);
        }
        layout(PC);
        return PC;
    }

    private State criarOuRecuperarEstadoParaOEstadoParalelo(Component p, ParallelState ps) {
        int id = ps.a.getID() * 10 + ps.b.getID();
        State s = p.getStateByID(id);
        if (s == null) {
            s = p.newState(id);
            if (ps.a.isInitial() && ps.b.isInitial()) {
                p.setInitialState(s);
            } else if (ps.a.isFinal() && ps.b.isFinal()) {
                p.setFinalState(s);
            } else if (ps.a.isError() || ps.b.isError()) {
                p.setErrorState(s);
            }
            s.setLabel("<" + ps.a.getID() + ", " + ps.b.getID() + ">");  
            System.out.println("Added state " + s.getLabel() + ".");
        }
        return s;
    }
}


