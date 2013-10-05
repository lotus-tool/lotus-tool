/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs;

import br.uece.mastergraphs.model.Vertice2;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

/**
 *
 * @author Emerson
 */
public class VerticeView extends Region implements Vertice2.Listener {
    
    private String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "W", "U", "V", "W", "X", "Y", "Z"};
    private static int contadorVertice;
    
    public static final int RAIO_VERTICE = 20;
    public static final int ESPESSURA_LINHA_VERTICE = 2;
    Circle circle;
    Text text;
    private Vertice2 vertice;
    private boolean primeiraVezY = true;
    private boolean primeiraVezX = true;

    public VerticeView(Vertice2 vertice) {        
        //Info info = new Info();
        //info.nome.setValue(letras[contadorVertice++]);
        //info.cor.setValue(Color.WHITE);
        //v.valorProperty().setValue(info);
        this.vertice = vertice;
        
        circle = new Circle(RAIO_VERTICE);
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(ESPESSURA_LINHA_VERTICE);
       // circle.fillProperty().bind(info.cor);
        
        getChildren().add(circle);
        text = new Text();
        getChildren().add(text);
        vertice.addListener(this);        
    }

    public void setNome(String nome) {
        text.setText(nome);
    }
    
    public void setCor(Color cor) {
        circle.setFill(cor);
    }

    @Override
    public void aoMudar(Vertice2 obj) {
        Object nome = (Object) obj.getValor("nome");
        nome = nome != null ? nome : "";
        text.setText(nome.toString());
        
        Paint cor = (Paint) obj.getValor("cor");
        cor = cor != null ? cor : Color.WHITE;
        circle.setFill(cor);
        
        String x = (String) vertice.getValor("view.x");
        if (primeiraVezX && x != null) {
            setLayoutX(Double.parseDouble(x));
            primeiraVezX = false;
        }
        String y = (String) vertice.getValor("view.y");
        if (primeiraVezY && y != null) {
            setLayoutY(Double.parseDouble(y));
            primeiraVezY = false;
        }        
    }

    public Vertice2 getVertice() {
        return vertice;
    }
    
}
