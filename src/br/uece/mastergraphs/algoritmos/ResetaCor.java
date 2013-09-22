package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class ResetaCor implements Algoritmo {

    Queue<String> filaDeRetorno = new LinkedList<String>();
    int[] visitados;
    int numeroDeVertices;
    String[] nomeDosVertices;
    private String[][] matrixLigacao;

    
    @Override
    public Queue rodar(Grafo grafo) {
        this.numeroDeVertices = grafo.getNumeroDeVertices();
        this.nomeDosVertices = grafo.getNomeDosVertices();
        this.matrixLigacao = grafo.getMatrixDeLigacoes();

        visitados = new int[numeroDeVertices];
        for (int i = 0; i < numeroDeVertices; i++) {
            if (visitados[i] != 1) {
                filaDeRetorno.add(nomeDosVertices[i]);                
                {
                grafo.pintarVertice(i, Color.WHITE);
                }
                visitados[i] = 1;
                adjacente(grafo, i);
            }
        }
        return filaDeRetorno;
    }

    private void adjacente(Grafo grafo, int vertice) {

        Queue<Integer> auxiliar = new LinkedList<Integer>();
        for (int i = 0; i < numeroDeVertices; i++) {
            if (!matrixLigacao[vertice][i].equals("0")
                    && visitados[i] != 1) {
                filaDeRetorno.add(nomeDosVertices[i]);
                
                grafo.pintarVertice(i, Color.WHITE);
                
                for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {    
                    if(!grafo.getMatrixDeLigacoes()[i][j].equals("0"))
                        grafo.pintarLigacao(i, j, Color.BLACK);
                }
                
                auxiliar.add(i);
                visitados[i] = 1;
            }
        }
        while (!auxiliar.isEmpty()) {
            int n = auxiliar.poll();
            adjacente(grafo, n);
        }

    }
}