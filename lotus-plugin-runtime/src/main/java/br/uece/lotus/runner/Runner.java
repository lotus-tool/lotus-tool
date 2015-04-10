package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.seed.ext.Plugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by emerson on 27/03/15.
 */
public class Runner {

    private final ScriptEngine mEngine;
    private final Component mComponent;

    public Runner(Component component) {
        ScriptEngineManager manager = new ScriptEngineManager();
        mEngine = manager.getEngineByName("JavaScript");
        mComponent = component;
    }

    public void start() throws ScriptException {
        step(mComponent.getInitialState());
    }

    private void step(State state) throws ScriptException {
        for (Transition t: state.getOutgoingTransitions()) {
            String guardExpression = t.getGuard();
            if (guardExpression == null || guardExpression.isEmpty() || mEngine.eval(guardExpression) == Boolean.TRUE) {
                String actionExpression = t.getLabel();
                if (actionExpression != null && !actionExpression.isEmpty()) {
                    mEngine.eval(t.getLabel());
                }
                State next = t.getDestiny();
                if (next.isFinal()) {
                    break;
                }
                step(next);
            }
        }
    }


    public Component getComponent() {
        return mComponent;
    }

    public ScriptEngine getEngine() {
        return mEngine;
    }
}
