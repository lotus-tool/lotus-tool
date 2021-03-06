package br.uece.lotus.uml.api.ds;

import br.uece.lotus.Transition;

import java.util.*;

import static br.uece.lotus.State.TEXTSTYLE_BOLD;
import static br.uece.lotus.State.TEXTSTYLE_NORMAL;

/**
 * Classe responsavel por gerênciar o gráfico dos Objetos no BMSC
 * @author Created by lva on 11/12/15.
 */
public class BlockDS {
    /**
     * O {@link BlockDS#mTextStyleBold} é alguma coisa
     */
    public final static String mTextStyleBold = TEXTSTYLE_BOLD;
    public final static String mTextStyleNormal = TEXTSTYLE_NORMAL;

    private final List<TransitionMSC> mTransicoesSaida = new ArrayList<>();
    private final List<TransitionMSC> mTransicoesEntrada = new ArrayList<>();
    private final Map<String, Object> mValues = new HashMap<>();
    private double mLayoutX;
    private double mLayoutY;
    private final List<Listener> mListeners = new ArrayList<>();
    private final ComponentDS componentDS;
    private int mID;
    private String mColor;
    private String mBorderColor;
    private Integer mBorderWidth;
    private String mTextColor;
    private Integer mTextSize;
    private String mTextStyle;
    private String mLabel;
    private int mVisitedBlockDSCount;


    BlockDS(ComponentDS c) {
        componentDS =c;}

    /**
     * Retorna o getComponentDS
     * @return
     */
    public ComponentDS getComponentDS(){return this.componentDS;}

    public void setID(int id){mID =id;}

    public int getID(){return mID;}

    public double getLayoutX() {
        return mLayoutX;
    }

    public void setLayoutX(double layoutX) {
        this.mLayoutX = layoutX;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public void setLayoutY(double layoutY) {
        this.mLayoutY = layoutY;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getColor(){return mColor;}

    public void setColor(String color){
        mColor =color;
        for(Listener l: mListeners){
            l.onChange(this);
        }
    }

    public String getBorderColor(){
        return mBorderColor;
    }

    public void setmBorderColor(String color){
        mBorderColor=color;
        for (Listener l: mListeners){
            l.onChange(this);
        }
    }

    public Integer getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(Integer width) {
        mBorderWidth = width;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }


    public double getLayoutY() {
        return this.mLayoutY;
    }

    public void setBorderColor(String mBorderColor) {
        this.mBorderColor = mBorderColor;
        for(Listener l : mListeners){
            l.onChange(this);
        }
    }

    public void setTextStyle(String mTextStyle) {
        this.mTextStyle = mTextStyle;
        for(Listener l : mListeners){
            l.onChange(this);
        }

    }

    public interface Listener{
        void onChange( BlockDS diagrama);
    }

    public String getTextColor() {
        return mTextColor;
    }

    public void setTextColor(String color) {
        mTextColor = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public Integer getTextSize() {
        return mTextSize;
    }

    public void setTextSize(Integer size) {
        mTextSize = size;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getTextStyle() {
        return mTextStyle;
    }

    public void setTextSyle(String color) {
        mTextStyle = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }
    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public TransitionMSC getTransitionTo(BlockDS ds) {
        for (TransitionMSC t : mTransicoesSaida) {
            if (t.getDestiny().equals(ds)) {
                return t;
            }
        }
        return null;
    }

    public List<TransitionMSC> getTransitionsTo(BlockDS ds) {
        List<TransitionMSC> r = new ArrayList<>();
        for (TransitionMSC t : mTransicoesSaida) {
            if (t.getDestiny().equals(ds)) {
                r.add(t);
            }
        }
        return r;
    }

    public TransitionMSC getTransitionByLabel(String label) {
        for (TransitionMSC t : mTransicoesSaida) {
            if (t.getLabel().equals(label)) {
                return t;
            }
        }
        return null;
    }

    void copy(BlockDS ds) {
        mID = ds.mID;
        mBorderColor = ds.mBorderColor;
        mBorderWidth = ds.mBorderWidth;
        mColor = ds.mColor;
        mLabel = ds.mLabel;
        mLayoutX = ds.mLayoutX;
//        mLayoutY = ds.mLayoutY;
        mTextColor = ds.mTextColor;
        mTextSize = ds.mTextSize;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.mID;
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
        final BlockDS other = (BlockDS) obj;
        if (this.mID != other.mID) {
            return false;
        }
        return true;
    }
    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

    public int getmVisitedBlockDSCount() { return mVisitedBlockDSCount; }

    public void setmVisitedBlockDSCount(int value) { mVisitedBlockDSCount = value; }

    public void incrementStatesCount() { mVisitedBlockDSCount++; }

    public void decrementStatesCount() { mVisitedBlockDSCount--; }

   /* public Iterable<Transition> getIncomingTransitions() {
        return mTransicoesEntrada;
    }

    public Iterable<Transition> getOutgoingTransitions() {
        return mTransicoesSaida;
    }

    public List<Transition> getIncomingTransitionsList() {
        return Collections.unmodifiableList(mTransicoesEntrada);
    }

    public List<Transition> getOutgoingTransitionsList() {
        return Collections.unmodifiableList(mTransicoesSaida);
    }*/






    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    void addIncomingTransition(TransitionMSC t) {
        mTransicoesEntrada.add(t);
    }

    void addOutgoingTransition(TransitionMSC t) {
        mTransicoesSaida.add(t);
    }

    void removeIncomingTransition(TransitionMSC transition) {
        mTransicoesEntrada.remove(transition);
    }

    void removeOutgoingTransition(TransitionMSC transition) {
        mTransicoesSaida.remove(transition);
    }

    public int getOutgoingTransitionsCount() {
        return mTransicoesSaida.size();
    }

    public int getIncomingTransitionsCount() {
        return mTransicoesEntrada.size();
    }
    public List<TransitionMSC> getOutgoingTransitionsList(){
        return Collections.unmodifiableList(mTransicoesSaida);
    }

    public List<TransitionMSC> getIncomingTransitionsList(){
        return Collections.unmodifiableList(mTransicoesEntrada);
    }
}

