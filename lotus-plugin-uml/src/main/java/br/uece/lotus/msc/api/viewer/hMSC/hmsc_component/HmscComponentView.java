/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.hMSC.hmsc_component;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.api.viewer.hMSC.hmsc_block.HmscBlockView;
import br.uece.lotus.msc.api.viewer.hMSC.interception_node.InterceptionNodeView;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;

import java.io.File;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public interface HmscComponentView {
    
    
    public interface Listener{
        void onCreatedBlockHmscView(HmscComponentView hmscComponentView, HmscBlockView hmscBlockView);
        void onCreatedInterceptionNodeView(HmscComponentView hmscComponentView,
                                           InterceptionNodeView interceptionNodeView);
        void onCreatedTransitionMSCView(HmscComponentView hmscComponentView, TransitionMSCView transitionMSCView);
    }
    
    public HmscComponent getHmscComponent();
    public void setComponentBuildDS(HmscComponent hmscComponent);
    public void addListener(Listener listener);
    public void removeListener(Listener listener);

    GenericElementView locateGenericElementView(Point2D point2D);
    TransitionMSCView locateTransitionBuildView(Point2D point2D);
    
    AnchorPane getNode();
    void setBlockBuildContextMenu(ContextMenu contextMenu);
    void saveAsPng(File arq);
    
}
