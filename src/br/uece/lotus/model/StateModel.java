package br.uece.lotus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateModel implements Model {

    private int mID;
    private List<TransitionModel> mTransicoesSaida = new ArrayList<>();
    private List<TransitionModel> mTransicoesEntrada = new ArrayList<>();
    private Map<String, String> mValues = new HashMap<>();
    private Object mTag;

    public void setID(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }

    public List<TransitionModel> getTransicoesSaida() {
        return mTransicoesSaida;
    }

    public List<TransitionModel> getTransicoesEntrada() {
        return mTransicoesEntrada;
    }

    @Override
    public void setValue(String chave, String valor) {
        mValues.put(chave, valor);
    }

    @Override
    public String getValue(String chave) {
        return mValues.get(chave);
    }

    public Object getTag() {
        return mTag;        
    }
    
    public void setTag(Object tag) {
        mTag = tag;
    }

    public Map<String, String> getValues() {
        return mValues;
    }
}