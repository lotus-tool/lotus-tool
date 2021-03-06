/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;

/**
 *
 * @author Bruno Barbosa
 */
public interface WindowManagerDS {
    
    public interface Listener{
        void onCreateWindow(WindowDS w);
    }
    
    public void show(StandardModeling buildDS);
    public void show(ComponentDS cds);
    public void show(Component c);

    public void close(StandardModeling buildDS);
    public void close(ComponentDS cds);
    public void close(Component c);
    
    public void hide(StandardModeling buildDS);
    public void hide(ComponentDS cds);
    public void hide(Component c);
    
    public void hideAll();
    
    public void addListener(Listener l);
    
}
