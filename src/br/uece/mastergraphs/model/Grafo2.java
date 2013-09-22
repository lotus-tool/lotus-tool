/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs.model;

import br.uece.mastergraphs.Grafo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Paint;

/**
 *
 * @author Emerson
 */
public class Grafo2 implements Grafo {
    
    private List<Ligacao2> ligacoes = new ArrayList<>();
    private List<Vertice2> vertices = new ArrayList<>();    
    private List<Listener> listeners = new ArrayList<>();
    private Vertice2.Listener verticeListener = new Vertice2.Listener() {        
        @Override
        public void aoMudar(Vertice2 obj) {
            for (Listener l : listeners) {
                l.aoMudar(obj);
            }
        }
    };
    private Ligacao2.Listener ligacaoListener = new Ligacao2.Listener() {
        @Override
        public void aoMudar(Ligacao2 ligacao) {
            for (Listener l : listeners) {
                l.aoMudar(ligacao);
            }
        }
    };

    @Override
    public int getNumeroDeVertices() {
        return vertices.size();
    }

    @Override
    public String[] getNomeDosVertices() {
         List<String> aux = new ArrayList<>();
        for (Vertice2 v: vertices) {
            aux.add(v.getValor("nome").toString());
        }
        return aux.toArray(new String[0]);
    }

    @Override
    public String[][] getMatrixDeLigacoes() {
        String[][] aux = new String[vertices.size()][vertices.size()];
        for (Vertice2 v: vertices) {
            int indiceVertice = vertices.indexOf(v);
            for (Vertice2 outro: vertices) {
                String peso = "0";
                for (Ligacao2 l: v.destinos) {
                    if (l.destino.equals(outro)) {
                        peso = l.getValor("peso").toString();
                        break;
                    }
                }
                int indiceOutroVertice = vertices.indexOf(outro);
                aux[indiceVertice][indiceOutroVertice] = peso;
            }
        }
        return aux;
    }

    @Override
    public void pintarVertice(int indice, Paint fill) {
        Vertice2 vertice = vertices.get(indice);        
        vertice.putValor("cor", fill);
    }

    @Override
    public void pintarLigacao(int indiceVerticeOrigem, int indiceVerticeDestino, Paint fill) {
        Vertice2 origem = vertices.get(indiceVerticeOrigem);
        Vertice2 destino = vertices.get(indiceVerticeDestino);
        for (Ligacao2 l: origem.destinos) {
            if (l.getDestino().equals(destino)) {
                l.putValor("cor", fill);
            }
        }
    }
    
    public interface Listener {

        void aoAdicionar(Vertice2 vertice);

        void aoAdicionar(Ligacao2 ligacao);

        void aoMudar(Vertice2 vertice);

        void aoMudar(Ligacao2 vertice);

        void aoRemover(Vertice2 v);

        void aoRemover(Ligacao2 l);
    }
    
    void adicionarLigacoes(Ligacao2 novo) {
        ligacoes.add(novo);
        novo.addListener(ligacaoListener);
        for (Listener l : listeners) {
            l.aoAdicionar(novo);
        }
    }
    
    public Vertice2 novoVertice() {
        Vertice2 novo = new Vertice2(this);
        vertices.add(novo);        
        novo.addListener(verticeListener);
        for (Listener l : listeners) {
            l.aoAdicionar(novo);
        }
        return novo;
    }
    
    public List<Vertice2> getVertices() {
        return new ArrayList<>(vertices);
    }
    
    public List<Ligacao2> getLigacoes() {
        return ligacoes;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        
        for (Vertice2 vertice : vertices) {
            sb.append("\t");
            sb.append(vertice);
            sb.append("\n");
        }
        for (Ligacao2 ligacao : ligacoes) {
            sb.append("\t");
            sb.append(ligacao);
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException {
        // GrafoEmersonImpl g = new GrafoEmersonImpl();
        Grafo2 g = new Grafo2();
        g.novoVertice().putValor("nome", "A").ligarA(g.novoVertice().putValor("nome", "B"));
        
        System.out.println(g);
    }
    
    public void remover(Ligacao2 ligacao) {
        ligacao.origem.destinos.remove(ligacao);
        ligacao.destino.origens.remove(ligacao);
        ligacoes.remove(ligacao);
        ligacao.removeListener(ligacaoListener);
        for (Listener l : listeners) {
            l.aoRemover(ligacao);
        }
    }
    
    public void remover(Vertice2 v) {
        int i = 0;
        while (v.origens.size() > 0) {
            remover(v.origens.get(i));            
            i++;
        }
        i = 0;
        while (v.destinos.size() > 0) {
            remover(v.destinos.get(i));
            i++;
        }
        v.removeListener(verticeListener);
        vertices.remove(v);
        for (Listener l : listeners) {
            l.aoRemover(v);
        }
        
    }
    
    public void addListener(Listener l) {
        listeners.add(l);
    }
    
    public void removeListener(Listener l) {
        listeners.remove(l);
    }
    
    public void clear() {
        while (ligacoes.size() > 0) {
            remover(ligacoes.get(0));
        }
        while (vertices.size() > 0) {
            remover(vertices.get(0));
        }
    }
}
