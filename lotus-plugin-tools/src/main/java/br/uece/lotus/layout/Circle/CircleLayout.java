/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.layout.Circle;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import static java.sql.DriverManager.println;
import java.time.Clock;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author vieir_000
 */
public class CircleLayout {
    
     static final double xOrigem=100;
     static final double yOrigem=100;
     static final double xVetor=100;
     static final double yVetor=0;
     
      //Precisão de 3 casas decimais
    public static double round(double v) {
        return ((int) Math.round(v * 1000)) / 1000.0;
    }
      
    public double[] Matriz_de_Rotacao(double x_entrada,double y_entrada,double angInd){
        double x_saida=(round(Math.cos(Math.toRadians(angInd)))*x_entrada)+(round(Math.sin(Math.toRadians(angInd))*y_entrada));
        System.out.println("Xvetor valor");
        System.out.println((round(Math.cos(Math.toRadians(angInd)))*x_entrada)+(round(Math.sin(Math.toRadians(angInd))*y_entrada)));
        double y_saida=(round(Math.cos(Math.toRadians(angInd)))*y_entrada)-(round(Math.sin(Math.toRadians(angInd)))*x_entrada);
         System.out.println("Yvetor valor");
        System.out.println((round(Math.cos(Math.toRadians(angInd)))*y_entrada)-(round(Math.sin(Math.toRadians(angInd)))*x_entrada));
        double [] vetorRotacionado = new double[2];
        System.out.println(x_saida);
         System.out.println(y_saida);
        vetorRotacionado[0]=x_saida;
        vetorRotacionado[1]=y_saida;
        return vetorRotacionado;
    }
    
     void aplicar(Component c) {
        int quantState=0;
        for (State s: c.getStates()){
            quantState+=1;
            
        }
        System.out.println(quantState);
        if(quantState != 0) { double angInd= 360/quantState;}
        //setando os pontos cartesianos
        ArrayList<State>estados=(ArrayList<State>) c.getStates();
        //setando o primeiro estado para servir de referencia para os outros;
        estados.get(0).setLayoutX(200);//setando os pontos final do raio
        estados.get(0).setLayoutY(100);//
         for(int i=0;i<estados.size()-1;i++){ 
             if(quantState==1){
             break;}
             
            double[] vetor=this.Matriz_de_Rotacao(estados.get(i).getLayoutX()-100, estados.get(i).getLayoutY()-100, angInd);
            estados.get(i+1).setLayoutX(vetor[0]+xOrigem);
            estados.get(i+1).setLayoutY(vetor[1]+yOrigem);
            System.out.println("Valor de x:"+estados.get(i+1).getLayoutX()+"Valor de y:"+estados.get(i+1).getLayoutX());
           }   
 
    }
}