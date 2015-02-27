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

import br.uece.lotus.Transition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;

/**
 *
 * @author emerson
 */
public class SelfTransitionView extends TransitionView {

    private TransicaoCirculo mTc;

    @Override
    public boolean isInsideBounds(double x, double y) {        
//        double r = elipseRatio(x, y, TransicaoCirculo.RAIO_ELIPSE_X, TransicaoCirculo.RAIO_ELIPSE_Y, mTc.getLayoutX(), mTc.getLayoutY());
//        return r > 0.3 && r < 1.3;
        return mTc.getBoundsInParent().contains(new Point2D(x, y));
    }

    @Override
    protected void prepareView() {
        StateView origem = (StateView) getTransition().getSource().getValue("view");
//        StateView destino = (StateView) getTransition().getDestiny().getValue("view");
        mTc = new TransicaoCirculo();
//        mTc.setStyle("-fx-border-width: 1px; -fx-border-color: red;");
        getChildren().add(mTc);
        mTc.layoutXProperty().bind(origem.layoutXProperty().add(origem.widthProperty().divide(2)));
        mTc.layoutYProperty().bind(origem.layoutYProperty().subtract(origem.widthProperty().divide(4)));
    }

    @Override
    protected void updateView() {
        Transition t = getTransition();
        String style = "-fx-stroke-width: " + t.getWidth() + ";";
        style += "-fx-stroke: " + t.getColor() + ";";
        mTc.setBorderStyle(style);
        mTc.setColorStyle("-fx-fill: " + t.getColor() + ";");
        style = "-fx-text-fill: " + t.getTextColor() + ";";
        style += "-fx-font-weight: " + t.getTextStyle() + ";";
        style += "-fx-font-size: " + t.getTextSize() + ";";
        mTc.setTextStyle(style);
        mTc.setText(getComputedLabel());        
    }

    private static class DistanciaElipse extends DoubleBinding {

        public DoubleProperty xi;
        public DoubleProperty yi;
        public static final int COMPONENTE_X = 0;
        public static final int COMPONENTE_Y = 1;
        public int componente;

        public DistanciaElipse(DoubleProperty xi, DoubleProperty yi, int componente) {
            super.bind(xi, yi);
            this.xi = xi;
            this.yi = yi;
            this.componente = componente;
        }

        @Override
        protected double computeValue() {
            if (componente == COMPONENTE_X) {
                return xi.getValue() - 25;
            } else {
                return yi.getValue();
            }

        }
    }

    private static class DistanciaSetaElipse extends DoubleBinding {

        public static final int COMPONENTE_X = 0;
        public static final int COMPONENTE_Y = 1;
        public DoubleProperty xi;
        public DoubleProperty yi;
        public int componente;

        public DistanciaSetaElipse(DoubleProperty xi, DoubleProperty yi, int componente) {
            super.bind(xi, yi);
            this.xi = xi;
            this.yi = yi;
            this.componente = componente;
        }

        @Override
        protected double computeValue() {
            if (componente == COMPONENTE_X) {
                return xi.getValue() - 55;
            } else {
                return yi.getValue() - 6;
            }
        }
    }

    private double elipseRatio(double x, double y, double rx, double ry, double h, double k) {
        return (x - h) * (x - h) / (rx * rx) + (y - k) * (y - k) / (ry * ry);
    }

}

class TransicaoCirculo extends Region {

    static final double RAIO_ELIPSE_X = 25;
    static final double RAIO_ELIPSE_Y = 20;

    private Label mRotulo;
    private Polygon mSeta;
    private Ellipse mLoop;

    public TransicaoCirculo() {
        mRotulo = new Label();
        mSeta = new Polygon(new double[]{
            0.0, 10.0,
            10.0, 5.0,
            0.0, 0.0
        });
        mSeta.setRotate(90);

        mLoop = new Ellipse();
        mLoop.setFill(null);
        mLoop.setRadiusX(25);
        mLoop.setRadiusY(20);

        mLoop.setLayoutX(RAIO_ELIPSE_X);
        mLoop.setLayoutY(RAIO_ELIPSE_Y);
        mSeta.setLayoutX(2 * RAIO_ELIPSE_X - 5);
        mSeta.setLayoutY(RAIO_ELIPSE_Y);        
        mRotulo.setLayoutX(2 * RAIO_ELIPSE_X + 5);
        mRotulo.setLayoutY(RAIO_ELIPSE_Y - 3);
        getChildren().addAll(mRotulo, mSeta, mLoop);
    }

    void setBorderStyle(String style) {
        mLoop.setStyle(style);
        
    }

    void setTextStyle(String style) {
        mRotulo.setStyle(style);        
    }

    void setText(String s) {
        mRotulo.setText(s);
    }

    void setColorStyle(String s) {
        mSeta.setStyle(s);
    }

}
