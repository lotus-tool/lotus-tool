/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.uml.api.viewer.bMSC;



import br.uece.lotus.uml.api.ds.*;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.File;

/**
 *
 * @author Bruno Barbosa
 */
public interface ComponentDSView {

    public interface Listener{
        void onBlockDsViewCreated(ComponentDSView cv , BlockDSView dsViewe);
        void onTransitionViewCreated(ComponentDSView cbv, TransitionMSCView tb);

    }
    public ComponentDS getmComponentDS();
    public void setComponentDS(ComponentDS ds);
    public void addListener(Listener l);
    public void removeListener(Listener l);
//    public int getCountTransition();
    BlockDSView locateBlockDSView(Point2D p);
    TransitionMSCView locateTransitionView(Circle c);

    AnchorPane getNode();
    void setBlockDSContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
}
