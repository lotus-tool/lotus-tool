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

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 *
 * @author emerson
 */
public class ElipseTransitionView extends TransitionView {

    private TransicaoEmArco mCurva;

    @Override
    public boolean isInsideBounds(double x, double y) {
        StateView origem = (StateView) getTransition().getSource().getValue("view");
        StateView destino = (StateView) getTransition().getDestiny().getValue("view");

//        double rY = mCurva.getHeight() / 2;
//        double rX = mCurva.getWidth() / 2;
//        double cX = mCurva.getLayoutX() + rX;
//        double cY = mCurva.getLayoutY() + rY;
//        
//        double r = elipseRatio(x, y, rX, rY, cX, cY);
//        System.out.printf("r(%.2f, %.2f, %.2f, %.2f) = %.2f\n", rY, rX, cX, cY, r);        
//        return r > 0.3 && r < 1.3;      
        return mCurva.isInsideBounds(x, y);

    }

    @Override
    protected void prepareView() {
        Transition t = getTransition();
        State origem = t.getSource();
        State destino = t.getDestiny();
        StateView origemView = (StateView) origem.getValue("view");
        StateView destinoView = (StateView) destino.getValue("view");

        int n = origem.getTransitionsTo(destino).size();
        mCurva = new TransicaoEmArco(origemView, destinoView);
        mCurva.levelProperty().set(n);

//        mCurva.setStyle("-fx-border-width: 1px; -fx-border-color: red;");
        mCurva.layoutXProperty().bind(origemView.layoutXProperty().add(origemView.heightProperty().divide(2)));
        mCurva.layoutYProperty().bind(origemView.layoutYProperty().subtract(mCurva.heightProperty()).add(origemView.heightProperty().divide(2)));

        Rotate r = new Rotate();
        DoubleBinding angle = Geom.angle(origemView, destinoView);
        r.angleProperty().bind(new CartesianCase(origemView, destinoView)
                .firstAndSecond(-90)
                .thirthAndFourth(90)
                .secondAndThirth(angle.add(180))
                .first(angle)
                .second(angle.add(180))
                .thirth(angle.add(180))
                .fourth(angle)                
        );
        r.setAxis(Rotate.Z_AXIS);
        r.pivotYProperty().bind(mCurva.heightProperty());
        mCurva.getTransforms().add(r);
        mCurva.textRotateProperty().bind(new CartesianCase(origemView, destinoView)
                .second(180)
                .thirth(180)
        );

//        CartesianCase cc = new CartesianCase(origemView, destinoView)
//                .first(curva.tamanhoProperty())
//                .fourth(curva.tamanhoProperty())
//                .second(curva.tamanhoProperty().divide(2))
//                .thirth(curva.tamanhoProperty().divide(2));
//        cc.invalidate();
//        System.out.println("cc: " + cc.getValue());
//        r.pivotYProperty().set(cc.getValue());        
//        r.pivotYProperty().bind(cc);
//        curva.flipHorizontal().bind(new CartesianCase(origemView, destinoView).second(1).thirth(1));
//        if ((n % 2) == 0) {
//            curva.flipHorizontal().bind(new CartesianCase(origemView, destinoView).second(1).thirth(1));
//            curva.translateXProperty().bind(new CartesianCase(origemView, destinoView)
//                    .firstAndSecond(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .second(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .thirth(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .thirthAndFourth(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//            );
//            curva.translateYProperty().bind(new CartesianCase(origemView, destinoView)
//                    .firstAndSecond(origemView.heightProperty().multiply(-2))
//                    .second(origemView.layoutYProperty().subtract(destinoView.layoutYProperty()).multiply(-1).add(curva.tamanhoProperty().divide(2)))
//                    .thirth(destinoView.layoutYProperty().subtract(origemView.layoutYProperty()).add(curva.tamanhoProperty().divide(2)))
//                    .thirthAndFourth(0)
//            );
//            curva.flipVertical().bind(new CartesianCase(origemView, destinoView)
//                    .first(1)
//                    .fourth(1));
//        } else {
//            curva.flipHorizontal().bind(new CartesianCase(origemView, destinoView).second(1).thirth(1));
//            curva.translateXProperty().bind(new CartesianCase(origemView, destinoView)
//                    .firstAndSecond(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .second(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .thirth(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//                    .thirthAndFourth(origemView.layoutXProperty().subtract(destinoView.layoutXProperty()).multiply(-1))
//            );
//            curva.translateYProperty().bind(new CartesianCase(origemView, destinoView)
//                    .firstAndSecond(origemView.heightProperty().multiply(-2))
//                    .second(origemView.layoutYProperty().subtract(destinoView.layoutYProperty()).multiply(-1).add(curva.tamanhoProperty().divide(2)))
//                    .thirth(destinoView.layoutYProperty().subtract(origemView.layoutYProperty()).add(curva.tamanhoProperty().divide(2)))
//                    .thirthAndFourth(0)
//            );
//            curva.flipVertical().bind(new CartesianCase(origemView, destinoView)
//                    .second(1)
//                    .thirth(1));
//        }
        getChildren().add(mCurva);

    }

