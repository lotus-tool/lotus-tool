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
public class CombinedFragments {
    
    private String xmiIdCombinedFragment, operator;
    private List<InteractionOperand> interactionOperands;

    public CombinedFragments(String xmiIdCombinedFragment, String operator, List<InteractionOperand> interactionOperands) {
        this.xmiIdCombinedFragment = xmiIdCombinedFragment;
        this.operator = operator;
        this.interactionOperands = interactionOperands;
    }

    public String getXmiIdCombinedFragment() {
        return xmiIdCombinedFragment;
    }

    public void setXmiIdCombinedFragment(String xmiIdCombinedFragment) {
        this.xmiIdCombinedFragment = xmiIdCombinedFragment;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<InteractionOperand> getInteractionOperands() {
        return interactionOperands;
    }

    public void setInteractionOperands(List<InteractionOperand> interactionOperands) {
        this.interactionOperands = interactionOperands;
    }

    
   
}