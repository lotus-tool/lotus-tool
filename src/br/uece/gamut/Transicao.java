package br.uece.gamut;

/**
 *
 * @author emerson
 */
public interface Transicao {
    
    Vertice getOrigem();
    Vertice getDestino();

    void setTag(String chave, Object valor);
    Object getTag(String chave);
    
}
