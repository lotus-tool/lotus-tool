/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Bruno Barbosa
 */
public class ElipseTransitionMSCViewImpl extends TransitionMSCViewImpl{

    @Override
    protected void prepareView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void updateView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInsideBounds_hMSC(Circle circle) {
        return false;
    }

    @Override
    public boolean isInsideBounds_bMSC(Circle circle) {
        return false;
    }

    @Override
    public Line getLineTransition() {
        return null;
    }


}
