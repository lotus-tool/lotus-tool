/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 *
 * @author Bruno Barbosa
 */
public abstract class TransitionMSCViewImpl extends Region implements TransitionMSCView, TransitionMSC.Listener{

    protected TransitionMSC mTransition;
    protected HmscView hMscSource;
    protected HmscView hMscDestiny;
    protected BlockDSView mscSource;
    protected BlockDSView mscDestiny;
    
    
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public TransitionMSC getTransition() {
        return mTransition;
    }

    @Override
    public void setTransitionMSC(TransitionMSC t, Node component) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected abstract void prepareView();
    protected abstract void updateView();
    
    @Override
    public void onChange(TransitionMSC transitionBuildDS) {
        Platform.runLater(this::updateView);
    }
    
    protected String getComputedLabel(){
        String s = "";
        //Falta implementar
        return s;
    }
}
