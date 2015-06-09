package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emerson on 10/06/15.
 */
public class RunnerContext {

    private static final String SCRIPT_ENGINE_LANGUAGE = "JavaScript";
    private static final String INVALID_STEP_TRANSITION_MESSAGE = "Invalid transition step!";
    StringBuilder sb = new StringBuilder();
    private final Writer mWriter = new Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public void close() throws IOException {

        }
    };

    private Component mComponent;
    private ScriptEngine mScriptEngine;
    private State mCurrentState;
    private List<Transition> mEnabledTransitions;
    private List<Transition> mDisabledTransitions;
    private Map<String, Object> mSymbols;
    private List<String> mTrace;

    public RunnerContext(Component component) {
        mComponent = component;
        mScriptEngine = new ScriptEngineManager().getEngineByName(SCRIPT_ENGINE_LANGUAGE);
        mScriptEngine.getContext().setWriter(mWriter);
        mSymbols = new HashMap<>();
        mTrace = new ArrayList<>();
        mEnabledTransitions = new ArrayList<>();
        mDisabledTransitions = new ArrayList<>();
        changeCurrentState(mComponent.getInitialState());
    }

    private void changeCurrentState(State state) {
        mCurrentState = state;
        analisarGuardasDasTransicoes();
        atualizarValoresDosSimbolos();
    }

    public void step(Transition transition) {
        if (!mEnabledTransitions.contains(transition)) {
            throw new RuntimeException(INVALID_STEP_TRANSITION_MESSAGE);
        }
        mTrace.add(transition.getLabel());
        executar(transition.getLabel());
        changeCurrentState(transition.getDestiny());
    }

    private void executar(String script) {
        System.out.println("running " + script);
        if (script == null || script.isEmpty()) {
            return;
        }
        try {
            mScriptEngine.eval(script);
        } catch (ScriptException e) {
            //o que fazer com o erro?
        }
    }

    private void atualizarValoresDosSimbolos() {
        for (Map.Entry<String, Object> e: mSymbols.entrySet()) {
            String name = e.getKey();
            Object value = mScriptEngine.get(e.getKey());
            System.out.println("symbol " + name + ": " + value);
            mSymbols.put(name, value);
        }
    }

    private void analisarGuardasDasTransicoes() {
        mEnabledTransitions.clear();
        mDisabledTransitions.clear();
        for (Transition t: mCurrentState.getOutgoingTransitions()) {
            if (avalieGuarda(t)) {
                mEnabledTransitions.add(t);
            } else {
                mDisabledTransitions.add(t);
            }
        }
    }

    private boolean avalieGuarda(Transition t) {
        String s = t.getGuard();
        try {
            boolean v = s == null || s.isEmpty() || mScriptEngine.eval(s) == Boolean.TRUE;
            System.out.println("avaliando \"" + s + "\" is " + v );
            return v;
        } catch (ScriptException e) {
            //write in log
        }
        return false;
    }

    public List<String> getTrace() {
        return mTrace;
    }

    public Map<String, Object> getSymbols() {
        return mSymbols;
    }

    public String getOutput() {
        return sb.toString();
    }

    public List<Transition> getDisabledActions() {
        return mDisabledTransitions;
    }

    public List<Transition> getEnabledActions() {
        return mEnabledTransitions;
    }

    public State getCurrentState() {
        return mCurrentState;
    }

    public void putSymbol(String name, String value) {
        mSymbols.put(name, value);
    }
}
