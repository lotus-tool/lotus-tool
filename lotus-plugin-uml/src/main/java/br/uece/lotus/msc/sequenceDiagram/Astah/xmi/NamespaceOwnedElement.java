/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.sequenceDiagram.Astah.xmi;

/**
 *
 * @author Bruno Barbosa
 */
public class NamespaceOwnedElement{
    
    //ClassifierRole ID = grupo do ator ou classe
    //ClassifierBase = indica quem é o ator ou classe
    
    private String XmiIDClassifierRole, XmiIDREFclassifierBase;

    public NamespaceOwnedElement(String XmiIDClassifierRole, String XmiIDREFclassifierBase) {
        this.XmiIDClassifierRole = XmiIDClassifierRole;
        this.XmiIDREFclassifierBase = XmiIDREFclassifierBase;
    }

    public String getXmiIDClassifierRole() {
        return XmiIDClassifierRole;
    }

    public void setXmiIDClassifierRole(String XmiIDClassifierRole) {
        this.XmiIDClassifierRole = XmiIDClassifierRole;
    }

    public String getXmiIDREFclassifierBase() {
        return XmiIDREFclassifierBase;
    }

    public void setXmiIDREFclassifier(String XmiIDREFclassifierBase) {
        this.XmiIDREFclassifierBase = XmiIDREFclassifierBase;
    }

    
}
