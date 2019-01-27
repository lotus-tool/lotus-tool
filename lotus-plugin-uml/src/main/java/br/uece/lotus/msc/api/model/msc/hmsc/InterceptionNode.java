package br.uece.lotus.msc.api.model.msc.hmsc;

import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeViewImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptionNode implements GenericElement {
    private int ID;
    private  List<TransitionMSC> outgoingTransitionList = new ArrayList<>();
    private  List<TransitionMSC> incomingTransitionList = new ArrayList<>();
    private final HmscComponent hmscComponent;
    private Map<String, Object> valueMap = new HashMap<>();
    private final List<InterceptionNode.Listener> listeners = new ArrayList<>();
    private double layoutX;
    private double layoutY;
    private String color = "black";
    private Integer borderWidth = 1;
    private String textColor;
    private String textStyle;

    public InterceptionNode(HmscComponent hmscComponent) {
        this.hmscComponent = hmscComponent;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;

        customNotify();
    }

    public void setColor(String color) {
        this.color = color;

        customNotify();
    }

    public String getColor() {
        return color;
    }

    public Integer getBorderWidth() {
        return borderWidth;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
        customNotify();
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
        customNotify();
    }

    public interface Listener{
        void onChange(InterceptionNode interceptionNode);
    }

    public  void removeListener(InterceptionNodeViewImpl interceptionNodeView) {
        listeners.remove(interceptionNodeView);
    }

    public void addListener(InterceptionNodeViewImpl interceptionNodeView) {
        listeners.add(interceptionNodeView);
    }


    private void customNotify() {
        for(InterceptionNode.Listener l : listeners){
            l.onChange(this);
        }
    }


    public List<TransitionMSC> getOutgoingTransitionList() {
        return outgoingTransitionList;
    }

    @Override
    public void removeOutgoingTransition(TransitionMSC t) {
        outgoingTransitionList.remove(t);
    }

    @Override
    public void removeIncomingTransition(TransitionMSC t) {
        incomingTransitionList.remove(t);
    }

    @Override
    public int getID() {
        return this.ID;
    }



    public List<TransitionMSC> getIncomingTransitionList() {
        return incomingTransitionList;
    }

    public HmscComponent getHmscComponent() {
        return hmscComponent;
    }

    public List<InterceptionNode.Listener> getListeners() {
        return listeners;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    @Override
    public void addOutgoingTransition(TransitionMSC t) {
        outgoingTransitionList.add(t);
    }

    @Override
    public void addIncomingTransition(TransitionMSC t) {
        incomingTransitionList.add(t);
    }

    @Override
    public TransitionMSC getTransitionTo(GenericElement genericElement){
        for(TransitionMSC t : outgoingTransitionList){
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
        for(TransitionMSC t : outgoingTransitionList){
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


    public InterceptionNode setID(int ID) {
        this.ID = ID;

        customNotify();

        return this;
    }


    public InterceptionNode addTransitionGoingOut(TransitionMSC transitionGoingOut) {
        this.outgoingTransitionList.add(transitionGoingOut);

        customNotify();

        return this;
    }

    public InterceptionNode setOutgoingTransitionList(List<TransitionMSC> outgoingTransitionList) {
        if(outgoingTransitionList == null){
            this.outgoingTransitionList.clear();
        }else {
            this.outgoingTransitionList.addAll(outgoingTransitionList);
        }

        customNotify();

        return this;

    }

    public InterceptionNode addTransitionGoingIn(TransitionMSC transitionGoingIn) {

        this.incomingTransitionList.add(transitionGoingIn);

        customNotify();

        return this;
    }

    public InterceptionNode setIncomingTransitionList(List<TransitionMSC> incomingTransitionList) {
        if(this.incomingTransitionList == null){
            this.incomingTransitionList.clear();
        }else {
            this.incomingTransitionList.addAll(incomingTransitionList);
        }

        customNotify();

        return this;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;

        customNotify();


    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;

        customNotify();

    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public Object getValue(String key){
        return valueMap.get(key);
    }

    public void putValue(String key,Object object){
         valueMap.put(key, object);
    }

    @Override
    public String getLabel() {
        return this.getClass().getSimpleName().concat("{"+String.valueOf(getID())+"}");
    }
}
