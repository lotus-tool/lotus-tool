/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.uml.api.ds.TransitionMSC;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 *
 * @author Bruno Barbosa
 */
public interface TransitionMSCView {
    
    Node getNode();

    interface Factory {
        TransitionMSCView create(TransitionMSC t);
    }

    abstract boolean isInsideBounds(Circle circle);

    TransitionMSC getTransition();
    void setTransitionMSC(TransitionMSC t, Object component);


    public class Geometry {
        public static final int LINE = 0;
        public static final int CURVE = 1;
    }
}
