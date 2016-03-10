/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.uml.api.ds.TransitionMSC;


/**
 *
 * @author Bruno Barbosa
 */
public class TransitionMSCViewFactory implements TransitionMSCView.Factory{

    @Override
    public TransitionMSCView create(TransitionMSC t) {
        if (t.getSource().equals(t.getDestiny())) {
            return new SelfTransitionMSCViewImpl();
        } else {
            Integer aux = (Integer) t.getValue("view.type");
            int transitionGeometry = aux != null ? aux : TransitionMSCView.Geometry.LINE;
            switch (transitionGeometry) {
                case TransitionMSCView.Geometry.CURVE:
                    return new ElipseTransitionMSCViewImpl();
                default:
                    return new LineTransitionMSCViewImpl();
            }
        }
    }
}
