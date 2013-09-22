package br.uece.mastergraphs.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class Ligacao {

	Vertice origem;
	Vertice destino;
	private ObjectProperty<Object> valor = new SimpleObjectProperty<Object>();
	
	Ligacao(Vertice va, Vertice vb) {
		origem = va;
		destino = vb;
	}

	public Vertice getOrigem() {
		return origem;
	}
	
	public Vertice getDestino() {
		return destino;
	}
	
	public ObjectProperty<Object> getValor() {
		return valor;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(origem);
		sb.append("->");
		sb.append(destino);
		return sb.toString();
	}
	
}
