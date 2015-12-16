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

    
    public interface Listener{
        void onChange(ComponentDS cds);
        //falta adicionar os outros listener. Quando for adicionar me avisa, pq tem que implementar o restante
        //que falta nas outras classes que dependen desses listener. se nao ja vai dar erro na compilacao ou na abertura
        //da tela
    }
    
    private List<Component> mComponentsLTS = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();
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
    
    public void setName(String s) {
        this.mName = s;
    }

    public String getName() {
        return this.mName;
    }
    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
}
