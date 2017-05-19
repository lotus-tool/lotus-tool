/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class CicloOuNao {
    
    private String transition;
    private List<String> transitionsCiclo;
    boolean ciclo;

    public CicloOuNao(String transition, List<String> transitionsCiclo, boolean ciclo) {
        this.transition = transition;
        this.transitionsCiclo = transitionsCiclo;
        this.ciclo = ciclo;
    }

    public CicloOuNao() {
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public List<String> getTransitionsCiclo() {
        return transitionsCiclo;
    }

    public void setTransitionsCiclo(List<String> transitionsCiclo) {
        this.transitionsCiclo = transitionsCiclo;
    }

    public boolean isCiclo() {
        return ciclo;
    }

    public void setCiclo(boolean ciclo) {
        this.ciclo = ciclo;
    }
}
