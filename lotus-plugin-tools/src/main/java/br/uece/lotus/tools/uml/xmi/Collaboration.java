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
public class Collaboration {
    
    private String xmiIDcollaboration;
    private List<NamespaceOwnedElement> grupoCollaboration;
    private List<Interaction> grupoMessagens;

    public Collaboration(String xmiIDcollaboration) {
        this.xmiIDcollaboration = xmiIDcollaboration;
    }

    public String getXmiIDcollaboration() {
        return xmiIDcollaboration;
    }

    public void setXmiIDcollaboration(String xmiIDcollaboration) {
        this.xmiIDcollaboration = xmiIDcollaboration;
    }

    public List<NamespaceOwnedElement> getGrupoCollaboration() {
        return grupoCollaboration;
    }

    public void setGrupoCollaboration(List<NamespaceOwnedElement> grupoCollaboration) {
        this.grupoCollaboration = grupoCollaboration;
    }

    public List<Interaction> getGrupoMessagens() {
        return grupoMessagens;
    }

    public void setGrupoMessagens(List<Interaction> grupoMessagens) {
        this.grupoMessagens = grupoMessagens;
    }

    
    
}
