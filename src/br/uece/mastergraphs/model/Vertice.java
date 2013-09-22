package br.uece.mastergraphs.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Vertice {

    private GrafoEmersonImpl grafo;
    private ObjectProperty<Object> valor = new SimpleObjectProperty<Object>();
    ObservableList<Ligacao> origens = FXCollections.observableArrayList();
    ObservableList<Ligacao> destinos = FXCollections.observableArrayList();

    Vertice(GrafoEmersonImpl grafo) {
        this.grafo = grafo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valor.getValue());
        return sb.toString();
    }

    public ObjectProperty<Object> valorProperty() {
        return valor;
    }

    ;

	public Ligacao ligarA(Vertice destino) {
        Ligacao novo = new Ligacao(this, destino);
        //grafo.ligacoes.add(novo);
        destinos.add(novo);
        destino.origens.add(novo);
        return novo;
    }
}
