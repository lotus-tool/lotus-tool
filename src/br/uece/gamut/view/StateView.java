package br.uece.gamut.view;

import br.uece.gamut.model.Model;
import br.uece.gamut.model.StateModel;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class StateView extends Region implements View {

    public static final int RAIO_CIRCULO = 15;
    public static final int ESPESSURA_PERIMETRO_CIRCULO = 2;
    private static final Color COR_SELECIONADO = Color.PURPLE;
    private static final double ESPESSURA_LINHA_CIRCULO_DESTACADO = 2;
    private static final double ESPESSURA_LINHA_CIRCULO_NORMAL = 1;
    private Circle circle;
    private Label text;    
    private Color mCorAtual;
    private StateModel mModel;
    
    public StateView() {
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

    @Override
    public void setSelecionado(boolean selecionado) {
        circle.setFill(selecionado ? COR_SELECIONADO : mCorAtual);        
        //TODO: Setar a fonte de text com o estilo bold
    }
        
    @Override
    public void setDestacado(boolean destacado) {
        circle.setStrokeWidth(destacado ? ESPESSURA_LINHA_CIRCULO_DESTACADO : ESPESSURA_LINHA_CIRCULO_NORMAL);
    }

    @Override
    public boolean isSelecionado() {
        return circle.getFill() == COR_SELECIONADO;
    }

    public void setModel(StateModel v) {
        mModel = v;
        if (v == null) {
            return;
        }
        text.setText(v.getValue(ComponentEditor.TAG_LABEL));
        setLayoutX(Double.parseDouble(v.getValue(ComponentEditor.TAG_POS_X)));
        setLayoutY(Double.parseDouble(v.getValue(ComponentEditor.TAG_POS_Y)));        
    }

    @Override
    public boolean pontoPertenceAoObjeto(double x, double y) {
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

    @Override
    public void getPropriedades() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StateModel getModel() {
        return mModel;
    }
    
}
