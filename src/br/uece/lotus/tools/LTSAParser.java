package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LTSAParser {
    
    private static final Pattern STATE_ID = Pattern.compile("[a-zA-Z][a-zA-Z:0-9._]*");
    private static final Pattern EQUALS = Pattern.compile(Pattern.quote("="));
    private static final Pattern OPEN_PARENTESIS = Pattern.compile(Pattern.quote("("));
    private static final Pattern CLOSE_PARENTESIS = Pattern.compile(Pattern.quote(")"));
    private static final Pattern ACTION_ID = STATE_ID;
    private static final Pattern ACTION_OPERATOR = Pattern.compile(Pattern.quote("->"));
    private static final Pattern PIPE = Pattern.compile(Pattern.quote("|"));
    private static final Pattern COMMA = Pattern.compile(Pattern.quote(","));
    private static final Pattern DOT = Pattern.compile(Pattern.quote("."));
    private Component mComponent;
    private final Map<String, State> mStates = new HashMap<>();

    public Component parseFile(InputStream input) throws FileNotFoundException, IOException {
        mComponent = new Component();        
        try (Reader r = new InputStreamReader(input)) {
            SimpleScanner sc = new SimpleScanner(r);
            while (true) {
                String estadoOrigem = sc.next(STATE_ID);                
                sc.next(EQUALS);
                if (sc.has(OPEN_PARENTESIS)) {
                    sc.next(OPEN_PARENTESIS);
                    while (true) {                        
                        String acao = sc.next(ACTION_ID);
                        sc.next(ACTION_OPERATOR);
                        String estadoDestino = sc.next(STATE_ID);
                        adicionarTransicao(estadoOrigem, acao, estadoDestino);
                        if (!sc.has(PIPE)) {
                            break;
                        }
                        sc.next(PIPE);
                    }
                    sc.next(CLOSE_PARENTESIS);
                } else {
                    String nome = sc.next(STATE_ID);
                    alias(estadoOrigem, nome);
                }
                if (!sc.has(COMMA)) {
                    sc.next(DOT);
                    break;
                }
                sc.next(COMMA);
            }
        }
        mComponent.setInitialState(mComponent.getStateByID(0));        
        return mComponent;
    }

    private void adicionarTransicao(String estadoOrigem, String acao, String estadoDestino) {
        State src = getOrCreateState(estadoOrigem);
        State dst = getOrCreateState(estadoDestino);
        Transition t = mComponent.newTransition(src, dst);
        t.setLabel(acao);
    }

    private void alias(String estadoOrigem, String nome) {
        State s = getOrCreateState(estadoOrigem);
        mStates.put(nome, s);
        mComponent.setName(estadoOrigem);
    }

    private State getOrCreateState(String estadoOrigem) {
        State s = mStates.get(estadoOrigem);
        if (s == null) {
            s = mComponent.newState(mComponent.getStatesCount());
            mStates.put(estadoOrigem, s);            
        }
        return s;
    }

}
