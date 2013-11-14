/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
            20.0, 0.0,
            40.0, 40.0,
            0.0, 40.0
        });

        polygon1.setFill(Color.BLACK);

        //     polygon1.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 15 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
        polygon1.layoutXProperty().bind(new PosSetaX(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
        polygon1.layoutYProperty().bind(new PosSetaY(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
        
        //   polygon1.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 15 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));


        polygon1.rotateProperty().bind(new RotacaoFlecha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));


        //Line
        transicao.startXProperty().bind(origem.layoutXProperty());
        transicao.startYProperty().bind(origem.layoutYProperty());
        transicao.endXProperty().bind(destino.layoutXProperty());
        transicao.endYProperty().bind(destino.layoutYProperty());

        TextField tfTransicao = new TextField();
        tfTransicao.setText("oi");
        //.layoutXProperty().bind(new DistanciaTexto(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
        // lbTransicao.layoutYProperty().bind(new DistanciaTexto(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));

        tfTransicao.setLayoutX(300);
        tfTransicao.setLayoutX(300);


        getChildren().add(tfTransicao);
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

    private static class DistanciaTexto extends DoubleBinding {

        public DoubleProperty xi;
        public DoubleProperty yi;
        public DoubleProperty xf;
        public DoubleProperty yf;

        public DistanciaTexto(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
            super.bind(xi, yi, xf, yf);
            this.xi = xi;
            this.yi = yi;
            this.xf = xf;
            this.yf = yf;
        }

        @Override
        protected double computeValue() {
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double c;
            if (xi.getValue() < xf.getValue()) {
            }
            return distance / 2;
        }
    }

    private static class RotacaoFlecha extends DoubleBinding {

        private final DoubleProperty xi;
        private final DoubleProperty yi;
        private final DoubleProperty xf;
        private final DoubleProperty yf;

        public RotacaoFlecha(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
            super.bind(xi, yi, xf, yf);
            this.xi = xi;
            this.yi = yi;
            this.xf = xf;
            this.yf = yf;
        }

        @Override
        protected double computeValue() {
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double tang = deltaY / deltaX;
            double tangRad = Math.atan(tang);
            double tangGrau = tangRad * 36 / 2 * Math.PI;
           // System.out.println(String.format("deltaX: %.2f deltaY: %.2f tang: %.2f tangRad: %.2f tangGrau: %.2f", deltaX, deltaY, tang, tangRad, tangGrau));
            boolean primeiroQuadrante = xi.getValue() < xf.getValue() && yi.getValue() > yf.getValue();
            boolean segundoQuadrante = xi.getValue() > xf.getValue() && yi.getValue() > yf.getValue();
            boolean terceiroQuadrante = xi.getValue() > xf.getValue() && yi.getValue() < yf.getValue();
            boolean quartoQuadrante = xi.getValue() < xf.getValue() && yi.getValue() < yf.getValue();
            if (primeiroQuadrante) {
                return 90 - tangGrau;
            } else if (segundoQuadrante) {
                return -(90 - tangGrau);
            } else if (terceiroQuadrante) {
                return 270 - tangGrau;

            } else if (quartoQuadrante) {
                return -(270 - tangGrau);
            }
            return 0;
        }

        private double radToDegree(double tangRad) {
            return 360.0 * tangRad / 2 * Math.PI;
        }
    }

    private static class PosSetaX extends DoubleBinding {

        public DoubleProperty xi;
        public DoubleProperty yi;
        public DoubleProperty xf;
        public DoubleProperty yf;

        public PosSetaX(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
            super.bind(xi, yi, xf, yf);
            this.xi = xi;
            this.yi = yi;
            this.xf = xf;
            this.yf = yf;
        }

        @Override
        protected double computeValue() {
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double sen = deltaX/distance;
            double xLinha = deltaX/2;
            System.out.println("xi: " + xi.getValue() + "xi': " + sen*distance/2 );
           
            boolean primeiroQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean segundoQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean terceiroQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() <= yf.getValue();
            boolean quartoQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() <= yf.getValue();
            if (primeiroQuadrante || quartoQuadrante ) {
                return xi.getValue() + xLinha;
            } else if (segundoQuadrante ||terceiroQuadrante ) {
                return xi.getValue() - xLinha;
            }
            return 0;
            //return xi.getValue() +( sen*distance/2);
             
        }
    }
    
    private static class PosSetaY extends DoubleBinding {

        public DoubleProperty xi;
        public DoubleProperty yi;
        public DoubleProperty xf;
        public DoubleProperty yf;

        public PosSetaY(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
            super.bind(xi, yi, xf, yf);
            this.xi = xi;
            this.yi = yi;
            this.xf = xf;
            this.yf = yf;
        }

        @Override
        protected double computeValue() {
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double yLinha = deltaY/2;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double sen = deltaY/distance;
            System.out.println("detalY: " + deltaY + " /2: " + deltaY/2);
            //return yi.getValue() -( sen*distance/2);
            
            boolean primeiroQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean segundoQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean terceiroQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() <= yf.getValue();
            boolean quartoQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() <= yf.getValue();
            if (primeiroQuadrante) {
                return yi.getValue() - yLinha;
            } else if (segundoQuadrante) {
                return yi.getValue() - yLinha;
            } else if (terceiroQuadrante) {
                return yi.getValue() + yLinha;
            } else if (quartoQuadrante) {
                return yi.getValue() + yLinha;
            }
            
            return 0;
        }
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
