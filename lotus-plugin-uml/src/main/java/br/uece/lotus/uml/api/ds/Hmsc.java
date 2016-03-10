/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import static br.uece.lotus.State.TEXTSTYLE_BOLD;
import static br.uece.lotus.State.TEXTSTYLE_NORMAL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 */
public class Hmsc {
    
    private final StandardModeling mComponentBuildDS;
    
    public interface Listener{
        void onChange(Hmsc blockBuildDS);
    }
    
    public final static String mTextStyleNormal = TEXTSTYLE_NORMAL;
    public final static String mTextStyleBold = TEXTSTYLE_BOLD;
    
    private final static String mColorBlockFull = "gren";
    private final static String mColorBlockEmpyt = "red";
    
    //Propriedade do grafico
    private int mID;
    private final List<TransitionMSC> mTransicaoSaida = new ArrayList<>();
    private final List<TransitionMSC> mTransicaoEntrada = new ArrayList<>();
    private final Map<String, Object> mValues = new HashMap<>();
    private final List<Listener> mListeners = new ArrayList<>();
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
    private ComponentDS mDiagramSequence;

    public Hmsc(StandardModeling mComponentBuildDS) {
        this.mComponentBuildDS = mComponentBuildDS;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public double getLayoutX() {
        return mLayoutX;
    }

    public void setLayoutX(double mLayoutX) {
        this.mLayoutX = mLayoutX;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public double getLayoutY() {
        return mLayoutY;
    }

    public void setLayoutY(double mLayoutY) {
        this.mLayoutY = mLayoutY;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public String getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(String mBorderColor) {
        this.mBorderColor = mBorderColor;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public Integer getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(Integer mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public String getTextColor() {
        return mTextColor;
    }

    public void setTextColor(String mTextColor) {
        this.mTextColor = mTextColor;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public String getTextStyle() {
        return mTextStyle;
    }

    public void setTextStyle(String mTextStyle) {
        this.mTextStyle = mTextStyle;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public Integer getTextSize() {
        return mTextSize;
    }

    public void setTextSize(Integer mTextSize) {
        this.mTextSize = mTextSize;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }
    
    public Object getValue(String key){
        return mValues.get(key);
    }
    
    public void setValue(String key, Object value){
        mValues.put(key, value);
    }

    public String getColorStatus() {
        return mColorStatus;
    }

    public void setColorStatus(String mColorStatus) {
        this.mColorStatus = mColorStatus;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public boolean isFull() {
        return mColorStatus.equals(mColorBlockFull);
    }

    public ComponentDS getmDiagramSequence() {
        return mDiagramSequence;
    }

    public void setmDiagramSequence(ComponentDS mDiagramSequence){
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
        final Hmsc other = (Hmsc) obj;
        if (this.mID != other.mID) {
            return false;
        }
        return true;
    }
    
    public void addListener(Hmsc.Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Hmsc.Listener l) {
        mListeners.remove(l);
    }
    
    void addIncomingTransition(TransitionMSC t) {
        mTransicaoEntrada.add(t);
    }

    void addOutgoingTransition(TransitionMSC t) {
        mTransicaoSaida.add(t);
    }

    void removeIncomingTransition(TransitionMSC transition) {
        mTransicaoEntrada.remove(transition);
    }

    void removeOutgoingTransition(TransitionMSC transition) {
        mTransicaoSaida.remove(transition);
    }

    public int getOutgoingTransitionsCount() {
        return mTransicaoSaida.size();
    }
    
    public int getIncomingTransitionsCount() {
        return mTransicaoEntrada.size();
    }
    
    public TransitionMSC getTransitionBuildDSTo(Hmsc bbds){
        for(TransitionMSC t: mTransicaoSaida){
            //falta implementar a transition
        }
        return null;
    }
    
    public List<TransitionMSC> getTransitionsBuildDSTo(Hmsc bbds){
        List<TransitionMSC> l = new ArrayList<>();
        for(TransitionMSC t:mTransicaoSaida){
            //falta implementar a transition
        }
        return l;
    }
}
