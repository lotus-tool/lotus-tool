package br.uece.mastergraphs.algoritmos;

import br.uece.mastergraphs.Algoritmo;
import br.uece.mastergraphs.Grafo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class Coloracao implements Algoritmo {
	Queue filaDeRetorno;
	String[] corDeCadaVertice; // pode ser colocado string depois

	String[] vetorDeCoresProibidas;
	int posicaoVetorDeCoresProibidas = 0;

	int numeroDeCores = 0;
	String[] coresReais = { "#dd544d", "#769828", "#4992ff", "#c0c0c0", "#ffcc00",
			"#006400" , "#FFFF00"};
        String[] tabela = { "red", "blue", "pink", "black", "orange",
			"green", "yellow" };

    @Override
	public Queue rodar(Grafo grafo) {
		inicializar(grafo);
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			if (corDeCadaVertice[i].equals("-1")) {
				for (int j = 0; j < grafo.getNumeroDeVertices(); j++) {
					boolean bool = (!grafo.getMatrixDeLigacoes()[i][j]
							.equals("0") || !grafo.getMatrixDeLigacoes()[j][i]
							.equals("0"));
					if (bool) {

						if (corDeCadaVertice[j].equals("-1")) {
							continue;
						} else {
							vetorDeCoresProibidas[posicaoVetorDeCoresProibidas++] = corDeCadaVertice[j];
						}
					}
				}
			}
			atribuirCor(i);
			reinicializarVetorPosicaoProibida(grafo);
		}
		colocaNafilaDeRetorno(grafo);
		return filaDeRetorno;
	}

	private void inicializar(Grafo grafo) {
		corDeCadaVertice = new String[grafo.getNumeroDeVertices()];
		vetorDeCoresProibidas = new String[grafo.getNumeroDeVertices()];
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			corDeCadaVertice[i] = "-1";
			vetorDeCoresProibidas[i] = "-1";
		}
		filaDeRetorno = new LinkedList<String>();
	}

	private void reinicializarVetorPosicaoProibida(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			vetorDeCoresProibidas[i] = "-1";
		}
		posicaoVetorDeCoresProibidas = 0;
	}

	private void atribuirCor(int receptor) {
		String cor = "";
		int i;
		for (i = 0; i < vetorDeCoresProibidas.length; i++) {
			cor = coresReais[i];
			boolean achei = false;
			for (int j = 0; j < posicaoVetorDeCoresProibidas; j++) {
				if (cor.equals(vetorDeCoresProibidas[j])) {
					achei = true;
					break;
				}
			}
			if (!achei) {
				break;
			}
		}
		corDeCadaVertice[receptor] = coresReais[i];
	}

	private void colocaNafilaDeRetorno(Grafo grafo) {
		for (int i = 0; i < grafo.getNumeroDeVertices(); i++) {
			filaDeRetorno.add(grafo.getNomeDosVertices()[i] + ","
					+ corDeCadaVertice[i]);
                                            {
                    grafo.pintarVertice(i, Color.web(corDeCadaVertice[i]));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuscaEmLargura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		}
	}
}
