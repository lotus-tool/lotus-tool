package br.uece.lotus.uml.app;


import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.TransitionMSC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lucas Vieira Alves
 * 03/09/2018
 */

public class ProbabilitySetter {

    public static void setProbabilityFromTransitionMSCAndObjectActions(Component component, List<TransitionMSC> transitionMSCList) throws Exception{

        Map<String, TransitionMSC> labelTalAndTransitionMap =  buildMapLabelAndTransitionMSC(transitionMSCList);

        for(Transition transition : component.getTransitionsList()){

            String label = getLabel(transition);
            TransitionMSC correspondingTrasitionMSC = labelTalAndTransitionMap.get(label);

            if(correspondingTrasitionMSC == null){
                transition.setProbability(1D);
            }else {
                transition.setProbability(correspondingTrasitionMSC.getProbability());
            }



        }


    }


    private static Map<String, TransitionMSC> buildMapLabelAndTransitionMSC(List<TransitionMSC> transitionMSCList) throws Exception {
        Map<String, TransitionMSC> labelTalAndTransitionMap = new HashMap<>();

        for(TransitionMSC transitionMSC : transitionMSCList){
            String label = (String) transitionMSC.getValue(LifeLTSBuilder.TRANSITION_LABEL);

            if(labelTalAndTransitionMap.containsKey(label)){
                throw new Exception("Transitions in HMSC with same Labels!");
            }else {
                labelTalAndTransitionMap.put(label, transitionMSC);
            }

        }
        return labelTalAndTransitionMap;
    }

    private static String getLabel(Transition trasition) {
        String labelWithDot = trasition.getLabel();
        String [] array = labelWithDot.split("[.]");
        return array[1];
    }
}
