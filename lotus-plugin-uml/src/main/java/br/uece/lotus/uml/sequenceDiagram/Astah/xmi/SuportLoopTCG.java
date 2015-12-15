/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.Astah.xmi;

import br.uece.lotus.State;

/**
 *
 * @author Bruno Barbosa
 */
public class SuportLoopTCG {
    private String constraintName;
    private String[] loop;

    public SuportLoopTCG(String constraintName, String[] loop) {
        this.constraintName = constraintName;
        this.loop = loop;
    }

    public String getConstrainName() {
        return constraintName;
    }

    public void setConstrainName(String constrainName) {
        this.constraintName = constrainName;
    }

    public String[] getLoop() {
        return loop;
    }

    public void setLoop(String[] loop) {
        this.loop = loop;
    }
    
    
}
