/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class TransitionBuildDS {
    
    public static class Builder {
        private final ComponentBuildDS mComponentBuild;
        private final TransitionBuildDS mTransitionBuild;

       public Builder(ComponentBuildDS mComponentBuild, TransitionBuildDS mTransitionBuild) {
           this.mComponentBuild = mComponentBuild;
           this.mTransitionBuild = mTransitionBuild;
       }

       public Builder setValue(String s, Object o) {
           mTransitionBuild.setValue(s, o);
           return this;
       }

       public TransitionBuildDS create() {
           mComponentBuild.add(mTransitionBuild);
           return mTransitionBuild;
       }

       public Builder setLabel(String label) {
           mTransitionBuild.setLabel(label);
           return this;
       }

       public Builder setProbability(Double probability) {
           mTransitionBuild.setProbability(probability);
           return this;            
       }

       public Builder setViewType(int type) {
           mTransitionBuild.setValue("view.type", type);
           return this;
       }
    }
    
    public interface Listener {

        void onChange(TransitionBuildDS transitionBuildDS);
    }
    
    public static final String TEXTSTYLE_NORMAL = "normal";
    public static final String TEXTSTYLE_BOLD = "bold";
    //Grafica
    private BlockBuildDS mSource;
    private BlockBuildDS mDestiny;
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

    public TransitionBuildDS(BlockBuildDS mSource, BlockBuildDS mDestiny) {
        this.mSource = mSource;
        this.mDestiny = mDestiny;
    }

    public BlockBuildDS getSource() {
        return mSource;
    }

    public BlockBuildDS getDestiny() {
        return mDestiny;
    }

    public String getColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
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
        final TransitionBuildDS other = (TransitionBuildDS) obj;
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
