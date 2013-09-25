/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut;

import java.util.List;

/**
 *
 * @author emerson
 */
public interface Grafo {
    
    List<Vertice> getVertices();
    List<Ligacao> getLigacoes();
    void adicionarVertice(Vertice v);
    
}
