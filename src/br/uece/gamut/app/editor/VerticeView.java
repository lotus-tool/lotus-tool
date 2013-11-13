/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

/**
 *
 * @author emerson
 */
public class VerticeView extends Region implements Vertice {
   
    private Circle circle;    
    public static final int RAIO_CIRCULO = 15;
    public static final int ESPESSURA_PERIMETRO_CIRCULO = 2;
   
    private List<Transicao> ligacoes = new ArrayList<>();
    private Text text;

      
    public VerticeView(int contador){
        setId("Vertice " + contador);
        circle = new Circle(RAIO_CIRCULO);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.BLACK);
        if(contador == 0){
            circle.setFill(Color.RED);
        } else{
            circle.setFill(Color.CYAN);
        }                
        getChildren().add(circle);      
       
        text = new Text();
        System.out.println(circle.getLayoutX());
        
        
        text.setLayoutX(text.getLayoutBounds().getMaxX());
        text.setLayoutY(text.getLayoutBounds().getMaxY());   
        getChildren().add(text);
        
    }

    

    
     public Circle getCircle() {
        return circle;
    }
   
    @Override
    public List<Transicao> getLigacoes() {
        return this.ligacoes;
    }

    void setPosicao(double sceneX, double sceneY) {
        setLayoutX(sceneX);
        setLayoutY(sceneY);
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setRotulo(String string) {
        text.setText(string);
        
    }
    
    public String getRotulo(){
        return text.getText();
    }
    
    public Paint getCor(){
       return circle.getFill();
    }
    
    public void setCor(Paint cor){
        circle.setFill(cor);
    }
}
