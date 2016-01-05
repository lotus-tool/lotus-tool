/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;

import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import java.io.File;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public interface ComponentBuildDSView {
    
    
    public interface Listener{
        void onBlockBuildViewCreated(ComponentBuildDSView cbv, BlockBuildDSView bb);
        void onTransitionBuildViewCreated(ComponentBuildDSView cbv, TransitionBuildDSView tb);
    }
    
    public ComponentBuildDS getComponentBuildDS();
    public void setComponentBuildDS(ComponentBuildDS cbds);
    public void addListener(Listener l);
    public void removeListener(Listener l);
    
    BlockBuildDSView locateBlockBuildView(Point2D point);
    TransitionBuildDSView locateTransitionBuildView(Point2D point);
    
    AnchorPane getNode();
    void setBlockBuildContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
    
}
