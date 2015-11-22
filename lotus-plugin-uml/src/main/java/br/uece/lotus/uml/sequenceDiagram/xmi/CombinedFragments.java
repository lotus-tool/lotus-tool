/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.xmi;

import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class CombinedFragments {
    
    private String xmiIdCombinedFragment, operator, nome;
    private List<InteractionOperand> interactionOperands;

    public CombinedFragments(String xmiIdCombinedFragment, String operator, String nome, List<InteractionOperand> interactionOperands) {
        this.xmiIdCombinedFragment = xmiIdCombinedFragment;
        this.operator = operator;
        this.nome = nome;
        this.interactionOperands = interactionOperands;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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