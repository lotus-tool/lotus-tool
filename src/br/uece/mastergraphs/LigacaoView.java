/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs;

import br.uece.mastergraphs.model.Ligacao;
import br.uece.mastergraphs.model.Ligacao2;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;



/**
 *
 * @author Emerson
 */
public class LigacaoView extends Region implements Ligacao2.Listener {

    public static final int RAIO_CABECA_DE_FLECHA = 4;
    private Text text;
    private Line linha;
    static int contador = 1;
    private Ligacao2 ligacao;
    
    public LigacaoView(Ligacao2 l, final Region origem, final Region destino) {
        ligacao = l;
        
        linha = new Line();
        linha.startXProperty().bind(origem.layoutXProperty());
        linha.startYProperty().bind(origem.layoutYProperty());        
        linha.endXProperty().bind(destino.layoutXProperty());
        linha.endYProperty().bind(destino.layoutYProperty());
        
        //Info info = new Info();
        //info.cor.setValue(Color.BLACK);
        //l.getValor().setValue(info);
        linha.setStrokeWidth(2);
        linha.setStroke(Color.BLACK);

        getChildren().add(linha);

        Circle arrow = new Circle(RAIO_CABECA_DE_FLECHA, Color.BLACK);
        arrow.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 20 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
        arrow.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 20 + RAIO_CABECA_DE_FLECHA, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
        getChildren().add(arrow);

        text = new Text();
        text.layoutXProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 20 + RAIO_CABECA_DE_FLECHA + 20, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_X));
        text.layoutYProperty().bind(new DistanciaLinha(origem.layoutXProperty(), origem.layoutYProperty(), destino.layoutXProperty(), destino.layoutYProperty(), 20 + RAIO_CABECA_DE_FLECHA + 20, DistanciaLinha.FIM, DistanciaLinha.COMPONENTE_Y));
        getChildren().add(text);

        //info.nome.set("" + ((int) (Math.random() * 100)));
        //text.textProperty().bind(info.nome);

        linha.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                setCursor(Cursor.HAND);
            }
        });
        l.addListener(this);
    }

    @Override
    public void aoMudar(Ligacao2 l) {
        Object nome = l.getValor("peso");
        nome = nome != null ? nome : "";
        text.setText(nome.toString());
        
        Paint cor = (Paint) l.getValor("cor");
        cor = cor != null ? cor : Color.BLACK;
        linha.setStroke(cor);
    }

    public Ligacao2 getLigacao() {
        return ligacao;
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
        System.out.println("computeValue()");
        double deltaX = Math.abs(xi.getValue() - xf.getValue());
        double deltaY = Math.abs(yi.getValue() - yf.getValue());
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double c = distanciaDesejada / distance * (componente == COMPONENTE_X ? deltaX : deltaY);
        if (alinhamento == INICIO) {
            if (componente == COMPONENTE_X) {
                if (xi.getValue() < xf.getValue()) {
                    c = xi.getValue() + c;
                } else {
                    c = xi.getValue() - c;
                }
            } else {
                if (yi.getValue() < yf.getValue()) {
                    c = yi.getValue() + c;
                } else {
                    c = yi.getValue() - c;
                }                
            }            
        } else {
            if (componente == COMPONENTE_X) {
                if (xf.getValue() < xi.getValue()) {
                    c = xf.getValue() + c;
                } else {
                    c = xf.getValue() - c;
                }
            } else {
                if (yf.getValue() > yi.getValue()) {
                    c = yf.getValue() - c;
                } else {
                    c = yf.getValue() + c;                           
                }
            }
        }        
        return c;
    }
    
}