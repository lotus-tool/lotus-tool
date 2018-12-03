/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;

/**
 *
 * @author Bruno Barbosa
 */
public interface WindowManagerMSC {
    
    public interface Listener{
        void onCreateWindow(WindowMSC windowMSC);
    }
    
    public void show(HmscComponent hmscComponent);
    public void show(BmscComponent bmscComponent);
    public void show(Component component);

    public void close(HmscComponent hmscComponent);
    public void close(BmscComponent bmscComponent);
    public void close(Component component);
    
    public void hide(HmscComponent hmscComponent);
    public void hide(BmscComponent bmscComponent);
    public void hide(Component component);
    
    public void hideAll();
    
    public void addListener(Listener listener);
    
}
