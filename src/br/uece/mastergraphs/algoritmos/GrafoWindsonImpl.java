package br.uece.mastergraphs.algoritmos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GrafoWindsonImpl {
	private int numeroDeVertices;
	private String[] nomeDosVertices;
	private String[][] matrixDeLigacoes;

	public GrafoWindsonImpl() {
		lerDeUmArquivo();
	}

	private void lerDeUmArquivo() {
		File arquivo = verificaCaminhoDoArquivo();
		try {
			// Indicamos o arquivo que ser� lido
			FileReader fileReader = new FileReader(arquivo);

			// Criamos o objeto bufferReader que nos
			// oferece o m�todo de leitura readLine()
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// String que ir� receber cada linha do arquivo

			String linha = "";
			linha = bufferedReader.readLine();
			int auxiliar = Integer.parseInt(linha);
			numeroDeVertices = auxiliar;
			// inicializa��o
			matrixDeLigacoes = new String[numeroDeVertices][numeroDeVertices];
			nomeDosVertices = new String[numeroDeVertices];

			// inicializa a matriz e vetor

			linha = bufferedReader.readLine();
			String[] linhaQuebradaEmPalavras = linha.split(",");

			for (int i = 0; i < numeroDeVertices; i++) {
				nomeDosVertices[i] = linhaQuebradaEmPalavras[i];
			}

			int i = 0;
			while ((linha = bufferedReader.readLine()) != null) {
				linhaQuebradaEmPalavras = linha.split(",");
				for (int j = 0; j < numeroDeVertices; j++) {
					matrixDeLigacoes[i][j] = linhaQuebradaEmPalavras[j];
				}
				i++;
			}

			// liberamos o fluxo dos objetos ou fechamos o arquivo
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File verificaCaminhoDoArquivo() {
		File arq = new File(
				"D://Novidades/Estudo/Uece 2012.2/Grafos/Trab/MatrizGrafo.txt");
		try {
			boolean statusArq = arq.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arq;
	}

	public void mostrarGrafoMatrix() {
		System.out.print("Nomes: ");
		for (int i = 0; i < numeroDeVertices; i++)
			System.out.print(nomeDosVertices[i] + ",");
		System.out.println("\nMatrizDeLiga��es: ");
		for (int i = 0; i < numeroDeVertices; i++) {
			for (int j = 0; j < numeroDeVertices; j++) {
				System.out.print(matrixDeLigacoes[i][j] + ",");
			}
			System.out.println();

		}
	}

	public int getNumeroDeVertices() {
		return numeroDeVertices;
	}

	public String[] getNomeDosVertices() {
		return nomeDosVertices;
	}

	public String[][] getMatrixDeLigacoes() {
		return matrixDeLigacoes;
	}

}
