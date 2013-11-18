package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private Line transicao;
    private final Label txtRotulo;
    private Map<String, Object> tags = new HashMap<>();
    private static String TAG_PESO_RAIO_ARCO = "_peso";
    private final TextField edtRotulo;
    private final Polygon polygon1;

    private int maiorPeso(Vertice origem, Vertice destino) {
        int maior = 0;
        for (Transicao t : origem.getTransicoesSaida()) {
            if (t.getDestino() != destino) {
                continue;
            }
            int p = (int) t.getTag(TAG_PESO_RAIO_ARCO);
            if (p > maior) {
                maior = p;
            }
        }
        return maior;
    }

    public TransicaoView(final VerticeView origem, final VerticeView destino) {
        this.origem = origem;
        this.destino = destino;

//        int pesoRaioArco = maiorPeso(origem, destino) + 1;//Math.max(maiorPeso(origem, destino), maiorPeso(destino, origem)) + 1;
//        double raioYArcoTransicao = 25 * pesoRaioArco;
//        tags.put(TAG_PESO_RAIO_ARCO, pesoRaioArco);

        polygon1 = new Polygon(new double[]{
            5.0, 0.0,
            10.0, 10.0,
            0.0, 10.0
        });
        polygon1.setFill(Color.BLACK);
//        polygon1.layoutXProperty().bind(new PosMedia(origem.layoutXProperty(), destino.layoutXProperty(), 10.0));        
//        polygon1.layoutYProperty().bind(new PosMedia(origem.layoutYProperty(), destino.layoutYProperty(), 10.0));        
        polygon1.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 30, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
        polygon1.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 30, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
        polygon1.rotateProperty().bind(new RotacaoFlecha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
        getChildren().add(polygon1);

//        Path path = new Path();
//        boolean par = true;//indiceTransicao % 2 == 0;
//        MoveTo moveTo = new MoveTo();
//        moveTo.xProperty().bind(par ? destino.layoutXProperty() : origem.layoutXProperty());
//        moveTo.yProperty().bind(par ? destino.layoutYProperty() : origem.layoutYProperty());//
//        ArcTo arcTo = new ArcTo();
//        arcTo.xProperty().bind(par ? origem.layoutXProperty() : destino.layoutXProperty());
//        arcTo.yProperty().bind(par ? origem.layoutYProperty() : destino.layoutYProperty());        
//        arcTo.radiusXProperty().bind(new Distancia(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));//        
//        arcTo.setRadiusY(raioYArcoTransicao);
//        arcTo.XAxisRotationProperty().bind(new RotacaoArco(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty()));
//        path.getElements().add(moveTo);
//        path.getElements().add(arcTo);        
//        getChildren().add(path);
//        path.toBack();

        transicao = new Line();
        transicao.startXProperty().bind(origem.layoutXProperty());
        transicao.startYProperty().bind(origem.layoutYProperty());
        transicao.endXProperty().bind(destino.layoutXProperty());
        transicao.endYProperty().bind(destino.layoutYProperty());
        getChildren().add(transicao);

        txtRotulo = new Label();
        txtRotulo.setText("--");
        txtRotulo.layoutXProperty().bind(polygon1.layoutXProperty().subtract(txtRotulo.widthProperty().divide(2)));
        txtRotulo.layoutYProperty().bind(polygon1.layoutYProperty().subtract(txtRotulo.heightProperty()));
        txtRotulo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                edtRotulo.setVisible(true);
                txtRotulo.setVisible(false);
            }
        });
        edtRotulo = new TextField("--");
        edtRotulo.setPrefColumnCount(10);
        edtRotulo.layoutXProperty().bind(polygon1.layoutXProperty().subtract(txtRotulo.widthProperty().divide(2)));
        edtRotulo.layoutYProperty().bind(polygon1.layoutYProperty().subtract(txtRotulo.heightProperty()));
        edtRotulo.setVisible(false);
        edtRotulo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setTag(GrafoEditor.TAG_LABEL, edtRotulo.getText());
                edtRotulo.setVisible(false);
                txtRotulo.setVisible(true);
            }
        });
        getChildren().add(txtRotulo);
        getChildren().add(edtRotulo);
    }

    @Override
    public Vertice getOrigem() {
        return this.origem;
    }

    @Override
    public Vertice getDestino() {
        return this.destino;
    }

    @Override
    public void setTag(String chave, Object valor) {
        switch (chave) {
            case GrafoEditor.TAG_LABEL:
                txtRotulo.setText(valor.toString());
                break;
        }
        tags.put(chave, valor);
    }

    @Override
    public Object getTag(String chave) {
        return tags.get(chave);
    }

    public boolean pontoPertenceAoObjeto(double x, double y) {
        return pontoPertenceAoRotulo(x, y) || pontoPertenceALinha(x, y);
    }

    private boolean pontoPertenceAoRotulo(double x, double y) {
        double _x = txtRotulo.getLayoutX();
        double _y = txtRotulo.getLayoutY();
        boolean a = x >= _x && x <= _x + txtRotulo.getWidth();
        boolean b = y >= _y && y <= _y + txtRotulo.getHeight();
        return a && b;
    }

    private boolean pontoPertenceALinha(double x, double y) {        
        double startX = transicao.getStartX();
        double endX = transicao.getEndX();
        if (x < startX || x > endX) {
            transicao.setStrokeWidth(1);
            return false;
        }
        double apx = x - startX;
        double apy = y - transicao.getStartY();
        double abx = endX - startX;
        double aby = transicao.getEndY() - transicao.getStartY();

        double ab2 = abx * abx + aby * aby;
        double ap_ab = apx * abx + apy * aby;
        double t = ap_ab / ab2;

        double cx = (transicao.getStartX() + abx * t);
        double cy = (transicao.getStartY() + aby * t);
        boolean selecionado = Math.abs(x - cx) < 5 && Math.abs(y - cy) < 5;
        transicao.setStrokeWidth(selecionado ? 2 : 1);
        //polygon1.setStrokeWidth(selecionado ? 2 : 1);
        return selecionado;
    }

    private static class Distancia extends DoubleBinding {

        private final DoubleProperty mXi;
        private final DoubleProperty mYi;
        private final DoubleProperty mXf;
        private final DoubleProperty mYf;

        public Distancia(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
            bind(xi, yi, xf, yf);
            mXi = xi;
            mYi = yi;
            mXf = xf;
            mYf = yf;
        }

        @Override
        protected double computeValue() {
            double x = mXi.getValue() - mXf.getValue();
            double y = mYi.getValue() - mYf.getValue();
            double r = Math.sqrt(x * x + y * y) / 2;
            System.out.println("r: " + r);
            return r;
        }
    }
}
class PontoMedioArco extends DoubleBinding {

