/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram;

import br.uece.lotus.uml.sequenceDiagram.xmi.AtorAndClasse;

/**
 *
 * @author Bruno Barbosa
 */
public class Mensagem {
    private AtorAndClasse enviando;
    private AtorAndClasse recebendo;
    private String msg, xmiIdMsg;


    public String getXmiIdMsg() {
        return xmiIdMsg;
    }

    public void setXmiIdMsg(String xmiIdMsg) {
        this.xmiIdMsg = xmiIdMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String label) {
        this.msg = label;
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

