package br.uece.lotus.viewer;

import br.uece.lotus.Transition;

/**
 *
 * @author emerson
 */
public class TransitionViewFactory {

    public static class Type {
        public static final int LINEAR = 0;
        public static final int SEMI_CIRCLE = 1;
    }

    public static TransitionView create(Transition t, int transitionViewType) {
        if (t.getSource().equals(t.getDestiny())) {
            return new SelfTransitionView();
        } else {
//            int n = t.getSource().getTransitionsTo(t.getDestiny()).size();
//            n += t.getDestiny().getTransitionsTo(t.getSource()).size();                            
//            System.out.println("-> n: " + n);            
            switch (transitionViewType) {
                case Type.SEMI_CIRCLE:
                    return new ElipseTransitionView();
                default:
                    return new LineTransitionView();
            }
        }
    }

}
