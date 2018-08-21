package br.uece.lotus.uml.app.runtime.model.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMSCCustom {
    private final Map<String, Object> values = new HashMap<String, Object>();
    private String name;
    private final List<StantardModelingCustom> stantardModelingCustoms = new ArrayList<>();

    public List<StantardModelingCustom> getStantardModelingCustoms() {
        return stantardModelingCustoms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addStantardModelingCustom(StantardModelingCustom StantardModelingCustom) {
        stantardModelingCustoms.add(StantardModelingCustom);

    }

    public StantardModelingCustom getStantardModelingCustom(int index) {
        return stantardModelingCustoms.get(index);
    }

    public void putValue (String key, Object value){
        values.put(key,value);
    }

    public Object getValue(String key){
        return values.get(key);
    }



}


