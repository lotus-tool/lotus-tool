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
    private Transition currentTransition = null;
    boolean nomesIguais = false;
    Transition TransDoMesmoNome = null;

    protected Component parseFile(InputStream input) {
        mComponent = new Component();
        State estadoInicial = mComponent.newState(0);
        mStates.put(estadoInicial.getLabel(), estadoInicial);
        if (input != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] trace = line.split(",");
                    System.out.println("trace.length:" + trace.length);

                    mCurrentState = estadoInicial;
                    currentTransition = null;

                    for (int i = 0; i < trace.length; i++) {
                        //currentTransition = null;
                        if (trace[i] != null) {
                            System.out.println("teste" + trace[i]);
                            currentTransition = verificarSeExisteTransicaoComEsseMesmoNomeNoComponente(trace[i].trim());
                            if (currentTransition != null) {
                                System.out.println(" tem transicaoAtual com esse nome");
                                // nomeDoStateDestino = currentTransition.getSource().getLabel();
                                // mCurrentState = currentTransition.getDestiny();
                                mCurrentState = currentTransition.getDestiny();
                                //nomeDoStateDestino = currentTransition.getSource().getLabel();
                                //mCurrentState = currentTransition.getDestiny();
                                //nomeDoStateDestino = String.valueOf(mComponent.getStatesCount());
                            } else {
                                System.out.println("não tem transicaoAtual com esse nome");

                                //verificando se existe a posicao i+1
                                if (i < trace.length - 1) {
                                    TransDoMesmoNome = verificarSeExisteTransicaoComEsseMesmoNomeNoComponente(trace[i + 1].trim());
                                    //caso não exista a posicao i+1
                                } else {
                                    TransDoMesmoNome = null;
                                }
                                //caso ponte
                                if (TransDoMesmoNome != null) {
                                    
                                    System.out.println(" tem transicaoProx com esse nome");
                                    nomeDoStateDestino = TransDoMesmoNome.getSource().getLabel();
                                    //mCurrentState = TransDoMesmoNome.getDestiny();
                                    System.out.println(mCurrentState.getLabel() + "::::" + nomeDoStateDestino);
                                    System.out.println(mCurrentState.getLabel() + " " + trace[i] + " " + nomeDoStateDestino);
                                    adicionarTransicao(mCurrentState.getLabel(), trace[i].trim(), nomeDoStateDestino);
                                } else {
                                    System.out.println(" não tem transicaoProx com esse nome");

                                    //verificando se existe a posicao i+1
                                    if (i < trace.length - 1) {
                                        nomesIguais = verificarSeProximoNomeDoVetorTraceEhIgualAoNomeAtualDoVetorTrace(trace[i].trim(), trace[i + 1].trim());
                                        //caso não exista a posicao i+1
                                    } else {
                                        nomesIguais = false;
                                    }
                                    if (nomesIguais) {
                                        System.out.println("vetor[i] e vetor[i++] são iguais");
                                        nomeDoStateDestino = mCurrentState.getLabel();
                                    } else {
                                        System.out.println("vetor[i] e vetor[i++] não  são iguais");
                                        nomeDoStateDestino = String.valueOf(mComponent.getStatesCount());
                                    }
                                    System.out.println(mCurrentState.getLabel() + " " + trace[i] + " " + nomeDoStateDestino);
                                    adicionarTransicao(mCurrentState.getLabel(), trace[i].trim(), nomeDoStateDestino);
                                }

                            }

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

    private Transition verificarSeExisteTransicaoComEsseMesmoNomeNoComponente(String nomeTrans) {
        for (State s : mComponent.getStates()) {
            Transition trans = s.getTransitionByLabel(nomeTrans);
            if (trans != null) {
                return trans;
            }

        }
        return null;

    }

    private boolean verificarSeProximoNomeDoVetorTraceEhIgualAoNomeAtualDoVetorTrace(String nomeAtual, String proxNome) {
        if (nomeAtual.equals(proxNome)) {return true;}
        else {return false;}
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


