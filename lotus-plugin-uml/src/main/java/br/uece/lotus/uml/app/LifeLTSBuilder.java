package br.uece.lotus.uml.app;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.lotus.viewer.TransitionView;
import java.util.*;

/**
 *
 * @author Lucas Vieira Alves
 */


public class LifeLTSBuilder {
    public static final String TRANSITION_LABEL = "transition_label";

    public static List<Component> builderLTS(StandardModeling standardModeling,
                                             List<Component> createdComponentsWithIndividualLTS) throws Exception {



        List<Component> createdComponentsWithLifeLTS = new ArrayList<>();

        Map <String, List<Component>> individualLTSMapByObject = buildComponentsWithIndividualLTSInMap(createdComponentsWithIndividualLTS);

        List<BlockDS> objectList = getAllBlockDSWithOutRepetition(standardModeling);

       // Map<String, Component> createdComponentsWithIndividualLTSMap = new HashMap<>(createdComponentsWithIndividualLTS.size());
      //  createdComponentsWithIndividualLTSMap = parseToMap(createdComponentsWithIndividualLTS);

        Component currentIndividualComponent = new Component();

        Map<String, List<String>> trasitionsHmscInMapBySrcHmsc;

        trasitionsHmscInMapBySrcHmsc = buildTrasitionsInMap(standardModeling.getTransitions());

        Queue<Hmsc> hmscQueue = new LinkedList<>();


        List<Component> componentListWithOutTals = concatIndividualLTSsWithOutTalTrasition(trasitionsHmscInMapBySrcHmsc, individualLTSMapByObject, objectList, hmscQueue);

         addTals(componentListWithOutTals, standardModeling.getTransitions());

         createdComponentsWithLifeLTS = componentListWithOutTals;

        return  createdComponentsWithLifeLTS;
    }

    private static void addTals(List<Component> componentListWithOutTals, List<TransitionMSC> transitionsHmsc) {


        for(TransitionMSC transitionMSC : transitionsHmsc){
            Hmsc srcHmsc = (Hmsc) transitionMSC.getSource();
            Hmsc dstHmsc = (Hmsc) transitionMSC.getDestiny();

            String srcHmscLabel = srcHmsc.getLabel();
            String dstHmscLabel = dstHmsc.getLabel();

            String labelTrasition = transitionMSC.getLabel();

            if(labelTrasition.isEmpty()){
                String tempLabelTranstion = buildTempLabelTranstion(transitionMSC);
                transitionMSC.putValue(TRANSITION_LABEL, tempLabelTranstion);

            }else {
                transitionMSC.putValue(TRANSITION_LABEL, labelTrasition);
            }

            for(Component componentWithoutTals : componentListWithOutTals){
                 int idSrc = Integer.valueOf(((String)componentWithoutTals.getValue(srcHmscLabel)).split(",")[1]);
                int idDst = Integer.valueOf(((String)componentWithoutTals.getValue(dstHmscLabel)).split(",")[0]);
                componentWithoutTals.buildTransition(idSrc, idDst)
                        .setLabel(srcHmscLabel.concat(".").concat(labelTrasition).concat(".").concat(dstHmscLabel)).setViewType(TransitionView.Geometry.CURVE)
                        .setGuard(transitionMSC.getGuard()).create();
            }


        }



    }

    private static String buildTempLabelTranstion(TransitionMSC transitionMSC) {
        Hmsc srcHmsc = (Hmsc) transitionMSC.getSource();
        Hmsc dstHmsc = (Hmsc) transitionMSC.getDestiny();

        String srcHmscLabel = srcHmsc.getLabel();
        String dstHmscLabel = dstHmsc.getLabel();
        String tempLabel = srcHmscLabel.concat(".").concat(dstHmscLabel);
       return tempLabel;

    }

