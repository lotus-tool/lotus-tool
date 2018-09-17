package br.uece.lotus.uml.app.runtime.controller;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.api.ds.Hmsc;

public class HMSCLTSMapper {


    public static Integer getStateIdInComponetAboutInitialHMSC(Component parallelComponent, Hmsc hmsc) {
        if(hmsc.get_Initial()){
            return parallelComponent.getInitialState().getID();
        }

        String labelHMSC = hmsc.getLabel();

        for(Transition transition : parallelComponent.getTransitionsList()){
            if(transition.getLabel().split("[.]")[2].equals(labelHMSC)){
                return transition.getDestiny().getID();
            }
        }



        return null;
}

    public static Integer getStateIdInComponetAboutFinalHMSC(Component parallelComponent, Hmsc hmsc) {
        if(hmsc.get_Initial()){
            return parallelComponent.getInitialState().getID();
        }

        String labelHMSC = hmsc.getLabel();

        for(Transition transition : parallelComponent.getTransitionsList()){
            if(transition.getLabel().split("[.]")[0].equals(labelHMSC)){
                return transition.getSource().getID();
            }
        }

        Integer initialStateFromHMSC = getStateIdInComponetAboutInitialHMSC(parallelComponent,hmsc);
        Integer currentStateId = initialStateFromHMSC;
        State currentState = parallelComponent.getStateByID(currentStateId);

        while (currentState != null){
            Transition currentTrasition = null;

            if(!currentState.getOutgoingTransitionsList().isEmpty()){
                 currentTrasition = currentState.getOutgoingTransitionsList().get(0);
            }else {
                return currentState.getID();
            }

            currentState = currentTrasition.getDestiny();

        }

        return null;
    }
}
