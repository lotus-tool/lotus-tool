package br.uece.lotus.msc.designer.windowLTS.parameterized_lts;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.TransitionView;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class ExecuteLTS {

  //  private static final String CURRENT_CONTEXT_VARIABLES = "current_context_variables";
    private Component composedComponent;
    private Component traceComponent;


    public ExecuteLTS(Component component) {
        this.composedComponent = component;

    }

    private List<Variable> descentOrder(List<Variable> variables) {

        List<Variable> ordenedList = new ArrayList<>(variables);
        ordenedList.sort((o1, o2) ->
                Integer.compare(o2.getName().length(), o1.getName().length())
        );
        return ordenedList;
    }


    public Component run() {
        traceComponent = new Component();

        if (composedComponent.getStatesList().isEmpty()) {

            //todo lança exception deve morrer a execução aqui !!!
        }


        State currentStateInComposedLTS = composedComponent.getInitialState();

        State currentStateInTraceLTS = createInitialState(traceComponent);


        Map<String, Variable> currentContextVariablesMap = cloneMap(Variable.getVariables());

     //   currentStateInComposedLTS.setValue(CURRENT_CONTEXT_VARIABLES, currentContextVariablesMap);

        System.out.println("state:" + currentStateInComposedLTS.getLabel());

        deepen(currentStateInComposedLTS, currentStateInTraceLTS, currentContextVariablesMap );

        layout(traceComponent);

        return traceComponent;
    }




    private State createInitialState(Component traceComponent) {
        return traceComponent.newState(0);
    }

    private void deepen(State currentStateInComposedLTS, State currentStateInTraceLTS, Map<String, Variable> currentContextVariablesMap) {

        System.out.println("state entrada:" + currentStateInComposedLTS.getLabel());
        printMap(currentContextVariablesMap);

        boolean isInterceptionNode = false;

        if(currentStateInComposedLTS.getValue("isInterceptionNode") != null){
            isInterceptionNode = (boolean) currentStateInComposedLTS.getValue("isInterceptionNode");
        }


        List<Transition> outgoingTransitionsList = new ArrayList<>();

        if(isInterceptionNode){
            outgoingTransitionsList = sortWithPriorityForExceptional(currentStateInComposedLTS);
        }else {
            outgoingTransitionsList = currentStateInComposedLTS.getOutgoingTransitionsList();
        }


        boolean alreadyEnteredInExceptionalState = false;

        for (Transition currentTransitionInComposedLTS : outgoingTransitionsList) {

            boolean isExceptionalDestinyState = isExceptional(currentTransitionInComposedLTS.getDestiny());

            if(!isExceptionalDestinyState && alreadyEnteredInExceptionalState){
                continue;
            }

            boolean passInGuard = attendGuardConditions(currentTransitionInComposedLTS, currentContextVariablesMap);

            if(isExceptionalDestinyState && passInGuard){
                alreadyEnteredInExceptionalState = true;
            }



            if (passInGuard) {
                //todo to check if there are a lot of unnecessary calls of cloneMap method
                Map<String, Variable> backupContextVariablesMap = cloneMap(currentContextVariablesMap);


                Map<String, Variable> updatedContextVariablesMap = processAction(currentTransitionInComposedLTS, cloneMap(currentContextVariablesMap));
                //todo Could have been fused the method processAction and processRange for performace
                Boolean passLimitsRange = processRange(updatedContextVariablesMap);

                if (passLimitsRange) {

                    updatedContextVariablesMap.clear();
                    updatedContextVariablesMap = cloneMap(backupContextVariablesMap);
                   // updatedContextVariablesMap.putAll(backupContextVariablesMap);
                    continue;

                } else {


                    currentStateInTraceLTS = buildTransition(currentTransitionInComposedLTS, currentStateInTraceLTS, updatedContextVariablesMap);
                }



                currentContextVariablesMap = saveCurrentContextIn(updatedContextVariablesMap);

                State newCurrentStateInComposedLTS = updadeCurrentState(currentTransitionInComposedLTS);

                deepen(newCurrentStateInComposedLTS, currentStateInTraceLTS, currentContextVariablesMap);

                currentContextVariablesMap = cloneMap(backupContextVariablesMap);

            }
        }



        System.out.println("state saida:" + currentStateInComposedLTS.getLabel());
        printMap(currentContextVariablesMap);
    }

    private void printMap(Map<String, Variable> currentContextVariablesMap) {
        String string = "";
        for(Map.Entry entry : currentContextVariablesMap.entrySet()){
        string  = string.concat(entry.getKey()+"="+((Variable)entry.getValue()).getCurrentValue()+" ");

        }
        System.out.println(string);

    }

    private boolean isExceptional(State destiny) {
       return destiny.getValue("isExceptional") != null && (boolean) destiny.getValue("isExceptional");
    }

    private List<Transition> sortWithPriorityForExceptional(State currentStateInComposedLTS) {
        List<Transition> transitions = new ArrayList<>();

        for(Transition transition : currentStateInComposedLTS.getOutgoingTransitionsList()){
            if((boolean)transition.getDestiny().getValue("isExceptional")){
                transitions.add(transition);
            }
        }

        for(Transition transition : currentStateInComposedLTS.getOutgoingTransitionsList()){
            if(!(boolean)transition.getDestiny().getValue("isExceptional")){
                transitions.add(transition);
            }
        }
        return transitions;
    }

    private State buildTransition(Transition currentTransitionInComposedLTS, State currentStateInTraceLTS, Map<String, Variable> updatedContextVariablesMap) {
        String label = currentTransitionInComposedLTS.getLabel();
        List<String> actions = currentTransitionInComposedLTS.getActions();
        String guard = currentTransitionInComposedLTS.getGuard();
        List<String> parameters = buildParameters(currentTransitionInComposedLTS, updatedContextVariablesMap);

        Integer destinyIdState = traceComponent.getStatesCount() + 1; // todo this method is not performative


        State destinyState = traceComponent.newState(destinyIdState);


        traceComponent.buildTransition(currentStateInTraceLTS, destinyState).setLabel(label)/*.setActions(actions).setGuard(guard)*/.setParameters(parameters).setViewType(TransitionView.Geometry.CURVE).create();

        return destinyState;
    }

    private List<String> buildParameters(Transition currentTransitionInComposedLTS, Map<String, Variable> updatedContextVariablesMap) {
        List<String> parameters = new ArrayList<>(updatedContextVariablesMap.size());

        for (String currentParam : currentTransitionInComposedLTS.getParameters()) {
            String nameVariable = currentParam.trim();

            Variable currentVariable = updatedContextVariablesMap.get(nameVariable);
            currentParam = String.valueOf(currentVariable.getCurrentValue());
            parameters.add(currentParam);
        }

        return parameters;
    }

    private Boolean processRange(Map<String, Variable> updatedContextVariablesMap) {
        for (Variable variable : updatedContextVariablesMap.values()) {
            Integer currentValue = variable.getCurrentValue();
            Integer initialValueRange = variable.getInitialValueRange();
            Integer finalValueRange = variable.getFinalValueRange();

            if (initialValueRange == null) {
                continue;
            }

            if (currentValue > finalValueRange) {
                return true;
            }

            if (currentValue < initialValueRange) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Variable> saveCurrentContextIn( Map<String, Variable> updatedContextVariablesMap) {
        return  cloneMap(updatedContextVariablesMap);

      // newCurrentStateInComposedLTS.setValue(CURRENT_CONTEXT_VARIABLES, currentContextVariablesMap);
    }

    private Map<String, Variable> cloneMap(Map<String, Variable> updatedContextVariablesMap) {
        Map<String, Variable> clonedMap = new HashMap<>(updatedContextVariablesMap.size());
        //create newInstance for remove referece
        for(Variable variable : updatedContextVariablesMap.values()){
            Variable newVariable = new Variable();
            newVariable.setInitialValue(variable.getInitialValue());
            newVariable.setCurrentValue(variable.getCurrentValue());
            newVariable.setFinalValueRange(variable.getFinalValueRange());
            newVariable.setInitialValueRange(variable.getInitialValueRange());
            newVariable.setName(variable.getName());

            clonedMap.put(newVariable.getName(), newVariable);
        }

        return clonedMap;
    }

    private State updadeCurrentState(Transition currentTransitionInComposedLTS ) {
        return currentTransitionInComposedLTS.getDestiny();
    }

    private Map<String, Variable> processAction(Transition currentTransitionInComposedLTS, Map<String, Variable> currentContextVariablesMap ) {


        if (!containAction(currentTransitionInComposedLTS)) {
            return currentContextVariablesMap;
        }

        List<Variable> variablesDescOrderList = descentOrder(new ArrayList<>(currentContextVariablesMap.values()));

        List<String> actions = currentTransitionInComposedLTS.getActions();

        processActions(actions, variablesDescOrderList, currentContextVariablesMap);

        return currentContextVariablesMap;


    }

    private void processActions(List<String> actions, List<Variable> variablesDescOrderList, Map<String, Variable> currentContextVariablesMap) {

    for(String action:actions){
        List<String> stringBeforeAndAfterIqualaction = Arrays.asList(action.split("="));
        String receiverVariable = stringBeforeAndAfterIqualaction.get(0).trim();
        String equation = stringBeforeAndAfterIqualaction.get(1).trim();
        Integer result = null;

        if(isConstante(equation)) {
            equation = action;

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            try {
                result = (Integer) engine.eval(equation);
            } catch (ScriptException e) {
                e.printStackTrace();
            }

        }else {
            for (Variable variable : variablesDescOrderList) {
                if (equation.contains(variable.getName())) {
                    equation = equation.replace(variable.getName(), String.valueOf(variable.getCurrentValue()));
                    ScriptEngineManager mgr = new ScriptEngineManager();
                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                    try {
                        result = (Integer) engine.eval(equation);
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                    break;
                }



            }
        }



        if (result != null) {
            //tester se eu tenho que setar dnvo ovalor no map ou ele já atualiza apenas pela refeência
            updateContextVariablesMap(receiverVariable, result, currentContextVariablesMap);
        }
    }
    }

    private boolean isConstante(String equation) {
            try
            {
                double d = Double.parseDouble(equation);
            }
            catch(NumberFormatException nfe)
            {
                return false;
            }
            return true;


    }

    private boolean containAction(Transition currentTransitionInComposedLTS) {
        return currentTransitionInComposedLTS.getActions() != null && (currentTransitionInComposedLTS.getActions()).size()!=0;
    }

    private void updateContextVariablesMap(String receiverVariable, Integer result, Map<String, Variable> currentContextVariablesMap) {
        Variable variable = currentContextVariablesMap.get(receiverVariable);
        variable.setCurrentValue(result);
        //tester se eu tenho que setar dnvo ovalor no map ou ele já atualiza apenas pela refeência
    }

    private boolean attendGuardConditions(Transition currentTransitionInComposedLTS,   Map<String, Variable> currentContextVariablesMap) {
        Boolean canPass = null;

        if (containGuard(currentTransitionInComposedLTS)) {

            canPass = attendThePassingRequirements(currentTransitionInComposedLTS, currentContextVariablesMap);
        } else {
            canPass = true;
        }
        return canPass;
    }

    private Boolean attendThePassingRequirements(Transition currentTransitionInComposedLTS,   Map<String, Variable> currentContextVariablesMap) {

        String operation = null;
        String guard = currentTransitionInComposedLTS.getGuard().trim();

        Boolean resultBoolean = false;

        State sourceState = currentTransitionInComposedLTS.getSource();


        List<Variable> variablesDescOrderList = descentOrder(new ArrayList<>(currentContextVariablesMap.values()));


        if(guard.contains("TRUE")){
            guard = guard.replace("TRUE", "true");
        }

        if(guard.contains("FALSE")){
            guard = guard.replace("FALSE", "false");
        }

        operation = guard;

        resultBoolean = executeOperation(operation, variablesDescOrderList);

        return resultBoolean;


    }



    private Boolean executeOperation(String operation, List<Variable> variablesDescOrderList) {
        Boolean resultBoolean = null;
        for (Variable variable : variablesDescOrderList) {
            if (operation.contains(variable.getName())) {
                operation = operation.replace(variable.getName(), String.valueOf(variable.getCurrentValue()));
            }
        }

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            resultBoolean = (Boolean) engine.eval(operation);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return resultBoolean;
    }

    private boolean containGuard(Transition currentTransitionInComposedLTS) {
        return currentTransitionInComposedLTS.getGuard() != null && !currentTransitionInComposedLTS.getGuard().equals("");
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
