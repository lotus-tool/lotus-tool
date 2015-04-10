package br.uece.lotus.viewer;

import br.uece.lotus.State;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 * Represents a visualization of a @see State
 * Created by emerson on 03/03/15.
 */
public interface StateView {

    Node getNode();

    interface Factory {
        StateView create();
    }

    boolean isInsideBounds(Point2D point);

    State getState();
    void setState(State state);

}
