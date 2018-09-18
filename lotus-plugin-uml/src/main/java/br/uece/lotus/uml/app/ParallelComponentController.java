package br.uece.lotus.uml.app;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.model.ParallelCompositor;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.StandardModelingView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class ParallelComponentController {




    private StandardModeling standardModeling;

    public ParallelComponentController(StandardModeling standardModeling) {
        this.standardModeling = standardModeling;
    }

    public  List<Component> buildIndividualComponents() throws Exception {
        List<Component> createdComponentsWithIndividualLTS = tryBuildIndividualLTS();
        return createdComponentsWithIndividualLTS;

    }


    private List<Component> tryBuildIndividualLTS() throws Exception {
        return IndividualLTSBuilder.buildLTS(standardModeling);

    }

    public List<Component> buildLifeComponents() throws Exception {
        List<Component> createdComponentsWithLifeLTS = tryBuildLifeLTS();
        return createdComponentsWithLifeLTS;
    }

    private List<Component> tryBuildLifeLTS() throws Exception {
        List<Component> createdComponentsWithIndividualLTS = tryBuildIndividualLTS();
        return LifeLTSBuilder.builderLTS(standardModeling, createdComponentsWithIndividualLTS);

    }

    public Component buildParallelComponent() throws Exception {
        Component createdParallelComponent = tryBuildParallelComponent();

        trySetProbabilityFromTransitionMSC(createdParallelComponent, standardModeling.getTransitions());

    //    updateIdsAndLabels(createdParallelComponent);
        return createdParallelComponent;
    }

    private void updateIdsAndLabels(Component createdParallelComponent) {
        Map<Integer,Integer> map = new HashMap<>();
        int newId =0;
        for(State state: createdParallelComponent.getStates()){
            map.put(state.getID(), newId);
            state.setID(newId);
            newId++;
        }

        for(State state : createdParallelComponent.getStates()){
            String [] array = state.getLabel().trim().split(",");
            String oldBeforeDot = array[0].replace("<","").trim();
            String oldAfterDot = array[1].replace(">","").trim();

            String newBeforeDot = String.valueOf(map.get(Integer.valueOf(oldBeforeDot)));
            String newAfterDot = String.valueOf(map.get(Integer.valueOf(oldAfterDot)));

           String newLabel = "<"+newBeforeDot.concat(".")+" ".concat(newAfterDot).concat(">");
           state.setLabel(newLabel);


        }

    }

    private Component tryBuildParallelComponent() throws Exception {
        List<Component> createdComponentsWithLifeLTS = tryBuildLifeLTS();
        return parallelComposition(createdComponentsWithLifeLTS);
    }

    public static Component parallelComposition(List<Component> Components) throws Exception{

            int tam = Components.size();
            if (tam < 2) {
                throw new Exception("Select at least 2(two) components!");
            }
            Component a = Components.get(0);
            Component b = Components.get(1);
            Component newComponent = new ParallelCompositor().compor(a, b);
            String name = a.getName() + " || " + b.getName();
            for(int i = 2; i < tam; i++){
                b = Components.get(i);
                newComponent = new ParallelCompositor().compor(newComponent, b);
                name += " || " + b.getName();
            }
            newComponent.setName(name);
            return newComponent;


    }


    public void trySetProbabilityFromTransitionMSC(Component parallelComponent, List<TransitionMSC> transitions) {
        try {
            ProbabilitySetter.setProbabilityFromTransitionMSCAndObjectActions(parallelComponent, transitions);
        } catch (Exception e) {

            Alert emptyAlert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            emptyAlert.show();
            e.printStackTrace();
        }
    }


    public void addParallelComponentInLeftPanel(StandardModelingWindowImpl standardModelingWindow, Component parallelComponent) {
        standardModelingWindow.getmViewer().getComponentBuildDS().createGeneralLTS(parallelComponent);
    }

    public void addLifeComponentsInLeftPanel(StandardModelingWindowImpl standardModelingWindow,List<Component> createdComponentsWithLifeLTS) {
        standardModelingWindow.getmViewer().getComponentBuildDS().createListLTS(createdComponentsWithLifeLTS);
    }

    public void addIndividualComponentsInLeftPanel(StandardModelingWindowImpl standardModelingWindow, List<Component> createdComponentsWithIndividualLTS) {
        standardModelingWindow.projectExplorerPluginDS.removeFragmetsLTS();
        standardModelingWindow.getmViewer().getComponentBuildDS().createListLTS(createdComponentsWithIndividualLTS);
    }

    public StandardModeling getStandardModeling() {
        return standardModeling;
    }

    public void setStandardModeling(StandardModeling standardModeling) {
        this.standardModeling = standardModeling;
    }

}
