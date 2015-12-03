package br.uece.lotus.tools.probabilitiesAnnotator;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.List;
import java.util.Random;

/**
 * Created by Ranniery on 01/12/2015.
 */
public class ProbabilitiesAnnotatorAlgorithm {

    public static void Equitably(Component a){
        double residue;
        double individualP;
        int transitionsCount;

        for (State state : a.getStates()) {
            transitionsCount = state.getOutgoingTransitionsCount();
            try{
                individualP = 100 / transitionsCount;
            }catch (ArithmeticException AEx){
                continue;
            }
            individualP = individualP / 100 ;
            residue = 1 - (individualP * transitionsCount);
            //System.out.println("State " + state.getID() + ": transitionsCount: " + transitionsCount + ". individualP: " + individualP +
            //                    ". residue: " + residue);
            for (Transition transition : state.getOutgoingTransitionsList()) {
                transition.setProbability(individualP);
            }
            if(transitionsCount >= 1){
                List<Transition> outGoingTransitions = state.getOutgoingTransitionsList();
                Transition firstTransition = outGoingTransitions.get(0);
                firstTransition.setProbability(individualP += residue);
            }
        }
    }

    public static void Random(Component a){
        double residue;
        double individualP;
        Random rand = new Random();

        for (State state : a.getStates()) {
            residue = 1;
            individualP = 0;

            for (Transition transition : state.getOutgoingTransitionsList()) {
                individualP = residue * rand.nextDouble();
                transition.setProbability(individualP);
                residue -= individualP;
                //System.out.println("State: " + state.getID() + ". Transition: " + transition.getLabel() +
                //        ". individualP: " + individualP + ". residue: " + residue);
            }

            if(state.getOutgoingTransitionsCount() >= 1){
                List<Transition> outGoingTransitions = state.getOutgoingTransitionsList();
                Transition firstTransition = outGoingTransitions.get(0);
                individualP = firstTransition.getProbability();
                firstTransition.setProbability(individualP += residue);
            }
        }
    }
}
