/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.uml.api.viewer.block;



import br.uece.lotus.uml.api.ds.*;
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
    /*void tamalhoPadrao();
    void reajuste(); */

    public interface Listener{
        void onBlockDsViewCreated(ComponentDSView cv , BlockDSView dsViewe);

    }
    /*public Component getComponent();*/
    public ComponentDS getmComponentDS();
    public void setComponentDS(ComponentDS ds);
    /*public void setComponent(Component c);*/
    public void addListener(Listener l);
    public void removeListener(Listener l);

    BlockDSView locateBlockDSView(/*Point2D point*/Circle c);
    /*TransitionView locateTransitionView(Point2D point);*/

    AnchorPane getNode();
    void setBlockDSContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
}
