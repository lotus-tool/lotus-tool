package br.uece.lotus.viewer;

import br.uece.lotus.Component;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;


import java.io.File;

/**
 * Represents a visualization of a Component
 * Created by emerson on 03/03/15.
 */
public interface ComponentView {

    void tamalhoPadrao();

    void reajuste();

    public interface Listener {

        void onStateViewCreated(ComponentView cv, StateView v);
        void onTransitionViewCreated(ComponentView cv, TransitionView v);

    }
    public Component getComponent();

    public void setComponent(Component c);
    public void addListener(Listener l);
    public void removeListener(Listener l);

    StateView locateStateView(Point2D point);
    TransitionView locateTransitionView(Point2D point);

    AnchorPane getNode();
    void setStateContextMenu(ContextMenu menu);
    void saveAsPng(File arq);

}
