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

/**
 *
 * @author emerson
 */
public class VerticeView extends Region implements Vertice {

    private Circle circle;
    public static final int RAIO_CIRCULO = 15;
    public static final int ESPESSURA_PERIMETRO_CIRCULO = 2;
    private List<Transicao> mTransicoesSaida = new ArrayList<>();
    private List<Transicao> mTransicoesEntrada = new ArrayList<>();
    private Label text;
    private int mId;
    private Map<String, Object> mTags = new HashMap<>();

    public VerticeView(int id) {
        mId = id;
        circle = new Circle(RAIO_CIRCULO);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.CYAN);
        getChildren().add(circle);

        text = new Label();  
        text.setLayoutX(-2);
        text.setLayoutY(-8);
        //text.setLayoutX(-5);
        //text.setLayoutY(-5);
        getChildren().add(text);
    }

    public Circle getCircle() {
        return circle;
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
                text.setText(valor.toString());
                break;
            case GrafoEditor.TAG_POS_X:
                setLayoutX((Double) valor);
                break;
            case GrafoEditor.TAG_POS_Y:
                setLayoutY((Double) valor);
                break;
            case GrafoEditor.TAG_COR:
                circle.setFill((Color) valor);
                break;
            case GrafoEditor.TAG_DEFAULT:                
                setTag(GrafoEditor.TAG_COR, (Boolean) valor ? Color.RED: Color.CYAN);
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
        double raio = circle.getRadius();
        double dx = x - getLayoutX() ;
        double dy = y - getLayoutY();        
        double d2 = dx * dx + dy * dy;        
        boolean pertence =  d2 < raio * raio;
        //System.out.println("dx: " + dx + " dy: " + dy + " dist^2: " + d2 + " raio^2: " + (raio * raio));
        circle.setStrokeWidth(pertence ? 2 : 1);
        return pertence;
    }
}
