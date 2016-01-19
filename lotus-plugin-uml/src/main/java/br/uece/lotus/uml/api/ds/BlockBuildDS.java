/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import static br.uece.lotus.State.TEXTSTYLE_BOLD;
import static br.uece.lotus.State.TEXTSTYLE_NORMAL;
import br.uece.lotus.uml.sequenceDiagram.Astah.DiagramaSequencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno Barbosa
 */
public class BlockBuildDS {
    
    private final ComponentBuildDS mComponentBuildDS;
    
    public interface Listener{
        void onChange(BlockBuildDS blockBuildDS);
    }
    
    private final String mTextStyleNormal = TEXTSTYLE_NORMAL;
    private final String mTextStyleBold = TEXTSTYLE_BOLD;
    
    private final static String mColorBlockFull = "gren";
    private final static String mColorBlockEmpyt = "red";
    
    //Propriedade do grafico
    private int mID;
    private final List<TransitionBuildDS> mTransicaoSaida = new ArrayList<>();
    private final List<TransitionBuildDS> mTransicaoEntrada = new ArrayList<>();
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
    private DiagramaSequencia mDiagramSequence;//Diferenciar para poder ter diagrama sem ser do astah
    private boolean mFull = false;

    public BlockBuildDS(ComponentBuildDS mComponentBuildDS) {
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

    public DiagramaSequencia getmDiagramSequence() {
        return mDiagramSequence;
    }

    public void setmDiagramSequence(DiagramaSequencia mDiagramSequence) {
        this.mDiagramSequence = mDiagramSequence;
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
        final BlockBuildDS other = (BlockBuildDS) obj;
        if (this.mID != other.mID) {
            return false;
        }
        return true;
    }
    
    public void addListener(BlockBuildDS.Listener l) {
        mListeners.add(l);
    }

    public void removeListener(BlockBuildDS.Listener l) {
        mListeners.remove(l);
    }
    
    void addIncomingTransition(TransitionBuildDS t) {
        mTransicaoEntrada.add(t);
    }

    void addOutgoingTransition(TransitionBuildDS t) {
        mTransicaoSaida.add(t);
    }

    void removeIncomingTransition(TransitionBuildDS transition) {
        mTransicaoEntrada.remove(transition);
    }

    void removeOutgoingTransition(TransitionBuildDS transition) {
        mTransicaoSaida.remove(transition);
    }

    public int getOutgoingTransitionsCount() {
        return mTransicaoSaida.size();
    }
    
    public int getIncomingTransitionsCount() {
        return mTransicaoEntrada.size();
    }
    
    public TransitionBuildDS getTransitionBuildDSTo(BlockBuildDS bbds){
        for(TransitionBuildDS t: mTransicaoSaida){
            //falta implementar a transition
        }
        return null;
    }
    
    public List<TransitionBuildDS> getTransitionsBuildDSTo(BlockBuildDS bbds){
        List<TransitionBuildDS> l = new ArrayList<>();
        for(TransitionBuildDS t:mTransicaoSaida){
            //falta implementar a transition
        }
        return l;
    }
}
