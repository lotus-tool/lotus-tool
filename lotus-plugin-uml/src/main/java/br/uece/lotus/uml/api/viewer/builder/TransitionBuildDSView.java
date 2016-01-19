/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.builder;

import br.uece.lotus.uml.api.ds.TransitionBuildDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface TransitionBuildDSView {
    
    Node getNode();

    interface Factory {
        TransitionBuildDSView create(TransitionBuildDSView t);
    }

    abstract boolean isInsideBounds(Point2D point);

    TransitionBuildDS getTransition();
    void setTransitionBuildDS(TransitionBuildDS t);


    public class Geometry {
        public static final int LINE = 0;
        public static final int CURVE = 1;
    }
}
