package br.uece.lotus.uml.app.runtime.model;

import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.app.runtime.utils.checker.ConditionalOperator;
import javafx.beans.property.SimpleStringProperty;

public class Equation {

    private SimpleStringProperty firstHMSCProperty;
    private SimpleStringProperty secondHMSCProperty;
    private SimpleStringProperty probabilityProperty;
    private SimpleStringProperty conditionalOperationProperty;
    private SimpleStringProperty templateProperty;

    private Hmsc firstHMSC;
    private Hmsc secondHMSC;
    private Double probability;
    private ConditionalOperator conditionalOperator;
    private String template;


    public Equation() {
        firstHMSCProperty = new SimpleStringProperty("firstHMSCProperty");
        secondHMSCProperty = new SimpleStringProperty("secondHMSCProperty");
        probabilityProperty = new SimpleStringProperty("probabilityProperty");
        conditionalOperationProperty = new SimpleStringProperty("conditionalOperationProperty");
        templateProperty = new SimpleStringProperty("templateProperty");
    }


    public String getTemplateProperty() {
        return templateProperty.get();
    }


    public void setTemplateProperty(String templateProperty) {
        this.templateProperty.set(templateProperty);
    }

    public String getFirstHMSCProperty() {
        return firstHMSCProperty.get();
    }

    public Equation setFirstHMSC(Hmsc firstHMSC) {
        this.firstHMSC = firstHMSC;
        this.firstHMSCProperty.set(firstHMSC.getLabel());
        return this;
    }

    public String getSecondHMSCProperty() {
        return secondHMSCProperty.get();
    }

    public Equation setSecondHMSC(Hmsc secondHMSC) {
        this.secondHMSC = secondHMSC;
        this.secondHMSCProperty.set(secondHMSC.getLabel());
        return this;
    }



    public Equation set2HMSCProperty(String nSteps){
        this.secondHMSCProperty.set(nSteps);
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

    public Equation setTemplate(String template) {
        this.template = template;
        this.templateProperty.set(template);
        return this;
    }



    public Hmsc getFirstHMSC() {
        return firstHMSC;
    }

    public Hmsc getSecondHMSC() {
        return secondHMSC;
    }

    public Double getProbability() {
        return probability;
    }

    public ConditionalOperator getConditionalOperator() {
        return conditionalOperator;
    }

    public String getTemplate() {
        return template;
    }


}
