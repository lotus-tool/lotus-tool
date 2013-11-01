/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 *
 * @author emerson
 */
public class TransicaoView extends Region implements Transicao {

    public static final int RAIO_CABECA_DE_FLECHA = 5;
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

        polygon1.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 15 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));

        polygon1.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 15 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));


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

    class DistanciaLinha extends DoubleBinding {

        public static final int COMPONENTE_X = 0;
        public static final int COMPONENTE_Y = 1;
        public static final int INICIO = 0;
        public static final int FIM = 1;
        public DoubleProperty xi;
        public DoubleProperty yi;
        public DoubleProperty xf;
        public DoubleProperty yf;
        public double distanciaDesejada;
        public int componente = COMPONENTE_X;
        public int alinhamento = FIM;
        public int vgap = 4;

        public DistanciaLinha(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf, double distanciaDesejada, int alinhamento, int componente) {
            super.bind(xi, yi, xf, yf);
            this.xi = xi;
            this.yi = yi;
            this.xf = xf;
            this.yf = yf;
            this.distanciaDesejada = distanciaDesejada;
            this.alinhamento = alinhamento;
            this.componente = componente;
        }

        @Override
        protected double computeValue() {
            System.out.println("computeValue()");
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double c = distanciaDesejada / distance * (componente == COMPONENTE_X ? deltaX : deltaY);
            if (alinhamento == INICIO) {
                if (componente == COMPONENTE_X) {
                    if (xi.getValue() < xf.getValue()) {
                        c = xi.getValue() + c;
                    } else {
                        c = xi.getValue() - c;
                    }
                } else {
                    if (yi.getValue() < yf.getValue()) {
                        c = yi.getValue() + c;
                    } else {
                        c = yi.getValue() - c;
                    }
                }
            } else {
                if (componente == COMPONENTE_X) {
                    if (xf.getValue() < xi.getValue()) {
                        c = xf.getValue() + c;
                    } else {
                        c = xf.getValue() - c;
                    }
                } else {
                    if (yf.getValue() > yi.getValue()) {
                        c = yf.getValue() - c;
                    } else {
                        c = yf.getValue() + c;
                    }
                }
            }
            return c;
        }
    }
}
