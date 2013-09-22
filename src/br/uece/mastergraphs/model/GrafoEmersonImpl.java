package br.uece.mastergraphs.model;

import br.uece.mastergraphs.Grafo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;

public class GrafoEmersonImpl implements Grafo, Serializable {

//	ObservableList<Ligacao2> ligacoes = FXCollections.observableArrayList();
//	private ObservableList<Vertice2> vertices = FXCollections.observableArrayList();
//
//	public Vertice2 novoVertice(Object valor) {
////		Vertice2 novo = new Vertice2(this);
////		novo.valorProperty().set(valor);
////		vertices.add(novo);
////		return novo;
//	}
//	
//	public ObservableList<Vertice> getVertices() {
////		return vertices;
//	}
//	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("{\n");
//		
//		for (Vertice vertice: vertices) {
//			sb.append("\t");
//			sb.append(vertice);
//			sb.append("\n");
//		}
//		for (Ligacao ligacao: ligacoes) {
//			sb.append("\t");
//			sb.append(ligacao);
//			sb.append("\n");
//		}
//		sb.append("}");
//		return sb.toString();
//	}
	
	public static void main(String[] args) throws IOException {
//		GrafoEmersonImpl g = new GrafoEmersonImpl();
//		g.novoVertice("A").ligarA(g.novoVertice("B"));
//		
//		System.out.println(g);
	}
	
	public void remover(Ligacao l) {
//		l.origem.destinos.remove(l);
//		l.destino.origens.remove(l);
//		ligacoes.remove(l);
	}
	
	public void remover(Vertice v) {
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
	}

    @Override
    public int getNumeroDeVertices() {
        //throw new UnsupportedOperationException("Not supported yet.");
//        return vertices.size();
        return 0;
    }

    @Override
    public String[] getNomeDosVertices() {
        //throw new UnsupportedOperationException("Not supported yet.");
        List<String> aux = new ArrayList<>();
//        for (Vertice v: vertices) {
//            aux.add(v.valorProperty().getValue().toString());
//        }
        return aux.toArray(new String[0]);
    }

    @Override
    public String[][] getMatrixDeLigacoes() {
//        String[][] aux = new String[vertices.size()][vertices.size()];
//        for (Vertice2 v: vertices) {
//            int indiceVertice = vertices.indexOf(v);
//            for (Vertice2 outro: vertices) {
//                String peso = "0";
//                for (Ligacao2 l: v.destinos) {
//                    if (l.destino.equals(outro)) {
//                        peso = l.getValor("peso");
//                        break;
//                    }
//                }
//                int indiceOutroVertice = vertices.indexOf(outro);
//                aux[indiceVertice][indiceOutroVertice] = peso;
//            }
//        }
        return null;
    }
    
    @Override
    public void pintarVertice(int indice, Paint fill) {
//        Vertice2 vertice = vertices.get(indice);        
//        vertice.putValor("cor", fill);
    }
    
    @Override
    public void pintarLigacao(int indiceVerticeOrigem, int indiceVerticeDestino, Paint fill) {
//        Vertice2 origem = vertices.get(indiceVerticeOrigem);
//        Vertice2 destino = vertices.get(indiceVerticeDestino);
//        for (Ligacao2 l: origem.destinos) {
//            if (l.getDestino().equals(destino)) {
//                l.putValor("cor", fill);
//            }
//        }
    }

}