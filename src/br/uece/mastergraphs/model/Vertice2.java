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
public class Vertice2 {
    
    private Map<String, Object> valores = new HashMap<>();
    List<Ligacao2> origens = new ArrayList<>();
    List<Ligacao2> destinos = new ArrayList<>();
    private Grafo2 grafo;
    private List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        void aoMudar(Vertice2 obj);
    }
    
    public Vertice2(Grafo2 grafo) {
        this.grafo = grafo;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valores);
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
    
    public Ligacao2 ligarA(Vertice2 destino) {
        Ligacao2 novo = new Ligacao2(this, destino);
        destinos.add(novo);
        destino.origens.add(novo);
        grafo.adicionarLigacoes(novo);        
        return novo;
    }    
    
    public void addListener(Listener l) {
        listeners.add(l);
    }
    
    public void removeListener(Listener l) {
        listeners.remove(l);
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
    
    public Vertice2 putValor(String chave, Object valor) {
        valores.put(chave, valor);        
        for (Listener l: listeners) {
            l.aoMudar(this);
        }
        return this;
    }
}
