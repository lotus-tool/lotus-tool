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
                
    public List<Transition> compare(Component component1, Component component2){
        List<Transition> sharedActions = new ArrayList<>();
        Iterable<Transition> transitionsFromComponent1 = component1.getTransitions();
        Iterable<Transition> transitionsFromComponent2 = component2.getTransitions();
        for(Transition transitionFromComponent1 : transitionsFromComponent1){

            if(thereIsTransitionInComponent(transitionFromComponent1, component2)){
                sharedActions.add(transitionFromComponent1);
            }
        }
        return sharedActions;
    }
    
    public boolean thereIsTransitionInComponent( Transition transition, Component c){
        Iterable<Transition> transicoes = c.getTransitions();
        String [] t_action = transition.getLabel().split("[.]");
        try{
            for(Transition aux : transicoes){
                String [] aux_action = aux.getLabel().split("[.]");
                if(aux_action[2].equals( t_action[2] ) ){
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException e){
            //System.out.println("");
            for(Transition aux : transicoes){
                if(aux.getLabel().equals( transition.getLabel() ) ){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean contem(List<Transition> l, Transition t){
        String [] t_action = t.getLabel().split("[.]");
        try{
            for(Transition aux : l){
                String [] aux_action = aux.getLabel().split("[.]");
                if(aux_action[2].equals( t_action[2] )){
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException e){
            for(Transition aux : l){
                if(aux.getLabel().equals( t.getLabel() )){
                    return true;
                }
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
    
    public Component compor(Component component1, Component component2) {


        // CORRIGIR AS SHARED ACTIONS, PEGAR APENAS A AÇÃO SEM O OBJETO QUE A CHAMA


        Component newComponent = new Component();
        newComponent.setAutoUpdateLabels(false);
        List<Transition> sharedActions;
        Queue<ParallelState> parallelStatesQueue = new LinkedList<>();
        List<ParallelState> visitedParallelStates = new ArrayList<>();

        sharedActions = compare(component1, component2); // OK! - Felipe Vidal

      //  System.out.println("Shared actions: ");
        for(Transition action : sharedActions){
            System.out.print(action.getLabel() + " ");
        }
     //   System.out.println("");

        ParallelState firstParallelState = new ParallelState(component1.getInitialState(), component2.getInitialState()); // Estado ( 0 , 0 )
        firstParallelState.setCompositeState(buildParallelState(newComponent, firstParallelState));
        parallelStatesQueue.add(firstParallelState);

        while (!parallelStatesQueue.isEmpty()) {

            ParallelState currentParallelState = parallelStatesQueue.remove();
            if (visitedParallelStates.contains(currentParallelState)) {
                continue;
            }

            for (Transition transitionOutFromFirstState : currentParallelState.getFirstState().getOutgoingTransitions()) {

                ParallelState newParalledState = null;

                Transition t2 = getTransitionByLabel(currentParallelState.getSecondState().getOutgoingTransitionsList(),
                        transitionOutFromFirstState.getLabel()); // Verifica se B tem a mesma transição saindo que A.

                if(!contem(sharedActions, transitionOutFromFirstState)){
                    newParalledState = new ParallelState(transitionOutFromFirstState.getDestiny(), currentParallelState.getSecondState());
                }else{
                    if(t2 != null){
                        newParalledState = new ParallelState(transitionOutFromFirstState.getDestiny(), t2.getDestiny());
                    }else{
                        continue;
                    }
                }

                newParalledState.setCompositeState(buildParallelState(newComponent, newParalledState));

                // Até aqui OK! - Felipe vidal
                // Possivel erro nesse bloco, já que ele verifica todo o Componente newComponent se existe essa transição, porém era pra olhar só no estado
                List<Transition> transitionsTo = currentParallelState.getCompositeState().getTransitionsTo(newParalledState.getCompositeState()); // Verifica se o estado composto já tem a transição para esse novo estado
                Transition.Builder tt = null;
               // if(transitionsTo.size() == 0){
                    tt = newComponent.buildTransition(currentParallelState.getCompositeState(), newParalledState.getCompositeState());
                //}else{
                 //   continue;
               // }

                tt.setLabel(transitionOutFromFirstState.getLabel());
                tt.setGuard(transitionOutFromFirstState.getGuard());
                tt.setParameters(transitionOutFromFirstState.getParameters());
                tt.setActions(transitionOutFromFirstState.getActions());

                if(currentParallelState.getCompositeState().getID() > newParalledState.getCompositeState().getID()){
                 tt.setViewType(1);
                }else {
                    tt.setViewType(1);
                }
                Transition transicao = tt.create();
                //System.out.println("Added transitionOutFromFirstState " + transitionOutFromFirstState.getLabel() + " .");

                if (!parallelStatesQueue.contains(newParalledState)) {
                    parallelStatesQueue.add(newParalledState);
                }
            }

            for (Transition t : currentParallelState.getSecondState().getOutgoingTransitions()) {
                //System.out.println("Visiting state " + currentParallelState.b.getLabel() + " of LTS B.");
                ParallelState newPrlState = null;
                Transition t2 = getTransitionByLabel(currentParallelState.getFirstState().getOutgoingTransitionsList(),t.getLabel());

                if(!contem(sharedActions, t)){
                    newPrlState = new ParallelState(currentParallelState.getFirstState(), t.getDestiny());
                }else{

                    if(t2 != null && newComponent.getTransitionByAction(t.getLabel().split("[.]")[2]) == null){
                        newPrlState = new ParallelState(t2.getDestiny(), t.getDestiny());
                    }else{
                        continue;
                    }

                }

                newPrlState.setCompositeState(buildParallelState(newComponent, newPrlState));

                List<Transition> transitionsTo = currentParallelState.getCompositeState().getTransitionsTo(newPrlState.getCompositeState());
                Transition.Builder tt = null;
             //   if(transitionsTo.size() == 0){
                    tt = newComponent.buildTransition(currentParallelState.getCompositeState(), newPrlState.getCompositeState());
             //   }else{
             //       continue;
             //   }
                tt.setLabel(t.getLabel());
                tt.setGuard(t.getGuard());
                tt.setActions(t.getActions());
                tt.setParameters(t.getParameters());
                if(currentParallelState.getCompositeState().getID() > newPrlState.getCompositeState().getID()){
                    tt.setViewType(1);

                }else {
                    tt.setViewType(1);
                }
                Transition transicao = tt.create();
                //System.out.println("Added transition " + t.getLabel() + ".");

                if (!parallelStatesQueue.contains(newPrlState)) {
                    parallelStatesQueue.add(newPrlState);
                }
            }
            visitedParallelStates.add(currentParallelState); // Indico se o Estado Composto foi Visitado;

        }

        AjustarIDs(newComponent);

        layout(newComponent);
        return newComponent;
    }

    private void AjustarIDs(Component PC){
        int newId =0;
        for(State state : PC.getStates()){
            state.setID(newId);
            newId++;
        } }

    private  Transition getTransitionByLabel(Component component, String label){
        String [] t_action = label.split("[.]");
        try{
            for(Transition aux : component.getTransitions()){
                String [] aux_action = aux.getLabel().split("[.]");
                if(aux_action[2].equals( t_action[2] )){
                    return aux;
                }
            }
        } catch (IndexOutOfBoundsException e){
            for(Transition aux : component.getTransitions()){
                if(aux.getLabel().equals(label)){
                    return aux;
                }
            }
        }
        return null;
    }

    private Transition getTransitionByLabel(List<Transition> transitionList, String label){
//        List<Transition> list = s.getOutgoingTransitionsList();
        String [] t_action = label.split("[.]");
        try{
            for(Transition aux : transitionList){
                String [] aux_action = aux.getLabel().split("[.]");
                if(aux_action[2].equals( t_action[2] )){
                    return aux;
                }
            }
        } catch (IndexOutOfBoundsException e){
            for(Transition aux : transitionList){
                if(aux.getLabel().equals(label)){
                    return aux;
                }
            }
        }
        return null;
    }

    private State buildParallelState(Component p, ParallelState PrlState) {
        int id = PrlState.getFirstState().getID() * 10 + PrlState.getSecondState().getID();
       // System.out.println("ID é: "+id+"\tA é: "+PrlState.getFirstState().getLabel()+"\tB é: "+PrlState.getSecondState().getLabel());
        State s = p.getStateByID(id);
        if (s == null) {
            s = p.newState(id);
            if (PrlState.getFirstState().isInitial() && PrlState.getSecondState().isInitial()) {
                p.setInitialState(s);
            } else if (PrlState.getFirstState().isFinal() && PrlState.getSecondState().isFinal()) {
                p.setFinalState(s);
            } else if (PrlState.getFirstState().isError() || PrlState.getSecondState().isError()) {
                p.setErrorState(s);
            }
            s.setLabel("<" + PrlState.getFirstState().getID() + ", " + PrlState.getSecondState().getID() + ">");
          //  System.out.println("Added state " + s.getLabel() + "."+"\t ID: "+s.getID());
        }
        return s;
    }
}


