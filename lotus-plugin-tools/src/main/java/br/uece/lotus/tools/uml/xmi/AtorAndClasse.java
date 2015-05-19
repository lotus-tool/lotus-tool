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
    
    private String nome,xmiID;
    
    public AtorAndClasse(String nome, String xmiID){
        this.nome = nome;
        this.xmiID = xmiID;
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
