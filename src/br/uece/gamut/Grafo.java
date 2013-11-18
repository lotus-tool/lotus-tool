package br.uece.gamut;

import java.util.List;

/**
 *
 * @author emerson
 */
public interface Grafo {
    
    List<Vertice> getVertices();
    List<Transicao> getTransicoes();
    //void adicionarVertice(Vertice v);

    public Vertice getVertice(int id);
    public Vertice newVertice(int id);    
    public Transicao newTransicao(int idOrigem, int idDestino);    
    
}
