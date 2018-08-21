package br.uece.lotus.uml.app.runtime.model.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMSCCustom implements TransitionHMSCCustom.Listener {
    private final StantardModelingCustom StantardModelingCustom;
    private Integer id;
    private String label;
    private final Map<String, Object> values = new HashMap<>();
    private List<Listener> listeners = new ArrayList<>();

    private final List<TransitionHMSCCustom> outGoingTransitions = new ArrayList<>();
    private final List<TransitionHMSCCustom> inGoingTransitions = new ArrayList<>();

    public interface Listener {

        void onHMSCCustomCreated(HMSCCustom HMSCCustom);


    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public HMSCCustom(StantardModelingCustom StantardModelingCustom, Integer id) {
        this.id = id;
        this.StantardModelingCustom = StantardModelingCustom;

        addListener(StantardModelingCustom);

        for(Listener l : listeners){
            l.onHMSCCustomCreated(this);
        }
    }

    public Integer getId() {
        return id;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void putValue (String key, Object value){
        values.put(key,value);
    }

    public Object getValue(String key){
        return values.get(key);
    }

    public TransitionHMSCCustom getTransitionByLabel(String label) {
        for (TransitionHMSCCustom transitionHMSCCustomOut : outGoingTransitions){
            if(transitionHMSCCustomOut.getLabel().equals(label)){
                return transitionHMSCCustomOut;
            }
        }
        return null;
    }

    private void addTransitionIn(TransitionHMSCCustom transitionHMSCCustomIn){
        inGoingTransitions.add(transitionHMSCCustomIn);
    }

    private void addTransitionOut (TransitionHMSCCustom transitionHMSCCustomOut){
        outGoingTransitions.add(transitionHMSCCustomOut);
    }

    public List<TransitionHMSCCustom> getOutGoingTransitions() {
        return outGoingTransitions;
    }

    public List<TransitionHMSCCustom> getInGoingTransitions() {
        return inGoingTransitions;
    }

    public StantardModelingCustom getStantardModelingCustom() {
        return StantardModelingCustom;
    }

    @Override
    public void onCreatedTransition(TransitionHMSCCustom transitionHMSCCustom, HMSCCustom srcHMSCCustom, HMSCCustom destHMSCCustom) {
        if(id == srcHMSCCustom.getId()){
            addTransitionOut(transitionHMSCCustom);

        }else if(id == destHMSCCustom.getId()){
            addTransitionIn(transitionHMSCCustom);

        }
    }



}
