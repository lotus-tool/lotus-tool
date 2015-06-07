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
public class AtorAndClasse {
    
    private String nome,xmiID,tipo;
    
    public AtorAndClasse(String nome, String xmiID, String tipo) {
        this.nome = nome;
        this.xmiID = xmiID;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getXmiID() {
        return xmiID;
    }

    public void setXmiID(String xmiID) {
        this.xmiID = xmiID;
    }
}
