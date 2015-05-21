/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml.xmi;

import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class InteractionFragments {
    
    private String name,operator;
    private List<String> ClassifierRoleLoopOrAlt;

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClassifierRoleLoopOrAlt() {
        return ClassifierRoleLoopOrAlt;
    }

    public void setClassifierRoleLoopOrAlt(List<String> ClassifierRoleLoopOrAlt) {
        this.ClassifierRoleLoopOrAlt = ClassifierRoleLoopOrAlt;
    }

    
    
    
}
