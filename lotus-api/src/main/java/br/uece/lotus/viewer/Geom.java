package br.uece.lotus.viewer;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;

/**
 * Created by emerson on 03/03/15.
 */
class Geom {

    static DoubleBinding distance(Node origem, Node destino) {
        return new DistanciaLinha(origem, destino);
    }

    static DoubleBinding angle(Node origem, Node destino) {
        return new Angulo(origem, destino);
    }

    static class Angulo extends DoubleBinding {

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
                //System.out.println("zero");
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

    static class CartesianCase extends DoubleBinding {

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

    //        System.out.printf("%s, %s, %s, %s\n", primeiroQuadrante, segundoQuadrante, terceiroQuadrante, quartoQuadrante);
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

    static class DistanciaLinha extends DoubleBinding {

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
}
