/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import br.uece.lotus.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class TransitionMSC {

    
    public static class Builder {
        private /*final*/ StandardModeling mComponentBuild;
        private /*final*/ TransitionMSC mTransitionMSC;
        private /*final*/ ComponentDS mComponentDS;

        public Builder(StandardModeling mComponentBuild, TransitionMSC mTransitionMSC) {
           this.mComponentBuild = mComponentBuild;
           this.mTransitionMSC = mTransitionMSC;
       }

        public Builder(ComponentDS mComponentDS, TransitionMSC mTransitionMSC) {
            this.mComponentDS = mComponentDS;
            this.mTransitionMSC = mTransitionMSC;
        }

       public Builder setValue(String s, Object o) {
           mTransitionMSC.setValue(s, o);
           return this;
       }

       public TransitionMSC create() {
           mComponentBuild.add(mTransitionMSC);
           return mTransitionMSC;
       }
       
       public TransitionMSC createForXml() {
           mComponentBuild.add2(mTransitionMSC);
           return mTransitionMSC;
       }

        public TransitionMSC createDS() {
            mComponentDS.add(mTransitionMSC);
            return mTransitionMSC;
        }
       
       public Builder setIdSequence(int id){
           mTransitionMSC.setIdSequence(id);
           return this;
       }

       public Builder setLabel(String label) {
           mTransitionMSC.setLabel(label);
           return this;
       }

       public Builder setProbability(Double probability) {
           mTransitionMSC.setProbability(probability);
           return this;            
       }
       
       public Builder setGuard(String guard){
           mTransitionMSC.setGuard(guard);
           return this;
       }

       public Builder setViewType(int type) {
           mTransitionMSC.setValue("view.type", type);
           return this;
       }
    }
    
    public interface Listener {

        void onChange(TransitionMSC transitionMSC);
    }
    
    public static final String TEXTSTYLE_NORMAL = "normal";
    public static final String TEXTSTYLE_BOLD = "bold";
    //Grafica
    private Object mSource;  // declarado como object para ser tanto hMSC como bMSC
    private Object mDestiny;
    private final Map<String, Object> mValues = new HashMap<>();
    private final List<Listener> mListeners = new ArrayList<>();
    //View
    private String mColor = "black";
    private int mWidth = 1;
    private String mTextColor = "black";
    private String mTextStyle = TEXTSTYLE_NORMAL;
    private int mTextSize = 13;
    //Transition
    private String mLabel;
    private Double mProbability;
    private String mGuard;
    private Integer mIdSequence;

    public TransitionMSC(Object mSource, Object mDestiny) {
        this.mSource = mSource;
        this.mDestiny = mDestiny;
    }

    public Object getSource() {
        return mSource;
    }

    public Object getDestiny() {
        return mDestiny;
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

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
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

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public Double getProbability() {
        return mProbability;
    }

    public void setProbability(Double mProbability) {
        this.mProbability = mProbability;
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
    
    public void setGuard(String guard) {
        this.mGuard = guard;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }
    
    public String getGuard(){
        return mGuard;
    }

    public void setIdSequence(int id) {
        this.mIdSequence = id;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }
    
    public Integer getIdSequence(){
        return mIdSequence;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.mSource);
        hash = 97 * hash + Objects.hashCode(this.mDestiny);
        hash = 97 * hash + Objects.hashCode(this.mLabel);
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
        final TransitionMSC other = (TransitionMSC) obj;
        if (!Objects.equals(this.mSource, other.mSource)) {
            return false;
        }
        if (!Objects.equals(this.mDestiny, other.mDestiny)) {
            return false;
        }
        if (!Objects.equals(this.mLabel, other.mLabel)) {
            return false;
        }
        if (!Objects.equals(this.mProbability, other.mProbability)) {
            return false;
        }
        if (!Objects.equals(this.mGuard, other.mGuard)) {
            return false;
        }
        if (this.mIdSequence != other.mIdSequence) {
            return false;
        }
        return true;
    }

    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }
    
}
