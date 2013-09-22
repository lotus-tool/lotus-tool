package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class OrdenacaoTopologica implements Algoritmo {
	Queue<String> filaRetorno = new LinkedList<String>();
	int[] visitados;

	String[][] matrizDeTrabalho;
	int[] numeroDeLigacoesRecebidas;

	public Queue<String> rodar(Grafo grafo) {
		inicializaMatrizDeTrabalho(grafo);
		numeroDeLigacoesRecebidas = new int[grafo.getNumeroDeVertices()];
		visitados = new int[grafo.getNumeroDeVertices()];
		
		while (existeLigacoes(grafo) || todosVerticesNaoForaoVisitados()) {
			contarLigacoes(grafo);
			colocaNaFilaDeRetorno(grafo);
		}
			colocaNaFilaDeRetorno(grafo);

		return filaRetorno;
	}

	private boolean todosVerticesNaoForaoVisitados() {
		for (int i = 0; i < visitados.length; i++) {
			if (visitados[i]!=1) {
				return true;
			}
		}
		return false;
	}

	private boolean existeLigacoes(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {
				if (!matrizDeTrabalho[i][j].equals("0")) {
					return true;
				}
			}
		}
		return false;
	}

	private void colocaNaFilaDeRetorno(Grafo grafo) {
		for (int i = 0; i < numeroDeLigacoesRecebidas.length; i++) {
			if (numeroDeLigacoesRecebidas[i] == 0 && visitados[i] != 1) {
				visitados[i] = 1;
				filaRetorno.add(grafo.getNomeDosVertices()[i]);
				zeraLigacoes(grafo, i);
                                
                    {
                    grafo.pintarVertice(i, Color.BLUE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
			}
		}
	}

	private void zeraLigacoes(Grafo grafo, int vertice) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (!matrizDeTrabalho[vertice][i].equals("0")) {
				matrizDeTrabalho[vertice][i] = "0";
			}
		}
	}

	private void inicializaMatrizDeTrabalho(Grafo grafo) {
		matrizDeTrabalho = new String[grafo.getNumeroDeVertices()][grafo
				.getNumeroDeVertices()];
		for (int i = 0; i < matrizDeTrabalho.length; i++) {
			for (int j = 0; j < matrizDeTrabalho.length; j++) {
				matrizDeTrabalho[i][j] = grafo.getMatrixDeLigacoes()[i][j];
			}
		}
	}

	private void contarLigacoes(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			numeroDeLigacoesRecebidas[i] = 0;
		}
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {
				if (!matrizDeTrabalho[i][j].equals("0"))
					numeroDeLigacoesRecebidas[j]++;
			}
		}
	}
}
