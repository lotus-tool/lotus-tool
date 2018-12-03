package br.uece.lotus.msc.app;

import br.uece.lotus.Component;
import br.uece.lotus.State;

import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSViewImpl;

import java.util.*;

/**
 *
 * @author Lucas Vieira Alves
 * 03/09/2018
 */


public class IndividualLTSBuilder {
    public static List<Component> buildLTS(HmscComponent currentStandardModeling) throws Exception  {





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

        Map<String, BmscBlock> allBlockDSWithOutRepetition = getAllBlockDSWithOutRepetition(currentStandardModeling);

        Map<String, BmscBlock> allBlockDSWithOutRepetitionBackUp = new HashMap<>();
        allBlockDSWithOutRepetitionBackUp.putAll(allBlockDSWithOutRepetition);

        List<Component> createdComponents = new ArrayList<>();

        int currentIndexBmscComponent = 0;

        for(GenericElement genericElement : currentStandardModeling.getGenericElementList()){

            List<BmscBlock> bmscBlockList =  null;

            if(genericElement instanceof HmscBlock){

                BmscComponent  currentBmscComponent = ((HmscBlock)genericElement).getBmscComponet();
                bmscBlockList = (List<BmscBlock>) currentBmscComponent.getBmscBlockList();


                for(BmscBlock currentBmscBlock : bmscBlockList){

                    List<TransitionMSC> allTransitonFromCurrentBmscBlock = getAllTransitionFromBlockDS(currentBmscBlock);

                    currentIndexBmscComponent++;

                    Component currentComponentLTS = new Component();
                    currentComponentLTS.setName(buildName(currentBmscBlock.getLabel(), genericElement.getLabel()));
                    currentComponentLTS.setID(buildID(currentStandardModeling, currentIndexBmscComponent));

                    currentComponentLTS.setValue("object_label", currentBmscBlock.getLabel());
                    currentComponentLTS.setValue("BMSC_label", currentBmscBlock.getLabel());

                    createdComponents.add(currentComponentLTS);


                    State currentState = null;

                    if(allTransitonFromCurrentBmscBlock.size()==0){
                        currentComponentLTS.newState(0);
                    }

                    for(TransitionMSC currentTransitionMSC : allTransitonFromCurrentBmscBlock){

                        String createdLabel = buildLabelToCurrentTransitionLTS(currentTransitionMSC);

                        State srcState, dstState;

                        int currentIDSrcState = currentComponentLTS.getStatesCount();

                        if(currentState == null){
                            srcState = currentComponentLTS.newState(currentIDSrcState);
                            srcState.setValue("isExceptional",((HmscBlock) genericElement).isExceptional());
                        }else {
                            srcState = currentState;
                        }

                        int currentIDDstState = currentComponentLTS.getStatesCount();

                        dstState = currentComponentLTS.newState(currentIDDstState);
                        dstState.setValue("isExceptional",((HmscBlock) genericElement).isExceptional());
                        currentComponentLTS.buildTransition(srcState, dstState)
                                .setLabel(createdLabel)
                                .setActions(currentTransitionMSC.getActions())
                                .setParameters(currentTransitionMSC.getParameters())
                                .create();


                        currentState = dstState;
                    }

                    setInitialAndFinalIDStates(currentComponentLTS);

                    layout(currentComponentLTS);

                    allBlockDSWithOutRepetition.remove(currentBmscBlock.getLabel());
                }

            }else if(genericElement instanceof InterceptionNode){

                Component currentComponentLTS = new Component();
                currentComponentLTS.setName(genericElement.getLabel());

                createdComponents.add(currentComponentLTS);

                currentComponentLTS.newState(0).setValue("isInterceptionNode", true);

            }


           //buildComponentsWithJustInitialState
           for(Map.Entry<String, BmscBlock> currentBlockPair : allBlockDSWithOutRepetition.entrySet()){



               Component currentComponentLTS = new Component();
               currentComponentLTS.setName(buildName(currentBlockPair.getKey(), genericElement.getLabel()));
               currentComponentLTS.setID(buildID(currentStandardModeling, currentIndexBmscComponent));


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

    private static Map<String, BmscBlock> getAllBlockDSWithOutRepetition(HmscComponent standardModeling) {

      Map<String, BmscBlock> blocksWithOutRepetition = new HashMap<>();
      for(HmscBlock currentHmscHmscBlock : standardModeling.getHmscBlockList()) {
          BmscComponent bmscComponent = currentHmscHmscBlock.getBmscComponet();
          for(BmscBlock bmscBlock : bmscComponent.getBmscBlockList()){
              blocksWithOutRepetition.put(bmscBlock.getLabel(), bmscBlock);
          }
      }

      return blocksWithOutRepetition;
    }


    private static boolean standardModelingIsSelectedInProjectExplorerPanel(HmscComponent standardModeling) {
        return standardModeling != null;
    }

    private static boolean standardModelingIsEmpty(HmscComponent standardModeling) {
        return standardModeling.getHmscBlockList().isEmpty();
    }

    private static boolean thereIsHMSCWithOutBMSC(HmscComponent standardModeling) {

        for(HmscBlock currentHMSCHmscBlock : standardModeling.getHmscBlockList()){
            if(!containsBMSC(currentHMSCHmscBlock)){
                return true;
            }
        }
        return false;

    }
    private static boolean thereIsBMSCWithOutObject(HmscComponent standardModeling) {
        for(HmscBlock currentHMSCHmscBlock : standardModeling.getHmscBlockList()){
            if(!containsObjects(currentHMSCHmscBlock)){
                return true;
            }
        }
        return false;
    }

    private static boolean containsObjects(HmscBlock currentHMSCHmscBlock) {
        return currentHMSCHmscBlock.getBmscComponet().getBlockDSCount()!=0;
    }

    private static boolean containsBMSC(HmscBlock currentHMSCHmscBlock) {
        return currentHMSCHmscBlock.getBmscComponet() != null;
    }


    private static List<TransitionMSC> getAllTransitionFromBlockDS(BmscBlock bmscBlock) {
        int sizeList = bmscBlock.getIncomingTransitionsCount()+ bmscBlock.getOutgoingTransitionsCount();
        //  /\ this helps arraylist be more efficient

        List<TransitionMSC> allTrasition = new ArrayList<>(sizeList);
        allTrasition.addAll(bmscBlock.getOutgoingTransitionsList());
        allTrasition.addAll(bmscBlock.getIncomingTransitionsList());

        allTrasition.sort(Comparator.comparing(TransitionMSC::getIdSequence));

       return allTrasition;

    }

    private static String buildName(String componentDSName, String HMSCLabel) {
        return componentDSName+"_"+HMSCLabel+"_individual";
    }

    private static int buildID(HmscComponent standardModeling, int currentIndexComponentDS) {
       return  (standardModeling.getID() * 1000)+ 300 + currentIndexComponentDS;
    }

    private static String buildLabelToCurrentTransitionLTS(TransitionMSC currentTransitionMSC) {
        BmscBlock srcBmscBlock = null, dstBmscBlock = null;

        if(currentTransitionMSC.getSource() instanceof BlockDSViewImpl){
            srcBmscBlock = ((BlockDSViewImpl) currentTransitionMSC.getSource()).getBlockDS();
        }

        if(currentTransitionMSC.getDestiny() instanceof BlockDSViewImpl){
            dstBmscBlock =((BlockDSViewImpl) currentTransitionMSC.getDestiny()).getBlockDS();
        }

        if(currentTransitionMSC.getSource() instanceof BmscBlock){
            srcBmscBlock = (BmscBlock) currentTransitionMSC.getSource();
        }

        if(currentTransitionMSC.getDestiny() instanceof BmscBlock){
            dstBmscBlock = (BmscBlock) currentTransitionMSC.getDestiny();
        }



        String createdLabel = srcBmscBlock.getLabel()+"."
                + dstBmscBlock.getLabel() +"."
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
