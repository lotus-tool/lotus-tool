/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 *
 * @author emerson
 */
public class TransicaoView extends Region implements Transicao {
    
    private VerticeView destino;
    private VerticeView origem;
    private Line transicao = new Line();    
    
    public TransicaoView(final VerticeView origem, final VerticeView destino) {
        this.origem = origem;
        this.destino = destino;
        Polygon polygon1 = new Polygon(new double[]{
            5.0, 0.0,
            10.0, 10.0,
            0.0, 10.0        
        });
        
        polygon1.setFill(Color.BLACK);
        
        polygon1.layoutXProperty().bind(new DoubleBinding() {
            {
                super.bind(transicao.endXProperty(), transicao.startXProperty());
            }

            @Override
            protected double computeValue() {
                if(origem.getLayoutX() < destino.getLayoutX()){                                                
                     return transicao.endXProperty().getValue() - 15 ;
                }else{
                    
                   return transicao.endXProperty().getValue() + 15;
                }
                
            }
        });
        
        polygon1.layoutYProperty().bind(new DoubleBinding() {
            {
                super.bind(transicao.endYProperty(), transicao.startYProperty());
            }
            @Override
            protected double computeValue() {
                if(origem.getLayoutY() > destino.getLayoutY()){
                    
                    return transicao.endYProperty().getValue() - 15;
                    
                }else{
                    return transicao.endYProperty().getValue() + 15;
                }
            }
        });       


        //Line
        transicao.startXProperty().bind(origem.layoutXProperty());
        transicao.startYProperty().bind(origem.layoutYProperty());
        transicao.endXProperty().bind(destino.layoutXProperty());
        transicao.endYProperty().bind(destino.layoutYProperty());
        getChildren().add(transicao);
        getChildren().add(polygon1);
        
        
    }
    
    @Override
    public Vertice getOrigem() {
        return this.origem;
    }
    
    @Override
    public Vertice getDestino() {
        return this.destino;
    }
    
    private static class PerimetroCirculo extends DoubleBinding {
        
        public PerimetroCirculo() {
        }
        
        @Override
        protected double computeValue() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
