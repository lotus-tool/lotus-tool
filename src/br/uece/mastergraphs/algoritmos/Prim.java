package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class Prim implements Algoritmo {
	Queue<String> filaDeRetorno = new LinkedList<String>();
	Comparator<String> comparator;
	PriorityQueue<String> filaDeLigacoes;
	int totalCustoDoPercurso = 0;
	int[] vetorControleDeCiclo;
	int[] visitados;
	final int numeroInicial = 0;

	public Queue rodar(Grafo grafo) {
		inicializar(grafo);
		adicionaNaFilaDePrioridadeDeLigacoes(grafo, numeroInicial);
		visitados[numeroInicial] = 1;
		inicializaVetorControleDeCiclo(grafo);
		while (!filaDeLigacoes.isEmpty()) {
			String ligacao = filaDeLigacoes.poll();
			int vertice = verticeNaoVisitado(ligacao);
			if (visitados[vertice] != 1) {
				visitados[vertice] = 1;
				adicionaNaFilaDePrioridadeDeLigacoes(grafo, vertice);
				if (!gerarCiclo(ligacao)) {
					filaDeRetorno.add(ligacao);
                                                        { 
                                                            int origem = Integer.parseInt(ligacao.split(",")[1]);
                                                            int destino = Integer.parseInt(ligacao.split(",")[2]);
                                                            grafo.pintarLigacao(origem, destino, Color.GREEN);
                                                            try {
                                                             Thread.sleep(3000);
                                                            } catch (InterruptedException ex) {
                                                                Logger.getLogger(Prim.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                        }  
					atualizaVetorControleDeCiclo(ligacao);
					totalCustoDoPercurso += valorDaligacao(ligacao);
				}
			}

		}
		filaDeRetorno.add("total:" + totalCustoDoPercurso);

		return filaDeRetorno;
	}

	private int verticeNaoVisitado(String ligacao) {
		int vertice1 = getVertice1(ligacao);
		int vertice2 = getVertice2(ligacao);
		if (visitados[vertice1] == 1)
			return vertice2;
		else
			return vertice1;
	}

	private void transfereFilaDePrioridadeParaNormal() {
		while (!filaDeLigacoes.isEmpty()) {
			filaDeLigacoes.add(filaDeLigacoes.poll());
		}

	}

	void inicializar(Grafo grafo) {
		comparator = new ComparaNaFilaDePrioridade();
		filaDeLigacoes = new PriorityQueue<>(grafo.getNumeroDeVertices(),
				comparator);
		visitados = new int[grafo.getNumeroDeVertices()];

	}

	private void adicionaNaFilaDePrioridadeDeLigacoes(Grafo grafo, int vertice) {
		for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][j].equals("0")) {
				filaDeLigacoes.add(grafo.getMatrixDeLigacoes()[vertice][j]
						+ "," + vertice + "," + j);
			}
//			if (!grafo.getMatrixDeLigacoes()[j][vertice].equals("0")) {
//				filaDeLigacoes.add(grafo.getMatrixDeLigacoes()[j][vertice]
//						+ "," + j + "," + vertice);
//			}

		}
	}

	private class ComparaNaFilaDePrioridade implements Comparator<String> {

		@Override
		public int compare(String string1, String string2) {
			int n1 = valorDaligacao(string1);
			int n2 = valorDaligacao(string2);
			if (n1 < n2)
				return -1;
			else
				return 1;
		}

	}

	private int valorDaligacao(String string) {
		return Integer.parseInt(string.split(",")[0]);
	}

	private void inicializaVetorControleDeCiclo(Grafo grafo) {
		vetorControleDeCiclo = new int[grafo.getNumeroDeVertices()];
		for (int i = 0; i < vetorControleDeCiclo.length; i++) {
			vetorControleDeCiclo[i] = i;
		}
	}

	private boolean gerarCiclo(String ligacao) {
		int v1 = getVertice1(ligacao);
		int v2 = getVertice2(ligacao);

		if (vetorControleDeCiclo[v1] == vetorControleDeCiclo[v2])
			return true;
		else
			return false;
	}

	private int getVertice1(String ligacao) {
		return Integer.parseInt(ligacao.split(",")[1]);
	}

	private int getVertice2(String ligacao) {
		return Integer.parseInt(ligacao.split(",")[2]);
	}

	private void atualizaVetorControleDeCiclo(String ligacao) {
		int v1 = getVertice1(ligacao);
		int v2 = getVertice2(ligacao);

		if (vetorControleDeCiclo[v1] < vetorControleDeCiclo[v2]) {
			int valorVelho = vetorControleDeCiclo[v2];
			int valorNovo = vetorControleDeCiclo[v1];
			vetorControleDeCiclo[v2] = vetorControleDeCiclo[v1];
			preencheVetor(valorVelho, valorNovo);

		} else {
			int valorVelho = vetorControleDeCiclo[v1];
			int valorNovo = vetorControleDeCiclo[v2];
			vetorControleDeCiclo[v1] = vetorControleDeCiclo[v2];
			preencheVetor(valorVelho, valorNovo);
		}
	}

	private void preencheVetor(int valorVelho, int ValorNovo) {
		for (int i = 0; i < vetorControleDeCiclo.length; i++)
			if (valorVelho == vetorControleDeCiclo[i]) {
				vetorControleDeCiclo[i] = ValorNovo;
			}
	}
}
