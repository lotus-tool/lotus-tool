package br.uece.gamut;

import java.util.List;

/**
 *
 * @author emerson
 */
public interface Vertice {
    
    List<Transicao> getTransicoesSaida();
    List<Transicao> getTransicoesEntrada();
    
    void setTag(String chave, Object valor);
    Object getTag(String chave);

    public int getID();
    
}