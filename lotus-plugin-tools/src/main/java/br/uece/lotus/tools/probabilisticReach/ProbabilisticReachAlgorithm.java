/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.probabilisticReach;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Ranniery
 */
public class ProbabilisticReachAlgorithm {
    
    public double probabilityBetween (Component a, int source, int target, int steps, int actionTargetID) {
        //adicionar mais um parametro k para saber qual estado de destino da ação, além do source.
        //no final dos steps, caso a posição (i,k) da matriz estiver nula, return 0. caso contrario return valor normal.

        //fazer com que a posição (0,0) da matriz seja sempre 1.

        int tam = a.getStatesCount();
        double[][] probabilities = new double[tam][tam];
        probabilities = zerar(probabilities, tam);
        List<Transition> transitions = transitionsList(a);
        int i;
        int j;
        for(Transition t : transitions){
            i = (t.getSource().getID());
            j = (t.getDestiny().getID());
            probabilities[i][j] = t.getProbability();
        }
        zerarDiagonal(probabilities, tam);
        probabilities[0][0] = 1;
        i = source;
        j = target;
        // montar a matriz de probabilidades já é um step.
        steps--;
//      multiplica as matrizes um monte de vez
        double[][] mult = probabilities;
        double difference = 1;
        double total = 0;
        int count = 0;
        if(steps == 0) total = probabilities[source][target];
        //Parar se a diferença for pequena o bastante e o count > 10.
        while((abs(difference) > 0.01 || count < 20) && (steps > 0)){
            total += probabilities[i][j];
            difference = probabilities[i][j];
            probabilities = multiply(probabilities, mult, tam);
            //mult = probabilities; <- ainda nao funciona corretamente
            difference = difference - probabilities[i][j];
            count++;
            steps--;
            if(abs(difference) > 0.0001){
                count = 0;
            }
        }
        if(total > 1){
           total = 1;
        }
        total = truncar(total, 5);
        double teste = probabilities[i][actionTargetID];
        teste = truncar(teste, 2);
        System.out.println(teste);
        if(probabilities[i][actionTargetID] == 0) return 0;
        return total;
    }
    
    public double[][] multiply(double[][] matrixA, double[][] matrixB, int tam){
        double sum = 0;
        double[][] multiply = new double[tam][tam];
        for (int i = 0 ; i < tam ; i++ )
         {
            for (int j = 0 ; j < tam ; j++ )
            {   
               for (int k = 0 ; k < tam ; k++ )
               {
                  sum += matrixA[i][k]*matrixB[k][j];
               }
 
               multiply[i][j] = sum;
               sum = 0;
            }
        }
        return multiply;
    }
    
    public List<Transition> transitionsList(Component a){
        Iterable<Transition> aux = a.getTransitions();
        List<Transition> aux2 = new ArrayList();
        for(Transition t : aux){
            aux2.add(t);
        }
        return aux2;
    }
    
    public int getStatesSize(Iterable<State> states){
        List<State> statesL = new ArrayList();
        for(State aux : states){
            statesL.add(aux);
        }
        int size = statesL.size();
        return size;
    }
    
    public double[][] zerar (double[][] probabilities, int tam){
        for(int i = 0; i < tam; i++){
            for(int j = 0; j < tam; j++){
                probabilities[i][j] = 0;
            }
        }
        return probabilities;
    }
    
    public double[][] zerarDiagonal (double[][] probabilities, int tam){
        for(int i = 0; i < tam; i++){
            probabilities[i][i] = 0;
        }
        return probabilities;
    }
    
    public static double truncar(double d, int casas_decimais){  
  
        int var1 = (int) d;   // Remove a parte decimal do número... 2.3777 fica 2  
        double var2 = var1*Math.pow(10,casas_decimais); // adiciona zeros..2.0 fica 200.0  
        double var3 = (d - var1)*Math.pow(10,casas_decimais); /** Primeiro retira a parte decimal fazendo 2.3777 - 2 ..fica 0.3777, depois multiplica por 10^(casas decimais) 
        por exemplo se o número de casas decimais que queres considerar for 2, então fica 0.3777*10^2 = 37.77 **/  
        int var4 = (int) var3; // Remove a parte decimal da var3, ficando 37  
        int var5 = (int) var2; // Só para não haver erros de precisão: 200.0 passa a 200  
        int resultado = var5+var4; // O resultado será 200+37 = 237  
        double resultado_final = resultado/Math.pow(10,casas_decimais); // Finalmente divide-se o resultado pelo número de casas decimais, 237/100 = 2.37  
        return resultado_final; // Retorna o resultado_final :P   
    } 
}
