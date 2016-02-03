/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.builder;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.TransitionBuildDS;
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
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentBuildDSViewImpl extends AnchorPane implements ComponentBuildDSView, ComponentBuildDS.Listener{

    private ComponentBuildDS mComponentBuild;
    private ContextMenu mBlockBuildContextMenu;
    private List<Listener> mListeners = new ArrayList<>();
    private List<BlockBuildDSView> mBlockViews = new ArrayList<>();
    private List<TransitionBuildDSView> mTransitionViews = new ArrayList<>();
    private BlockBuildDSViewFactory blockBuildFactory;
    private TransitionBuildDSViewFactory transitionBuildFactory;

    public ComponentBuildDSViewImpl() {
        blockBuildFactory = new BlockBuildDSViewFactory();
        transitionBuildFactory = new TransitionBuildDSViewFactory();
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
        clear();
        if(cbds == null){
            return;
        }
        mComponentBuild = cbds;
        mComponentBuild.addListener(this);
        for(BlockBuildDS b : mComponentBuild.getBlocos()){
            showBlock(b);
        }
        for(TransitionBuildDS t : mComponentBuild.getTransitions()){
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
    public BlockBuildDSView locateBlockBuildView(Point2D point) {
        for(BlockBuildDSView b : mBlockViews){
            if(b.isInsideBounds(point)){
                return b;
            }
        }
        return null;
    }

    @Override
    public TransitionBuildDSView locateTransitionBuildView(Point2D point) {
        for(TransitionBuildDSView t : mTransitionViews){
            //falta implementar a transition 
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
            Logger.getLogger(ComponentBuildDSViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void clear() {
        if (mComponentBuild == null) {
            return;
        }
        for (BlockBuildDS b : mComponentBuild.getBlocos()) {
            hideBlock(b);
        }
        mComponentBuild = null;
    }
    
}
