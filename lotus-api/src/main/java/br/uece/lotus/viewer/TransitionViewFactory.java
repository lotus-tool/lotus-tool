package br.uece.lotus.viewer;

import br.uece.lotus.Transition;

/**
 *
 * @author emerson
 */
public class TransitionViewFactory implements TransitionView.Factory {

    @Override
    public TransitionView create(Transition t) {
        if (t.getSource().equals(t.getDestiny())) {
            return new SelfTransitionViewImpl();
        } else {
            Integer aux = (Integer) t.getValue("view.type");
            int transitionGeometry = aux != null ? aux : TransitionView.Geometry.LINE;
            switch (transitionGeometry) {
                case TransitionView.Geometry.CURVE:
                    return new ElipseTransitionViewImpl();
                default:
                    return new LineTransitionViewImpl();
            }
        }
    }
}
