/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml.xmi;

/**
 *
 * @author Bruno Barbosa
 */
public class Interaction {
    
    private String xmiIDREFcollaboration; //Aponta para a Collaboration dele
    private String xmiIDMsg,nomeMensagem,enviando,recebendo;

    public Interaction(String xmiIDREFcollaboration, String xmiIDMsg, String nomeMensagem, String enviando, String recebendo) {
        this.xmiIDREFcollaboration = xmiIDREFcollaboration;
        this.xmiIDMsg = xmiIDMsg;
        this.nomeMensagem = nomeMensagem;
        this.enviando = enviando;
        this.recebendo = recebendo;
    }
    

    public String getXmiIDREFcollaboration() {
        return xmiIDREFcollaboration;
    }

    public String getXmiIDMsg() {
        return xmiIDMsg;
    }

    public void setXmiIDMsg(String xmiIDMsg) {
        this.xmiIDMsg = xmiIDMsg;
    }

    public void setXmiIDREFcollaboration(String xmiIDREFcollaboration) {
        this.xmiIDREFcollaboration = xmiIDREFcollaboration;
    }

    public String getNomeMensagem() {
        return nomeMensagem;
    }

    public void setNomeMensagem(String nomeMensagem) {
        this.nomeMensagem = nomeMensagem;
    }

    public String getEnviando() {
        return enviando;
    }

    public void setEnviando(String enviando) {
        this.enviando = enviando;
    }

    public String getRecebendo() {
        return recebendo;
    }

    public void setRecebendo(String recebendo) {
        this.recebendo = recebendo;
    }
    
}
