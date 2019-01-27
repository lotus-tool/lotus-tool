package br.uece.lotus.msc.designer.windowLTS.parameterized_lts;

import java.util.HashMap;
import java.util.Map;

public class Variable {
    public static Map<String, Variable> variables = new HashMap<>();
    String name = null;
    Integer initialValue = null;
    Integer currentValue = null;
    Integer initialValueRange = null;
    Integer finalValueRange = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public static Map<String, Variable> getVariables() {
        return variables;
    }

    public Integer getInitialValueRange() {
        return initialValueRange;
    }

    public void setInitialValueRange(Integer initialValueRange) {
        this.initialValueRange = initialValueRange;
    }

    public Integer getFinalValueRange() {
        return finalValueRange;
    }

    public void setFinalValueRange(Integer finalValueRange) {
        this.finalValueRange = finalValueRange;
    }

    public static void setVariables(Map<String, Variable> variables) {
        Variable.variables = variables;
    }

    public static void addVariables(Variable variable) {
        variables.put(variable.getName(),variable);
    }

}
