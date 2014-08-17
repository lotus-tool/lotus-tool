/*
 * The MIT License
 *
 * Copyright 2014 emerson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.viewer;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 *
 * @author emerson
 */
public class LineTransitionView extends TransitionView {
    
    private final Label mRotulo;
    private final Polygon mSeta;
    private final Line mLinha;
    
    public LineTransitionView() {
        mRotulo = new Label();
        getChildren().add(mRotulo);
        
        mSeta = new Polygon(new double[]{
            0.0, 10.0,
            10.0, 5.0,
            0.0, 0.0
        });
        getChildren().add(mSeta);
        
        mLinha = new Line();
        getChildren().add(mLinha);
    }
    
    @Override
    public boolean isInsideBounds(double x, double y) {
        double startX = mLinha.getStartX();
        double endX = mLinha.getEndX();
        if (x < Math.min(startX, endX) || x > Math.max(startX, endX)) {
            return false;
        }
        double apx = x - startX;
        double apy = y - mLinha.getStartY();
        double abx = endX - startX;
        double aby = mLinha.getEndY() - mLinha.getStartY();
        
        double ab2 = abx * abx + aby * aby;
        double ap_ab = apx * abx + apy * aby;
        double t = ap_ab / ab2;
        
        double cx = (mLinha.getStartX() + abx * t);
        double cy = (mLinha.getStartY() + aby * t);
        return Math.abs(x - cx) < 5 && Math.abs(y - cy) < 5;
    }
    
    @Override
    protected void prepareView() {
        StateView origem = (StateView) getTransition().getSource().getValue("view");
        StateView destino = (StateView) getTransition().getDestiny().getValue("view");
        
        mSeta.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
        mSeta.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
        
        mSeta.translateXProperty().bind(origem.widthProperty().divide(2));
        mSeta.translateYProperty().bind(origem.heightProperty().divide(2));
        
        mSeta.rotateProperty().bind(Geom.angle(origem, destino));
        mSeta.rotateProperty().bind(new CartesianCase(origem, destino)
                .first(Geom.angle(origem, destino))
                .second(Geom.angle(origem, destino).add(180))
                .thirth(Geom.angle(origem, destino).add(180))
                .fourth(Geom.angle(origem, destino))
        );
        
        mLinha.startXProperty().bind(origem.layoutXProperty().add(origem.widthProperty().divide(2)));
        mLinha.startYProperty().bind(origem.layoutYProperty().add(origem.heightProperty().divide(2)));
        mLinha.endXProperty().bind(destino.layoutXProperty().add(destino.widthProperty().divide(2)));
        mLinha.endYProperty().bind(destino.layoutYProperty().add(destino.heightProperty().divide(2)));
        
        mRotulo.layoutXProperty().bind(mSeta.layoutXProperty().subtract(mRotulo.widthProperty().divide(2)));
        mRotulo.layoutYProperty().bind(mSeta.layoutYProperty().subtract(mRotulo.heightProperty()));
        
        mRotulo.translateXProperty().bind(origem.widthProperty().divide(2));
        mRotulo.translateYProperty().bind(origem.heightProperty().divide(2));
    }
    
    @Override
    protected void updateView() {
        String style = "-fx-stroke-width: " + getTransition().getWidth() + ";";
        style += "-fx-stroke: " + getTransition().getColor() + ";";
        mLinha.setStyle(style);
        mSeta.setStyle("-fx-fill: " + getTransition().getColor() + ";");
        
        style = "-fx-text-fill: " + getTransition().getTextColor() + ";";
        style += "-fx-font-weight: " + getTransition().getTextStyle() + ";";
        style += "-fx-font-size: " + getTransition().getTextSize() + ";";
        mRotulo.setStyle(style);
        mRotulo.setText(getComputedLabel());
    }
    
    private static class DistanciaLinha extends DoubleBinding {
        
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
        private double DEFAULT_OFFSET_X = 5;
        private double DEFAULT_OFFSET_Y = 5;
        
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
            double deltaX = Math.abs(xi.getValue() - xf.getValue());
            double deltaY = Math.abs(yi.getValue() - yf.getValue());
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            
            boolean primeiroQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean segundoQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean terceiroQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() <= yf.getValue();
            boolean quartoQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() <= yf.getValue();
//            System.out.printf("deltaX: %f deltaY: %f\n", deltaX, deltaY);
//            System.out.printf("1: %s 2: %s 3: %s 4:%s\n", "" + primeiroQuadrante, "" + segundoQuadrante, "" + terceiroQuadrante, "" + quartoQuadrante);
            if (componente == COMPONENTE_X) {
                if (primeiroQuadrante && quartoQuadrante) {
                    return xf.getValue() - distanciaDesejada - DEFAULT_OFFSET_X;
                } else if (segundoQuadrante && terceiroQuadrante) {
                    return xf.getValue() + distanciaDesejada - DEFAULT_OFFSET_X;
                } else if (primeiroQuadrante || quartoQuadrante) {
                    return xf.getValue() - (distanciaDesejada / distance * deltaX) - DEFAULT_OFFSET_X;
                } else if (segundoQuadrante || terceiroQuadrante) {
                    return xf.getValue() + (distanciaDesejada / distance * deltaX) - DEFAULT_OFFSET_X;
                }
            } else if (componente == COMPONENTE_Y) {
                if (primeiroQuadrante && segundoQuadrante) {
                    return yf.getValue() + distanciaDesejada - DEFAULT_OFFSET_Y;
                } else if (terceiroQuadrante && quartoQuadrante) {
                    return yf.getValue() - distanciaDesejada - DEFAULT_OFFSET_Y;
                } else if (primeiroQuadrante || segundoQuadrante) {
                    return yf.getValue() + (distanciaDesejada / distance * deltaY) - DEFAULT_OFFSET_Y;
                } else if (terceiroQuadrante || quartoQuadrante) {
                    return yf.getValue() - (distanciaDesejada / distance * deltaY) - DEFAULT_OFFSET_Y;
                }
            }
            return 0;
        }
    }
}
