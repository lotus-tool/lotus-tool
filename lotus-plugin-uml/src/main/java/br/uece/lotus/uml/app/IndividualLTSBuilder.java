package br.uece.lotus.uml.app;

import br.uece.lotus.Component;
import br.uece.lotus.State;

import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSViewImpl;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;

import java.util.*;

/**
 *
 * @author Lucas Vieira Alves
 * 03/09/2018
 */


public class IndividualLTSBuilder {
    public static List<Component> buildLTS(StandardModeling currentStandardModeling) throws Exception  {





        if(!standardModelingIsSelectedInProjectExplorerPanel(currentStandardModeling)){
            throw new Exception("Standard Modeling is not selected in Project Explorer Panel!");
        }


        if(standardModelingIsEmpty(currentStandardModeling)){
           throw new Exception("Standard Modeling Is Empty!");
        }

        if(thereIsHMSCWithOutBMSC(currentStandardModeling)) {
            throw new Exception("There is Empty HMSC!");

        }

        if(thereIsBMSCWithOutObject(currentStandardModeling)) {
            throw new Exception("There is Empty BMSC!");

        }

        Map<String, BlockDS> allBlockDSWithOutRepetition = getAllBlockDSWithOutRepetition(currentStandardModeling);

        Map<String, BlockDS> allBlockDSWithOutRepetitionBackUp = new HashMap<>();
        allBlockDSWithOutRepetitionBackUp.putAll(allBlockDSWithOutRepetition);

        List<Component> createdComponents = new ArrayList<>();

        int currentIndexComponentDS = 0;

        for(Hmsc currentHMSC : currentStandardModeling.getBlocos()){
           ComponentDS currentComponentDS = currentHMSC.getmDiagramSequence();



           for(BlockDS currentBlockDS : currentComponentDS.getBlockDS()){

               List<TransitionMSC> allTransitonFromCurrentBlockDS = getAllTransitionFromBlockDS(currentBlockDS);

               currentIndexComponentDS++;

               Component currentComponentLTS = new Component();
               currentComponentLTS.setName(buildName(currentBlockDS.getLabel(), currentHMSC.getLabel()));
               currentComponentLTS.setID(buildID(currentStandardModeling, currentIndexComponentDS));

               currentComponentLTS.setValue("object_label", currentBlockDS.getLabel());
               currentComponentLTS.setValue("BMSC_label", currentBlockDS.getLabel());

               createdComponents.add(currentComponentLTS);


               State currentState = null;

               if(allTransitonFromCurrentBlockDS.size()==0){
                   currentComponentLTS.newState(0);
               }

               for(TransitionMSC currentTransitionMSC : allTransitonFromCurrentBlockDS){

                   String createdLabel = buildLabelToCurrentTransitionLTS(currentTransitionMSC);

                   State srcState, dstState;

                   int currentIDSrcState = currentComponentLTS.getStatesCount();

                   if(currentState == null){
                       srcState = currentComponentLTS.newState(currentIDSrcState);
                   }else {
                       srcState = currentState;
                   }

                   int currentIDDstState = currentComponentLTS.getStatesCount();

                   dstState = currentComponentLTS.newState(currentIDDstState);
                   currentComponentLTS.buildTransition(srcState, dstState)
                            .setLabel(createdLabel)
                            .setActions(currentTransitionMSC.getActions())
                            .setParameters(currentTransitionMSC.getParameters())
                            .create();

                    
                    currentState = dstState;
               }

               setInitialAndFinalIDStates(currentComponentLTS);

               layout(currentComponentLTS);

               allBlockDSWithOutRepetition.remove(currentBlockDS.getLabel());
           }

           //buildComponentsWithJustInitialState
           for(Map.Entry<String, BlockDS> currentBlockPair : allBlockDSWithOutRepetition.entrySet()){



               Component currentComponentLTS = new Component();
               currentComponentLTS.setName(buildName(currentBlockPair.getKey(), currentHMSC.getLabel()));
               currentComponentLTS.setID(buildID(currentStandardModeling, currentIndexComponentDS));


               currentComponentLTS.setValue("object_label", currentBlockPair.getKey());
               currentComponentLTS.setValue("BMSC_label", currentBlockPair.getKey());
               currentComponentLTS.newState(0).setAsInitial();
               createdComponents.add(currentComponentLTS);

                setInitialAndFinalIDStates(currentComponentLTS);



               layout(currentComponentLTS);
           }

           allBlockDSWithOutRepetition.clear();
           allBlockDSWithOutRepetition.putAll(allBlockDSWithOutRepetitionBackUp);


        }
        return createdComponents;


    }

