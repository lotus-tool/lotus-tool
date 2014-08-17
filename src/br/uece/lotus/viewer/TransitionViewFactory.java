package br.uece.lotus.viewer;

import br.uece.lotus.Transition;

/**
 *
 * @author emerson
 */
public class TransitionViewFactory {
    
    public static TransitionView create(Transition t) {
        if (t.getSource().equals(t.getDestiny())) {
            return new SelfTransitionView();
        } else {
            int n = t.getSource().getTransitionsTo(t.getDestiny()).size();
            n += t.getDestiny().getTransitionsTo(t.getSource()).size();                            
//            System.out.println("-> n: " + n);
            if (n == 1) {
                return new LineTransitionView();
            } else {
                return new ElipseTransitionView();
            }
//            return new LineTransitionView();
        }
    }
    
}
