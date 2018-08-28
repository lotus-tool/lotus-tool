package br.uece.lotus.uml.app;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;

import java.util.ArrayList;
import java.util.List;

public class LifeLTSBuilder {
    public static List<Component> builderLTS(StandardModeling selectedComponentBuildDS,
                                             List<Component> createdComponentsWithLifeLTS) throws Exception {

        List<Component> createdComponentsWithIndividualLTS = new ArrayList<>();
        Component currentIndividualComponent = new Component();

        Hmsc currentHMSC = selectedComponentBuildDS.getHmsc_inicial();


//        for (Hmsc currentHMSC : selectedComponentBuildDS.getBlocos()){
//            for(BlockDS currentBlockDS : currentHMSC.getmDiagramSequence().getBlockDS()){
//
//                String nameComponentRequested
//                        = currentBlockDS.getLabel()+"_"+currentHMSC.getLabel()+"_life";
//
//                Component requestedComponet = getComponentFromName(nameComponentRequested,
//                        createdComponentsWithIndividualLTS);
//
//                if(requestedComponet == null){
//                    throw new Exception("Component with Name" + nameComponentRequested+"not found!");
//                }
//
//                currentIndividualComponent.setName(currentBlockDS.getLabel()+"_"+"individual");
//
//
//            }
//        }

        return createdComponentsWithLifeLTS;
    }

    private static Component getComponentFromName(String nameComponentRequested,
                                                  List<Component> createdComponentsWithIndividualLTS) {
        for(Component component : createdComponentsWithIndividualLTS){
            if(component.getName().equals(nameComponentRequested)){
                return component;
            }
        }
        return null;
    }
}
