/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.window;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface WindowMSC {
    
    HmscComponent getHmscComponent();
    BmscComponent getBmscComponent();
    Component getComponentLTS();
    
    void setHmscComponent(HmscComponent hmscComponent);
    void setBmscComponent(BmscComponent bmscComponent);
    void setComponentLTS(Component componentLTS);
    
    String getTitle();
    Node getNode();
}
