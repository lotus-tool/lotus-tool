package br.uece.mastergraphs;

import javafx.scene.paint.Paint;

public interface Grafo {

    int getNumeroDeVertices();
    String[] getNomeDosVertices();
    String[][] getMatrixDeLigacoes();
    void pintarVertice(int indice, Paint fill);
    void pintarLigacao(int indiceVerticeOrigem, int indiceVerticeDestino, Paint fill);
    
}
