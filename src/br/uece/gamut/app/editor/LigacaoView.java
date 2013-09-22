/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Ligacao;
import br.uece.gamut.Vertice;

/**
 *
 * @author emerson
 */
public class LigacaoView implements Ligacao {
    
    private VerticeView destino;
    private VerticeView origem;

    public LigacaoView(VerticeView origem, VerticeView destino) {
        this.origem = origem;
        this.destino = destino;
    }

    
    
    @Override
    public Vertice getOrigem() {
        return this.origem;
    }

    @Override
    public Vertice getDestino() {
        return this.destino;
    }
    
}
