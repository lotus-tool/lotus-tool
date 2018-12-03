package br.uece.lotus.msc.designer.windowLTS.parameterized_lts;

import java.util.HashMap;
import java.util.Map;

public class Variable {
    public static Map<String, Variable> variables = new HashMap<>();
    String name = null;
    Double initialValue = null;
    Double currentValue = null;
    Double initialValueRange = null;
    Double finalValueRange = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public static Map<String, Variable> getVariables() {
        return variables;
    }

    public Double getInitialValueRange() {
        return initialValueRange;
    }

    public void setInitialValueRange(Double initialValueRange) {
        this.initialValueRange = initialValueRange;
    }

    public Double getFinalValueRange() {
        return finalValueRange;
    }

    public void setFinalValueRange(Double finalValueRange) {
        this.finalValueRange = finalValueRange;
    }

    public static void setVariables(Map<String, Variable> variables) {
        Variable.variables = variables;
    }

    public static void addVariables(Variable variable) {
        variables.put(variable.getName(),variable);
    }

}
