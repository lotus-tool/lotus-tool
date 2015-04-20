package br.uece.lotus.viewer;

import br.uece.lotus.State;

/**
 * Created by emerson on 10/04/15.
 */
public class StateViewFactory implements StateView.Factory {
    @Override
    public StateView create() {
        return new StateViewImpl();
    }
}
