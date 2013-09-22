package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;

public class MinimoCaminho implements Algoritmo {
	Queue<String> filaDeRetorno;
	int[] visitados;
	int[] distancia;
	String[] predecessor;
	final int valorMaximoDistancia = 99999;
	int verticeInicial;
	int verticeFinal;

    @Override
	public Queue<String> rodar(Grafo grafo) {
		inicializar(grafo);
                String verticeInicial=JOptionPane.showInputDialog("Digite o nome do Vertice Inicial");
                String verticeFinal=JOptionPane.showInputDialog("Digite o nome do Vertice Final");
		this.verticeInicial=pegaVerticePelaString(grafo, verticeInicial);
                this.verticeFinal=pegaVerticePelaString(grafo, verticeFinal);
		//this.verticeInicial=0;
                distancia[this.verticeInicial] = 0;
		visitados[this.verticeInicial] = 1;

		while (percorreuCaminhoNescessario(grafo)) {
                        int verticeSelecionado = selecionarVertice(grafo);
                        System.out.println("loop");
			for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {

				boolean existeLigacao = !grafo.getMatrixDeLigacoes()[verticeSelecionado][i]
								.equals("0");

				if (existeLigacao) {
					if (distancia[i] > (distancia[verticeSelecionado] + peso(
							verticeSelecionado, i, grafo))) {
						distancia[i] = distancia[verticeSelecionado]
								+ peso(verticeSelecionado, i, grafo);
						predecessor[i] = grafo.getNomeDosVertices()[verticeSelecionado];
					}
				}
			}
		}
		colocaNaFilaDeRetorno(grafo);
		return filaDeRetorno;
	}

	private void pegaValorVerticeInicialEFinal(Grafo grafo,
			String verticeInicial, String verticeFinal) {

		this.verticeInicial = pegaVerticePelaString(grafo, verticeInicial);
		this.verticeFinal = pegaVerticePelaString(grafo, verticeFinal);

	}

	private void inicializar(Grafo grafo) {
		filaDeRetorno = new LinkedList<String>();

		visitados = new int[grafo.getNumeroDeVertices()];
		distancia = new int[grafo.getNumeroDeVertices()];
		predecessor = new String[grafo.getNumeroDeVertices()];
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			distancia[i] = valorMaximoDistancia;
			predecessor[i] = "-1"; // -1 �w o valor defalt para string
			visitados[i] = 0;
		}
	}

	private boolean percorreuCaminhoNescessario(Grafo grafo) {
          /*  if(distancia[verticeFinal]!=valorMaximoDistancia && distancia[verticeInicial]!=valorMaximoDistancia) {
                if(!predecessor[verticeFinal].equals("-1") && !predecessor[verticeInicial].equals("-1"))
                    if(visitados[verticeFinal]==1)
            */
            for(int i =0;i<grafo.getNumeroDeVertices();i++) {
            if(visitados[i]!=1) {
                System.out.println(distancia[verticeFinal]+" "+distancia[verticeInicial]);
                return true;
            }
            
            }
                return false;
	}

	private int selecionarVertice(Grafo grafo) {
		int valorMinimo = valorMaximoDistancia; //valor do peso
		int minimo =this.verticeInicial; // valor do vertice
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {

			if (visitados[i] != 1 && valorMinimo > distancia[i]) {
				valorMinimo = distancia[i];
				minimo = i;
			}

		}
		visitados[minimo] = 1;
		return minimo;
	}

	private int peso(int i, int j, Grafo grafo) {
		return Integer.parseInt(grafo.getMatrixDeLigacoes()[i][j]);
	}

	private void colocaNaFilaDeRetorno(Grafo grafo) {
                for(int i=0; i<grafo.getNumeroDeVertices();i++) {
                    System.out.println(distancia[i]+" "+predecessor[i]);
                }
		Stack<String> auxiliarDeInversao = new Stack<String>();
		int i = verticeFinal;
		auxiliarDeInversao.add(grafo.getNomeDosVertices()[i]);
		while (i != verticeInicial) {
			i = pegaVerticePelaString(grafo, predecessor[i]);
			auxiliarDeInversao.add(grafo.getNomeDosVertices()[i]);
                        if(i==0 && predecessor[i].equals("0")) {
                            JOptionPane.showMessageDialog(null, "Caminho não encontrado.");
                        }
		}
                Queue<String> filaDePintura=new LinkedList<String>();

		while (!auxiliarDeInversao.isEmpty()) {
                    String s=auxiliarDeInversao.pop();	
                    filaDeRetorno.add(s);
                    filaDePintura.add(s);
		}
                
                while(!filaDePintura.isEmpty()) {
                    String s=filaDePintura.remove();
                    int p=pegaVerticePelaString(grafo, s);
                    {
                    grafo.pintarVertice(p, Color.BLUE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }

		int custoTotal = distancia[verticeFinal];
		filaDeRetorno.add("Custo m�nimo total: " + custoTotal);
	}

	private int pegaVerticePelaString(Grafo grafo, String vertice) {
		for (int i = 0; i < grafo.getNomeDosVertices().length; i++) {
			if (grafo.getNomeDosVertices()[i].equals(vertice)) {
				return i;
			}
		}
		// nunca vem para c�
		return 99999;
	}
}
