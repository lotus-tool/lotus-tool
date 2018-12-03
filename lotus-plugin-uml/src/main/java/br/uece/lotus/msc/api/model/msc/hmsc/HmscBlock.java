/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.model.msc.hmsc;

import static br.uece.lotus.State.TEXTSTYLE_BOLD;
import static br.uece.lotus.State.TEXTSTYLE_NORMAL;

import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockViewImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscBlock implements GenericElement {
    
    private final HmscComponent hmscComponent;
    
    public interface Listener{
        void onChange(HmscBlock hmscHmscBlock);
    }
    
    public final static String mTextStyleNormal = TEXTSTYLE_NORMAL;
    public final static String mTextStyleBold = TEXTSTYLE_BOLD;
    
    private final static String mColorBlockFull = "green";
    private final static String mColorBlockEmpyt = "red";
    private boolean isInitial;
    private boolean isExceptional = false;
    //Propriedade do grafico
    private int mID;
    private final List<TransitionMSC> mTransicaoSaida = new ArrayList<>();
    private final List<TransitionMSC> mTransicaoEntrada = new ArrayList<>();
    private final Map<String, Object> mValues = new HashMap<>();
    private final List<Listener> listeners = new ArrayList<>();
    //Propriedade do View
    private String mLabel;
    private double mLayoutX;
    private double mLayoutY;
    private String mColor;
    private String mBorderColor;
    private Integer mBorderWidth;
    private String mTextColor;
    private String mTextStyle = mTextStyleNormal;
    private Integer mTextSize;
    private String mColorStatus = mColorBlockEmpyt;
    //Propriedade Bloco de DS
    private BmscComponent mDiagramSequence;


    public HmscBlock(HmscComponent hmscComponent) {
        this.hmscComponent = hmscComponent;
    }

    public int getID() {
        return mID;
    }



    public void setID(int mID) {
        this.mID = mID;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }


    public double getLayoutX() {
        return mLayoutX;
    }

    public void setLayoutX(double mLayoutX) {
        this.mLayoutX = mLayoutX;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public double getLayoutY() {
        return mLayoutY;
    }

    public void setLayoutY(double mLayoutY) {
        this.mLayoutY = mLayoutY;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public String getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(String mBorderColor) {
        this.mBorderColor = mBorderColor;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public Integer getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(Integer mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public String getTextColor() {
        return mTextColor;
    }

    public void setTextColor(String mTextColor) {
        this.mTextColor = mTextColor;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public String getTextStyle() {
        return mTextStyle;
    }

    public void setTextStyle(String mTextStyle) {
        this.mTextStyle = mTextStyle;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public Integer getTextSize() {
        return mTextSize;
    }

    public void setTextSize(Integer mTextSize) {
        this.mTextSize = mTextSize;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    @Override
    public Object getValue(String key){
        return mValues.get(key);
    }

    @Override
    public void putValue(String key, Object value){
        mValues.put(key, value);
    }

    public String getColorStatus() {
        return mColorStatus;
    }

    public void setColorStatus(String mColorStatus) {
        this.mColorStatus = mColorStatus;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public boolean isFull() {
        return mColorStatus.equals(mColorBlockFull);
    }

    public void setFull(boolean b){

        if(b){
            mColorStatus=mColorBlockFull;
        }else {
            mColorStatus= mColorBlockEmpyt;
        }


        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public BmscComponent getBmscComponet() {
        return mDiagramSequence;
    }

    public void setDiagramSequence(BmscComponent mDiagramSequence){
        if(mDiagramSequence != null){
            this.mDiagramSequence = mDiagramSequence;
            setColorStatus(mColorBlockFull);
        }
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.mID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HmscBlock other = (HmscBlock) obj;
        if (this.mID != other.mID) {
            return false;
        }
        return true;
    }
    
    public void addListener(HmscBlock.Listener l) {
        listeners.add(l);
    }

    public void removeListener(HmscBlock.Listener l) {
        listeners.remove(l);
    }

    public void addIncomingTransition(TransitionMSC t) {
        mTransicaoEntrada.add(t);
    }

    public void addOutgoingTransition(TransitionMSC t) {
        mTransicaoSaida.add(t);
    }

    public void removeIncomingTransition(TransitionMSC transition) {
        mTransicaoEntrada.remove(transition);
    }

    public void removeOutgoingTransition(TransitionMSC transition) {
        mTransicaoSaida.remove(transition);
    }

    public int getOutgoingTransitionsCount() {
        return mTransicaoSaida.size();
    }
    
    public int getIncomingTransitionsCount() {
        return mTransicaoEntrada.size();
    }


    @Override
    public TransitionMSC getTransitionTo(GenericElement genericElement){
        for(TransitionMSC t : mTransicaoSaida){
            try {
                if (((GenericElementView) t.getDestiny()).getGenericElement().equals(genericElement)) {
                    return t;
                }
            }catch (ClassCastException e){
                if((t.getDestiny()).equals(genericElement)){
                    return t;
                }
            }
        }
        return null;
    }


    @Override
    public List<TransitionMSC> getTransitionsTo(GenericElement genericElement){
        List<TransitionMSC> l = new ArrayList<>();
        for(TransitionMSC t : mTransicaoSaida){
            try {
                if (((GenericElementView) t.getDestiny()).getGenericElement().equals(genericElement)) {
                    l.add(t);
                }
            }catch (ClassCastException e){
                if((t.getDestiny()).equals(genericElement)){
                    l.add(t);
                }
            }
        }
        return l;
    }
    
    public List<TransitionMSC> getOutgoingTransitionList(){
        return Collections.unmodifiableList(mTransicaoSaida);
    }
    
    public List<TransitionMSC> getIncomingTransitionList(){
        return Collections.unmodifiableList(mTransicaoEntrada);
    }

    public void setInitial(Boolean b){
        this.isInitial = b;
        for(Listener l : listeners){
            l.onChange(this);
        }
    }

    public void setExceptional(Boolean b){
        this.isExceptional = b;
        if(b){
            this.setColor(HmscBlockViewImpl.EXCEPTIONAL_COLOR);
            this.setBorderWidth(HmscBlockViewImpl.EXCEPTIONAL_BORDER_WIDTH);
        }else {
            this.setColor(HmscBlockViewImpl.DEFAULT_COLOR);
            this.setBorderWidth(HmscBlockViewImpl.DEFAULT_BORDER_WIDTH);
        }
        for(Listener l : listeners){
            l.onChange(this);
        }
    }



    public boolean isInitial(){
        return this.isInitial;
    }

    public boolean isExceptional() {
        return this.isExceptional;
    }
}
