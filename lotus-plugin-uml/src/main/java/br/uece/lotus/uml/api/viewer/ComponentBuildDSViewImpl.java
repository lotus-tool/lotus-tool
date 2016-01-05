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
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public class ComponentBuildDSViewImpl extends AnchorPane implements ComponentBuildDSView, ComponentBuildDS.Listener{

    @Override
    public ComponentBuildDS getComponentBuildDS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setComponentBuildDS(ComponentBuildDS cbds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListener(Listener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(Listener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onBlockCreate(ComponentBuildDS buildDS, BlockBuildDS bbds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onBlockRemove(ComponentBuildDS buildDS, BlockBuildDS bbds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTransitionCreate(ComponentBuildDS buildDS, TransitionBuildDS t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTransitionRemove(ComponentBuildDS buildDS, TransitionBuildDS t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
