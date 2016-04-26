/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.hMSC;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCViewFactory;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.transition.SelfTransitionMSCViewImpl;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCViewImpl;
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
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModelingViewImpl extends AnchorPane implements StandardModelingView, StandardModeling.Listener{

    private StandardModeling mComponentBuild;
    private ContextMenu mBlockBuildContextMenu;
    private List<Listener> mListeners = new ArrayList<>();
    private List<HmscView> mBlockViews = new ArrayList<>();
    private List<TransitionMSCView> mTransitionViews = new ArrayList<>();
    private HmscViewFactory blockBuildFactory;
    private TransitionMSCViewFactory transitionBuildFactory;

    public StandardModelingViewImpl() {
        blockBuildFactory = new HmscViewFactory();
        transitionBuildFactory = new TransitionMSCViewFactory();
    }
    
    @Override
    public StandardModeling getComponentBuildDS() {
        return mComponentBuild;
    }

    @Override
    public void setComponentBuildDS(StandardModeling cbds) {
        if(mComponentBuild != null){
            mComponentBuild.removeListener(this);
        }
        clear();
        if(cbds == null){
            return;
        }
        mComponentBuild = cbds;
        mComponentBuild.addListener(this);
        for(Hmsc b : mComponentBuild.getBlocos()){
            showBlock(b);
        }
        for(TransitionMSC t : mComponentBuild.getTransitions()){
            showTransition(t);
        }
    }

    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    @Override
    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    @Override
    public HmscView locateBlockBuildView(Point2D point) {
        for(HmscView b : mBlockViews){
            if(b.isInsideBounds(point)){
                return b;
            }
        }
        return null;
    }

    @Override
    public TransitionMSCView locateTransitionBuildView(Circle c) {
        for(TransitionMSCView t : mTransitionViews){
            if(t.isInsideBounds_hMSC(c)){
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
    public void setBlockBuildContextMenu(ContextMenu menu) {
        mBlockBuildContextMenu = menu;
    }

    @Override
    public void saveAsPng(File arq) {
        WritableImage img = snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", arq);
        } catch (IOException ex) {
            Logger.getLogger(StandardModelingViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onChange(StandardModeling buildDS) {
        
    }

    @Override
    public void onBlockCreate(StandardModeling buildDS, Hmsc bbds) {
        showBlock(bbds);
    }

    @Override
    public void onBlockRemove(StandardModeling buildDS, Hmsc bbds) {
        hideBlock(bbds);
    }
    
    @Override
    public void onBlockCreateBMSC(StandardModeling sm, Hmsc hmsc, ComponentDS bmsc) {
        
    }

    @Override
    public void onTransitionCreate(StandardModeling buildDS, TransitionMSC t) {
        showTransition(t);
    }

    @Override
    public void onTransitionRemove(StandardModeling buildDS, TransitionMSC t) {
        hideTransition(t);
    }

    private void showBlock(Hmsc bbds) {
        HmscView view;
        Node node;
        synchronized (this){
            if (bbds.getValue("view") == null) {
                view = blockBuildFactory.create();
                mBlockViews.add(view);
                node = view.getNode();
                view.setHMSC(bbds);
                bbds.setValue("view", view);
                getChildren().add(node);
            }
        }
    }

    private void hideBlock(Hmsc bbds) {
        ObservableList<Node> aux = getChildren();
        HmscView view;
        Hmsc b = null;
        
        synchronized (this){
            view = (HmscView) bbds.getValue("view");
            if (view != null) {
                b = view.getHMSC();
            }
        }
        if (view == null) {
            return;
        }
        
        for(TransitionMSC t : b.getOutgoingTransitionsList()){
            hideTransition(t);
        }
        
        for(TransitionMSC t : b.getIncomingTransitionsList()){
            hideTransition(t);
        }
        
        synchronized (this) {
            aux.remove(view);
            bbds.setValue("view", null);
            view.setHMSC(null);
            mBlockViews.remove(view);
        }
    }

    private void showTransition(TransitionMSC t) {
        TransitionMSCView view;
        Node node;
        synchronized(this){
            if(t.getValue("view") == null){
                view = transitionBuildFactory.create(t);
                mTransitionViews.add(view);
                view.setTransitionMSC(t, mComponentBuild);
                t.setValue("view", view);
                node = view.getNode();
                getChildren().add(node);
                if (view instanceof SelfTransitionMSCViewImpl) {
                    ((SelfTransitionMSCViewImpl) view).gethMSCsourceView().getNode().toFront();
                } else {
                    node.toBack();
                }
                for (Listener l: mListeners) {
                    l.onTransitionBuildViewCreated(this, view);
                }
            }
        }
    }

    private void hideTransition(TransitionMSC t) {
        synchronized(this){
            TransitionMSCViewImpl view = (TransitionMSCViewImpl) t.getValue("view");
            if(view != null){
                mTransitionViews.remove(view);
                view.setTransitionMSC(null, null);
                t.setValue("view", null);
                getChildren().remove(view);
            }
        }
    }

    private void clear() {
        if (mComponentBuild == null) {
            return;
        }
        for (Hmsc b : mComponentBuild.getBlocos()) {
            hideBlock(b);
        }
        mComponentBuild = null;
    }

    @Override
    public void onComponentLTSCreate(Component c) {}
}
