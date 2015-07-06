package br.uece.lotus.tools.layout;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerson on 10/04/15.
 */
public class TreeLayouter {

    private static final int PADDING_X = 200;
    private static final int PADDING_Y = 100;

    public void layout(Component component) {
        List<State> states = new ArrayList<>();
        List<State> visitedStates = new ArrayList<>();
        State is = component.getInitialState();
        is.setLayoutX(30);
        is.setLayoutY(30);
        states.add(is);

        while (!states.isEmpty()) {
            State s = states.remove(0);
            int i = 0;
            double currentX = s.getLayoutX();
            double currentY = s.getLayoutY();
            for (Transition t : s.getOutgoingTransitions()) {
                State destiny = t.getDestiny();
                if (!visitedStates.contains(destiny)) {
                    destiny.setLayoutX(currentX + PADDING_X);
                    destiny.setLayoutY(currentY + PADDING_Y * i);
                    states.add(destiny);
                    i++;
                }
            }
            visitedStates.add(s);
        }
    }

}
