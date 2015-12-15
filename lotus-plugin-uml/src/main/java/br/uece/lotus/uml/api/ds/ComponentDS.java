/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import br.uece.lotus.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentDS {

    private int id;
    private List<Component> mComponentsLTS = new ArrayList<>();
    private String mName;
    
    
    
    
    
    
    
    public List<Component> getmComponentsLTS() {
        return mComponentsLTS;
    }

    public void setmComponentsLTS(List<Component> mComponentsLTS) {
        this.mComponentsLTS = mComponentsLTS;
    }
    
    public void addComponentLTS(Component c){
        mComponentsLTS.add(c);
    }
    public void removeComponentLTS(Component c){
        mComponentsLTS.remove(c);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String s) {
        this.mName = s;
    }

    public String getName() {
        return this.mName;
    }
    
}
