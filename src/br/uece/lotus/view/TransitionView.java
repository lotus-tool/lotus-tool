package br.uece.lotus.view;

import br.uece.lotus.model.TransitionModel;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;


public class TransitionView extends Region implements View {

    private static final double ESPESSURA_LINHA_DESTAQUE = 2;
    private static final double ESPESSURA_LINHA_NORMAL = 1;
    private static final Paint COR_LINHA_SELECIONADA = Color.PURPLE;
    private static final Paint COR_LINHA_NORMAL = Color.BLACK;
    private static final double ESCALA_SETA_DESTAQUE = 1.5;
    private static final double ESCALA_SETA_NORMAL = 1.0;
    private final Label mRotulo;
    private final Polygon mSeta;
    private Line mLinha;
    private boolean mSelecionado;
    private TransitionModel mModel;
    private final Ellipse mLoop;

    public TransitionView() {
        mRotulo = new Label();
        mSeta = new Polygon(new double[]{
            5.0, 0.0,
            10.0, 10.0,
            0.0, 10.0
        });

        mLoop = new Ellipse();
        mLoop.setRadiusX(25);
        mLoop.setRadiusY(20);
        mLoop.setFill(null);
        mLoop.setStroke(Color.BLACK);
        getChildren().add(mLoop);

        mSeta.setFill(COR_LINHA_NORMAL);
        getChildren().add(mSeta);

        mLinha = new Line();
        getChildren().add(mLinha);

        mRotulo.setTextFill(COR_LINHA_NORMAL);        
        getChildren().add(mRotulo);
    }

    public void setModel(TransitionModel t) {
        mModel = t;

        if (mModel == null) {
            return;
        }

        StateView origem = (StateView) mModel.getOrigem().getTag();
        StateView destino = (StateView) mModel.getDestino().getTag();

        if (origem == destino) {
            mLoop.layoutXProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
            mLoop.layoutYProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));
            mSeta.layoutXProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
            mSeta.layoutYProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));

            mLoop.setVisible(true);
            mLinha.setVisible(false);
        } else {
            mSeta.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
            mSeta.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 60, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
            mSeta.rotateProperty().bind(new RotacaoSeta(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));

            mLinha.startXProperty().bind(origem.layoutXProperty());
            mLinha.startYProperty().bind(origem.layoutYProperty());
            mLinha.endXProperty().bind(destino.layoutXProperty());
            mLinha.endYProperty().bind(destino.layoutYProperty());

            mRotulo.layoutXProperty().bind(mSeta.layoutXProperty().subtract(mRotulo.widthProperty().divide(2)));
            mRotulo.layoutYProperty().bind(mSeta.layoutYProperty().subtract(mRotulo.heightProperty()));

            mLoop.setVisible(false);
            mLinha.setVisible(true);
        }
        refresh();
    }

    @Override
    public boolean isSelecionado() {
        return mSelecionado;
    }

    @Override
    public void setSelecionado(boolean selecionado) {
        mSelecionado = selecionado;
        mLinha.setStrokeWidth(selecionado ? ESPESSURA_LINHA_DESTAQUE : ESPESSURA_LINHA_NORMAL);
        Paint p = selecionado ? COR_LINHA_SELECIONADA : COR_LINHA_NORMAL;
        mLinha.setStroke(p);
        mSeta.setFill(p);
        mRotulo.setTextFill(p);
    }

    @Override
    public void setDestacado(boolean destacado) {
        mLinha.setStrokeWidth(destacado ? ESPESSURA_LINHA_DESTAQUE : ESPESSURA_LINHA_NORMAL);
        double s = destacado ? ESCALA_SETA_DESTAQUE : ESCALA_SETA_NORMAL;
        mSeta.setScaleX(s);
        mSeta.setScaleY(s);
    }

    @Override
    public boolean pontoPertenceAoObjeto(double x, double y) {
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
        boolean selecionado = Math.abs(x - cx) < 5 && Math.abs(y - cy) < 5;
        return selecionado;
    }

    @Override
    public void getPropriedades() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TransitionModel getModel() {
        return mModel;
    }

    public void refresh() {        
        String rotulo = mModel.getValue(ComponentEditor.TAG_LABEL);
        String probabilidade = mModel.getValue(ComponentEditor.TAG_PROBABILIDADE);
        String guarda = mModel.getValue(ComponentEditor.TAG_GUARD);
        
        String s = "";
        if (rotulo != null) {
            s += rotulo;
        }
        if (probabilidade != null) {
            s += " " + probabilidade;
        }
        if (guarda != null) {
            s += " [" + guarda + "]";
        }
        mRotulo.setText(s);
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
}
class RotacaoSeta extends DoubleBinding {

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
        boolean primeiroQuadrante = xi.getValue() < xf.getValue() && yi.getValue() >= yf.getValue();
        boolean segundoQuadrante = xi.getValue() > xf.getValue() && yi.getValue() >= yf.getValue();
        boolean terceiroQuadrante = xi.getValue() > xf.getValue() && yi.getValue() <= yf.getValue();
        boolean quartoQuadrante = xi.getValue() < xf.getValue() && yi.getValue() <= yf.getValue();
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
    private double OFFSET_X = 5;
    private double OFFSET_Y = 5;

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
        double c = distanciaDesejada / distance * (componente == COMPONENTE_X ? deltaX : deltaY);

        boolean primeiroQuadrante = xi.getValue() < xf.getValue() && yi.getValue() >= yf.getValue();
        boolean segundoQuadrante = xi.getValue() > xf.getValue() && yi.getValue() >= yf.getValue();
        boolean terceiroQuadrante = xi.getValue() > xf.getValue() && yi.getValue() <= yf.getValue();
        boolean quartoQuadrante = xi.getValue() < xf.getValue() && yi.getValue() <= yf.getValue();

        if (componente == COMPONENTE_X) {
            if (primeiroQuadrante || quartoQuadrante) {
                c = xf.getValue() - c - OFFSET_X;
            } else if (segundoQuadrante || terceiroQuadrante) {
                c = xf.getValue() + c - OFFSET_X;
            }
        } else if (componente == COMPONENTE_Y) {
            if (primeiroQuadrante || segundoQuadrante) {
                c = yf.getValue() + c - OFFSET_Y;
            } else if (terceiroQuadrante || quartoQuadrante) {
                c = yf.getValue() - c - OFFSET_Y;
            }
        }
        return c;
    }
}
