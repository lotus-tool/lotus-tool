/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.bMSC;
import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.api.viewer.transition.SelfTransitionMSCViewImpl;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCViewFactory;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCViewImpl;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Line;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentDSViewImpl extends AnchorPane implements ComponentDSView, ComponentDS.Listener {
    private ComponentDS mComponentDS;
    private ContextMenu mDSContextMenu;
    private BlockDSViewFactory mBlockDSViewFactory;
    private List<ComponentDSView.Listener> mListeners = new ArrayList<>();
    private List<BlockDSView> mBlocksDSView = new ArrayList<>();
    private TransitionMSCViewFactory transitionFactory;
    private List<TransitionMSCView> mTransitionViews = new ArrayList<>();



    /*private List<TransitionView> mTransitionViews = new ArrayList<>();*/
    public ComponentDSViewImpl(){
        transitionFactory = new TransitionMSCViewFactory();
        mBlockDSViewFactory = new BlockDSViewFactory();
    }

    @Override
    public void setBlockDSContextMenu(ContextMenu menu) {
        mDSContextMenu = menu;
    }


    @Override
    public ComponentDS getmComponentDS() {
        return mComponentDS;
    }

    @Override
    public void setComponentDS(ComponentDS componentDS) {
        if (mComponentDS != null) {
            mComponentDS.removeListener(this);
        }
        clear();
        if (componentDS == null) {
            return;
        }
        mComponentDS = componentDS;
        mComponentDS.addListener(this);
        for (BlockDS blockDS : mComponentDS.getBlockDS()) {
            showBlockDS(blockDS);
        }
        for (TransitionMSC tm : mComponentDS.getAllTransitions()) {
            showTransition(tm);
        }
        setYTransitions();
    }



    private void showBlockDS(BlockDS blockDS) {
        BlockDSView view;
        Node node;
        synchronized (this){
            if(blockDS.getValue("view")==null){
                view = mBlockDSViewFactory.create();
                mBlocksDSView.add(view);
                node= view.getNode();
                view.setBlockDS(blockDS);
                blockDS.setValue("view",view);
                getChildren().add(node);
            }
        }
    }

    private void clear() {
        if (mComponentDS == null) {
            return;
        }
        for (BlockDS ds : mComponentDS.getBlockDS()) {
            hideBlockDS(ds);
        }
        mComponentDS = null;
    }

    private void hideBlockDS(BlockDS blockDS) {
        ObservableList<Node> aux = getChildren();
        BlockDSView view;
        BlockDS ds = null;

        synchronized (this) {
            view = (BlockDSView) blockDS.getValue("view");
            if (view != null) {
                ds = view.getBlockDS();
            }
        }
        if (view == null) {
            return;
        }
        for (TransitionMSC t : ds.getOutgoingTransitionsList()) {
            hideTransition(t);
        }
        for (TransitionMSC t : ds.getIncomingTransitionsList()) {
            hideTransition(t);
        }
        synchronized (this) {
            aux.remove(view);
            blockDS.setValue("view", null);
            view.setBlockDS(null);
            mBlocksDSView.remove(view);
        }
    }

    @Override
    public void addListener(Listener l) {mListeners.add(l);}

    @Override
    public void removeListener(Listener l) {mListeners.remove(l);}

//    @Override
//    public int getCountTransition() {
//        return mTransitionViews.size();
//    }

    @Override
    public BlockDSView locateBlockDSView(Point2D point) {
        for (BlockDSView v: mBlocksDSView) {
            if (v.isInsideBounds(point)) {
                return v;
            }
        }
        return null;
    }
    @Override
    public TransitionMSCView locateTransitionView(Circle c) {
        for(TransitionMSCView t : mTransitionViews){
            if(t.isInsideBounds_bMSC(c)){
                return t;
            }
        }
        return null;
    }


    @Override
    public AnchorPane getNode() {return this;}


    @Override
    public void saveAsPng(File arq) {
        /*public void saveAsPng(File file) {
            WritableImage img = snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(ComponentViewImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    @Override
    public void onChange(ComponentDS componentDS) {
    }

    @Override
    public void onBlockDSCreated(ComponentDS componentDS, BlockDS blockDS) {showBlockDS(blockDS);}

    @Override
    public void onBlockDSRemoved(ComponentDS componentDS, BlockDS blockDS) { hideBlockDS(blockDS);}

    @Override
    public void onTransitionCreate(ComponentDS buildDS, TransitionMSC t) {showTransition(t);}

    @Override
    public void onTransitionRemove(ComponentDS buildDS, TransitionMSC t) {hideTransition(t);}

    private void showTransition(TransitionMSC t) {
        TransitionMSCView view;
        Node node;
        synchronized(this){
            if(t.getValue("view") == null){
                view = transitionFactory.create(t);
                mTransitionViews.add(view);
                view.setTransitionMSC(t, mComponentDS);
                t.putValue("view", view);
                node = view.getNode();
                getChildren().add(node);
                if (view instanceof SelfTransitionMSCViewImpl) {
                    ((SelfTransitionMSCViewImpl) view).gethMSCsourceView().getNode().toFront();
                } else {
                    node.toBack();
                }
                for (Listener l: mListeners) {
                    l.onTransitionViewCreated(this, view);
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
                t.putValue("view", null);
                getChildren().remove(view);
            }
        }
    }

    private void setYTransitions() {
        double y = 145;
        for(TransitionMSC t : mComponentDS.getAllTransitions()){
            TransitionMSCViewImpl view = (TransitionMSCViewImpl) t.getValue("view");
            Line line = view.getLineTransition();
            line.setStartY(y);
            line.setEndY(y);
            y += 50;
        }
    }
}