    private final DoubleProperty mXi;
    private final DoubleProperty mYi;
    private final DoubleProperty mXf;
    private final DoubleProperty mYf;
    public static final int EIXO_X = 0;
    public static final int EIXO_Y = 1;
    private final int mEixo;

    public PontoMedioArco(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf, DoubleProperty rotateProperty, int eixo) {
        bind(xi, yi, xf, yf, rotateProperty);
        mXi = xi;
        mYi = yi;
        mXf = xf;
        mYf = yf;
        mEixo = eixo;
    }

    @Override
    protected double computeValue() {
        double posInicial = mEixo == EIXO_X ? mXi.getValue() : mYi.getValue();
        double posFinal = mEixo == EIXO_X ? mXf.getValue() : mYf.getValue();
        double distancia = Math.abs(posInicial - posFinal);
        double deslocamentoMedio = (distancia / 2);
        double ancora = Math.min(posInicial, posFinal);

//        boolean primeiroQuadrante = mXi.getValue() < mXf.getValue() && mYi.getValue() >= mYf.getValue();
//        boolean segundoQuadrante = mXi.getValue() > mXf.getValue() && mYi.getValue() >= mYf.getValue();
//        boolean terceiroQuadrante = mXi.getValue() > mXf.getValue() && mYi.getValue() <= mYf.getValue();
//        boolean quartoQuadrante = mXi.getValue() < mXf.getValue() && mYi.getValue() <= mYf.getValue();

        double r = ancora + deslocamentoMedio;

        if (mEixo == EIXO_Y) {
            return r + 10;
        }
        System.out.println("ponto " + mEixo + ": " + r);
        return r;
    }
}