    private static List<Component> concatIndividualLTSsWithOutTalTrasition(Map<String, List<String>> trasitionsHmscInMapBySrcHmsc,
                                                                Map<String, List<Component>> individualLTSMapByObject,
                                                                List<BlockDS> objectList,
                                                                Queue<Hmsc> hmscQueue) {

        List<Component> componentListByObjectWithOutTalTrasition = new ArrayList<>();

        Component currentComponentByObjectOutTalTrasition;
        // todo setar o id e o nome

        for(BlockDS object : objectList){
            currentComponentByObjectOutTalTrasition = new Component();
            componentListByObjectWithOutTalTrasition.add(currentComponentByObjectOutTalTrasition);

          List<Component> componentListByObject =  individualLTSMapByObject.get(object.getLabel());
          currentComponentByObjectOutTalTrasition.setName(object.getLabel().concat("_life"));

          for(Component componentByObject : componentListByObject){

              addStatesAndTrasitionIn(componentByObject, currentComponentByObjectOutTalTrasition);
          }



          layout(currentComponentByObjectOutTalTrasition);






        }
        return componentListByObjectWithOutTalTrasition;

    }

    private static void addStatesAndTrasitionIn(Component componentSrc, Component componentDst) {
        int delta = componentDst.getStatesCount();

       updateInternalInitialAndFinalIDsStates(componentSrc, componentDst);

       if(componentSrc.getTransitionsList().size() == 0){
           State oldSrcState = componentSrc.getInitialState();

           int oldIdSrc = oldSrcState.getID();
           int newIdSrc = oldIdSrc+delta;
            componentDst.newState(newIdSrc);
       }

       for(Transition trasitionSrc : componentSrc.getTransitionsList()){
           State oldSrcState = trasitionSrc.getSource();
           int oldIdSrc = oldSrcState.getID();

           State oldDstState = trasitionSrc.getDestiny();

           int oldIdDst = oldDstState.getID();

           int newIdSrc = oldIdSrc+delta;
           int newIdDst = oldIdDst+delta;

           State newSrcState;
           State newDstState;

           if(componentDst.getStateByID(newIdSrc)!=null){
               newSrcState = componentDst.getStateByID(newIdSrc);
           }else {
                newSrcState = componentDst.newState(newIdSrc);
           }

           if(componentDst.getStateByID(newIdDst)!=null){
               newDstState = componentDst.getStateByID(newIdDst);
           }else {
               newDstState = componentDst.newState(newIdDst);
           }



         // newSrcState.putValue("hmsc",componentSrc.getName().split("_")[0]);

           componentDst.buildTransition(newIdSrc, newIdDst)
                   .setLabel(trasitionSrc.getLabel()).setViewType(TransitionView.Geometry.CURVE)
                   .create();
       }

    }

    private static void updateInternalInitialAndFinalIDsStates(Component componentSrc, Component componentDst) {
        int delta = componentDst.getStatesCount();
        String old_initial_and_final_id_state = (String) componentSrc.getValue("initial_and_final_id_state");

        String oldIdInitialStateInString = old_initial_and_final_id_state.split(",")[0];
        String oldIdFinalStateInString = old_initial_and_final_id_state.split(",")[1];

        int oldIdInitialState = Integer.valueOf(oldIdInitialStateInString);
        int oldIdFinalState = Integer.valueOf(oldIdFinalStateInString);

        int newIdInitialState = oldIdInitialState+delta;

        int newIdFinalState = oldIdFinalState+delta;

        String new_initial_and_final_id_state = newIdInitialState+","+newIdFinalState;

        String labelHmsc = getLabel(componentSrc);

        componentDst.setValue(labelHmsc, new_initial_and_final_id_state);
    }

    private static String getLabel(Component componentSrc) {
        return componentSrc.getName().split("_")[1].trim();
    }

