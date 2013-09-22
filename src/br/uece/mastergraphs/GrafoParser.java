/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs;

import br.uece.mastergraphs.model.Grafo2;
import br.uece.mastergraphs.model.Ligacao2;
import br.uece.mastergraphs.model.Vertice2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Emerson
 */
public class GrafoParser {
    
    public void load(Grafo2 grafo, InputStream input) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String linha = null;
        while ((linha = in.readLine()) != null) {            
            if (linha.charAt(0) == '[') {
                Vertice2 v = grafo.novoVertice();
                linha = linha.substring(1, linha.length() - 1);
                String[] opcoes = linha.split(" ");
                for (int i = 0; i < opcoes.length; i++) {
                    String[] opcao = opcoes[i].split("=");
                    v.putValor(opcao[0], opcao[1]);
                }
            } else {
                String aux = linha.substring(0, linha.indexOf('['));
                String[] vertices = aux.split(">");
                Vertice2 origem = grafo.getVertices().get(Integer.parseInt(vertices[0]));                
                Vertice2 destino = grafo.getVertices().get(Integer.parseInt(vertices[1]));
                
                Ligacao2 ligacao = origem.ligarA(destino);
                
                linha = linha.substring(linha.indexOf('[') + 1, linha.length() - 1);
                String[] opcoes = linha.split(" ");
                for (int i = 0; i < opcoes.length; i++) {
                    String[] opcao = opcoes[i].split("=");
                    ligacao.putValor(opcao[0], opcao[1]);
                }
            }
        }
    }
    
    public void save(Grafo2 grafo, OutputStream output) {
        PrintWriter out = new PrintWriter(output);
        for (Vertice2 vertice: grafo.getVertices()) {
            printOpcoes(vertice.getChaves(), vertice.getValores(), out);
        }
        for (Ligacao2 ligacao: grafo.getLigacoes()) {
            out.print(grafo.getVertices().indexOf(ligacao.getOrigem()) + ">" + grafo.getVertices().indexOf(ligacao.getDestino()));
            printOpcoes(ligacao.getChaves(), ligacao.getValores(), out);
        }
        out.close();
    }

    private void printOpcoes(String[] chaves, Object[] valores, PrintWriter out) {        
        out.print('[');
        for (int i = 0; i < chaves.length; i++) {
            if (!"view".equals(chaves[i])) {
                out.print(chaves[i] + "=" + valores[i] + " ");
            }
        }
        out.println(']');
    }
    
}
