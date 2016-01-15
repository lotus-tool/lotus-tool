/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;
import br.uece.lotus.uml.api.ds.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Point2D;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentDSViewImpl extends AnchorPane implements ComponentDSView, ComponentDS.Listener {
    private ComponentDS mComponentDS;
    private ContextMenu mDSContextMenu;
    /* private TransitionViewFactory mTransitionViewFactory;*/
    private BlockDSViewFactory mBlockDSViewFactory;
    private List<ComponentDSView.Listener> mListeners = new ArrayList<>();
    private List<BlockDSView> mBlocksDSView = new ArrayList<>();


    /*private List<TransitionView> mTransitionViews = new ArrayList<>();*/
    public ComponentDSViewImpl(){
    /*    mTransitionViewFactory = new TransitionViewFactory();*/
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
        /*for (Transition tm : mComponentBlockDS.getTransitions()) {
            showTransition(tm);
        }*/
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
        /*for (Transition t : ds.getOutgoingTransitions()) {
            hideTransition(t);
        }
        for (Transition t : ds.getIncomingTransitions()) {
            hideTransition(t);
        }*/
        synchronized (this) {
            aux.remove(view);
            blockDS.setValue("view", null);
            view.setBlockDS(null);
            mBlocksDSView.remove(view);
        }
    }

   /* private void hideTransition(Transition t) {
        synchronized (this) {
            TransitionViewImpl view = (TransitionViewImpl) t.getValue("view");
            if (view != null) {
                mTransitionViews.remove(view);
                view.setTransition(null);
                t.setValue("view", null);
                getChildren().remove(view);
            }
        }*/

    /*private void showTransition(Transition t) {
        TransitionView view;
        Node node;
        synchronized (this) {
            if (t.getValue("view") == null) {
                view = mTransitionViewFactory.create(t);
                mTransitionViews.add(view);
                view.setTransition(t);
                t.setValue("view", view);
                node = view.getNode();
                getChildren().add(node);
               if (view instanceof SelfTransitionViewImpl) {
                    ((SelfTransitionViewImpl) view).getSourceStateView().getNode().toFront();
                } else {
                    node.toBack();
                }
                for (ComponentView.Listener l: mListeners) {
                    l.onTransitionViewCreated(this, view);
                }
            }
        }
    }
*/

    @Override
    public void addListener(Listener l) {mListeners.add(l);}

    @Override
    public void removeListener(Listener l) {mListeners.remove(l);}

    @Override
    public BlockDSView locateBlockDSView(Point2D point) {
        for (BlockDSView v: mBlocksDSView) {
            if (v.isInsideBounds(point)) {
                return v;
            }
        }
        return null;
    }
    /*@Override
    public TransitionView locateTransitionView(Point2D point) {
        for (TransitionView v: mTransitionViews) {
            if (v.isInsideBounds(point)) {
                return v;
            }
        }
        return null;
    }*/


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

   /* @Override
    public void onTransitionCreated(Component component, Transition transition) {
        showTransition(transition);
    }

    @Override
    public void onTransitionRemoved(Component component, Transition transition) {
        hideTransition(transition);
    }*/
}

