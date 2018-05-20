/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ranniery
 */
package br.uece.lotus.model;

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
        String [] t_action = t.getLabel().split("[.]");
        for(Transition aux : transicoes){
            String [] aux_action = aux.getLabel().split("[.]");
            if(aux_action[2].equals( t_action[2] ) ){
                return true;
            }
        }
        return false;
    }
    
    public boolean contem(List<Transition> l, Transition t){
        String [] t_action = t.getLabel().split("[.]");
        for(Transition aux : l){
            String [] aux_action = aux.getLabel().split("[.]");
            if(aux_action[2].equals( t_action[2] )){
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
            state.setLayoutX(i * 200);
            state.setLayoutY(300 + (i % 10));
            i++;
        }
    }
    
    public Component compor(Component cA, Component cB) {


        // CORRIGIR AS SHARED ACTIONS, PEGAR APENAS A AÇÃO SEM O OBJETO QUE A CHAMA


        Component PC = new Component();
        PC.setAutoUpdateLabels(false);
        List<Transition> sharedActions = new ArrayList<>();
        Queue<ParallelState> Q = new LinkedList<>();
        List<ParallelState> mVisitedStates = new ArrayList<>();

        //System.out.println("Component A: " + cA.getName()+ " Component B: " + cB.getName());

        sharedActions = compare(cA, cB); // OK! - Felipe Vidal
        System.out.println("Shared actions: ");
        for(Transition action : sharedActions){
            System.out.print(action.getLabel() + " ");
        }
        System.out.println();
        ParallelState firstPrlState = new ParallelState(cA.getInitialState(), cB.getInitialState()); // Estado ( 0 , 0 )
        firstPrlState.setCompositeState(criarEstadoParalelo(PC, firstPrlState));
        Q.add(firstPrlState);

        while (!Q.isEmpty()) {

            ParallelState aux = Q.remove();
            if (mVisitedStates.contains(aux)) {
                continue;
            }
            for (Transition t : aux.getA().getOutgoingTransitions()) {
                //System.out.println("Visiting state " + aux.a.getLabel() + " of LTS A.");
                ParallelState newPrlState = null;
                Transition t2 = getTransitionByLabel(aux.getB().getOutgoingTransitionsList(),t.getLabel()); // Verifica se B tem a mesma transição saindo que A.
                if(!contem(sharedActions, t)){
                    newPrlState = new ParallelState(t.getDestiny(), aux.getB());
                }else{
                    if(t2 != null){
                        newPrlState = new ParallelState(t.getDestiny(), t2.getDestiny());
                    }else{
                        continue;
                    }
                }

                newPrlState.setCompositeState(criarEstadoParalelo(PC, newPrlState));

                // Até aqui OK! - Felipe vidal
                // Possivel erro nesse bloco, já que ele verifica todo o Componente PC se existe essa transição, porém era pra olhar só no estado
                List<Transition> transitionsTo = aux.getCompositeState().getTransitionsTo(newPrlState.getCompositeState()); // Verifica se o estado composto já tem a transição para esse novo estado
                Transition.Builder tt = null;
               // if(transitionsTo.size() == 0){
                    tt = PC.buildTransition(aux.getCompositeState(), newPrlState.getCompositeState());
                //}else{
                 //   continue;
               // }

                tt.setLabel(t.getLabel());
                if(aux.getCompositeState().getID() > newPrlState.getCompositeState().getID()){
                 tt.setViewType(1);
                }else {
                    tt.setViewType(1);
                }
                Transition transicao = tt.create();
                //System.out.println("Added transition " + t.getLabel() + " .");

                if (!Q.contains(newPrlState)) {
                    Q.add(newPrlState);
                }
            }

            for (Transition t : aux.getB().getOutgoingTransitions()) {
                //System.out.println("Visiting state " + aux.b.getLabel() + " of LTS B.");
                ParallelState newPrlState = null;
                Transition t2 = getTransitionByLabel(aux.getA().getOutgoingTransitionsList(),t.getLabel());

                if(!contem(sharedActions, t)){
                    newPrlState = new ParallelState(aux.getA(), t.getDestiny());
                }else{
                    System.out.println("split [2]: "+PC.getTransitionByAction(t.getLabel().split("[.]")[2]));
                    if(t2 != null && PC.getTransitionByAction(t.getLabel().split("[.]")[2]) == null){
                        newPrlState = new ParallelState(t2.getDestiny(), t.getDestiny());
                    }else{
                        continue;
                    }
                }

                newPrlState.setCompositeState(criarEstadoParalelo(PC, newPrlState));

                List<Transition> transitionsTo = aux.getCompositeState().getTransitionsTo(newPrlState.getCompositeState());
                Transition.Builder tt = null;
             //   if(transitionsTo.size() == 0){
                    tt = PC.buildTransition(aux.getCompositeState(), newPrlState.getCompositeState());
             //   }else{
             //       continue;
             //   }
                tt.setLabel(t.getLabel());
                if(aux.getCompositeState().getID() > newPrlState.getCompositeState().getID()){
                    tt.setViewType(1);

                }else {
                    tt.setViewType(1);
                }
                Transition transicao = tt.create();
                //System.out.println("Added transition " + t.getLabel() + ".");

                if (!Q.contains(newPrlState)) {
                    Q.add(newPrlState);
                }
            }
            mVisitedStates.add(aux); // Indico se o Estado Composto foi Visitado;

        }

        //AjustarIDs(PC);

        layout(PC);
        return PC;
    }

    private void AjustarIDs(Component PC){
        int id;
        for(State state : PC.getStates()){
            id = Integer.parseInt(state.getLabel());
            state.setID(id);
        }
    }

    private Transition getTransitionByLabel(List<Transition> l, String label){
//        List<Transition> list = s.getOutgoingTransitionsList();
        String [] t_action = label.split("[.]");
        for(Transition aux : l){
            String [] aux_action = aux.getLabel().split("[.]");
            if(aux_action[2].equals( t_action[2] )){
                return aux;
            }
        }
        return null;
    }

    private State criarEstadoParalelo(Component p, ParallelState PrlState) {
        int id = PrlState.getA().getID() * 10 + PrlState.getB().getID();
        System.out.println("ID é: "+id+"\tA é: "+PrlState.getA().getLabel()+"\tB é: "+PrlState.getB().getLabel());
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
            System.out.println("Added state " + s.getLabel() + "."+"\t ID: "+s.getID());
        }
        return s;
    }
}


