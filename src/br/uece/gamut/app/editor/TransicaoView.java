/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;

/**
 *
 * @author emerson
 */
public class TransicaoView extends Region implements Transicao {

    private VerticeView destino;
    private VerticeView origem;
    private Line transicao = new Line();
    private Line setaCima = new Line();
    

    

    public TransicaoView(VerticeView origem, VerticeView destino) {
        this.origem = origem;
        this.destino = destino;
        
       //Quad Curve 
        transicao.startXProperty().bind(origem.layoutXProperty());
        transicao.startYProperty().bind(origem.layoutYProperty());
        transicao.endXProperty().bind(destino.layoutXProperty());
        transicao.endYProperty().bind(destino.layoutYProperty());
        getChildren().add(transicao);
        getChildren().add(setaCima);
        

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