    @Override
    protected void updateView() {
        Transition t = getTransition();
        String style = "-fx-stroke-width: " + t.getWidth() + ";";
        style += "-fx-stroke: " + t.getColor() + ";";
        mCurva.setArcStyle(style);
        mCurva.setSetaStyle("-fx-fill: " + t.getColor() + ";");

        style = "-fx-text-fill: " + t.getTextColor() + ";";
        style += "-fx-font-weight: " + t.getTextStyle() + ";";
        style += "-fx-font-size: " + t.getTextSize() + ";";
        mCurva.setTextStyle(style);
        mCurva.setText(getComputedLabel());

    }

    private double elipseRatio(double x, double y, double rx, double ry, double h, double k) {
        return (x - h) * (x - h) / (rx * rx) + (y - k) * (y - k) / (ry * ry);
    }

}

class TransicaoEmArco extends Region implements ChangeListener<Number> {

    public void setArcStyle(String s) {
        mArc.setStyle(s);
    }

    public void setTextStyle(String s) {
        mRotulo.setStyle(s);
    }

    public void setSetaStyle(String s) {
        mSeta.setStyle(s);
    }

    public void setText(String s) {
        mRotulo.setText(s);
    }

    public DoubleProperty textRotateProperty() {
        return mRotulo.rotateProperty();
    }

    private static final int CURVATURA = 25;

    private final Label mRotulo = new Label();
    private final Polygon mSeta = new Polygon(new double[]{
        0.0, 10.0,
        10.0, 5.0,
        0.0, 0.0
    });
    ;
    private final Arc mArc = new Arc();
    private final Node mOrigem;
    private final Node mDestino;
    private final IntegerProperty mLevel = new SimpleIntegerProperty(1);
    private final DoubleProperty mTamanho = new SimpleDoubleProperty();
    private final DoubleProperty mFlipHorizontal = new SimpleDoubleProperty();
    private final DoubleProperty mFlipVertical = new SimpleDoubleProperty();

    {
        mFlipHorizontal.addListener(this);
        mFlipVertical.addListener(this);
        mLevel.addListener(this);
    }

    public TransicaoEmArco(Node origem, Node destino) {
        mOrigem = origem;
        mDestino = destino;

        getChildren().addAll(mRotulo, mSeta, mArc);
        mArc.radiusXProperty().bind(Geom.distance(origem, destino).divide(2));
        mArc.radiusYProperty().bind(Geom.distance(origem, destino).divide(4));
//        mArc.radiusYProperty().addListener(this);
        mArc.centerXProperty().bind(mArc.radiusXProperty());
        mArc.centerYProperty().bind(mArc.radiusYProperty().add(17));
//        mArc.lengthProperty().bind(Geom.angle(origem, destino).subtract(180).multiply(-1));
        mArc.setLength(180);
        mArc.setType(ArcType.OPEN);
        mArc.setFill(null);
        mArc.setStroke(Color.BLACK);

        mSeta.layoutXProperty().bind(mArc.radiusXProperty());
        mSeta.setLayoutY(12);
        mRotulo.setLayoutY(0);
        mRotulo.layoutXProperty().bind(mArc.radiusXProperty().subtract(mRotulo.widthProperty().divide(2)));
        updateView();
    }

    public DoubleProperty flipHorizontal() {
        return mFlipHorizontal;
    }

    public DoubleProperty flipVertical() {
        return mFlipVertical;
    }

    private void updateView() {
        mSeta.setRotate(mFlipHorizontal.get() == 1 ? 180 : 0);
        mArc.setStartAngle(mFlipVertical.get() == 1 ? 180 : 0);
        mSeta.setLayoutY(mFlipVertical.get() == 1 ? mArc.getRadiusY() * 2 : 12);
        mTamanho.set(mFlipVertical.get() == 1 ? mArc.getRadiusY() * 2 : mArc.getRadiusY());
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        updateView();
    }

    public DoubleProperty tamanhoProperty() {
        return mArc.radiusYProperty();
    }

    public IntegerProperty levelProperty() {
        return mLevel;
    }

    public boolean isInsideBounds(double x, double y) {
        boolean r = getBoundsInParent().contains(new Point2D(x, y));
//        double[] vertx = new double[] {
//            getLayoutBounds().getMinX(),
//            getBoundsInParent().getMaxX(),            
//        }

//        System.out.printf("%s\n", "" + r);
        return r;
    }

    public StringProperty textProperty() {
        return mRotulo.textProperty();
    }

