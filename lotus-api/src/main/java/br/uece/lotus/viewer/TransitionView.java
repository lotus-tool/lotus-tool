/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
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
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Region;

public abstract class TransitionView extends Region implements View, Transition.Listener {

//    static final double RAIO_ELIPSE_X = 25;
//    static final double RAIO_ELIPSE_Y = 20;
//    private final Label mRotulo;
//    private final Polygon mSeta;
//    private final Line mLinha;
//    private Ellipse mLoop;
    private Transition mTransition;
//    private Arc mArc;

    public TransitionView() {
//        mRotulo = new Label();
//        mSeta = new Polygon(new double[]{
//            0.0, 10.0,
//            10.0, 5.0,
//            0.0, 0.0
//        });
//        getChildren().add(mSeta);
//
//        mLoop = new Ellipse();
//        mLoop.setFill(null);
//        mLoop.setRadiusX(25);
//        mLoop.setRadiusY(20);
//        getChildren().add(mLoop);
//
//        mLinha = new Line();
//        getChildren().add(mLinha);
//        getChildren().add(mRotulo);
//
//        mArc = new Arc();
//        getChildren().add(mArc);
    }

    public void setTransition(Transition t) {
        if (mTransition != null) {
            mTransition.removeListener(this);
        }
        mTransition = t;
        if (mTransition != null) {
            mTransition.addListener(this);
            prepareView();
            updateView();
        }
    }

//    @Override
//    public boolean isInsideBounds(double x, double y) 
//    {
//        StateView origem = (StateView) mTransition.getSource().getValue("view");
//        StateView destino = (StateView) mTransition.getDestiny().getValue("view");
//        boolean selecionado;
//        int n = 0;
//        if (origem == destino) {
//            double r = elipseRatio(x, y, RAIO_ELIPSE_X, RAIO_ELIPSE_Y, mLoop.getLayoutX(), mLoop.getLayoutY());
//            selecionado = r > 0.3 && r < 1.3;
//        } else if (n > 0) {
//            selecionado = false;
//        } else {
//            double startX = mLinha.getStartX();
//            double endX = mLinha.getEndX();
//            if (x < Math.min(startX, endX) || x > Math.max(startX, endX)) {
//                return false;
//            }
//            double apx = x - startX;
//            double apy = y - mLinha.getStartY();
//            double abx = endX - startX;
//            double aby = mLinha.getEndY() - mLinha.getStartY();
//
//            double ab2 = abx * abx + aby * aby;
//            double ap_ab = apx * abx + apy * aby;
//            double t = ap_ab / ab2;
//
//            double cx = (mLinha.getStartX() + abx * t);
//            double cy = (mLinha.getStartY() + aby * t);
//            selecionado = Math.abs(x - cx) < 5 && Math.abs(y - cy) < 5;
//        }
//        return selecionado;
//    }
    public Transition getTransition() {
        return mTransition;
    }

