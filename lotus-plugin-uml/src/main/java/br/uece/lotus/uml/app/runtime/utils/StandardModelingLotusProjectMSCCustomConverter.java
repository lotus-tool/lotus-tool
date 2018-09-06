//package br.uece.lotus.uml.app.runtime.utils;
//
//import br.uece.lotus.uml.api.ds.Hmsc;
//import br.uece.lotus.uml.api.ds.StandardModeling;
//import br.uece.lotus.uml.api.ds.TransitionMSC;
//import br.uece.lotus.uml.app.runtime.model.custom.HMSCCustom;
//import br.uece.lotus.uml.app.runtime.model.custom.ProjectMSCCustom;
//import br.uece.lotus.uml.app.runtime.model.custom.StantardModelingCustom;
//import br.uece.lotus.uml.app.runtime.model.custom.TransitionHMSCCustom;
//
//
//public class StandardModelingLotusProjectMSCCustomConverter
//        implements ProjectMSCConverter<StandardModeling> {
//    ProjectMSCCustom projectMSCCustom;
//    private StantardModelingCustom stantardModelingCustom;
//
//    public ProjectMSCCustom toConverter(StandardModeling standardModeling) throws Exception {
//        projectMSCCustom = new ProjectMSCCustom();
//        projectMSCCustom.setName(standardModeling.getName());
//        parseAboutHMSC(standardModeling);
//
//        return projectMSCCustom;
//    }
//
//    private void parseAboutHMSC(StandardModeling standardModeling) {
//
//        stantardModelingCustom = new StantardModelingCustom();
//        projectMSCCustom.addStantardModelingCustom(stantardModelingCustom);
//
//        parseAboutHMSCCustom(standardModeling);
//
//        parseAboutTransitionHMSCCustom(standardModeling);
//    }
//
//    private void parseAboutHMSCCustom(StandardModeling standardModeling) {
//        HMSCCustom HMSCCustom;
//
//        for(Hmsc tempHmsc : standardModeling.getBlocos() ){
//            HMSCCustom = new HMSCCustom(stantardModelingCustom, tempHmsc.getID());
//            HMSCCustom.setLabel(tempHmsc.getLabel());
//        }
//
//    }
//
//    private void parseAboutTransitionHMSCCustom(StandardModeling standardModeling) {
//        TransitionHMSCCustom transitionHMSCCustom;
//        Hmsc srcBMSC, dstBMSC;
//        HMSCCustom srcHMSCCustom, dstHMSCCustom;
//
//        for(TransitionMSC transitionMSC: standardModeling.getTransitions()){
//            srcBMSC = (Hmsc) transitionMSC.getSource();
//            dstBMSC = (Hmsc) transitionMSC.getDestiny();
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
//    public void toUpdate(ProjectMSCCustom projectMSCCustom, StandardModeling standardModeling) throws Exception {
//
//        updateProbability(projectMSCCustom, standardModeling);
//
//
//
//    }
//
//    private void updateProbability(ProjectMSCCustom projectMSCCustom, StandardModeling standardModeling) {
//
//
//        StantardModelingCustom stantardModelingCustom = projectMSCCustom.getStantardModeling(0);
//
//        for(TransitionHMSCCustom transitionMSCCustom : stantardModelingCustom.getTransitionsHMSCCustom()){
//
//            TransitionMSC transitionMSC = getTransitionMSCFrom(standardModeling, transitionMSCCustom);
//
//            if (transitionMSC != null) {
//                transitionMSC.setProbability(transitionMSCCustom.getProbability());
//            }
//
//        }
//    }
//
//    private TransitionMSC getTransitionMSCFrom(StandardModeling standardModeling,
//                                               TransitionHMSCCustom currentTransitionHMSCCustom) {
//        boolean isTransitionMSC;
//        for(TransitionMSC tmpTransitionMSC: standardModeling.getTransitions()){
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
//        Hmsc srcHmsc = (Hmsc) tmpTransitionMSC.getSource();
//        HMSCCustom srcHMSCCustom = currentTransitionHMSCCustom.getSourceHMSCCustom();
//
//        return srcHMSCCustom.getId() == srcHmsc.getID();
//    }
//
//    private boolean sameDestinyId(TransitionMSC tmpTransitionMSC, TransitionHMSCCustom currentTransitionHMSCCustom) {
//
//        Hmsc destHmsc = (Hmsc) tmpTransitionMSC.getDestiny();
//        HMSCCustom destHMSCCustom = currentTransitionHMSCCustom.getDestinyHMSCCustom();
//
//        return destHMSCCustom.getId() == destHmsc.getID();
//    }
//
//    private boolean sameLabel(TransitionMSC tmpTransitionMSC, TransitionHMSCCustom currentTransitionHMSCCustom) {
//        return tmpTransitionMSC.getLabel().equals(currentTransitionHMSCCustom.getLabel());
//    }
//
//    @Override
//    public StandardModeling toUndo(ProjectMSCCustom projectMSCCustom) throws Exception {
//        return null;
//    }
//}
