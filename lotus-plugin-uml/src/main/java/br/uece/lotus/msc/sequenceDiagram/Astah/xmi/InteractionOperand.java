/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.sequenceDiagram.Astah.xmi;

import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class InteractionOperand {
    
    private String xmiIdIteractionOperand, interactionConstraintName;
    private List<String> xmiIdRefMsg;
    private List<String> interactionOperandFrags; // xmi.idref dos combinedFragments (só preenchido se houver blocos dentro de blocos)

    public InteractionOperand(String xmiIdIteractionOperand, String interactionConstraintName, List<String> xmiIdRefMsg, List<String> interactionOperandFrags) {
        this.xmiIdIteractionOperand = xmiIdIteractionOperand;
        this.interactionConstraintName = interactionConstraintName;
        this.xmiIdRefMsg = xmiIdRefMsg;
        this.interactionOperandFrags = interactionOperandFrags;
    }

    public String getXmiIdIteractionOperand() {
        return xmiIdIteractionOperand;
    }

    public void setXmiIdIteractionOperand(String xmiIdIteractionOperand) {
        this.xmiIdIteractionOperand = xmiIdIteractionOperand;
    }

    public String getInteractionConstraintName() {
        return interactionConstraintName;
    }

    public void setInteractionConstraintName(String interactionConstraintName) {
        this.interactionConstraintName = interactionConstraintName;
    }

    public List<String> getXmiIdRefMsg() {
        return xmiIdRefMsg;
    }

    public void setXmiIdRefMsg(List<String> xmiIdRefMsg) {
        this.xmiIdRefMsg = xmiIdRefMsg;
    }

    public List<String> getInteractionOperandFrags() {
        return interactionOperandFrags;
    }

    public void setInteractionOperandFrags(List<String> interactionOperandFrags) {
        this.interactionOperandFrags = interactionOperandFrags;
    }
}
