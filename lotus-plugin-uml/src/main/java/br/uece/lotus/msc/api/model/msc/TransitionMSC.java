/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.model.msc;

import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class TransitionMSC  {


    public static class Builder {
        private  HmscComponent mComponentBuild;
        private  TransitionMSC mTransitionMSC;
        private  BmscComponent mBmscComponent;

        public Builder(HmscComponent mComponentBuild, TransitionMSC mTransitionMSC) {
            this.mComponentBuild = mComponentBuild;
            this.mTransitionMSC = mTransitionMSC;
        }

        public Builder(BmscComponent mBmscComponent, TransitionMSC mTransitionMSC) {
            this.mBmscComponent = mBmscComponent;
            this.mTransitionMSC = mTransitionMSC;
        }

        public Builder setValue(String s, Object o) {
            mTransitionMSC.putValue(s, o);
            return this;
        }

        public TransitionMSC create() {
            mComponentBuild.add(mTransitionMSC);
            return mTransitionMSC;
        }

        public TransitionMSC createForXmlHMSC() {
            mComponentBuild.add2(mTransitionMSC);
            return mTransitionMSC;
        }

        public TransitionMSC createForXmlBMSC() {
            mBmscComponent.add2(mTransitionMSC);
            return mTransitionMSC;
        }

        public TransitionMSC createDS() {
            mBmscComponent.add(mTransitionMSC);
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

        public Builder addAction(String action){
            mTransitionMSC.addAction(action);
            return this;
        }

        public Builder setActions(List<String> actions){
            mTransitionMSC.setActions(actions);
            return this;
        }

        public Builder addParameter(String parameter){
            mTransitionMSC.addParameter(parameter);
            return this;
        }

        public Builder setParameters(List<String> parameters){
            mTransitionMSC.setParameters(parameters);
            return this;
        }


        public Builder setGuard(String guard){
            mTransitionMSC.setGuard(guard);
            return this;
        }

        public Builder setViewType(int type) {
            mTransitionMSC.putValue("view.type", type);
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
    private int borderWidth = 1;
    private String mTextColor = "black";
    private String mTextStyle = TEXTSTYLE_NORMAL;
    private int mTextSize = 13;
    //Transition
    private String mLabel;
    private List<String>mParamList = new ArrayList<>();
    private List<String>mActionList = new ArrayList<>();
    private Double mProbability;
    private String mGuard;
    private Integer mIdSequence;

    public TransitionMSC(Object mSource, Object mDestiny) {
        this.mSource = mSource;
        this.mDestiny = mDestiny;
    }

    public TransitionMSC addParameter(String param){
        this.mParamList.add(param);

        for(Listener l : mListeners){
            l.onChange(this);
        }

        return this;
    }

    public TransitionMSC setParameters(List<String> parameters) {

            mParamList.clear();

            if(parameters != null){

                mParamList.addAll(new ArrayList<>(parameters));

                for(Listener l : mListeners){
                    l.onChange(this);
                }
            }



        return this;

    }

    public TransitionMSC clearParameters(){
        this.mParamList.clear();

        for(Listener l : mListeners){
            l.onChange(this);
        }

        return this;

    }

    public List<String> getParameters() {
        return mParamList;
    }


    public TransitionMSC addAction(String action){
       this.mActionList.add(action);

        for(Listener l : mListeners){
            l.onChange(this);
        }

        return this;
    }

    public TransitionMSC setActions(List<String> actions){

        mActionList.clear();

        if(actions != null){
            mActionList.addAll(new ArrayList<>(actions));

            for(Listener l : mListeners){
                l.onChange(this);
            }
        }



        return this;
    }

    public TransitionMSC clearActions() {

        mActionList.clear();

        for(Listener l : mListeners){
            l.onChange(this);
        }

        return this;
    }


    public List<String> getActions(){
        return mActionList;
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

    public int getborderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int mWidth) {
        this.borderWidth = mWidth;
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

    public TransitionMSC setProbability(Double mProbability) {
        this.mProbability = mProbability;
        for(Listener l : mListeners){
            l.onChange(this);
        }
        return this;
    }


    public String getLabel() {
        return mLabel;
    }

    public TransitionMSC setLabel(String mLabel) {
        this.mLabel = mLabel;
        for(Listener l : mListeners){
            l.onChange(this);
        }
        return this;
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

        if (!Objects.equals(this.mParamList, other.mParamList)) {
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

    public void putValue(String key, Object value) {
        mValues.put(key, value);
    }

}