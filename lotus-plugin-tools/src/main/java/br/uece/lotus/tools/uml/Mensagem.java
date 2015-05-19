/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.tools.uml.xmi.AtorAndClasse;

/**
 *
 * @author Bruno Barbosa
 */
public class Mensagem {
    private AtorAndClasse enviando;
    private AtorAndClasse recebendo;
    private String label;
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    
    public AtorAndClasse getEnviando() {
        return enviando;
    }

    public void setEnviando(AtorAndClasse enviando) {
        this.enviando = enviando;
    }

    public AtorAndClasse getRecebendo() {
        return recebendo;
    }

    public void setRecebendo(AtorAndClasse recebendo) {
        this.recebendo = recebendo;
    }
    
}

