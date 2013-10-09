/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Ligacao;
import br.uece.gamut.Vertice;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

/**
 *
 * @author emerson
 */
public class VerticeView extends Region implements Vertice {
   
    private Circle circle;
    public static final int RAIO_CIRCULO = 20;
    public static final int ESPESSURA_PERIMETRO_CIRCULO = 2;
   
    private List<Ligacao> ligacoes = new ArrayList<>();
    private Text text;
    

    public VerticeView(int contador){
        circle = new Circle(RAIO_CIRCULO);
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(Color.BLACK);
        if(contador == 0){
            circle.setFill(Color.RED);
        } else{
            circle.setFill(Color.CYAN);
        }                
        getChildren().add(circle);      
       
        text = new Text();        
        getChildren().add(text);
        
    }
    
    @Override
    public List<Ligacao> getLigacoes() {
        return this.ligacoes;
    }

    void setPosicao(double sceneX, double sceneY) {
        setLayoutX(sceneX);
        setLayoutY(sceneY);
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setRotulo(String string) {
        text.setText(string);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
