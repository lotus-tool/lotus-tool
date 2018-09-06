package br.uece.lotus.uml.app.runtime.utils.component_service;

import br.uece.lotus.Transition;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.TransitionMSC;

import java.util.HashMap;
import java.util.Map;

public class ProjectDSConverterMSC implements br.uece.lotus.uml.app.runtime.utils.ProjectMSCConverter<StandardModeling> {

    @Override
    public ProjectDS toConverter(StandardModeling s) throws Exception {
        return null;
    }

    @Override
    public void toUpdate(ProjectDS projectDSIn, StandardModeling s) throws Exception {
        Map<String, TransitionMSC> map = new HashMap<>();
        for(TransitionMSC transitionMSC : projectDSIn.getStandardModeling().getTransitions()){
            map.put(transitionMSC.getLabel(),transitionMSC);
        }

        for(TransitionMSC transitionMSC : s.getTransitions()){
          TransitionMSC transitionMSC1 =  map.get(transitionMSC.getLabel());
          transitionMSC.setProbability(transitionMSC1.getProbability());
        }

    }

    @Override
    public StandardModeling toUndo(ProjectDS projectDSIn) throws Exception {
        return null;
    }
}
