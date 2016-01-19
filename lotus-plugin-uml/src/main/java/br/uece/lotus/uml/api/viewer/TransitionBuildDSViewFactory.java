/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;

import br.uece.lotus.viewer.ElipseTransitionViewImpl;
import br.uece.lotus.viewer.LineTransitionViewImpl;
import br.uece.lotus.viewer.SelfTransitionViewImpl;

/**
 *
 * @author Bruno Barbosa
 */
public class TransitionBuildDSViewFactory implements TransitionBuildDSView.Factory{

    @Override
    public TransitionBuildDSView create(TransitionBuildDSView t) {
    /*    if (t.getSource().equals(t.getDestiny())) {
            return new SelfTransitionViewImpl();
        } else {
            Integer aux = (Integer) t.getValue("view.type");
            int transitionGeometry = aux != null ? aux : TransitionBuildDSView.Geometry.LINE;
            switch (transitionGeometry) {
                case TransitionBuildDSView.Geometry.CURVE:
                    return new ElipseTransitionViewImpl();
                default:
                    return new LineTransitionViewImpl();
            }
        }
    */return null;}
    
}
