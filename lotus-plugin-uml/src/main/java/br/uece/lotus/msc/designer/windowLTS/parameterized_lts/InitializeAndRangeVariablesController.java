package br.uece.lotus.msc.designer.windowLTS.parameterized_lts;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
import br.uece.lotus.viewer.TransitionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.*;

public class InitializeAndRangeVariablesController {

    private final Component component;
    private final ProjectExplorerPluginMSC mProjectExplorerDS;

    @FXML
    TextArea variablesTextArea;

    @FXML
    Button runButton;

    Set<String> paramSet = new HashSet<>();
    private int countComponent = 1;

    public InitializeAndRangeVariablesController(Component component, ProjectExplorerPluginMSC mProjectExplorerDS) {
        this.mProjectExplorerDS = mProjectExplorerDS;
        this.component = component;
    }


    public void onCreatedView() {
        String paramTxt = "";

        paramSet = getAllParametersWithoutRepetition(component);

        if (!paramSet.isEmpty()) {
            for (String param : paramSet) {
                paramTxt = paramTxt.concat(param).concat(" ").concat("=").concat("\n");
            }
            variablesTextArea.setText(paramTxt);
        }

        runButton.setOnAction(runAction());

    }

    private EventHandler<ActionEvent> runAction() {
        return event -> {


            Map<String, Variable> variableMap = getVariablesWithInitialsValuesAndRanges();

            if (!Variable.getVariables().isEmpty()) {
                Variable.getVariables().clear();
            }

            Variable.getVariables().putAll(variableMap);

            ExecuteLTS executeLTS = new ExecuteLTS(component);

            Component traceComponent = executeLTS.run();

            traceComponent.setName("LTS parameterized "+countComponent);
            countComponent++;

          //  removeTransictionWithoutLabelsAndActionAndGuard(traceComponent);

            ProjectMSC projectMSC = searchCurrentProjectDS();

            if (projectMSC != null) {
                projectMSC.addComponentFragmentLTS(traceComponent);
            } else {
                //todo show exception
            }


        };
    }


    private void removeTransictionWithoutLabelsAndActionAndGuard(Component parallelComponent) {

        List<State> statesToRemove = new ArrayList<>();
        List<Transition> transitionsToAdd = new ArrayList<>();

              /*  Transition currentTransition = null;

        while ((currentTransition = containWithoutLabelsAndActionAndGuard(parallelComponent)) != null){
            join(currentTransition.getSource(), currentTransition.getDestiny(), transitionsToAdd, currentTransition);
        }
*/

        for(Transition currentTransition : parallelComponent.getTransitionsList()){
            if(currentTransition.getLabel() == null || currentTransition.getLabel().equals("")){
                if(currentTransition.getGuard() == null || currentTransition.getGuard().equals("") ){
                    if(currentTransition.getActions().size() == 0){
                        if(currentTransition.getParameters().size()==0){

                            join(currentTransition.getSource(), currentTransition.getDestiny(), transitionsToAdd, currentTransition);
                            System.out.println("Junta "+ currentTransition.getSource().getLabel()+" com "+ currentTransition.getDestiny().getLabel());
                            System.out.println("currentTransition"+ currentTransition.getLabel());

                            statesToRemove.add(currentTransition.getSource());
                        }
                    }

                }
            }
        }




        for (Transition transitionToAdd : transitionsToAdd) {
            Transition newTransition = parallelComponent.buildTransition(transitionToAdd.getSource(), transitionToAdd.getDestiny()).setViewType(TransitionView.Geometry.LINE).create();

            newTransition.setLabel(transitionToAdd.getLabel());
            newTransition.setProbability(transitionToAdd.getProbability());
            newTransition.setActions(transitionToAdd.getActions());
            newTransition.setGuard(transitionToAdd.getGuard());
            newTransition.setParameters(transitionToAdd.getParameters());
            newTransition.setValues(transitionToAdd.getValues());
            System.out.println("transitionToAdd "+ transitionToAdd.getLabel());

        }

        for (State stateToRemove : statesToRemove) {
            parallelComponent.remove(stateToRemove);
            System.out.println("stateToRemove "+ stateToRemove.getLabel());
        }

        if (containWithoutLabelsAndActionAndGuard(parallelComponent) != null) {
            removeTransictionWithoutLabelsAndActionAndGuard(parallelComponent);
            System.out.println(" CONTINUA REMOÇÃO");
        }

    }

