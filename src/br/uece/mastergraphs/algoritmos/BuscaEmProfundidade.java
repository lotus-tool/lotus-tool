package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class BuscaEmProfundidade implements Algoritmo {
	Queue<String> filaDeRetorno = new LinkedList<String>();
	int[] visitados;
	
    @Override
	public Queue rodar(Grafo grafo) {
		visitados=new int[grafo.getNumeroDeVertices()];
		
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (visitados[i]!=1) {
				profundidade(grafo, i);
			}
		
		}
		return filaDeRetorno;
	}

	private void profundidade(Grafo grafo, int vertice) {
            visitados[vertice]=1;	
            filaDeRetorno.add(grafo.getNomeDosVertices()[vertice]);
                                {
                grafo.pintarVertice(vertice, Color.BLUE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
		
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][i].equals("0") && visitados[i]!=1) {
				profundidade(grafo, i);
			}

		}
	}
}
