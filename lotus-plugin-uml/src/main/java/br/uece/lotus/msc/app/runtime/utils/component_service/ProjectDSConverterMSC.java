package br.uece.lotus.msc.app.runtime.utils.component_service;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;

import java.util.HashMap;
import java.util.Map;

public class ProjectDSConverterMSC implements br.uece.lotus.msc.app.runtime.utils.ProjectMSCConverter<HmscComponent> {

    @Override
    public ProjectMSC toConverter(HmscComponent s) throws Exception {
        return null;
    }

    @Override
    public void toUpdate(ProjectMSC projectMSCIn, HmscComponent s) throws Exception {
        Map<String, TransitionMSC> map = new HashMap<>();
        for(TransitionMSC transitionMSC : projectMSCIn.getStandardModeling().getTransitionMSCList()){
            map.put(transitionMSC.getLabel(),transitionMSC);
        }

        for(TransitionMSC transitionMSC : s.getTransitionMSCList()){
          TransitionMSC transitionMSC1 =  map.get(transitionMSC.getLabel());
          transitionMSC.setProbability(transitionMSC1.getProbability());
        }

    }

    @Override
    public HmscComponent toUndo(ProjectMSC projectMSCIn) throws Exception {
        return null;
    }
}
