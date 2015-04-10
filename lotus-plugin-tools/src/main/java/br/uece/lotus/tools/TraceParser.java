package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.TransitionViewFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucas on 04/03/15.
 */
public class TraceParser {
    private final Map<String, State> mStates = new HashMap<>();
    State mCurrentState;
    private Component mComponent;
    private int id = 0;
    private String nomeDoStateDestino;

    protected Component parseFile(InputStream input) {
        mComponent = new Component();
        State initialState = mComponent.newState(0);
        mStates.put(initialState.getLabel(), initialState);
        if (input != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] trace = line.split(",");
                    System.out.println("trace.length:" + trace.length);

                    mCurrentState = initialState;

                    for (int i = 0; i < trace.length; i++) {
                        if (trace[i] != null) {
                            System.out.println("vetor: " + trace[i]);
                            System.out.println(" não existe a transiçãoPonte: " + trace[i]);
                            System.out.println("vetor[i] e vetor[i++] não  são iguais");
                            nomeDoStateDestino = String.valueOf(mComponent.getStatesCount());
                            System.out.println(mCurrentState.getLabel() + " " + trace[i] + " " + nomeDoStateDestino);
                            adicionarTransicao(mCurrentState.getLabel(), trace[i].trim(), nomeDoStateDestino);


                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        new LTSALayouter().layout(mComponent);
        mComponent.setName("Untitled");
        return mComponent;
    }

    private Transition transicaoComLabel(String nomeTrans) {
        for (State s : mComponent.getStates()) {
            Transition trans = s.getTransitionByLabel(nomeTrans);
            if (trans != null) {
                return trans;
            }

        }
        return null;

    }

    private void adicionarTransicaoCasoVirgula(State estadoOrigem, String acao, State estadoDestino) {
        Transition tras = pegandoTransicaoPorStadoOrigEStadoDest(estadoOrigem, estadoDestino);//possivel problema, outra transiçao para o mesmo estado de ori e estado de dest
        if (tras == null) {
            return;
        }
        mCurrentState = estadoDestino;
        mComponent.buildTransition(estadoOrigem, estadoDestino)
                .setLabel(acao);
        tras.setLabel(tras.getLabel() + "," + acao);


    }

    private boolean verificandoSeEhCasoDeVirgula(Transition t, int posicaoDoTrace, String[] linhaDoTrace) {
        State stadoParaVerificar = t.getDestiny();
        System.out.println("PosicaoDoTrance: " + posicaoDoTrace);
        for (int i = posicaoDoTrace + 1; i < (linhaDoTrace.length); i++) {
            System.out.println("PosicaoDoTrance: " + i);
            System.out.println("String: " + linhaDoTrace[i]);
            t = stadoParaVerificar.getTransitionByLabel(linhaDoTrace[i]);
            if (t == null) {
                System.out.println("false");
                return false;
            }
        }
        System.out.println("true");
        return true;
    }


    private boolean verificarSeProximoNomeDoVetorTraceEhIgualAoNomeAtualDoVetorTrace(String nomeAtual, String proxNome) {
        if (nomeAtual.equals(proxNome)) {
            return true;
        } else {
            return false;
        }
    }

    private Transition pegandoTransicaoPorStadoOrigEStadoDest(State orig, State dest) {
        for (Transition t : mComponent.getTransitions()) {
            if (t.getSource().getLabel().equals(orig.getLabel()) && t.getDestiny().getLabel().equals(dest.getLabel())) {
                return t;
            }
        }
        return null;
    }

    private void adicionarTransicao(String estadoOrigem, String acao, String estadoDestino) {
        State src = getOrCreateState(estadoOrigem);
        State dst = getOrCreateState(estadoDestino);
        mCurrentState = dst;
        mComponent.buildTransition(src, dst)
                .setLabel(acao)
                .setViewType(TransitionViewFactory.Type.SEMI_CIRCLE)
                .create();

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
            if (mComponent.getStatesCount() == 0) {
                s.setAsInitial();
            }
            mStates.put(estadoOrigem, s);
        }
        return s;
    }

}


