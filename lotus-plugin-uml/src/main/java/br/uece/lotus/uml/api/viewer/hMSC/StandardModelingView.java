/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.hMSC;

import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.api.ds.StandardModeling;
import java.io.File;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 *
 * @author Bruno Barbosa
 */
public interface StandardModelingView {
    
    
    public interface Listener{
        void onBlockBuildViewCreated(StandardModelingView cbv, HmscView bb);
        void onTransitionBuildViewCreated(StandardModelingView cbv, TransitionMSCView tb);
    }
    
    public StandardModeling getComponentBuildDS();
    public void setComponentBuildDS(StandardModeling cbds);
    public void addListener(Listener l);
    public void removeListener(Listener l);
    
    HmscView locateBlockBuildView(Point2D point);
    TransitionMSCView locateTransitionBuildView(Circle c);
    
    AnchorPane getNode();
    void setBlockBuildContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
    
}
