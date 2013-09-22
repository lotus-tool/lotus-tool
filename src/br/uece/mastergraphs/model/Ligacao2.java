/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emerson
 */
public class Ligacao2 {    
    
    /**default**/ Vertice2 origem;
    /**default**/ Vertice2 destino;
    private Map<String, Object> valores = new HashMap<>();
    private List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        void aoMudar(Ligacao2 l);
    }
    
    public Ligacao2(Vertice2 origem, Vertice2 destino) {
        this.origem = origem;
        this.destino = destino;
    }

    public Vertice2 getOrigem() {
        return origem;
    }

    public Vertice2 getDestino() {
        return destino;
    }

    public Object getValor(String chave) {
        return valores.get(chave);
    }
    
    public String[] getChaves() {
        return valores.keySet().toArray(new String[0]);
    }
    
    public Object[] getValores() {
        return valores.values().toArray();
    }
    
    public void putValor(String chave, Object valor) {
        valores.put(chave, valor);        
        for (Listener l: listeners) {
            l.aoMudar(this);
        }
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }
    
    public void removeListener(Listener l) {
        listeners.remove(l);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(origem);
        sb.append("->");
        sb.append(destino);
        
        sb.append(" [");
        String[] chaves = getChaves();
        Object[] valores = getValores();
        if (chaves.length > 0) {
            sb.append(chaves[0]);
            sb.append(": ");
            sb.append(valores[0]);
        }
        for (int i = 1; i < chaves.length; i++) {
            sb.append(", ");
            sb.append(chaves[i]);
            sb.append(": ");
            sb.append(valores[i]);                    
        }
        sb.append("]");
        return sb.toString();
    }
}
