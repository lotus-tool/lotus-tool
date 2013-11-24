package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import static br.uece.gamut.app.editor.VerticeView.RAIO_CIRCULO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

/**
 *
 * @author emerson
 */
public class VerticeView extends Region implements Vertice {

    public static final int RAIO_CIRCULO = 15;
    public static final int ESPESSURA_PERIMETRO_CIRCULO = 2;
    private static final Color COR_SELECIONADO = Color.PURPLE;
    private List<Transicao> mTransicoesSaida = new ArrayList<>();
    private List<Transicao> mTransicoesEntrada = new ArrayList<>();
    private Circle circle;
    private Label text;
    private int mId;
    private Map<String, Object> mTags = new HashMap<>();
    private boolean mSelecionado;
    private Color mCorAtual;
    private double ESPESSURA_LINHA_CIRCULO_DESTACADO = 2;
    private double ESPESSURA_LINHA_CIRCULO_NORMAL = 1;

    public VerticeView(int id) {
        mId = id;
        circle = new Circle(RAIO_CIRCULO);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.BLACK);
        circle.setFill(mCorAtual = Color.CYAN);
        getChildren().add(circle);

        text = new Label();
        text.setLayoutX(-2);
        text.setLayoutY(-8);
        getChildren().add(text);
    }

    public void setSelecionado(boolean selecionado) {
        mSelecionado = selecionado;
        circle.setFill(selecionado ? COR_SELECIONADO : mCorAtual);
    }
    
    public void setDestacado(boolean destacado) {
        circle.setStrokeWidth(destacado ? ESPESSURA_LINHA_CIRCULO_DESTACADO : ESPESSURA_LINHA_CIRCULO_NORMAL);
        //TODO: Setar a fonte de text com o estilo bold
    }

    public boolean isSelecionado() {
        return mSelecionado;
    }

    @Override
    public List<Transicao> getTransicoesSaida() {
        return mTransicoesSaida;
    }

    @Override
    public List<Transicao> getTransicoesEntrada() {
        return mTransicoesEntrada;
    }

    @Override
    public void setTag(String chave, Object valor) {
        switch (chave) {
            case GrafoEditor.TAG_LABEL:   
                text.setText((String) valor);
                break;
            case GrafoEditor.TAG_POS_X:
                setLayoutX((Double) valor);
                break;
            case GrafoEditor.TAG_POS_Y:
                setLayoutY((Double) valor);
                break;
            case GrafoEditor.TAG_COR:
                mCorAtual = (Color) valor;
                circle.setFill(mSelecionado ? COR_SELECIONADO : mCorAtual);
                break;
            case GrafoEditor.TAG_DEFAULT:
                setTag(GrafoEditor.TAG_COR, (Boolean) valor ? Color.RED : Color.CYAN);
                break;
        }
        mTags.put(chave, valor);        
    }

    @Override
    public Object getTag(String chave) {
        return mTags.get(chave);
    }

    @Override
    public int getID() {
        return mId;
    }

    void addTransicaoSaida(TransicaoView t) {
        mTransicoesSaida.add(t);
    }

    void addTransicaoEntrada(TransicaoView t) {
        mTransicoesEntrada.add(t);
    }

    boolean pontoPertenceAoObjeto(double x, double y) {
        double mX = getLayoutX();
        double mY = getLayoutY();
        double raio = circle.getRadius();
        if (x < (mX - raio) || y < (mY - raio)) {
            return false;
        }        
        double diametro = 2 * raio;
        if (x > (mX + diametro) || y > (mY + diametro)) {
            return false;
        }

        double dx = x - mX;
        double dy = y - mY;
        double d2 = dx * dx + dy * dy;
        boolean pertence = d2 < raio * raio;
        //System.out.println("dx: " + dx + " dy: " + dy + " dist^2: " + d2 + " raio^2: " + (raio * raio));        
        return pertence;
    }
}