    /**
     * int pnpoly(int nvert, float *vertx, float *verty, float testx, float
     * testy) { int i, j, c = 0; for (i = 0, j = nvert-1; i < nvert; j = i++) {
     * if ( ((verty[i]>testy) != (verty[j]>testy)) && (testx <
     * (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i])
     * ) c = !c; } return c; }
     */
    private boolean pnpoly(double[] vertx, double[] verty, double testx, double testy) {
        int i, j = 0;
        boolean c = false;
        for (i = 0, j = vertx.length - 1; i < vertx.length; j++) {
            if (((verty[i] > testy) != (verty[j] > testy))
                    && (testx < (vertx[j] - vertx[i]) * (testy - verty[i]) / (verty[j] - verty[i]) + vertx[i])) {
                c = !c;
            }
        }
        return c;
    }

}

class DistanciaLinha extends DoubleBinding {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    public DistanciaLinha(Node a, Node b) {
        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
    }

    @Override
    protected double computeValue() {
        double deltaX = mXb.get() - mXa.get();
        double deltaY = mYb.get() - mYa.get();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}

class Min extends DoubleBinding {

    private final DoubleProperty mA;
    private final DoubleProperty mB;

    public Min(DoubleProperty a, DoubleProperty b) {
        bind(a, b);
        mA = a;
        mB = b;
    }

    @Override
    protected double computeValue() {
        return Math.min(mA.get(), mB.get());
    }

}

class Angulo extends DoubleBinding {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    public Angulo(Node a, Node b) {
        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
    }

    @Override
    protected double computeValue() {
        double deltaX = mXb.get() - mXa.get();
        double deltaY = mYb.get() - mYa.get();
        if (deltaX == 0) {
            System.out.println("zero");
            return 90;
        }
        if (deltaY == 0) {
            return 0;
        }
        double tang = deltaY / deltaX;
        double tangRad = Math.atan(tang);
        double tangGrau = tangRad * 36 / 2 * Math.PI;
        return tangGrau;
    }
}

class PontoMedio extends DoubleBinding {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    static final int TIPO_X = 0;
    static final int TIPO_Y = 1;
    private final int mTipo;

    public PontoMedio(Node a, Node b, int tipo) {
        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
        mTipo = tipo;
    }

    @Override
    protected double computeValue() {
//        double i = (mTipo == TIPO_X ? Math.min(mXa.get(), mXb.get()) : Math.min(mYa.get(), mYb.get()));
//        double f = (mTipo == TIPO_X ? Math.max(mXa.get(), mXb.get()) : Math.max(mYa.get(), mYb.get()));
        double r = (mTipo == TIPO_X ? Math.abs(mXa.get() - mXb.get()) : Math.abs(mYa.get() - mYb.get())) / 2;
        return r;
    }

}

class CartesianCase extends DoubleBinding {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    private DoubleBinding mDefaultBinding;
    private DoubleExpression mFirstAndSecondBinding;
    private DoubleExpression mTerceiroAndQuartoBinding;
    private DoubleExpression mFirstBinding;
    private DoubleExpression mSecondBinding;
    private DoubleExpression mThirthBinding;
    private DoubleExpression mFourthBinding;    
    private double mFirstValue;
    private double mSecondValue;
    private double mThirthValue;
    private double mFourthValue;
    private double mFirstAndSecondValue;
    private double mTerceiroAndQuartoValue;    
    private DoubleBinding mSecondAndThirthBinding;
    private double mSecondAndThirthValue;

    public CartesianCase(Node a, Node b) {
        super.bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
    }

