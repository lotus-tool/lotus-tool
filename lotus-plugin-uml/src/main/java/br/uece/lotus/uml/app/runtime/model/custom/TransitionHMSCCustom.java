package br.uece.lotus.uml.app.runtime.model.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionHMSCCustom {
    private final StantardModelingCustom StantardModelingCustom;
    private HMSCCustom sourceHMSCCustom, destinyHMSCCustom;
    private Double probability;
    private  String label;
    private final Map<String, Object> values = new HashMap<>();
    private List<Listener> listeners = new ArrayList<>();



    public  interface Listener {
        void onCreatedTransition(TransitionHMSCCustom transitionHMSCCustom, HMSCCustom srcHMSCCustom, HMSCCustom destHMSCCustom);
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }



    public TransitionHMSCCustom(StantardModelingCustom StantardModelingCustom, HMSCCustom sourceHMSCCustom, HMSCCustom destinyHMSCCustom) {
        this.StantardModelingCustom = StantardModelingCustom;
        this.sourceHMSCCustom = sourceHMSCCustom;
        this.destinyHMSCCustom = destinyHMSCCustom;

        addListener(StantardModelingCustom);
        addListener(this.sourceHMSCCustom);
        addListener(this.destinyHMSCCustom);

        for (Listener listener: listeners){
            listener.onCreatedTransition(this, sourceHMSCCustom, destinyHMSCCustom);
        }
    }

    public StantardModelingCustom getStantardModelingCustom() {
        return StantardModelingCustom;
    }


    public void setSourceHMSCCustom(HMSCCustom HMSCCustom) {
        sourceHMSCCustom = HMSCCustom;
    }

    public void setDestinyHMSCCustom(HMSCCustom HMSCCustom) {
        destinyHMSCCustom = HMSCCustom;
    }

    public HMSCCustom getSourceHMSCCustom() {
        return sourceHMSCCustom;
    }

    public HMSCCustom getDestinyHMSCCustom() {
        return destinyHMSCCustom;
    }

    public void putValue (String key, Object value){
        values.put(key,value);
    }

    public Object getValue(String key){
        return values.get(key);
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label){
        this.label = label;
    }



}
