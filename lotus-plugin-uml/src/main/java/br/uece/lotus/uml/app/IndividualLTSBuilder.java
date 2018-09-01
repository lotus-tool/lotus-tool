package br.uece.lotus.uml.app;

import br.uece.lotus.Component;
import br.uece.lotus.State;

import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;

import java.util.*;


public class IndividualLTSBuilder {
    public static List<Component> buildLTS(ProjectExplorerPluginDS projectExplorerPluginDS) throws Exception  {


        StandardModeling currentStandardModeling = projectExplorerPluginDS.getSelectedComponentBuildDS();


        if(!standardModelingIsSelectedInProjectExplorerPanel(projectExplorerPluginDS)){
            throw new Exception("Standard Modeling is not selected in Project Explorer Panel!");
        }


        if(standardModelingIsEmpty(projectExplorerPluginDS)){
           throw new Exception("Standard Modeling Is Empty!");
        }

        if(thereIsHMSCWithOutBMSC(projectExplorerPluginDS)) {
            throw new Exception("There is Empty HMSC!");

        }

        if(thereIsBMSCWithOutObject(projectExplorerPluginDS)) {
            throw new Exception("There is Empty BMSC!");

        }

        Map<String, BlockDS> allBlockDSWithOutRepetition = getAllBlockDSWithOutRepetition(projectExplorerPluginDS);

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

    private static Map<String, BlockDS> getAllBlockDSWithOutRepetition(ProjectExplorerPluginDS projectExplorerPluginDS) {
      List<ComponentDS> componentDSList =  projectExplorerPluginDS.getAll_BMSC();
      Map<String, BlockDS> blocksWithOutRepetition = new HashMap<>();
      for(ComponentDS componentDS : componentDSList){
          for(BlockDS blockDS : componentDS.getBlockDS()){
              blocksWithOutRepetition.put(blockDS.getLabel(),blockDS);
          }
      }
      return blocksWithOutRepetition;
    }


    private static boolean standardModelingIsSelectedInProjectExplorerPanel(ProjectExplorerPluginDS projectExplorerPluginDS) {
        return projectExplorerPluginDS.getSelectedComponentBuildDS() != null;
    }

    private static boolean standardModelingIsEmpty(ProjectExplorerPluginDS projectExplorerPluginDS) {
        return projectExplorerPluginDS.getSelectedComponentBuildDS().getBlocos().isEmpty();
    }

    private static boolean thereIsHMSCWithOutBMSC(ProjectExplorerPluginDS projectExplorerPluginDS) {

        for(Hmsc currentHMSC : projectExplorerPluginDS.getSelectedComponentBuildDS().getBlocos()){
            if(!containsBMSC(currentHMSC)){
                return true;
            }
        }
        return false;

    }
    private static boolean thereIsBMSCWithOutObject(ProjectExplorerPluginDS projectExplorerPluginDS) {
        for(Hmsc currentHMSC : projectExplorerPluginDS.getSelectedComponentBuildDS().getBlocos()){
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
        BlockDS srcBlockDS, dstBlockDS;
        srcBlockDS = (BlockDS) currentTransitionMSC.getSource();
        dstBlockDS = (BlockDS) currentTransitionMSC.getDestiny();

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
