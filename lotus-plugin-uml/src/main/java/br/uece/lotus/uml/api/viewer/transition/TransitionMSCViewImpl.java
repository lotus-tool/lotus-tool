/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 *
 * @author Bruno Barbosa
 */
public abstract class TransitionMSCViewImpl extends Region implements TransitionMSCView, TransitionMSC.Listener{

    protected TransitionMSC mTransition;
    protected HmscView hMscSource;
    protected HmscView hMscDestiny;
    protected Hmsc srcHMSC;
    protected Hmsc dstHMSC;
    protected BlockDSView bMscSource;
    protected BlockDSView bMscDestiny;
    protected BlockDS srcBMSC;
    protected BlockDS dstBMSC;
    protected String mValueType;
    
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public TransitionMSC getTransition() {
        return mTransition;
    }

    @Override
    public void setTransitionMSC(TransitionMSC t, Object component) {
        if(mTransition != null){
            mTransition.removeListener(this);
            hMscSource = null;
            hMscDestiny = null;
            bMscSource = null;
            bMscDestiny = null;
        }
        mTransition = t;
        if(mTransition != null){
            if(component instanceof StandardModeling){
                try {
                    hMscSource = (HmscView) t.getSource();
                    hMscDestiny = (HmscView) t.getDestiny();
                } catch (ClassCastException e) {
                    srcHMSC = (Hmsc) t.getSource();
                    dstHMSC = (Hmsc) t.getDestiny();
                }
                mValueType = "hMSC";
                mTransition.addListener(this);
                prepareView();
                updateView();
            }else{
                try{
                    bMscSource = (BlockDSView) t.getSource();
                    bMscDestiny = (BlockDSView) t.getDestiny();
                }catch(ClassCastException e){
                    srcBMSC = (BlockDS) t.getSource();
                    dstBMSC = (BlockDS) t.getDestiny();
                }
                mValueType = "bMSC";
                mTransition.addListener(this);
                prepareView();
                updateView();
            }
        }
    }
    
    protected abstract void prepareView();
    protected abstract void updateView();
    
    @Override
    public void onChange(TransitionMSC transitionMSC) {
        Platform.runLater(this::updateView);
    }
    
    protected String getComputedLabel(){
        String s = "";
        if(mValueType.equals("hMSC")){
            if(mTransition.getGuard() != null){
                if(mTransition.getGuard().equals("")){
                    s += "";
                }else{
                    s += " ("+mTransition.getGuard()+")";
                }
            }
            if(!mTransition.getActions().isEmpty()){
                for(String action : mTransition.getActions()){
                    s +="{"+action+"}";
                }
            }else {
                s += "";
            }

            if(mTransition.getProbability() != null){
                if(mTransition.getProbability() == null){
                    s += "";
                }else{
                    s += String.format(" %.2f", mTransition.getProbability());
                }
            }
            if(mTransition.getLabel() != null){
                s += " "+mTransition.getLabel();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        else if(mValueType.equals("bMSC")){
            if(mTransition.getIdSequence() != null){
                s += String.valueOf(mTransition.getIdSequence())+".";
            }
            if(mTransition.getGuard() != null){
                if(mTransition.getGuard().equals("")){
                    s += "";
                }else{
                    s += " ("+mTransition.getGuard()+")";
                }
            }
            if(mTransition.getLabel() != null){
                if(mTransition.getLabel().equals("")){
                    s += "";
                }else{
                    s += " "+mTransition.getLabel()+" ";
                }
            }

            if(!mTransition.getParameters().isEmpty()){
                for(String param : mTransition.getParameters()){
                    s +="["+param+"]";
                }
            }else {
                s += "";
            }

            if(!mTransition.getActions().isEmpty()){
                for(String action : mTransition.getActions()){
                    s +="{"+action+"}";
                }
            }else {
                s += "";
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        return s;
    }
    
    public HmscView gethMSCsourceView(){
        return hMscSource;
    }
    public HmscView getSrcHMSC(){
        return (HmscView)srcHMSC.getValue("view");
    }
    public HmscView gethMSCdestinyView(){
        return hMscDestiny;
    }
    
    public BlockDSView getbMSCsourceView(){
        return bMscSource;
    }
    
    public BlockDSView getbMSCdestinyView(){
        return bMscDestiny;
    }
}
