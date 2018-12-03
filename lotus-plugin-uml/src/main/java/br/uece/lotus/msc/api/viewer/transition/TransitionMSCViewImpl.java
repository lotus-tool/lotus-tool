/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.transition;

import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 *
 * @author Bruno Barbosa
 */
public abstract class TransitionMSCViewImpl extends Region implements TransitionMSCView, TransitionMSC.Listener {

    protected TransitionMSC transition;
    protected GenericElementView sourceGenericElementView;
    protected GenericElementView destinyGenericElementView;
    protected GenericElement sourceGenericElement;
    protected GenericElement destinyGenericElement;
    protected BlockDSView bMscSource;
    protected BlockDSView bMscDestiny;
    protected BmscBlock srcBMSC;
    protected BmscBlock dstBMSC;
    protected String mValueType;
    
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public TransitionMSC getTransition() {
        return transition;
    }

    @Override
    public void setTransitionMSC(TransitionMSC t, Object component) {
        if(transition != null){
            transition.removeListener(this);
            sourceGenericElementView = null;
            destinyGenericElementView = null;
            bMscSource = null;
            bMscDestiny = null;
        }
        transition = t;
        if(transition != null){
            if(component instanceof HmscComponent){
                try {
                    sourceGenericElementView = (GenericElementView) t.getSource();
                    destinyGenericElementView = (GenericElementView) t.getDestiny();
                } catch (ClassCastException e) {
                    sourceGenericElement = (GenericElement) t.getSource();
                    destinyGenericElement = (GenericElement) t.getDestiny();
                }
                mValueType = "GenericElement";
                transition.addListener(this);
                prepareView();
                updateView();
            }else{
                try{
                    bMscSource = (BlockDSView) t.getSource();
                    bMscDestiny = (BlockDSView) t.getDestiny();
                }catch(ClassCastException e){
                    srcBMSC = (BmscBlock) t.getSource();
                    dstBMSC = (BmscBlock) t.getDestiny();
                }
                mValueType = "bMSC";
                transition.addListener(this);
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
        if(mValueType.equals("GenericElement")){
            if(transition.getGuard() != null){
                if(transition.getGuard().equals("")){
                    s += "";
                }else{
                    s += " ("+ transition.getGuard()+")";
                }
            }
            if(!transition.getActions().isEmpty()){
                for(String action : transition.getActions()){
                    s +="{"+action+"}";
                }
            }else {
                s += "";
            }

            if(transition.getProbability() != null){
                if(transition.getProbability() == null){
                    s += "";
                }else{
                    s += String.format(" %.2f", transition.getProbability());
                }
            }
            if(transition.getLabel() != null){
                s += " "+ transition.getLabel();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        else if(mValueType.equals("bMSC")){
            if(transition.getIdSequence() != null){
                s += String.valueOf(transition.getIdSequence())+".";
            }
            if(transition.getGuard() != null){
                if(transition.getGuard().equals("")){
                    s += "";
                }else{
                    s += " ("+ transition.getGuard()+")";
                }
            }
            if(transition.getLabel() != null){
                if(transition.getLabel().equals("")){
                    s += "";
                }else{
                    s += " "+ transition.getLabel()+" ";
                }
            }

            if(!transition.getParameters().isEmpty()){
                for(String param : transition.getParameters()){
                    s +="["+param+"]";
                }
            }else {
                s += "";
            }

            if(!transition.getActions().isEmpty()){
                for(String action : transition.getActions()){
                    s +="{"+action+"}";
                }
            }else {
                s += "";
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        return s;
    }
    
    public GenericElementView getSourceGenericElementView(){
        return sourceGenericElementView;
    }
    public GenericElementView getSourceGenericElement(){
        return (GenericElementView) sourceGenericElement.getValue("view");
    }
    public GenericElementView gethMSCdestinyView(){
        return destinyGenericElementView;
    }
    
    public BlockDSView getbMSCsourceView(){
        return bMscSource;
    }
    
    public BlockDSView getbMSCdestinyView(){
        return bMscDestiny;
    }
}
