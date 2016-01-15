/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.TransitionBuildDS;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentBuildDSViewImpl extends AnchorPane implements ComponentBuildDSView, ComponentBuildDS.Listener{

    private ComponentBuildDS mComponentBuild;
    private List<Listener> mListeners = new ArrayList<>();
    private List<BlockBuildDSView> mBlockViews = new ArrayList<>();
    private List<TransitionBuildDSView> mTransitionViews = new ArrayList<>();
    private BlockBuildDSViewFactory blockBuildFactory;

    public ComponentBuildDSViewImpl() {
        blockBuildFactory = new BlockBuildDSViewFactory();
        //falta transitionFactory
    }
    
    
    
    @Override
    public ComponentBuildDS getComponentBuildDS() {
        return mComponentBuild;
    }

    @Override
    public void setComponentBuildDS(ComponentBuildDS cbds) {
        if(mComponentBuild != null){
            mComponentBuild.removeListener(this);
        }
        if(cbds == null){
            return;
        }
        mComponentBuild = cbds;
        mComponentBuild.addListener(this);
        // Precisa add os show dos block e transition
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
    public BlockBuildDSView locateBlockBuildView(Point2D point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TransitionBuildDSView locateTransitionBuildView(Point2D point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnchorPane getNode() {
        return this;
    }

    @Override
    public void setBlockBuildContextMenu(ContextMenu menu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveAsPng(File arq) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onChange(ComponentBuildDS buildDS) {
        
    }

    @Override
    public void onBlockCreate(ComponentBuildDS buildDS, BlockBuildDS bbds) {
        showBlock(bbds);
    }

    @Override
    public void onBlockRemove(ComponentBuildDS buildDS, BlockBuildDS bbds) {
        hideBlock(bbds);
    }

    @Override
    public void onTransitionCreate(ComponentBuildDS buildDS, TransitionBuildDS t) {
        showTransition(t);
    }

    @Override
    public void onTransitionRemove(ComponentBuildDS buildDS, TransitionBuildDS t) {
        hideTransition(t);
    }

    private void showBlock(BlockBuildDS bbds) {
        BlockBuildDSView view;
        Node node;
        synchronized (this){
            if (bbds.getValue("view") == null) {
                view = blockBuildFactory.create();
                mBlockViews.add(view);
                node = view.getNode();
                view.setBlockBuildDS(bbds);
                bbds.setValue("view", view);
                getChildren().add(node);
            }
        }
    }

    private void hideBlock(BlockBuildDS bbds) {
        ObservableList<Node> aux = getChildren();
        BlockBuildDSView view;
        BlockBuildDS b = null;
        
        synchronized (this){
            view = (BlockBuildDSView) bbds.getValue("view");
            if (view != null) {
                b = view.getBlockBuildDS();
            }
        }
        if (view == null) {
            return;
        }
        
        //Adicionar for apos implementacao das transitionBuild
        
        synchronized (this) {
            aux.remove(view);
            bbds.setValue("view", null);
            view.setBlockBuildDS(null);
            mBlockViews.remove(view);
        }
    }

    private void showTransition(TransitionBuildDS t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void hideTransition(TransitionBuildDS t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
