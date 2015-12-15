/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.Astah;

import java.util.List;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.AtorAndClasse;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.Collaboration;

/**
 *
 * @author Bruno Barbosa
 */
public class DiagramaSequencia {
    
    private List<Collaboration> grupo;
    private List<AtorAndClasse> atoresEclasses;

    public List<Collaboration> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<Collaboration> grupo) {
        this.grupo = grupo;
    }

    public List<AtorAndClasse> getAtoresEclasses() {
        return atoresEclasses;
    }

    public void setAtoresEclasses(List<AtorAndClasse> atoresEclasses) {
        this.atoresEclasses = atoresEclasses;
    }
    
}
