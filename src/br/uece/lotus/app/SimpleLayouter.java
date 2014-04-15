package br.uece.lotus.app;

import br.uece.lotus.model.ComponentModel;
import br.uece.lotus.model.StateModel;
import br.uece.lotus.view.ComponentEditor;

public class SimpleLayouter {
    
    public void run(ComponentModel component) {
        int i = 0;
        for (StateModel state: component.getVertices()) {
            state.setValue(ComponentEditor.TAG_POS_X, Integer.toString(i * 100));
            state.setValue(ComponentEditor.TAG_POS_Y, Integer.toString(100));
            i++;
        }
    } 
    
}
