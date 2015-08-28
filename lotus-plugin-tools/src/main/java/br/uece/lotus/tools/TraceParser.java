package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tools.layout.TreeLayouter;
import br.uece.lotus.viewer.TransitionView;

import java.io.*;
import java.util.ArrayList;
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
    private String namesDestinysState;
    private ArrayList<String> labals;

    protected Component parseFile(InputStream input) {
        mComponent = new Component();
        State StateInicial = criacaoDoStateInicial(mComponent);
        if (input != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String linha = null;
                while ((linha = reader.readLine()) != null) {
                    String[] transDoTrace = linha.split(",");

                    mCurrentState = StateInicial;

                    for (int i = 0; i < transDoTrace.length; i++) {
                        if (transDoTrace[i] != null) {
                            Transition mProximaTransicao = null;
                            Transition nextTransition = null;


                            Transition atualTransicao = verificaExisteTransicao(transDoTrace,i, mCurrentState);
                            if (atualTransicao != null) {
                                mCurrentState = percorre(atualTransicao);
                            } else {
                               // if (i + 1 < transDoTrace.length) {
//                                    System.out.println("transição atual no trace: "+transDoTrace[i]);
//                                    System.out.println("entra aqui");
                                    ////verifica se a próxima trTransição já existe;
                                    //mProximaTransicao = verificaExisteTransicao(transDoTrace[i + 1].trim(), mCurrentState);
                                    mProximaTransicao = verificaExisteTransicao(transDoTrace,i+1, mCurrentState);


                                    if (mProximaTransicao == null) {
//                                        mProximaTransicao = verificaExisteTransicao(transDoTrace[i + 1].trim());
                                        mProximaTransicao = verificaExisteTransicao(transDoTrace,i+1);
                                    }
                              //  }
                                if (mProximaTransicao != null) {
                                    //ponte
                                    namesDestinysState = mProximaTransicao.getSource().getLabel();
//                                    System.out.println(mCurrentState.getLabel() + " " + linhaDoTrace[i] + " " + namesDestinysState);
                                    adicionarTransicao(mCurrentState.getLabel(), transDoTrace[i].trim(), namesDestinysState);
//                                    System.out.println("Caso ponte");
                                    continue;
                                } else {
                                  //  if (i + 1 < transDoTrace.length) {
//                                        nextTransition = verificaExisteTransicao(transDoTrace[i + 1].trim());
                                        nextTransition = verificaExisteTransicao(transDoTrace,i+1);
                                  //  }
                                    if (nextTransition != null) {
                                        if (verificationCaseComma(nextTransition, i, transDoTrace)) {
//                                        System.out.println("caso virgula");
                                            addCommar(mCurrentState, transDoTrace[i].trim(), nextTransition.getSource());
                                            continue;
                                        } else {
                                            /////quebra em nova aŕvore
//                                            System.out.println("quebra em nova aŕvore");
                                            namesDestinysState = String.valueOf(mComponent.getStatesCount());
                                            continue;


                                        }
                                    }

                                }

//                                    if (verificationCaseComma(nextTransitionGoOutOfCurrentState, i, linhaDoTrace)) {
//                                        System.out.println("caso virgula");
//                                        addCommar(mCurrentState, linhaDoTrace[i].trim(), nextTransitionGoOutOfCurrentState.getSource());
//                                        continue;
//                                    }

//                                System.out.println("caso normal");
                                namesDestinysState = String.valueOf(mComponent.getStatesCount());
//                                    System.out.println(mCurrentState.getLabel() + " " + linhaDoTrace[i] + " " + namesDestinysState);
//                                    adicionarTransicao(mCurrentState.getLabel(), linhaDoTrace[i].trim(), namesDestinysState);
//                                System.out.println(mCurrentState.getLabel() + " " + linhaDoTrace[i] + " " + namesDestinysState);
                                adicionarTransicao(mCurrentState.getLabel(), transDoTrace[i].trim(), namesDestinysState);
                            }

                        }

                        //}


                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        // new LTSALayouter().layout(mComponent);
        new TreeLayouter().layout(mComponent);
        mComponent.setName("Untitled");
        return mComponent;
    }

    private State criacaoDoStateInicial(Component mComponent) {
        State initialState = mComponent.newState(0);
        mStates.put(initialState.getLabel(), initialState);
        return initialState;
    }

//    private Transition verificaExisteTransicao(String nameTrans, State currentState) {
//        for (Transition t : currentState.getOutgoingTransitionsList()) {
//            if (brokenLabel(t.getLabel(), nameTrans)) {
//                return t;
//            }
//
//        }
//        return null;
//
//    }
public Transition verificaExisteTransicao(String[] trace, int j, State currentState) {

    if (!(j  < trace.length)) {
        return null;

    }
    //if (j  < trace.length) {
      //  System.out.println("Entra aqui ???");
        String nameTrans = trace[j].trim();
        for (Transition t : currentState.getOutgoingTransitionsList()) {
            if (brokenLabel(t.getLabel(), nameTrans)) {
                return t;
            }

        }
  //  }
        return null;

    }



    private boolean brokenLabel(String label, String name) {
        String[] names = label.split(",");
        for (String s : names) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Transition verificaionExistenceTransitionGoingInOrGoingOut(String nameTrans, State currentState) {
//        System.out.println("entrou aquiii");

        for (Transition t : currentState.getOutgoingTransitionsList()) {
//            System.out.println(t.getLabel()+"XXXXXX"+nameTrans);
            if ((brokenLabel(t.getLabel(), nameTrans))) {
                return t;
            }
        }
        for (Transition r : currentState.getIncomingTransitionsList()) {
            if ((brokenLabel(r.getLabel(), nameTrans))) {
                return r;
            }

        }
        return null;

    }


//    private Transition verificaExisteTransicao(String nameTransistion) {
        private Transition verificaExisteTransicao(String [] trace , int j) {
        if (!(j  < trace.length)) {
            return null;

        }
        for (Transition t : mComponent.getTransitions()) {
            if ((brokenLabel(t.getLabel(), trace[j].trim()))) {
                return t;
            }
        }
        return null;
    }

    private State percorre(Transition trans) {

        return trans.getDestiny();
    }

    private void addCommar(State estadoOrigem, String acao, State estadoDestino) {
        Transition tras = pegandoTransicaoPorStadoOrigEStadoDest(estadoOrigem, estadoDestino);//possivel problema, outra transiçao para o mesmo estado de ori e estado de dest
        if (tras == null) {
            return;
        }
        //  System.out.println("tras" + tras.getLabel());
        mCurrentState = estadoDestino;
        mComponent.buildTransition(estadoOrigem, estadoDestino)
                .setLabel(acao);
        tras.setLabel(tras.getLabel() + "," + acao);


    }

    private boolean verificationCaseComma(Transition transition, int posicaoDoTrace, String[] linhaDoTrace) {
        boolean aux = false;

        State current = transition.getDestiny();
//        System.out.println("transition:" + transition.getLabel());
//        System.out.println("current:" + current.getLabel());
//        System.out.println("current.getOutgoingTransitionsList():" + current.getOutgoingTransitionsList().size());
//        System.out.println(" posicaoDoTrace+2 <linhaDoTrace.length:" + (posicaoDoTrace + 2) + "<= " + linhaDoTrace.length);

        if (current.getOutgoingTransitionsList().size() == 0 && posicaoDoTrace + 2 >= linhaDoTrace.length) {
//            System.out.println("entrou aquii");
            return true;
        }

        for (int x = posicaoDoTrace + 2; x < linhaDoTrace.length; x++) {
            for (Transition t : current.getOutgoingTransitionsList()) {
//                System.out.println("current.getOutgoingTransitionsList().size():" + t.getLabel());
//                System.out.println("transição:" + t.getLabel());
//                System.out.println("linhaDoTrace[x]:" + linhaDoTrace[x]);

                if (brokenLabel(t.getLabel(), linhaDoTrace[x].trim())) {
                    aux = true;
                    current = t.getDestiny();
                    break;
                }
            }
        }
//System.out.println("retorno do commme"+aux);
        return aux;

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
        if (pegandoTransicaoPorStadoOrigEStadoDest(src, dst) != null || pegandoTransicaoPorStadoOrigEStadoDest(dst, src) != null) {
            mComponent.buildTransition(src, dst)
                    .setLabel(acao)
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
        } else {
            mComponent.buildTransition(src, dst)
                    .setLabel(acao)
                    .setViewType(TransitionView.Geometry.LINE)
                    .create();
        }

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


