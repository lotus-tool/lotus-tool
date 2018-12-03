/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.bMSC;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.viewer.transition.SelfTransitionMSCViewImpl;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCViewFactory;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCViewImpl;
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
public class ComponentDSViewImpl extends AnchorPane implements ComponentDSView, BmscComponent.Listener {
    private BmscComponent mBmscComponent;
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
    public BmscComponent getBmscComponent() {
        return mBmscComponent;
    }

    @Override
    public void setComponentDS(BmscComponent bmscComponent) {
        if (mBmscComponent != null) {
            mBmscComponent.removeListener(this);
        }
        clear();
        if (bmscComponent == null) {
            return;
        }
        mBmscComponent = bmscComponent;
        mBmscComponent.addListener(this);
        for (BmscBlock bmscBlock : mBmscComponent.getBmscBlockList()) {
            showBlockDS(bmscBlock);
        }
        for (TransitionMSC tm : mBmscComponent.getAllTransitions()) {
            showTransition(tm);
        }
        setYTransitions();
    }



    private void showBlockDS(BmscBlock bmscBlock) {
        BlockDSView view;
        Node node;
        synchronized (this){
            if(bmscBlock.getValue("view")==null){
                view = mBlockDSViewFactory.create();
                mBlocksDSView.add(view);
                node= view.getNode();
                view.setBlockDS(bmscBlock);
                bmscBlock.setValue("view",view);
                getChildren().add(node);
            }
        }
    }

    private void clear() {
        if (mBmscComponent == null) {
            return;
        }
        for (BmscBlock ds : mBmscComponent.getBmscBlockList()) {
            hideBlockDS(ds);
        }
        mBmscComponent = null;
    }

    private void hideBlockDS(BmscBlock bmscBlock) {
        ObservableList<Node> aux = getChildren();
        BlockDSView view;
        BmscBlock ds = null;

        synchronized (this) {
            view = (BlockDSView) bmscBlock.getValue("view");
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
            bmscBlock.setValue("view", null);
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
            if(t.isInsideBoundsbMSC(c)){
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
    public void onChange(BmscComponent bmscComponent) {
    }

    @Override
    public void onBlockDSCreated(BmscComponent bmscComponent, BmscBlock bmscBlock) {showBlockDS(bmscBlock);}

    @Override
    public void onBlockDSRemoved(BmscComponent bmscComponent, BmscBlock bmscBlock) { hideBlockDS(bmscBlock);}

    @Override
    public void onTransitionCreate(BmscComponent buildDS, TransitionMSC t) {showTransition(t);}

    @Override
    public void onTransitionRemove(BmscComponent buildDS, TransitionMSC t) {hideTransition(t);}

    private void showTransition(TransitionMSC t) {
        TransitionMSCView view;
        Node node;
        synchronized(this){
            if(t.getValue("view") == null){
                view = transitionFactory.create(t);
                mTransitionViews.add(view);
                view.setTransitionMSC(t, mBmscComponent);
                t.putValue("view", view);
                node = view.getNode();
                getChildren().add(node);
                if (view instanceof SelfTransitionMSCViewImpl) {
                    ((SelfTransitionMSCViewImpl) view).getSourceGenericElementView().getNode().toFront();
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
        for(TransitionMSC t : mBmscComponent.getAllTransitions()){
            TransitionMSCViewImpl view = (TransitionMSCViewImpl) t.getValue("view");
            Line line = view.getLineTransition();
            line.setStartY(y);
            line.setEndY(y);
            y += 50;
        }
    }
}