    protected abstract void prepareView();
//    {
//        StateView origem = (StateView) mTransition.getSource().getValue("view");
//        StateView destino = (StateView) mTransition.getDestiny().getValue("view");
//
//        int n = destino.getState().getTransitionsTo(origem.getState()).size();
//        System.out.println("n: " + n);
//
//        if (origem == destino) {
//            mLoop.layoutXProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
//            mLoop.layoutYProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));
//            mSeta.layoutXProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
//            mSeta.layoutYProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));
//            mSeta.setRotate(180);
//            mRotulo.layoutXProperty().bind(mSeta.layoutXProperty().subtract(mRotulo.widthProperty()));
//            mRotulo.layoutYProperty().bind(mSeta.layoutYProperty().subtract(mRotulo.heightProperty().divide(2)));
//            mLoop.setVisible(true);
//            mLinha.setVisible(false);
//            mArc.setVisible(false);
//        } else if (n > 0) {
//            Node curva = new TransicaoEmArco(n, origem, destino);
////            curva.setRotationAxis(Rotate.Z_AXIS);
////            curva.rotateProperty().bind(Geom.angle(origem, destino));
//            Rotate r = new Rotate();
//            r.angleProperty().bind(Geom.angle(origem, destino));
//            r.setPivotX(0);
//            r.setPivotY(25);
//            curva.getTransforms().add(r);
//            curva.layoutXProperty().bind(Geom.min(origem.layoutXProperty(), destino.layoutXProperty()));
//            curva.layoutYProperty().bind(Geom.min(origem.layoutYProperty(), destino.layoutYProperty()).subtract(25));
//            getChildren().add(curva);
//            
////            mArc.centerXProperty().bind(Geom.middlePointX(origem, destino));
////            mArc.centerYProperty().bind(Geom.middlePointY(origem, destino));
////            mArc.radiusXProperty().bind(Geom.distance(origem, destino).divide(2));
////            Point3D p = new Point3D(origem.getLayoutX(), origem.getLayoutY(), 0);
////            mArc.setRotationAxis(Rotate.Z_AXIS);
////            mArc.rotateProperty().bind(Geom.angle(origem, destino));
////            mArc.setRadiusY(n * 20);
////            mArc.setStartAngle(0f);
////            mArc.setLength(180f);
////            mArc.setType(ArcType.OPEN);
////            mArc.setFill(null);
////            mArc.setStroke(Color.BLACK);
////
////            mSeta.layoutXProperty().bind(Geom.middlePointX(origem, destino));
////            mSeta.layoutYProperty().bind(Geom.middlePointY(origem, destino).subtract(n * 20 + 5));
//////            mSeta.setRotationAxis(Rotate.Z_AXIS);
////            mSeta.setRotationAxis(p);
////            mSeta.rotateProperty().bind(Geom.angle(origem, destino));
//            mLoop.setVisible(false);
//            mLinha.setVisible(false);
//            mArc.setVisible(true);
//        } else {
//            mSeta.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
//            mSeta.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
////            mSeta.rotateProperty().bind(new RotacaoSeta(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
//            mSeta.rotateProperty().bind(Geom.angle(origem, destino));
//
//            mLinha.startXProperty().bind(origem.layoutXProperty());
//            mLinha.startYProperty().bind(origem.layoutYProperty());
//            mLinha.endXProperty().bind(destino.layoutXProperty());
//            mLinha.endYProperty().bind(destino.layoutYProperty());
//
//            mRotulo.layoutXProperty().bind(mSeta.layoutXProperty().subtract(mRotulo.widthProperty().divide(2)));
//            mRotulo.layoutYProperty().bind(mSeta.layoutYProperty().subtract(mRotulo.heightProperty()));
//
//            mLoop.setVisible(false);
//            mLinha.setVisible(true);
//            mArc.setVisible(false);
//        }
//    }

    protected abstract void updateView();
//    {
//        String style = "-fx-stroke-width: " + mTransition.getWidth() + ";";
//        style += "-fx-stroke: " + mTransition.getColor() + ";";
//        mLinha.setStyle(style);
//        mLoop.setStyle(style);
//        mSeta.setStyle("-fx-fill: " + mTransition.getColor() + ";");
//
//        style = "-fx-text-fill: " + mTransition.getTextColor() + ";";
//        style += "-fx-font-weight: " + mTransition.getTextStyle() + ";";
//        style += "-fx-font-size: " + mTransition.getTextSize() + ";";
//        mRotulo.setStyle(style);
//        mRotulo.setText(getComputedLabel());
//    }

    @Override
    public void onChange(Transition transition) {
        Platform.runLater(() -> {
            updateView();
        });
    }

