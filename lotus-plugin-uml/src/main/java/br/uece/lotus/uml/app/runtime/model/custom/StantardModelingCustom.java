package br.uece.lotus.uml.app.runtime.model.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StantardModelingCustom implements HMSCCustom.Listener, TransitionHMSCCustom.Listener {


    private List<HMSCCustom> HMSCCustoms = new ArrayList<>();
    private final Map<String, Object> values = new HashMap<>();

    private List<TransitionHMSCCustom> transitionsHMSCCustom = new ArrayList<>();





    private void addHMSCCustom (HMSCCustom HMSCCustom){
        HMSCCustoms.add(HMSCCustom);
    }

    public List<HMSCCustom> getHMSCCustoms() {
        return HMSCCustoms;
    }

    public void setHMSCCustoms(List<HMSCCustom> HMSCCustoms) {
        this.HMSCCustoms = HMSCCustoms;
    }

    public void putValue (String key, Object value){
        values.put(key,value);
    }

    public Object getValue(String key){
        return values.get(key);
    }


    public HMSCCustom getInitialHMSCCustom(){
        return HMSCCustoms.get(0);
    }


    public int getHMSCCustomCount() {
        return HMSCCustoms.size();
    }

    public List<TransitionHMSCCustom> getTransitionsHMSCCustom() {
        return transitionsHMSCCustom;
    }

    public HMSCCustom getHMSCCustomFromId(Integer id){

        for(HMSCCustom currentHMSCCustom : HMSCCustoms){

            if( currentHMSCCustom.getId() == id){
                return currentHMSCCustom;
            }

        }
        return null;
    }

    public TransitionHMSCCustom getTransitionHMSCCustom(Integer srcState, Integer dstState){
        for(TransitionHMSCCustom transitionHMSCCustom : transitionsHMSCCustom){
            if(transitionHMSCCustom.getSourceHMSCCustom().getId() == srcState && transitionHMSCCustom.getDestinyHMSCCustom().getId() == dstState){
                return transitionHMSCCustom;
            }
        }
        return null;
    }


    @Override
    public void onHMSCCustomCreated(HMSCCustom HMSCCustom) {
        HMSCCustoms.add(HMSCCustom);
    }

    @Override
    public void onCreatedTransition(TransitionHMSCCustom transitionHMSCCustom, HMSCCustom srcHMSCCustom, HMSCCustom destHMSCCustom) {
        transitionsHMSCCustom.add(transitionHMSCCustom);
    }

    public HMSCCustom getHMSCCustomFromLabel(String labelHMSCCustom) {
        HMSCCustom tempHMSCCustom = null;

        for(HMSCCustom currentHMSCCustom : HMSCCustoms){

            if( currentHMSCCustom.getLabel().equals(labelHMSCCustom) ){
                tempHMSCCustom = currentHMSCCustom;
            }

        }
        return tempHMSCCustom;
    }
}
