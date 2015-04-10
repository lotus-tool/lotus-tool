package br.uece.lotus.viewer;

import br.uece.lotus.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 * Represents a visualization of a @see Transition
 * Created by emerson on 03/03/15.
 */
public interface TransitionView {

    Node getNode();

    interface Factory {
        TransitionView create(Transition t);
    }

    abstract boolean isInsideBounds(Point2D point);

    Transition getTransition();
    void setTransition(Transition t);


    public class Geometry {
        public static final int LINE = 0;
        public static final int CURVE = 1;
    }
}
