package br.uece.lotus.model;

import java.util.HashMap;
import java.util.Map;

public class TransitionModel implements Model {
        
    private StateModel mOrigem;
    private StateModel mDestino;
    private Map<String, String> mValues = new HashMap<>();
    private Object mTag;
    
    public StateModel getOrigem() {
        return mOrigem;
    }

    public void setOrigem(StateModel origem) {
        this.mOrigem = origem;
    }

    public StateModel getDestino() {
        return mDestino;
    }
    
    public void setDestino(StateModel destino) {
        this.mDestino = destino;
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