class RotacaoArco extends DoubleBinding {

    private final DoubleProperty xi;
    private final DoubleProperty yi;
    private final DoubleProperty xf;
    private final DoubleProperty yf;

    public RotacaoArco(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf) {
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
            return -tangGrau;
        } else if (segundoQuadrante) {
            return tangGrau;
        } else if (terceiroQuadrante) {
            return -tangGrau;
        } else if (quartoQuadrante) {
            return tangGrau;
        }
        return 0;
    }
}

class RotacaoFlecha extends DoubleBinding {

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

class PosMedia extends DoubleBinding {

    private final DoubleProperty posInicial;
    private final DoubleProperty posFinal;
    private final double tamanho;

    public PosMedia(DoubleProperty posInicial, DoubleProperty posFinal, double tamanho) {
        super.bind(posInicial, posFinal);
        this.posInicial = posInicial;
        this.posFinal = posFinal;
        this.tamanho = tamanho;
    }

    @Override
    protected double computeValue() {
        double distancia = Math.abs(posInicial.getValue() - posFinal.getValue());
        double deslocamentoMedio = (distancia - tamanho) / 2;
        double ancora = Math.min(posInicial.getValue(), posFinal.getValue());
        return ancora + deslocamentoMedio;
    }
}

class PosMedia2 extends DoubleBinding {

    static final int EIXO_X = 0;
    static final int EIXO_Y = 1;
    static final int OFFSET_TAMANHO = 10;
    static final int DISTANCIA_X_RELATIVA = 60;
    private final DoubleProperty xi;
    private final DoubleProperty yi;
    private final DoubleProperty xf;
    private final DoubleProperty yf;
    private final int eixo;

    public PosMedia2(DoubleProperty xi, DoubleProperty yi, DoubleProperty xf, DoubleProperty yf, int eixo) {
        super.bind(xi, yi, xf, yf);
        this.xi = xi;
        this.yi = yi;
        this.xf = xf;
        this.yf = yf;
        this.eixo = eixo;
    }

    @Override
    protected double computeValue() {
        double deltaX = Math.abs(xi.getValue() - xf.getValue());
        double deltaY = Math.abs(yi.getValue() - yf.getValue());
        boolean primeiroQuadrante = xi.getValue() < xf.getValue() && yi.getValue() >= yf.getValue();
        boolean segundoQuadrante = xi.getValue() > xf.getValue() && yi.getValue() >= yf.getValue();
        boolean terceiroQuadrante = xi.getValue() > xf.getValue() && yi.getValue() <= yf.getValue();
        boolean quartoQuadrante = xi.getValue() < xf.getValue() && yi.getValue() <= yf.getValue();

        double d = Math.min(DISTANCIA_X_RELATIVA, deltaX);
        if (eixo == EIXO_X) {
            if (primeiroQuadrante || quartoQuadrante) {
                double ancora = Math.max(xi.getValue(), xf.getValue());
                return ancora - d + OFFSET_TAMANHO;
            } else if (segundoQuadrante || terceiroQuadrante) {
                double ancora = Math.min(xi.getValue(), xf.getValue());
                return ancora + d - OFFSET_TAMANHO;
            }
        } else if (eixo == EIXO_Y) {
            double distancia = d * deltaY / (deltaX + d);
            if (primeiroQuadrante || segundoQuadrante) {
                double ancora = Math.min(yi.getValue(), yf.getValue());
                return ancora + distancia - OFFSET_TAMANHO;
            } else if (terceiroQuadrante || quartoQuadrante) {
                double ancora = Math.max(yi.getValue(), yf.getValue());
                return ancora - distancia + OFFSET_TAMANHO;
            }
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