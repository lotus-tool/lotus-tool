package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class Conexao implements Algoritmo {
	private Queue<String>[] vetorFilaDeRetorno;
	int posicaoDafila;
	private int numeroDeCompponentes;
	private int[] visitados;
	int verticeInicialParaConexao;
	String[] coresReais = { "#FF0000", "#0000CD", "#D02090", "#000000", "#00FFFF",
			"#006400" , "#FFFF00"};
        String[] tabela = { "red", "blue", "pink", "black", "aqua",
			"gree", "yellow" };
        
    @Override
	public Queue<String>[] rodar(Grafo grafo) {
		inicializarVariaveisInternas(grafo);
		String grafoConexo = GrafoConexo(grafo);
		reInicializaVisitados(grafo);
		if ("naoConexo".equals(grafoConexo)) {
			print("nao conexo");
			numeroDeCompponentes = contarNumeroDeComponentes(grafo);
			print(numeroDeCompponentes+"");
			inicializaFilaDeRetorno();
			inserirNafilaDeRetorno(grafo, numeroDeCompponentes);
		} else if ("fortementeConexo".equals(grafoConexo)) {
			print("forte");
			numeroDeCompponentes = 1;
			inicializaFilaDeRetorno();
			profundidadeAdapatadaParaConexo(grafo, 0, 0);
			/*
			 * esse 0,0 é que começa do vertice 0 e tem a posição zero na fila
			 * de retorno
			 */
		} else if ("fracamenteConexo".equals(grafoConexo)) {
			print("fraco"+ " "+verticeInicialParaConexao);
			numeroDeCompponentes = 1;
			inicializaFilaDeRetorno();
			profundidadeAdapatadaParaConexo(grafo, verticeInicialParaConexao, 0);
		}
		return vetorFilaDeRetorno;
	}

	private void inicializaFilaDeRetorno() {
		posicaoDafila = 0;
		vetorFilaDeRetorno = new Queue[numeroDeCompponentes];
		for (int i = 0; i < numeroDeCompponentes; i++)
			vetorFilaDeRetorno[i] = new LinkedList<String>();

	}

	private void inicializarVariaveisInternas(Grafo grafo) {
		visitados = new int[grafo.getNumeroDeVertices()];
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			visitados[i] = 0;
			numeroDeCompponentes = 0;
		}
	}

	private String GrafoConexo(Grafo grafo) {
		if (fortementeConexo(grafo)) {
			return "fortementeConexo";
		}
		if (fracamenteConexo(grafo)) {
			return "fracamenteConexo";
		} else {
			return "naoConexo";
		}

	}

	private boolean fracamenteConexo(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
                    reInicializaVisitados(grafo);
			if (visitados[i] == 0) {
				int contadorDeVerticesConectados = 0;
				contadorDeVerticesConectados = profundidadeAdaptadaParaContar(
						grafo, i, contadorDeVerticesConectados);
				if (contadorDeVerticesConectados == grafo.getNumeroDeVertices()) {
					verticeInicialParaConexao=i;
                                        print(i+" ");
					return true;
				}

			}
		}
		return false;
	}

	private void reInicializaVisitados(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			visitados[i] = 0;
		}
	}

	private boolean fortementeConexo(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			reInicializaVisitados(grafo);
			if (visitados[i] == 0) {
				int contadorDeVerticesConectados = 0;
				contadorDeVerticesConectados = profundidadeAdaptadaParaContar(
						grafo, i, contadorDeVerticesConectados);
				if (contadorDeVerticesConectados < grafo.getNumeroDeVertices()) {
					return false;
				}
			}
		}
		return true;
	}

	private int profundidadeAdaptadaParaContar(Grafo grafo, int vertice,
			int contadorDeVerticesConectados) {
		visitados[vertice] = 1;
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][i].equals("0")
					&& visitados[i] != 1) {
				contadorDeVerticesConectados = profundidadeAdaptadaParaContar(
						grafo, i, contadorDeVerticesConectados);
                                break;
			}

		}
		return contadorDeVerticesConectados + 1;
	}

	private void profundidadeAdapatadaParaConexo(Grafo grafo, int vertice,
			int componete) {
		vetorFilaDeRetorno[componete].add(grafo.getNomeDosVertices()[vertice]);
                                {
                    grafo.pintarVertice(vertice, Color.web(coresReais[posicaoDafila]));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		visitados[vertice] = 1;
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][i].equals("0")
					&& visitados[i] != 1) {
				profundidadeAdapatadaParaConexo(grafo, i, componete);
			}

		}
	}

	private int contarNumeroDeComponentes(Grafo grafo) {
		int numeroDeComponentes = 0;
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (visitados[i] != 1) {
				visitados[i] = 1;
				numeroDeComponentes++;
				profundidadeAdaptadaParaContarComponetes(grafo, i);
			}

		}
		return numeroDeComponentes;
	}

	private void profundidadeAdaptadaParaContarComponetes(Grafo grafo,
			int vertice) {
		visitados[vertice] = 1;
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][i].equals("0")
					&& visitados[i] != 1) {
				profundidadeAdaptadaParaContarComponetes(grafo, i);
			}

		}
	}

	private void inserirNafilaDeRetorno(Grafo grafo, int numeroDeCompponentes) {
		reInicializaVisitados(grafo);
		for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {
			if (visitados[j] != 1) {
				profundidadeAdaptadaParaColocarNaFila(grafo, j);
				posicaoDafila++;
			}
		}
	}

	private void profundidadeAdaptadaParaColocarNaFila(Grafo grafo, int vertice) {
		visitados[vertice] = 1;
		vetorFilaDeRetorno[posicaoDafila]
				.add(grafo.getNomeDosVertices()[vertice]);
                {
                    grafo.pintarVertice(vertice, Color.web(coresReais[posicaoDafila]));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		for (int k = 0; k < grafo.getNumeroDeVertices(); k++) {
			if (!grafo.getMatrixDeLigacoes()[vertice][k].equals("0")
					&& visitados[k] != 1) {
				profundidadeAdaptadaParaColocarNaFila(grafo, k);
			}
		}
	}
            private void print(String string) {
            System.out.println(string);
        }

}