    private static void setInitialAndFinalIDStates(Component currentComponentLTS) {
        currentComponentLTS.setValue("initial_and_final_id_state", currentComponentLTS.getInitialState().getID()
                +","+ (currentComponentLTS.getStatesCount()-1));
    }

    private static Map<String, BlockDS> getAllBlockDSWithOutRepetition(StandardModeling standardModeling) {

      Map<String, BlockDS> blocksWithOutRepetition = new HashMap<>();
      for(Hmsc currentHmsc : standardModeling.getBlocos()) {
          ComponentDS componentDS = currentHmsc.getmDiagramSequence();
          for(BlockDS blockDS : componentDS.getBlockDS()){
              blocksWithOutRepetition.put(blockDS.getLabel(),blockDS);
          }
      }

      return blocksWithOutRepetition;
    }


    private static boolean standardModelingIsSelectedInProjectExplorerPanel(StandardModeling standardModeling) {
        return standardModeling != null;
    }

    private static boolean standardModelingIsEmpty(StandardModeling standardModeling) {
        return standardModeling.getBlocos().isEmpty();
    }

    private static boolean thereIsHMSCWithOutBMSC(StandardModeling standardModeling) {

        for(Hmsc currentHMSC : standardModeling.getBlocos()){
            if(!containsBMSC(currentHMSC)){
                return true;
            }
        }
        return false;

    }
    private static boolean thereIsBMSCWithOutObject(StandardModeling standardModeling) {
        for(Hmsc currentHMSC : standardModeling.getBlocos()){
            if(!containsObjects(currentHMSC)){
                return true;
            }
        }
        return false;
    }

    private static boolean containsObjects(Hmsc currentHMSC) {
        return currentHMSC.getmDiagramSequence().getBlockDSCount()!=0;
    }

    private static boolean containsBMSC(Hmsc currentHMSC) {
        return currentHMSC.getmDiagramSequence() != null;
    }


    private static List<TransitionMSC> getAllTransitionFromBlockDS(BlockDS blockDS) {
        int sizeList = blockDS.getIncomingTransitionsCount()+ blockDS.getOutgoingTransitionsCount();
        //  /\ this helps arraylist be more efficient

        List<TransitionMSC> allTrasition = new ArrayList<>(sizeList);
        allTrasition.addAll(blockDS.getOutgoingTransitionsList());
        allTrasition.addAll(blockDS.getIncomingTransitionsList());

        allTrasition.sort(Comparator.comparing(TransitionMSC::getIdSequence));

       return allTrasition;

    }

    private static String buildName(String componentDSName, String HMSCLabel) {
        return componentDSName+"_"+HMSCLabel+"_individual";
    }

    private static int buildID(StandardModeling standardModeling, int currentIndexComponentDS) {
       return  (standardModeling.getID() * 1000)+ 300 + currentIndexComponentDS;
    }

    private static String buildLabelToCurrentTransitionLTS(TransitionMSC currentTransitionMSC) {
        BlockDS srcBlockDS = null, dstBlockDS = null;

        if(currentTransitionMSC.getSource() instanceof BlockDSViewImpl){
            srcBlockDS = ((BlockDSViewImpl) currentTransitionMSC.getSource()).getBlockDS();
        }

        if(currentTransitionMSC.getDestiny() instanceof BlockDSViewImpl){
            dstBlockDS =((BlockDSViewImpl) currentTransitionMSC.getDestiny()).getBlockDS();
        }

        if(currentTransitionMSC.getSource() instanceof BlockDS){
            srcBlockDS = (BlockDS) currentTransitionMSC.getSource();
        }

        if(currentTransitionMSC.getDestiny() instanceof BlockDS){
            dstBlockDS = (BlockDS) currentTransitionMSC.getDestiny();
        }



        String createdLabel = srcBlockDS.getLabel()+"."
                +dstBlockDS.getLabel() +"."
                +currentTransitionMSC.getLabel();
        return createdLabel;
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
