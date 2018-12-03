/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.hMSC.hmsc_component;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockViewFactory;
import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeView;
import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeViewFactory;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCViewFactory;
import br.uece.lotus.msc.api.viewer.transition.SelfTransitionMSCViewImpl;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCViewImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscComponentViewImpl extends AnchorPane implements HmscComponentView, HmscComponent.Listener{

    private HmscComponent hmscComponent;
    private ContextMenu contextMenu;
    private List<Listener> listeners = new ArrayList<>();
    private List<HmscBlockView> hmscViewList = new ArrayList<>();
    private List<InterceptionNodeView> interceptionNodeViewList = new ArrayList<>();
    private List<TransitionMSCView> transitionMSCViewList = new ArrayList<>();
    private HmscBlockViewFactory hmscBlockViewFactory;
    private InterceptionNodeViewFactory interceptionNodeViewFactory;
    private TransitionMSCViewFactory transitionMSCViewFactory;


    public HmscComponentViewImpl() {
        hmscBlockViewFactory = new HmscBlockViewFactory();
        interceptionNodeViewFactory = new InterceptionNodeViewFactory();
        transitionMSCViewFactory = new TransitionMSCViewFactory();
    }
    
    @Override
    public HmscComponent getHmscComponent() {
        return hmscComponent;
    }

    @Override
    public void setComponentBuildDS(HmscComponent hmscComponent) {
        if(this.hmscComponent != null){
            this.hmscComponent.removeListener(this);
        }
        clear();
        if(hmscComponent == null){
            return;
        }
        this.hmscComponent = hmscComponent;
        this.hmscComponent.addListener(this);

        for(HmscBlock b : this.hmscComponent.getHmscBlockList()){
            showHmsc(b);
        }

        for(InterceptionNode interceptionNode : this.getHmscComponent().getInterceptionNodeList()){
            showInterceptionNode(interceptionNode);
        }

        for(TransitionMSC t : this.hmscComponent.getTransitionMSCList()){
            showTransition(t);
        }
    }



    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public GenericElementView locateGenericElementView(Point2D point2D) {
        for(HmscBlockView b : hmscViewList){
            if(b.isInsideBounds(point2D)){
                return b;
            }
        }

        for(InterceptionNodeView interceptionNodeView : interceptionNodeViewList){
            if(interceptionNodeView.isInsideBounds(point2D)){
                return interceptionNodeView;
            }
        }
        return null;
    }

    @Override
    public TransitionMSCView locateTransitionBuildView(Point2D point2D) {
        for(TransitionMSCView t : transitionMSCViewList){
            if(t.isInsideBoundshMSC(point2D)){
                return t;
            } 
        }
        return null;
    }

    @Override
    public AnchorPane getNode() {
        return this;
    }

    @Override
    public void setBlockBuildContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    @Override
    public void saveAsPng(File arq) {
        WritableImage img = snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", arq);
        } catch (IOException ex) {
            Logger.getLogger(HmscComponentViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onChange(HmscComponent hmscComponent) {
        
    }

    @Override
    public void onCreateGenericElement(HmscComponent hmscComponent, GenericElement genericElement) {
        if(genericElement instanceof HmscBlock){

            showHmsc((HmscBlock) genericElement);

        }else if(genericElement instanceof InterceptionNode){

            showInterceptionNode((InterceptionNode) genericElement);
        }

    }

    @Override
    public void onRemoveGenericElement(HmscComponent hmscComponent, GenericElement genericElement) {
        if(genericElement instanceof HmscBlock){

            hideHmsc((HmscBlock) genericElement);

        }else if(genericElement instanceof InterceptionNode){

            hideInterceptionNode((InterceptionNode) genericElement);
        }

    }


    @Override
    public void onCreateBmscBlock(HmscComponent hmscComponent, HmscBlock hmscHmscBlock, BmscComponent bmsc) {
        
    }

    @Override
    public void onCreateTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC) {
        showTransition(transitionMSC);
    }

    @Override
    public void onRemoveTransitionMSC(HmscComponent hmscComponent, TransitionMSC transitionMSC) {
        hideTransition(transitionMSC);
    }

    private void showHmsc(HmscBlock hmscHmscBlock) {
        HmscBlockView view;
        Node node;
        synchronized (this){
            if (hmscHmscBlock.getValue("view") == null) {
                view = hmscBlockViewFactory.create();
                hmscViewList.add(view);
                node = view.getNode();
                view.setHMSC(hmscHmscBlock);
                hmscHmscBlock.putValue("view", view);
                getChildren().add(node);
            }
        }
    }

    private void showInterceptionNode(InterceptionNode interceptionNode) {
        InterceptionNodeView view;
        Node node;

        synchronized (this){
            if(interceptionNode.getValue("view") == null){
                view = interceptionNodeViewFactory.create();
                interceptionNodeViewList.add(view);
                node = view.getNode();
                view.setInterceptionNode(interceptionNode);
                interceptionNode.putValue("view", view);
                getChildren().add(node);
            }
        }
    }



    private void hideHmsc(HmscBlock hmscHmscBlock) {
        ObservableList<Node> aux = getChildren();
        HmscBlockView view;
        HmscBlock hmscHmscBlock1 = null;
        
        synchronized (this){
            view = (HmscBlockView) hmscHmscBlock.getValue("view");
            if (view != null) {
                hmscHmscBlock1 = view.getHMSC();
            }
        }
        if (view == null) {
            return;
        }
        
        for(TransitionMSC t : hmscHmscBlock1.getOutgoingTransitionList()){
            hideTransition(t);
        }
        
        for(TransitionMSC t : hmscHmscBlock1.getIncomingTransitionList()){
            hideTransition(t);
        }
        
        synchronized (this) {
            aux.remove(view);
            hmscHmscBlock.putValue("view", null);
            view.setHMSC(null);
            hmscViewList.remove(view);
        }
    }

    private void hideInterceptionNode(InterceptionNode interceptionNode) {
        ObservableList<Node> aux = getChildren();
        InterceptionNodeView view;
        InterceptionNode interceptionNode1 = null;

        synchronized (this){
            view = (InterceptionNodeView) interceptionNode.getValue("view");

            if(view !=null){
                interceptionNode1 = view.getInterceptionNode();
            }

        }

        if(view == null){
            return;
        }


        for(TransitionMSC t : interceptionNode1.getOutgoingTransitionList()){
            hideTransition(t);
        }

        for(TransitionMSC t : interceptionNode1.getIncomingTransitionList()){
            hideTransition(t);
        }

        synchronized (this) {
            aux.remove(view);
            interceptionNode.putValue("view", null);
            view.setInterceptionNode(null);
            interceptionNodeViewList.remove(view);
        }


    }

    private void showTransition(TransitionMSC t) {
        TransitionMSCView view;
        Node node;
        synchronized(this){
            if(t.getValue("view") == null){
                view = transitionMSCViewFactory.create(t);
                transitionMSCViewList.add(view);
                view.setTransitionMSC(t, hmscComponent);
                t.putValue("view", view);
                node = view.getNode();
                getChildren().add(node);
                if (view instanceof SelfTransitionMSCViewImpl) {
                    try {
                        ((SelfTransitionMSCViewImpl) view).getSourceGenericElementView().getNode().toFront();
                    }catch(NullPointerException e){
                        ((SelfTransitionMSCViewImpl) view).getSourceGenericElement().getNode().toFront();
                    }
                } else {
                    node.toBack();
                }
                for (Listener l: listeners) {
                    l.onCreatedTransitionMSCView(this, view);
                }
            }
        }
    }

    private void hideTransition(TransitionMSC t) {
        synchronized(this){
            TransitionMSCViewImpl view = (TransitionMSCViewImpl) t.getValue("view");
            if(view != null){
                transitionMSCViewList.remove(view);
                view.setTransitionMSC(null, null);
                t.putValue("view", null);
                getChildren().remove(view);
            }
        }
    }

    private void clear() {
        if (hmscComponent == null) {
            return;
        }
        for (HmscBlock hmscHmscBlock : hmscComponent.getHmscBlockList()) {
            hideHmsc(hmscHmscBlock);
        }

        for(InterceptionNode interceptionNode : hmscComponent.getInterceptionNodeList()){
            hideInterceptionNode(interceptionNode);
        }

        hmscComponent = null;


    }

    @Override
    public void onCreateComponentLTS(Component component) {}

    @Override
    public void onCreateGeneralComponentLTS(Component component) {}
}