    private Transition containWithoutLabelsAndActionAndGuard(Component parallelComponent) {
        for (Transition currentTransition : parallelComponent.getTransitionsList()) {
            if (currentTransition.getLabel() == null || currentTransition.getLabel().equals("")) {
                if (currentTransition.getGuard() == null || currentTransition.getGuard().equals("")) {
                    if (currentTransition.getActions().size() == 0) {
                        if (currentTransition.getParameters().size() == 0) {
                            return currentTransition;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void join(State source, State destiny, List<Transition> transitionsToAdd, Transition currentTransition) {

        Transition transitionToJump = currentTransition;


        List<Transition> allTransitionGoingIn = getAllTransitionGoingInFrom(source, transitionsToAdd);
        List<Transition> allTransitionGoingOut = getAllTransitionGoingOutFrom(source, transitionsToAdd);






        for (Transition inTransition :allTransitionGoingIn) {

            if (inTransition == transitionToJump) {
                continue;
            }

          /*  if (inTransition.getLabel() == null || inTransition.getLabel().equals("")) {
                continue;
            }*/

            buildTransitionIn(destiny, inTransition, transitionsToAdd);

        }

        for (Transition outTransition : allTransitionGoingOut) {

            if (outTransition == transitionToJump) {
                continue;
            }

          /*  if (outTransition.getLabel() == null || outTransition.getLabel().equals("")) {
                continue;
            }*/

            buildTransitionOut(destiny, outTransition, transitionsToAdd);

        }


    }

    private List<Transition> getAllTransitionGoingInFrom(State source, List<Transition> transitionsToAdd) {

        List<Transition> allTransitionGoingIn = new ArrayList<>();

        for(Transition transitionFromTransitionsToAdd : transitionsToAdd){
            if(transitionFromTransitionsToAdd.getDestiny() == source){
                allTransitionGoingIn.add(transitionFromTransitionsToAdd);
            }
        }



        if(!source.getIncomingTransitionsList().isEmpty()){
            allTransitionGoingIn.addAll(source.getIncomingTransitionsList());
        }

        return allTransitionGoingIn;
    }

/*    private List<Transition> getAllTransitionGoingInFrom(State source, List<Transition> transitionsToAdd) {

        List<Transition> allTransitionGoingIn = new ArrayList<>();

        for(Transition transitionFromTransitionsToAdd : transitionsToAdd){
            if(transitionFromTransitionsToAdd.getDestiny() == source){
                if(!containsTrasitionLabel(allTransitionGoingIn, transitionFromTransitionsToAdd.getLabel())){
                    allTransitionGoingIn.add(transitionFromTransitionsToAdd);
                }

            }
        }



        for (Transition transitionInFromState :source.getOutgoingTransitionsList()){
            if(!containsTrasitionLabel(allTransitionGoingIn,transitionInFromState.getLabel())){
                allTransitionGoingIn.add(transitionInFromState);
            }

        }
        return allTransitionGoingIn;
    }*/

    private List<Transition> getAllTransitionGoingOutFrom(State dest, List<Transition> transitionsToAdd) {

        List<Transition> allTransitionGoingOut = new ArrayList<>();

        for(Transition transitionFromTransitionsToAdd : transitionsToAdd){
            if(transitionFromTransitionsToAdd.getSource() == dest){
                if(containsTrasitionLabel(allTransitionGoingOut, transitionFromTransitionsToAdd.getLabel()))
                allTransitionGoingOut.add(transitionFromTransitionsToAdd);
            }
        }



        if(!dest.getIncomingTransitionsList().isEmpty()){
            allTransitionGoingOut.addAll(dest.getOutgoingTransitionsList());
        }

        return allTransitionGoingOut;
    }

    private boolean containsTrasitionLabel(List<Transition> transitionList, String label) {

        for(Transition transition : transitionList){
            if (transition.getLabel().equals(label)){
                return true;
            }
        }
        return false;
    }


    private void buildTransitionIn(State destState, Transition inTransition, List<Transition> transitionsToAdd) {


        State sourceState = inTransition.getSource();

        State currentState = inTransition.getDestiny();

        Transition newTransition = new Transition(sourceState, destState);

        newTransition.setLabel(inTransition.getLabel());
        newTransition.setProbability(inTransition.getProbability());
        newTransition.setActions(inTransition.getActions());
        newTransition.setGuard(inTransition.getGuard());
        newTransition.setParameters(inTransition.getParameters());
        newTransition.setValues(inTransition.getValues());


        Boolean isExceptional = false;
        Boolean isInterceptionNode = false;

        if (currentState.getValue("isExceptional") != null) {
            isExceptional = (Boolean) currentState.getValue("isExceptional");

            if (isExceptional) {
                destState.setValue("isExceptional", true);
            }
        }

        if (currentState.getValue("isInterceptionNode") != null) {
            isInterceptionNode = (Boolean) currentState.getValue("isInterceptionNode");
        }

        if (isInterceptionNode) {
            destState.setValue("isInterceptionNode", true);
        }

        transitionsToAdd.add(newTransition);

    }

    private void buildTransitionOut(State sourceState, Transition outTransition, List<Transition> transitionsToAdd) {


        State destState = outTransition.getDestiny();
        State currentState = outTransition.getSource();

        Transition newTransition = new Transition(sourceState, destState);
        newTransition.setLabel(outTransition.getLabel());
        newTransition.setProbability(outTransition.getProbability());
        newTransition.setActions(outTransition.getActions());
        newTransition.setGuard(outTransition.getGuard());
        newTransition.setParameters(outTransition.getParameters());
        newTransition.setValues(outTransition.getValues());

        Boolean isExceptional = false;
        Boolean isInterceptionNode = false;

        if (currentState.getValue("isExceptional") != null) {
            isExceptional = (Boolean) currentState.getValue("isExceptional");

            if (isExceptional) {
                sourceState.setValue("isExceptional", true);
            }
        }

        if (currentState.getValue("isInterceptionNode") != null) {
            isInterceptionNode = (Boolean) currentState.getValue("isInterceptionNode");
        }

        if (isInterceptionNode) {
            sourceState.setValue("isInterceptionNode", true);
        }

        transitionsToAdd.add(newTransition);

    }

    private ProjectMSC searchCurrentProjectDS() {
        for (ProjectMSC projectMSC : mProjectExplorerDS.getAllProjectsDS()) {
            if (projectMSC.getLTS_Composed().equals(component)) ;
            return projectMSC;
        }
        return null;
    }

    private void createVariables(Map<String, Double> initialsValuesMap) {
        for (Map.Entry valueEntry : initialsValuesMap.entrySet()) {
            Variable variable = new Variable();
            variable.setName((String) valueEntry.getKey());
            variable.setInitialValue((Integer) valueEntry.getValue());
            variable.setCurrentValue((Integer) valueEntry.getValue());

            Variable.addVariables(variable);
        }
    }

    private Map<String, Variable> getVariablesWithInitialsValuesAndRanges() {
        Map<String, Variable> variableHashMap = new HashMap<>();

        String variablesWithInitialsValuesAndRange = variablesTextArea.getText().trim();

        for (String line : variablesWithInitialsValuesAndRange.split("\n")) {

            if (line.equals("\n")) {
                continue;
            }

            if (line.contains(":")) {
                getRangeAndVariable(line, variableHashMap);
            } else if (line.contains("=")) {
                getInitialValueAndVariable(line, variableHashMap);
            }


        }
        return variableHashMap;
    }

    private void getInitialValueAndVariable(String line, Map<String, Variable> variableHashMap) {
        String[] arrayElementsEquation = line.split("=");
        String beforeEqual = arrayElementsEquation[0].trim();
        String nameVariable = beforeEqual;

        String afterEqual = arrayElementsEquation[1].trim();

        Integer initialValueVariable = Integer.valueOf(afterEqual);

        if (variableHashMap.containsKey(nameVariable)) {
            if (variableExistWithInitialValue(nameVariable, variableHashMap)) {

                //todo lançar excep variable já cadastrada

            } else {
                Variable currentVariable = variableHashMap.get(nameVariable);
                currentVariable.setInitialValue(initialValueVariable);
                currentVariable.setCurrentValue(initialValueVariable);

                variableHashMap.put(nameVariable, currentVariable);
            }

        } else {

            Variable currentVariable = new Variable();
            currentVariable.setInitialValue(initialValueVariable);
            currentVariable.setCurrentValue(initialValueVariable);
            currentVariable.setName(nameVariable);
            variableHashMap.put(nameVariable, currentVariable);
        }


    }

    private boolean variableExistWithInitialValue(String nameVariable, Map<String, Variable> variableHashMap) {
        return variableHashMap.get(nameVariable).getInitialValue() != null;
    }

    private void getRangeAndVariable(String line, Map<String, Variable> variableHashMap) {
        String colon = ":";
        String[] arrayElements = line.split(colon);

        String beforeColon = arrayElements[0].trim();
        String nameVariable = beforeColon;

        String afterColon = arrayElements[1].trim();
        String rangeString = afterColon;


        if (variableHashMap.containsKey(nameVariable)) {
            if (variableExistWithRange(nameVariable, variableHashMap)) {

                //todo lançar excep variable já cadastrada

            } else {
                Variable currentVariable = variableHashMap.get(nameVariable);

                String[] rangeElements = rangeString.split("\\.\\.");

                Integer initialValueRange = Integer.valueOf(rangeElements[0].trim());
                Integer finalValueRange = Integer.valueOf(rangeElements[1].trim());

                currentVariable.setInitialValueRange(initialValueRange);
                currentVariable.setFinalValueRange(finalValueRange);

                variableHashMap.put(nameVariable, currentVariable);
            }

        } else {
            String[] rangeElements = rangeString.split("\\.\\.");

            Integer initialValueRange = Integer.valueOf(rangeElements[0].trim());
            Integer finalValueRange = Integer.valueOf(rangeElements[1].trim());

            Variable currentVariable = new Variable();
            currentVariable.setInitialValueRange(initialValueRange);
            currentVariable.setFinalValueRange(finalValueRange);
            currentVariable.setName(nameVariable);
            variableHashMap.put(nameVariable, currentVariable);
        }


    }

    private boolean variableExistWithRange(String nameVariable, Map<String, Variable> variableHashMap) {
        return variableHashMap.get(nameVariable).getInitialValueRange() != null;
    }

    private Set<String> getAllParametersWithoutRepetition(Component component) {
        Set<String> paramSet = new HashSet<>();
        for (Transition transition : component.getTransitionsList()) {
            if (!transition.getParameters().isEmpty()) {
                for (String param : transition.getParameters()) {
                    paramSet.add(param);
                }

            }
        }

        return paramSet;
    }
}
