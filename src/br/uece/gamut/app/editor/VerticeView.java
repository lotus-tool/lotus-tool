/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Ligacao;
import br.uece.gamut.Vertice;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emerson
 */
public class VerticeView implements Vertice {

    private List<Ligacao> ligacoes = new ArrayList<>();
    
    @Override
    public List<Ligacao> getLigacoes() {
        return this.ligacoes;
    }
    
}
