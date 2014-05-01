package br.uece.lotus.model;

import br.uece.lotus.view.ComponentEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ComponentModel {

    private ObservableList<StateModel> mVertices = FXCollections.observableArrayList();
    private ObservableList<TransitionModel> mTransicoes = FXCollections.observableArrayList();
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ObservableList<StateModel> getVertices() {
        return mVertices;
    }

    public ObservableList<TransitionModel> getTransicoes() {
        return mTransicoes;
    }

    public StateModel getVertice(int id) {
        for (StateModel v : mVertices) {
            if (v.getID() == id) {
                return v;
            }
        }
        return null;
    }

    public StateModel newVertice(int id) {
        StateModel v = new StateModel();
        v.setID(id);
        mVertices.add(v);
        return v;
    }

    public void add(StateModel v) {
        mVertices.add(v);
    }

    public void add(TransitionModel t) {
        mTransicoes.add(t);
    }

    public TransitionModel newTransicao(StateModel origem, StateModel destino) {
        TransitionModel t = new TransitionModel();
        t.setOrigem(origem);
        t.setDestino(destino);
        origem.getTransicoesSaida().add(t);
        destino.getTransicoesEntrada().add(t);
        mTransicoes.add(t);
        return t;
    }

    public TransitionModel newTransicao(int idOrigem, int idDestino) {
        StateModel o = getVertice(idOrigem);
        StateModel d = getVertice(idDestino);
        TransitionModel t = new TransitionModel();
        t.setOrigem(o);
        t.setDestino(d);
        mTransicoes.add(t);
        return t;
    }

    public void remove(StateModel model) {
        for (TransitionModel tt : model.getTransicoesEntrada()) {
            mTransicoes.remove(tt);
        }
        for (TransitionModel tt : model.getTransicoesSaida()) {
            mTransicoes.remove(tt);
        }       
        mVertices.remove(model);
    }

    public void remove(TransitionModel model) {
        model.getOrigem().getTransicoesSaida().remove(model);
        model.getDestino().getTransicoesEntrada().remove(model);
        mTransicoes.remove(model);
    }

    @Override
    public String toString() {
        return mName;
    }
}
