/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Grafo;
import br.uece.gamut.Ligacao;
import br.uece.gamut.Vertice;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;

/**
 *
 * @author emerson
 */
public class GrafoView extends Region implements Grafo {

    private List<Vertice> vertices = new ArrayList<>();

    public void clear() {
        vertices.clear();
        getChildren().clear();
    }

    @Override
    public List<Vertice> getVertices() {
        return vertices;
    }

    @Override
    public List<Ligacao> getLigacoes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void adicionarVertice(Vertice v) {
        if (v instanceof VerticeView) {
            vertices.add(v);
            getChildren().add((Region) v);
        } else {
            throw new IllegalArgumentException("v deve ser do tipo VerticeView!");
        }

//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void removerVerticeView(Vertice v) {
        VerticeView vv = (VerticeView) v;
        vertices.remove(v);
        getChildren().remove(vv);
    }

    
}