    private static Map<String, List<Component>> buildComponentsWithIndividualLTSInMap(List<Component> createdComponentsWithIndividualLTS) {
        Map<String, List<Component>> individualLTSInMap = new HashMap<>();

        for(Component component : createdComponentsWithIndividualLTS){

            String [] array = component.getName().split("_");
            String objectLabel = array[0];
          //  String hmscLabel = array[1];

            if(individualLTSInMap.containsKey(objectLabel)){
                individualLTSInMap.get(objectLabel).add(component);
            }else {
                List<Component> componentList = new ArrayList<>();
                componentList.add(component);
                individualLTSInMap.put(objectLabel, componentList);
            }


        }

        return individualLTSInMap;
    }

//    private static void concatIndividualLTSsWithOutTalTrasition(Map<String, List<String>> trasitionsInMap,
//                                             Queue<Hmsc> hmscQueue,
//                                             StandardModeling selectedComponentBuildDS,
//                                             Map<String, List<String>> mapIndividualLTS) {
//
//        Hmsc initialHmsc = selectedComponentBuildDS.getHmsc_inicial();
//
//        hmscQueue.add(initialHmsc);
//
//        while (!hmscQueue.isEmpty()){
//            concat(trasitionsInMap, hmscQueue, selectedComponentBuildDS,
//                    mapIndividualLTS );
//        }
//
//
//
//
//    }

    private static Component concat(Map<String, List<String>> trasitionsInMap,
                                    Queue<Hmsc> hmscQueue, StandardModeling selectedComponentBuildDS,
                                    Map<String, List<String>> mapIndividualLTS) {

        Component component = new Component();

        Hmsc firstHmsc = hmscQueue.peek();

        List<String>  labelAndDstHmscStringsList = trasitionsInMap.get(firstHmsc.getLabel());

        for(String labelAndDstHmscString : labelAndDstHmscStringsList){
            String [] array = labelAndDstHmscString.split("$$");
            String labelTrasition  = array[0].trim();
            String dstHmscLabel = array[1].trim();

            component = concat(firstHmsc.getLabel(), labelTrasition, dstHmscLabel);
        }

        hmscQueue.remove();


        return component;
    }

    private static Component concat(String srcHmscLabel, String labelTrasition, String dstHmscLabel) {
        Component component = new Component();

        //for(Component individualComponent: )

        return component;
    }

    private static Map<String, List<String>> buildTrasitionsInMap(List<TransitionMSC> transitions) {
        Map<String, List<String>> stringListMap = new HashMap<>();

        for(TransitionMSC transitionMSC : transitions){
            Hmsc srcHmsc = (Hmsc) transitionMSC.getSource();
            Hmsc dstHmsc = (Hmsc) transitionMSC.getDestiny();

            String labelAndDstHmsc = transitionMSC.getLabel().concat("$$").concat(dstHmsc.getLabel());

            if(stringListMap.containsKey(srcHmsc.getLabel())){
                stringListMap.get(srcHmsc.getLabel()).add(labelAndDstHmsc);
            }else {
                List<String> labelAndDstHmscStrings = new ArrayList<>();
                labelAndDstHmscStrings.add(labelAndDstHmsc);
                stringListMap.put(srcHmsc.getLabel(),labelAndDstHmscStrings);
            }



        }

        return stringListMap;
    }

    private static Map<String, Component> parseToMap(List<Component> createdComponentsWithIndividualLTS) {
        Map<String, Component> map = new HashMap<>(createdComponentsWithIndividualLTS.size());
        for(Component component : createdComponentsWithIndividualLTS){
            map.put(component.getName(),component);
        }

        return map;
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

    private static List<BlockDS> getAllBlockDSWithOutRepetition(StandardModeling standardModeling) {
        Map<String, BlockDS> blocksWithOutRepetition = new HashMap<>();

        for(Hmsc hmsc : standardModeling.getBlocos()){
            ComponentDS currentComponentDS = hmsc.getmDiagramSequence();
                for(BlockDS blockDS : currentComponentDS.getBlockDS()){
                    blocksWithOutRepetition.put(blockDS.getLabel(),blockDS);
                }

        }

        return new ArrayList<BlockDS>(blocksWithOutRepetition.values());
    }

    private static void layout(Component component) {
        int i = 1;
        for (State state : component.getStates()) {
            state.setLayoutX(i * 200);
            state.setLayoutY(300 + (i % 10));
            i++;
        }
    }
}
