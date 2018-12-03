//package br.uece.lotus.msc.app.runtime.utils;
//
//import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
//import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
//import br.uece.lotus.msc.api.model.msc.TransitionMSC;
//import br.uece.lotus.msc.app.runtime.model.custom.HMSCCustom;
//import br.uece.lotus.msc.app.runtime.model.custom.ProjectMSCCustom;
//import br.uece.lotus.msc.app.runtime.model.custom.StantardModelingCustom;
//import br.uece.lotus.msc.app.runtime.model.custom.TransitionHMSCCustom;
//
//
//public class StandardModelingLotusProjectMSCCustomConverter
//        implements ProjectMSCConverter<HmscComponent> {
//    ProjectMSCCustom projectMSCCustom;
//    private StantardModelingCustom stantardModelingCustom;
//
//    public ProjectMSCCustom toConverter(HmscComponent hmscComponent) throws Exception {
//        projectMSCCustom = new ProjectMSCCustom();
//        projectMSCCustom.setName(hmscComponent.getName());
//        parseAboutHMSC(hmscComponent);
//
//        return projectMSCCustom;
//    }
//
//    private void parseAboutHMSC(HmscComponent hmscComponent) {
//
//        stantardModelingCustom = new StantardModelingCustom();
//        projectMSCCustom.addStantardModelingCustom(stantardModelingCustom);
//
//        parseAboutHMSCCustom(hmscComponent);
//
//        parseAboutTransitionHMSCCustom(hmscComponent);
//    }
//
//    private void parseAboutHMSCCustom(HmscComponent hmscComponent) {
//        HMSCCustom HMSCCustom;
//
//        for(HmscBlock tempHmsc : hmscComponent.getHmscBlockList() ){
//            HMSCCustom = new HMSCCustom(stantardModelingCustom, tempHmsc.getID());
//            HMSCCustom.setLabel(tempHmsc.getLabel());
//        }
//
//    }
//
//    private void parseAboutTransitionHMSCCustom(HmscComponent hmscComponent) {
//        TransitionHMSCCustom transitionHMSCCustom;
//        HmscBlock srcBMSC, dstBMSC;
//        HMSCCustom srcHMSCCustom, dstHMSCCustom;
//
//        for(TransitionMSC transitionMSC: hmscComponent.getTransitionMSCList()){
//            srcBMSC = (HmscBlock) transitionMSC.getSource();
//            dstBMSC = (HmscBlock) transitionMSC.getDestiny();
//
//            srcHMSCCustom = stantardModelingCustom.getHMSCCustomFromId(srcBMSC.getID());
//            dstHMSCCustom = stantardModelingCustom.getHMSCCustomFromId(dstBMSC.getID());
//
//            transitionHMSCCustom = new TransitionHMSCCustom(stantardModelingCustom, srcHMSCCustom, dstHMSCCustom);
//            transitionHMSCCustom.setLabel(transitionMSC.getLabel());
//            transitionHMSCCustom.setProbability(transitionMSC.getProbability());
//        }
//    }
//
//    @Override
//    public void toUpdate(ProjectMSCCustom projectMSCCustom, HmscComponent hmscComponent) throws Exception {
//
//        updateProbability(projectMSCCustom, hmscComponent);
//
//
//
//    }
//
//    private void updateProbability(ProjectMSCCustom projectMSCCustom, HmscComponent hmscComponent) {
//
//
//        StantardModelingCustom stantardModelingCustom = projectMSCCustom.getStantardModeling(0);
//
//        for(TransitionHMSCCustom transitionMSCCustom : stantardModelingCustom.getTransitionsHMSCCustom()){
//
//            TransitionMSC transitionMSC = getTransitionMSCFrom(hmscComponent, transitionMSCCustom);
//
//            if (transitionMSC != null) {
//                transitionMSC.setProbability(transitionMSCCustom.getProbability());
//            }
//
//        }
//    }
//
//    private TransitionMSC getTransitionMSCFrom(HmscComponent hmscComponent,
//                                               TransitionHMSCCustom currentTransitionHMSCCustom) {
//        boolean isTransitionMSC;
//        for(TransitionMSC tmpTransitionMSC: hmscComponent.getTransitionMSCList()){
//            isTransitionMSC = true;
//
//            isTransitionMSC = isTransitionMSC && sameSourcId(tmpTransitionMSC, currentTransitionHMSCCustom);
//            isTransitionMSC = isTransitionMSC && sameDestinyId(tmpTransitionMSC, currentTransitionHMSCCustom);
//            isTransitionMSC = isTransitionMSC && sameLabel(tmpTransitionMSC, currentTransitionHMSCCustom);
//
//            if(isTransitionMSC){
//                return tmpTransitionMSC;
//            }
//
//        }
//
//        return null;
//    }
//
//
//    private boolean sameSourcId(TransitionMSC tmpTransitionMSC, TransitionHMSCCustom currentTransitionHMSCCustom) {
//        HmscBlock srcHmsc = (HmscBlock) tmpTransitionMSC.getSource();
//        HMSCCustom srcHMSCCustom = currentTransitionHMSCCustom.getSourceHMSCCustom();
//
//        return srcHMSCCustom.getID() == srcHmsc.getID();
//    }
//
//    private boolean sameDestinyId(TransitionMSC tmpTransitionMSC, TransitionHMSCCustom currentTransitionHMSCCustom) {
//
//        HmscBlock destHmsc = (HmscBlock) tmpTransitionMSC.getDestiny();
//        HMSCCustom destHMSCCustom = currentTransitionHMSCCustom.getDestinyHMSCCustom();
//
//        return destHMSCCustom.getID() == destHmsc.getID();
//    }
//
//    private boolean sameLabel(TransitionMSC tmpTransitionMSC, TransitionHMSCCustom currentTransitionHMSCCustom) {
//        return tmpTransitionMSC.getLabel().equals(currentTransitionHMSCCustom.getLabel());
//    }
//
//    @Override
//    public HmscComponent toUndo(ProjectMSCCustom projectMSCCustom) throws Exception {
//        return null;
//    }
//}
