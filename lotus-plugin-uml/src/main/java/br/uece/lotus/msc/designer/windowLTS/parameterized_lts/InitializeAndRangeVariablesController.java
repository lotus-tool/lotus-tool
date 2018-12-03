package br.uece.lotus.msc.designer.windowLTS.parameterized_lts;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
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

    public InitializeAndRangeVariablesController(Component component, ProjectExplorerPluginMSC mProjectExplorerDS) {
        this.mProjectExplorerDS = mProjectExplorerDS;
        this.component = component;
    }




    public void onCreatedView() {
        String paramTxt = "";

        paramSet = getAllParametersWithoutRepetition(component);

        if(!paramSet.isEmpty()){
            for(String param : paramSet){
                paramTxt = paramTxt.concat(param).concat(" ").concat("=");
            }
            variablesTextArea.setText(paramTxt);
        }

        runButton.setOnAction(runAction());

    }

    private EventHandler<ActionEvent> runAction() {
        return event -> {



            Map<String, Variable> variableMap = getVariablesWithInitialsValuesAndRanges();

            if(!Variable.getVariables().isEmpty()){
                Variable.getVariables().clear();
            }

            Variable.getVariables().putAll(variableMap);

            ExecuteLTS executeLTS = new ExecuteLTS(component);

            Component traceComponent = executeLTS.run();

            ProjectMSC projectMSC = searchCurrentProjectDS();

            if(projectMSC !=null){
                projectMSC.addComponentFragmentLTS(traceComponent);
            }else {
                //todo show exception
            }




        };
    }

    private ProjectMSC searchCurrentProjectDS() {
        for(ProjectMSC projectMSC : mProjectExplorerDS.getAllProjectsDS()){
            if(projectMSC.getLTS_Composed().equals(component));
           return projectMSC;
        }
        return null;
    }

    private void createVariables(Map<String, Double> initialsValuesMap) {
        for(Map.Entry valueEntry: initialsValuesMap.entrySet()){
            Variable variable = new Variable();
            variable.setName((String) valueEntry.getKey());
            variable.setInitialValue((Double) valueEntry.getValue());
            variable.setCurrentValue((Double) valueEntry.getValue());

            Variable.addVariables(variable);
        }
    }

    private Map<String, Variable> getVariablesWithInitialsValuesAndRanges() {
        Map<String, Variable> variableHashMap = new HashMap<>();

        String variablesWithInitialsValuesAndRange = variablesTextArea.getText().trim();

        for(String line : variablesWithInitialsValuesAndRange.split("\n")){

            if(line.equals("\n")){
                continue;
            }

            if(line.contains(":")){
                getRangeAndVariable(line, variableHashMap);
            }else if (line.contains("=")){
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

        Double initialValueVariable = Double.valueOf(afterEqual);

        if(variableHashMap.containsKey(nameVariable)){
            if(variableExistWithInitialValue(nameVariable, variableHashMap)){

               //todo lançar excep variable já cadastrada

            }else {
                Variable currentVariable = variableHashMap.get(nameVariable);
                currentVariable.setInitialValue(initialValueVariable);
                currentVariable.setCurrentValue(initialValueVariable);

                variableHashMap.put(nameVariable, currentVariable);
            }

        }else {

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


        if(variableHashMap.containsKey(nameVariable)){
            if(variableExistWithRange(nameVariable, variableHashMap)){

                //todo lançar excep variable já cadastrada

            }else {
                Variable currentVariable = variableHashMap.get(nameVariable);

                String [] rangeElements =rangeString.split("\\.\\.");

                Double initialValueRange = Double.valueOf(rangeElements[0].trim());
                Double finalValueRange = Double.valueOf(rangeElements[1].trim());

                currentVariable.setInitialValueRange(initialValueRange);
                currentVariable.setFinalValueRange(finalValueRange);

                variableHashMap.put(nameVariable, currentVariable);
            }

        }else {
            String [] rangeElements =rangeString.split("\\.\\.");

            Double initialValueRange = Double.valueOf(rangeElements[0].trim());
            Double finalValueRange = Double.valueOf(rangeElements[1].trim());

            Variable currentVariable = new Variable();
            currentVariable.setInitialValueRange(initialValueRange);
            currentVariable.setFinalValueRange(finalValueRange);
            currentVariable.setName(nameVariable);
            variableHashMap.put(nameVariable, currentVariable);
        }


    }

    private boolean variableExistWithRange(String nameVariable, Map<String, Variable> variableHashMap) {
        return variableHashMap.get(nameVariable).getInitialValueRange()!= null;
    }

    private Set<String> getAllParametersWithoutRepetition(Component component) {
        Set<String> paramSet = new HashSet<>();
        for(Transition transition : component.getTransitionsList()){
            if(!transition.getParameters().isEmpty()){
                for(String param : transition.getParameters() ){
                    paramSet.add(param);
                }

            }
        }

        return paramSet;
    }
}
