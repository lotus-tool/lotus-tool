package br.uece.lotus.uml.app.runtime.model;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.app.runtime.utils.checker.ConditionalOperator;
import javafx.beans.property.SimpleStringProperty;

public class Equation {

    private SimpleStringProperty sourceProperty;
    private SimpleStringProperty destinyProperty;
    private SimpleStringProperty probabilityProperty;
    private SimpleStringProperty conditionalOperationProperty;

    private Hmsc sourceHMSC;
    private Hmsc destinyHMSC;
    private Double probability;
    private ConditionalOperator conditionalOperator;

    public Equation() {
        sourceProperty = new SimpleStringProperty("sourceProperty");
        destinyProperty = new SimpleStringProperty("destinyProperty");
        probabilityProperty = new SimpleStringProperty("probabilityProperty");
        conditionalOperationProperty = new SimpleStringProperty("conditionalOperationProperty");
    }

    public String getSourceProperty() {
        return sourceProperty.get();
    }

    public Equation setSource(Hmsc source) {
        this.sourceHMSC = source;
        this.sourceProperty.set(source.getLabel());
        return this;
    }

    public String getDestinyProperty() {
        return destinyProperty.get();
    }

    public Equation setDestiny(Hmsc destiny) {
        this.destinyHMSC = destiny;
        this.destinyProperty.set(destiny.getLabel());
        return this;
    }

    public String getProbabilityProperty() {
        return probabilityProperty.get();
    }

    public Equation setProbability(Double probability) {
        this.probability = probability;
        this.probabilityProperty.set(String.valueOf(probability));
        return this;
    }

    public String getConditionalOperationProperty() {
        return conditionalOperationProperty.get();
    }

    public Equation setConditionalOperation(ConditionalOperator conditionalOperation) {
        this.conditionalOperator = conditionalOperation;
        this.conditionalOperationProperty.set(conditionalOperation.toString());
        return this;
    }

    public Hmsc getSourceHMSC() {
        return sourceHMSC;
    }

    public Hmsc getDestinyHMSC() {
        return destinyHMSC;
    }

    public Double getProbability() {
        return probability;
    }

    public ConditionalOperator getConditionalOperator() {
        return conditionalOperator;
    }
}
