/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.model.msc.hmsc;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscComponent {
    
    private final Map<String, Object> values = new HashMap<>();
    private String name;
    private List<HmscBlock> hmscBlockList = new ArrayList<>();
    private List<InterceptionNode> interceptionNodeList = new ArrayList<>();
    private List<TransitionMSC> transitionMSCList = new ArrayList<>();
    private List<GenericElement> genericElementList = new ArrayList<>();

//    private int countHmscBlock = 0;
//    private int countIntercptionNode = 0;
//    private int countGenericElement = 0;

    private final List<Listener> listeners = new ArrayList<>();
    private int id;
    private HmscBlock initialHmscBlock = null;
    private int probscount = 0;





    public interface Listener{
        void onChange(HmscComponent hmscComponent);
//
//        void onCreateHmscBlock(HmscComponent hmscComponent, HmscBlock hmscHmscBlock);
//        void onRemoveHmscBlock(HmscComponent hmscComponent, HmscBlock hmscHmscBlock);
//
//        void onCreateInterceptionNode(HmscComponent hmscComponent, InterceptionNode interceptionNode);
//        void onRemoveInterceptionNode(HmscComponent hmscComponent, InterceptionNode interceptionNode);


        void onCreateGenericElement(HmscComponent hmscComponent, GenericElement genericElement);

        void onRemoveGenericElement(HmscComponent hmscComponent, GenericElement genericElement);

        void onCreateBmscBlock(HmscComponent hmscComponent, HmscBlock hmscHmscBlock, BmscComponent bmsc);

        void onCreateComponentLTS(Component component);
        void onCreateGeneralComponentLTS(Component component);

        void onCreateTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC);
        void onRemoveTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC);


    }

    public List<GenericElement> getGenericElementList() {
        return genericElementList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        HmscComponent hmscComponent = new HmscComponent();
        hmscComponent.name = name;

        for (HmscBlock olgHmscHmscBlock : hmscBlockList) {
            HmscBlock newHmscHmscBlock = hmscComponent.newHmsc(olgHmscHmscBlock.getID());
            copyHmsc(olgHmscHmscBlock, newHmscHmscBlock);
        }

        for (InterceptionNode olgInterceptionNode : interceptionNodeList) {
            InterceptionNode newInterceptionNode = hmscComponent.newInterceptionNode(olgInterceptionNode.getID());
            copyInterceptionNode(olgInterceptionNode, newInterceptionNode);
        }

        for (TransitionMSC oldTransition : transitionMSCList) {
            int src = ((HmscBlock)oldTransition.getSource()).getID();
            int dst = ((HmscBlock)oldTransition.getDestiny()).getID();
            TransitionMSC newTransition = hmscComponent.buildTransitionMSC(getHmscByID(src), getHmscByID(dst)).create();
            copyTransition(oldTransition, newTransition);
        }

        if (initialHmscBlock != null) {
            //c.getStateByID(mInitialState.getID())
            hmscComponent.setInitialHmscBlock(hmscComponent.getHmscByID(initialHmscBlock.getID()));
        }


        return hmscComponent;

    }


    private void copyHmsc(HmscBlock from, HmscBlock to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
        to.setLayoutY(from.getLayoutY());

        if (from.isInitial()) {
            to.setInitial(true);
        }
    }


    private void copyInterceptionNode(InterceptionNode olgInterceptionNode,
                                      InterceptionNode newInterceptionNode) {
        newInterceptionNode.setLayoutX(olgInterceptionNode.getLayoutX());
        newInterceptionNode.setLayoutY(olgInterceptionNode.getLayoutY());

    }

    private void copyTransition(TransitionMSC from, TransitionMSC to) {
        to.setLabel(from.getLabel());
        to.setProbability(from.getProbability());
        to.setGuard(from.getGuard());
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public HmscBlock getInitialHmscBlock(){ return this.initialHmscBlock;}

    public void setInitialHmscBlock(HmscBlock hmscHmscBlock){
        this.initialHmscBlock.setInitial(false);
        this.initialHmscBlock = hmscHmscBlock;
        this.initialHmscBlock.setInitial(true);
    }

    public int getProb(){
        return this.probscount;
    }

    public void setProb(int i){
        this.probscount = i;
    }

    public HmscBlock newHmsc(int id){
        HmscBlock hmscHmscBlock = new HmscBlock(this);
        hmscHmscBlock.setID(id);
        add(hmscHmscBlock);
        return hmscHmscBlock;
    }

    public InterceptionNode newInterceptionNode(int id) {
        InterceptionNode interceptionNode = new InterceptionNode(this);
        interceptionNode.setID(id);
        add(interceptionNode);
        return interceptionNode;
    }

    private void add(GenericElement genericElement) {
        genericElementList.add(genericElement);

        if(genericElement instanceof HmscBlock){

          hmscBlockList.add((HmscBlock) genericElement);

        }else if(genericElement instanceof InterceptionNode){
            interceptionNodeList.add((InterceptionNode) genericElement);
        }

        for(Listener l : listeners){
            l.onCreateGenericElement(this, genericElement);
        }
    }

    public void setBmscIntoHmsc(HmscBlock hmscHmscBlock, BmscComponent bmscComponent){
        hmscHmscBlock.setDiagramSequence(bmscComponent);
        for(Listener l : listeners){
            l.onCreateBmscBlock(this, hmscHmscBlock, bmscComponent);
        }
    }
    
    public void createListLTS(List<Component> c){
        for(Component cp : c){
            for(Listener l : listeners){
                l.onCreateComponentLTS(cp);
            }
        }
    }
    
    public void createGeneralLTS(Component c){
        for(Listener l : listeners){
            l.onCreateGeneralComponentLTS(c);
        }
    }


    public TransitionMSC newTransitionMSC(GenericElementView src, GenericElementView dst){
        if (src == null) {
            throw new IllegalArgumentException("src hMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst hMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        add(t);
        return t;
    }
    
    public TransitionMSC.Builder buildTransitionMSC(GenericElementView src, GenericElementView dst){
        if (src == null) {
            throw new IllegalArgumentException("src hMSCView can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst hMSCView can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        return new TransitionMSC.Builder(this, t);
    }
    
    public TransitionMSC.Builder buildTransitionMSC(GenericElement src, GenericElement dst){
        if (src == null) {
            throw new IllegalArgumentException("src hMSC can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst hMSC can't be null!");
        }
        TransitionMSC t = new TransitionMSC(src, dst);
        return new TransitionMSC.Builder(this, t);
    }
    
    public void add(TransitionMSC t){
        GenericElement src = ((GenericElementView) t.getSource()).getGenericElement();
        GenericElement dst = ((GenericElementView) t.getDestiny()).getGenericElement();
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        transitionMSCList.add(t);
        for(Listener l : listeners){
            l.onCreateTransitionMSC(this, t);
        }
    }
    
    public void add2(TransitionMSC t){
        GenericElement src = ((GenericElement) t.getSource());
        GenericElement dst = ((GenericElement) t.getDestiny());
        src.addOutgoingTransition(t);
        dst.addIncomingTransition(t);
        transitionMSCList.add(t);
        for(Listener l : listeners){
            l.onCreateTransitionMSC(this, t);
        }
    }
    
    public void remove(GenericElement genericElement){
        removeAllTransitonFrom(genericElement);

        genericElementList.remove(genericElement);

        if(genericElement instanceof HmscBlock){

            hmscBlockList.remove(genericElement);

        }else if(genericElement instanceof InterceptionNode){

            interceptionNodeList.remove(genericElement);
        }
        for(Listener l : listeners){
            l.onRemoveGenericElement(this, genericElement);
        }
    }

    private void removeAllTransitonFrom(GenericElement genericElement){

        List<TransitionMSC> transition = new ArrayList<>();

            transition.addAll(genericElement.getIncomingTransitionList());
            transition.addAll(genericElement.getOutgoingTransitionList());

        for(TransitionMSC t : transition){
            remove(t);
        }
    }

    
    public void remove(TransitionMSC t){
        if(t.getSource() instanceof GenericElement){
            ((GenericElement) t.getSource()).removeOutgoingTransition(t);
        }else {
            ((GenericElementView) t.getSource()).getGenericElement().removeOutgoingTransition(t);
        }
        if(t.getDestiny() instanceof GenericElement){
            ((GenericElement) t.getDestiny()).removeIncomingTransition(t);
        }else {
            ((GenericElementView) t.getDestiny()).getGenericElement().removeIncomingTransition(t);
        }
        transitionMSCList.remove(t);
        for(Listener l : listeners){
            l.onRemoveTransitionMSC(this, t);
        }
    }
    
    public HmscBlock getHmscByID(int id) {
        for(HmscBlock h : hmscBlockList){
            if(id == h.getID()){
                return h;
            }
        }
        return null;
    }

    public GenericElement getGenericElementByID(int id) {
        for(GenericElement genericElement  : getGenericElementList()){
            if(id == genericElement.getID()){
                return genericElement;
            }
        }
        return null;
    }

    public GenericElement getInterceptionNodeByID(int id) {
        for(InterceptionNode interceptionNode  : getInterceptionNodeList()){
            if(id == interceptionNode.getID()){
                return interceptionNode;
            }
        }
        return null;
    }

    public TransitionMSC getTransitionMSC(Integer srcState, Integer dstState){
        for(TransitionMSC transitionMSC : getTransitionMSCList()){
            if(((GenericElement)transitionMSC.getSource()).getID() == srcState && ((GenericElement)transitionMSC.getDestiny()).getID() == dstState){
                return transitionMSC;
            }
        }
        return null;
    }

    public HmscBlock getHmscByLabel(String label) {
        for(HmscBlock hmscHmscBlock : getHmscBlockList()){
            if( hmscHmscBlock.getLabel().equals(label)){
                return hmscHmscBlock;
            }
        }
        return null;
    }
    
    public Object getValue(String key) {
        return values.get(key);
    }

    public void setValue(String key, Object value) {
        values.put(key, value);
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public List<HmscBlock> getHmscBlockList() {
        return hmscBlockList;
    }

    public void setHmscBlockList(List<HmscBlock> hmscBlockList) {
        this.hmscBlockList = hmscBlockList;
    }

    public List<InterceptionNode> getInterceptionNodeList() {
        return interceptionNodeList;
    }

    public void setInterceptionNodeList(List<InterceptionNode> interceptionNodeList) {
        this.interceptionNodeList = interceptionNodeList;
    }

//    public int getCountHmscBlock() {
//        return hmscBlockList.size();
//    }
//
//    public void setCountHmscBlock(int countHmscBlock) {
//        this.countHmscBlock = countHmscBlock;
//    }
//
//    public int getCountIntercptionNode() {
//        return interceptionNodeList.size();
//    }
//
//    public void setCountIntercptionNode(int countIntercptionNode) {
//        this.countIntercptionNode = countIntercptionNode;
//    }
//
//    public int getCountGenericElement() {
//        return genericElementList.size();
//    }
//
//    public void setCountGenericElement(int countGenericElement) {
//        this.countGenericElement = countGenericElement;
//    }

    public List<TransitionMSC> getTransitionMSCList() {
        return transitionMSCList;
    }

    public void setTransitionMSCList(List<TransitionMSC> mTransitions) {
        this.transitionMSCList = mTransitions;
    }
    
    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.values);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.hmscBlockList);
        hash = 97 * hash + Objects.hashCode(this.interceptionNodeList);
        hash = 97 * hash + Objects.hashCode(this.transitionMSCList);
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
        final HmscComponent other = (HmscComponent) obj;
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.hmscBlockList, other.hmscBlockList)) {
            return false;
        }

        if (!Objects.equals(this.interceptionNodeList, other.interceptionNodeList)) {
            return false;
        }

        if (!Objects.equals(this.transitionMSCList, other.transitionMSCList)) {
            return false;
        }
        return true;
    }
    
}
