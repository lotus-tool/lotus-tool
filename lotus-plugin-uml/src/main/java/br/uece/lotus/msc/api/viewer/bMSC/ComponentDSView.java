/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.msc.api.viewer.bMSC;



import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.viewer.transition.TransitionMSCView;
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
    public BmscComponent getBmscComponent();
    public void setComponentDS(BmscComponent ds);
    public void addListener(Listener l);
    public void removeListener(Listener l);
//    public int getCountTransition();
    BlockDSView locateBlockDSView(Point2D p);
    TransitionMSCView locateTransitionView(Circle c);

    AnchorPane getNode();
    void setBlockDSContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
}