    protected String getComputedLabel() {
        String s = "";

        if (mTransition.getGuard() != null) {
            s += "[" + mTransition.getGuard() + "]";
        }

        if (mTransition.getProbability() != null) {
            s += String.format(" (%.2f)", mTransition.getProbability());
        }

        if (mTransition.getLabel() != null) {
            s += " " + mTransition.getLabel();
        }
        return s;
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

    private static class RotacaoSeta extends DoubleBinding {

        private final DoubleProperty xi;
        private final DoubleProperty yi;
        private final DoubleProperty xf;
        private final DoubleProperty yf;

        public RotacaoSeta(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
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
            //System.out.println(String.format("deltaX: %.2f deltaY: %.2f tang: %.2f tangRad: %.2f tangGrau: %.2f", deltaX, deltaY, tang, tangRad, tangGrau));
            boolean primeiroQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean segundoQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() >= yf.getValue();
            boolean terceiroQuadrante = xi.getValue() >= xf.getValue() && yi.getValue() <= yf.getValue();
            boolean quartoQuadrante = xi.getValue() <= xf.getValue() && yi.getValue() <= yf.getValue();
//            System.out.printf("1: %s 2: %s 3: %s 4:%s\n", "" + primeiroQuadrante, "" + segundoQuadrante, "" + terceiroQuadrante, "" + quartoQuadrante);
            if (terceiroQuadrante && quartoQuadrante) {
                return 180;
            } else if (primeiroQuadrante) {
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

    private double elipseRatio(double x, double y, double rx, double ry, double h, double k) {
        return (x - h) * (x - h) / (rx * rx) + (y - k) * (y - k) / (ry * ry);
    }

}

//class TransicaoEmArco extends Region {
//
//    private final Label mRotulo;
//    private final Polygon mSeta;
//    private final Arc mArc;
//    private final int mLevel;
//
//    public TransicaoEmArco(int level, Node origem, Node destino) {
//        mLevel = level;
//        mRotulo = new Label();
//        mSeta = new Polygon(new double[]{
//            0.0, 10.0,
//            10.0, 5.0,
//            0.0, 0.0
//        });
//        mArc = new Arc();
//        getChildren().addAll(mRotulo, mSeta, mArc);
//        double raio = mLevel * 25;                
//        mArc.radiusXProperty().bind(Geom.distance(origem, destino).divide(2));
//        mArc.setRadiusY(raio);
//        mArc.centerXProperty().bind(mArc.radiusXProperty().divide(2));
//        mArc.setCenterY(raio);        
//        mArc.setStartAngle(0f);
//        mArc.setLength(180f);
//        mArc.setType(ArcType.OPEN);
//        mArc.setFill(null);
//        mArc.setStroke(Color.BLACK);
//
//        mSeta.layoutXProperty().bind(Geom.middlePointX(origem, destino));
//        mSeta.layoutYProperty().bind(Geom.middlePointY(origem, destino).subtract(mLevel * 25 - 5));
//    }
//
//}
//
//class DistanciaLinha extends DoubleBinding {
//
//    private final DoubleProperty mXa;
//    private final DoubleProperty mYa;
//    private final DoubleProperty mXb;
//    private final DoubleProperty mYb;
//
//    public DistanciaLinha(Node a, Node b) {
//        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
//        mXa = a.layoutXProperty();
//        mYa = a.layoutYProperty();
//        mXb = b.layoutXProperty();
//        mYb = b.layoutYProperty();
//    }
//
//    @Override
//    protected double computeValue() {
//        double deltaX = mXb.get() - mXa.get();
//        double deltaY = mYb.get() - mYa.get();
//        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//    }
//
//}
//
//class Min extends DoubleBinding {
//
//    private final DoubleProperty mA;
//    private final DoubleProperty mB;    
//
//    public Min(DoubleProperty a, DoubleProperty b) {
//        bind(a, b);
//        mA = a;
//        mB = b;        
//    }
//
//    @Override
//    protected double computeValue() {
//        return Math.min(mA.get(), mB.get());
//    }
//
//}
//
//class Angulo extends DoubleBinding {
//
//    private final DoubleProperty mXa;
//    private final DoubleProperty mYa;
//    private final DoubleProperty mXb;
//    private final DoubleProperty mYb;
//
//    public Angulo(Node a, Node b) {
//        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
//        mXa = a.layoutXProperty();
//        mYa = a.layoutYProperty();
//        mXb = b.layoutXProperty();
//        mYb = b.layoutYProperty();
//    }
//
//    @Override
//    protected double computeValue() {
//        double deltaX = mXb.get() - mXa.get();
//        double deltaY = mYb.get() - mYa.get();
//        if (deltaX == 0) {
//            return 90;
//        }
//        if (deltaY == 0) {
//            return 0;
//        }
//        double tang = deltaY / deltaX;
//        double tangRad = Math.atan(tang);
//        double tangGrau = tangRad * 36 / 2 * Math.PI;
//        return tangGrau;
//    }
//}
//
//class PontoMedio extends DoubleBinding {
//
//    private final DoubleProperty mXa;
//    private final DoubleProperty mYa;
//    private final DoubleProperty mXb;
//    private final DoubleProperty mYb;
//
//    static final int TIPO_X = 0;
//    static final int TIPO_Y = 1;
//    private final int mTipo;
//
//    public PontoMedio(Node a, Node b, int tipo) {
//        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
//        mXa = a.layoutXProperty();
//        mYa = a.layoutYProperty();
//        mXb = b.layoutXProperty();
//        mYb = b.layoutYProperty();
//        mTipo = tipo;
//    }
//
//    @Override
//    protected double computeValue() {
////        double i = (mTipo == TIPO_X ? Math.min(mXa.get(), mXb.get()) : Math.min(mYa.get(), mYb.get()));
////        double f = (mTipo == TIPO_X ? Math.max(mXa.get(), mXb.get()) : Math.max(mYa.get(), mYb.get()));
//        double r = (mTipo == TIPO_X ? Math.abs(mXa.get() - mXb.get()) : Math.abs(mYa.get() - mYb.get())) / 2;
//        return r;
//    }
//
//}
//
//class PontoMedioPoint3D extends ObjectBinding<Point3D> {
//
//    private final DoubleProperty mXa;
//    private final DoubleProperty mYa;
//    private final DoubleProperty mXb;
//    private final DoubleProperty mYb;
//
//    public PontoMedioPoint3D(Node a, Node b) {
//        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
//        mXa = a.layoutXProperty();
//        mYa = a.layoutYProperty();
//        mXb = b.layoutXProperty();
//        mYb = b.layoutYProperty();        
//    }
//
//    @Override
//    protected Point3D computeValue() {
//        double xi = Math.min(mXa.get(), mXb.get());
//        double xf = Math.max(mXa.get(), mXb.get());
//        double x = xi + ((xf - xi) / 2);
//        
//        double yi = Math.min(mYa.get(), mYb.get());
//        double yf = Math.max(mYa.get(), mYb.get());        
//        double y = yi + ((yf - yi) / 2);
//        System.out.printf("(%f, %f)\n", x, y);
//        return new Point3D(x, y, 1);
//    }
//
//}
//
//class PontoTopLeft extends DoubleBinding {
//
//    private final DoubleProperty mXa;
//    private final DoubleProperty mYa;
//    private final DoubleProperty mXb;
//    private final DoubleProperty mYb;
//
//    static final int TIPO_X = 0;
//    static final int TIPO_Y = 1;
//    private final int mTipo;
//
//    public PontoTopLeft(Node a, Node b, int tipo) {
//        bind(a.layoutXProperty(), a.layoutYProperty(), b.layoutXProperty(), b.layoutYProperty());
//        mXa = a.layoutXProperty();
//        mYa = a.layoutYProperty();
//        mXb = b.layoutXProperty();
//        mYb = b.layoutYProperty();
//        mTipo = tipo;
//    }
//
//    @Override
//    protected double computeValue() {
//        return (mTipo == TIPO_X ? Math.min(mXa.get(), mXb.get()) : Math.min(mYa.get(), mYb.get()));        
//    }
//
//}
//
//class Geom {
//
//    static DoubleBinding distance(Node origem, Node destino) {
//        return new DistanciaLinha(origem, destino);
//    }
//
//    static DoubleBinding angle(Node origem, Node destino) {
//        return new Angulo(origem, destino);
//    }
//
//    static DoubleBinding middlePointX(Node origem, Node destino) {
//        return new PontoMedio(origem, destino, PontoMedio.TIPO_X);
//    }
//
//    static DoubleBinding middlePointY(Node origem, Node destino) {
//        return new PontoMedio(origem, destino, PontoMedio.TIPO_Y);
//    }
//    
//    static ObjectBinding<Point3D> middlePoint3D(Node origem, Node destino) {
//        return new PontoMedioPoint3D(origem, destino);
//    }
//    
//    static DoubleBinding topLeftPointX(Node origem, Node destino) {
//        return new PontoTopLeft(origem, destino, PontoMedio.TIPO_X);
//    }
//
//    static DoubleBinding topLeftPointY(Node origem, Node destino) {
//        return new PontoTopLeft(origem, destino, PontoMedio.TIPO_Y);
//    }
//    
//    static DoubleBinding min(DoubleProperty a, DoubleProperty b) {
//        return new Min(a, b);
//    }
//
//}
