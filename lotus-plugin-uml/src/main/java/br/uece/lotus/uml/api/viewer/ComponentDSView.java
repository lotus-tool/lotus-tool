/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;
import br.uece.lotus.Component;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

import java.io.File;

/**
 *
 * @author Bruno Barbosa
 */
public interface ComponentDSView {
    public interface Listener{
        void onBlockDsViewCreated(ComponentView cv , BlockDSView dsViewe);

    }
    public Component getComponent();

    public void setComponent(Component c);
    public void addListener(Listener l);
    public void removeListener(Listener l);

    BlockDSView locateBlockDSView(Point2D point);
    /*TransitionView locateTransitionView(Point2D point);*/

    AnchorPane getNode();
    void setStateContextMenu(ContextMenu menu);
    void saveAsPng(File arq);
}
