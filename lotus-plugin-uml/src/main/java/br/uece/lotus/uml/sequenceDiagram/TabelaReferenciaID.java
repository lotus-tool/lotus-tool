/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram;

/**
 *
 * @author Bruno Barbosa
 */
public class TabelaReferenciaID {
    private int idRelativo;
    private String idClassOrActor;

    public TabelaReferenciaID(int idRelativo, String idClassOrActor) {
        this.idRelativo = idRelativo;
        this.idClassOrActor = idClassOrActor;
    }
    
    public int getIdRelativo() {
        return idRelativo;
    }

    public void setIdRelativo(int idRelativo) {
        this.idRelativo = idRelativo;
    }

    public String getIdClassOrActor() {
        return idClassOrActor;
    }

    public void setIdClassOrActor(String idClassOrActor) {
        this.idClassOrActor = idClassOrActor;
    }
    
    
}