    @Override
    protected double computeValue() {
        boolean primeiroQuadrante = mXa.getValue() <= mXb.getValue() && mYa.getValue() >= mYb.getValue();
        boolean segundoQuadrante = mXa.getValue() >= mXb.getValue() && mYa.getValue() >= mYb.getValue();
        boolean terceiroQuadrante = mXa.getValue() >= mXb.getValue() && mYa.getValue() <= mYb.getValue();
        boolean quartoQuadrante = mXa.getValue() <= mXb.getValue() && mYa.getValue() <= mYb.getValue();

        System.out.printf("%s, %s, %s, %s\n", primeiroQuadrante, segundoQuadrante, terceiroQuadrante, quartoQuadrante);
        if (segundoQuadrante && terceiroQuadrante) {
            return mSecondAndThirthBinding != null ? mSecondAndThirthBinding.get() : mSecondAndThirthValue;
        } else if (primeiroQuadrante && segundoQuadrante) {
            return mFirstAndSecondBinding != null ? mFirstAndSecondBinding.get() : mFirstAndSecondValue;
        } else if (terceiroQuadrante && quartoQuadrante) {
            return mTerceiroAndQuartoBinding != null ? mTerceiroAndQuartoBinding.get() : mTerceiroAndQuartoValue;
        } else if (primeiroQuadrante && !segundoQuadrante && !terceiroQuadrante && !quartoQuadrante) {
//            System.out.println("1 " + (mFirstBinding != null ? mFirstBinding.get() : mFirstValue));
            return mFirstBinding != null ? mFirstBinding.get() : mFirstValue;
        } else if (!primeiroQuadrante && segundoQuadrante && !terceiroQuadrante && !quartoQuadrante) {
//            System.out.println("2 " + (mSecondBinding != null ? mSecondBinding.get() : mSecondValue));
            return mSecondBinding != null ? mSecondBinding.get() : mSecondValue;
        } else if (!primeiroQuadrante && !segundoQuadrante && terceiroQuadrante && !quartoQuadrante) {
//            System.out.println("3 " + (mSecondBinding != null ? mSecondBinding.get() : mSecondValue));
            return mThirthBinding != null ? mThirthBinding.get() : mThirthValue;
        } else if (!primeiroQuadrante && !segundoQuadrante && !terceiroQuadrante && quartoQuadrante) {
//            System.out.println("4 " + (mSecondBinding != null ? mSecondBinding.get() : mSecondValue));
            return mFourthBinding != null ? mFourthBinding.get() : mFourthValue;
        } else {
//            System.out.println("zero!");
            return 0;
        }
    }

    public CartesianCase first(DoubleExpression db) {
        mFirstBinding = db;
        return this;
    }

    public CartesianCase first(double v) {
        mFirstValue = v;
        return this;
    }

    public CartesianCase second(DoubleExpression db) {
        mSecondBinding = db;
        return this;
    }

    public CartesianCase second(double v) {
        mSecondValue = v;
        return this;
    }

    public CartesianCase thirth(DoubleExpression db) {
        mThirthBinding = db;
        return this;
    }

    public CartesianCase thirth(double v) {
        mThirthValue = v;
        return this;
    }

    public CartesianCase fourth(DoubleExpression db) {
        mFourthBinding = db;
        return this;
    }

    public CartesianCase fourth(double v) {
        mFourthValue = v;
        return this;
    }

    public CartesianCase firstAndSecond(DoubleExpression db) {
        mFirstAndSecondBinding = db;
        return this;
    }

    public CartesianCase firstAndSecond(double v) {
        mFirstAndSecondValue = v;
        return this;
    }

    public CartesianCase thirthAndFourth(DoubleExpression db) {
        mTerceiroAndQuartoBinding = db;
        return this;
    }

    public CartesianCase thirthAndFourth(double v) {
        mTerceiroAndQuartoValue = v;
        return this;
    }
    
    public CartesianCase secondAndThirth(DoubleBinding add) {
        mSecondAndThirthBinding = add;
        return this;
    }

}

class PontoMedioPoint3D extends ObjectBinding<Point3D> {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    public PontoMedioPoint3D(Node a, Node b) {
        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
    }

    @Override
    protected Point3D computeValue() {
        double xi = Math.min(mXa.get(), mXb.get());
        double xf = Math.max(mXa.get(), mXb.get());
        double x = xi + ((xf - xi) / 2);

        double yi = Math.min(mYa.get(), mYb.get());
        double yf = Math.max(mYa.get(), mYb.get());
        double y = yi + ((yf - yi) / 2);
        System.out.printf("(%f, %f)\n", x, y);
        return new Point3D(x, y, 1);
    }

}

class PontoTopLeft extends DoubleBinding {

    private final DoubleProperty mXa;
    private final DoubleProperty mYa;
    private final DoubleProperty mXb;
    private final DoubleProperty mYb;

    static final int TIPO_X = 0;
    static final int TIPO_Y = 1;
    private final int mTipo;

    public PontoTopLeft(Node a, Node b, int tipo) {
        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
        mXa = a.layoutXProperty();
        mYa = a.layoutYProperty();
        mXb = b.layoutXProperty();
        mYb = b.layoutYProperty();
        mTipo = tipo;
    }

    @Override
    protected double computeValue() {
        return (mTipo == TIPO_X ? Math.min(mXa.get(), mXb.get()) : Math.min(mYa.get(), mYb.get()));
    }

}

class Geom {

    static DoubleBinding distance(Node origem, Node destino) {
        return new DistanciaLinha(origem, destino);
    }

    static DoubleBinding angle(Node origem, Node destino) {
        return new Angulo(origem, destino);
    }

//    static DoubleBinding middlePointX(Node origem, Node destino) {
//        return new PontoMedio(origem, destino, PontoMedio.TIPO_X);
//    }
//
//    static DoubleBinding middlePointY(Node origem, Node destino) {
//        return new PontoMedio(origem, destino, PontoMedio.TIPO_Y);
//    }    
}
