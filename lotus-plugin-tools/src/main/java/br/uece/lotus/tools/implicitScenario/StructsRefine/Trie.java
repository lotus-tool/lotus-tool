package br.uece.lotus.tools.implicitScenario.StructsRefine;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tools.layout.TreeLayouter;
import br.uece.lotus.viewer.TransitionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucas on 14/05/16.
 */
public class Trie {
    private final Map<String, State> mStates = new HashMap<>();

    public Component getmComponent() {
        return mComponent;
    }

    public void setmComponent(Component mComponent) {
        this.mComponent = mComponent;
    }

    private Component mComponent;
    private State mCurrentState;
    private String namesDestinysState;

    public Component createComponet(ArrayList<String> trace){
    ArrayList<String> arrayList = new ArrayList<>();
        for(String s : trace){
           arrayList.add(s.trim());

        }
        trace.clear();;
        trace.addAll(arrayList);
        mComponent = new Component();
        State StateInicial = criacaoDoStateInicial(mComponent);


        if (trace != null) {
            for (String linha : trace) {
                String[] transDoTrace = linha.split(",");
                mCurrentState = StateInicial;

                for (int i = 0; i < transDoTrace.length; i++) {
                    if (transDoTrace[i] != null) {
                        Transition mProximaTransicao = null;
                        Transition nextTransition = null;

                        Transition atualTransicao = verificaExisteTransicao(transDoTrace, i, mCurrentState);
                        if (atualTransicao != null) {
                            mCurrentState = percorre(atualTransicao);

                        }
                        else{
                            mProximaTransicao = verificaExisteTransicao(transDoTrace,i+1, mCurrentState);

                            if (mProximaTransicao != null) {
                                //ponte
                                namesDestinysState = mProximaTransicao.getSource().getLabel();
//                                    System.out.println(mCurrentState.getLabel() + " " + linhaDoTrace[i] + " " + namesDestinysState);
                                adicionarTransicao(mCurrentState.getLabel(), transDoTrace[i].trim(), namesDestinysState);
                                    //System.out.println("Caso ponte");
                                continue;
                            }



                            namesDestinysState = String.valueOf(mComponent.getStatesCount());
                            adicionarTransicao(mCurrentState.getLabel(), transDoTrace[i].trim(), namesDestinysState);
                        }
                    }
                    }
                }
            }
        new TreeLayouter().layout(mComponent);
        mComponent.setName("Untitled");
        return mComponent;
        }


    private State criacaoDoStateInicial(Component mComponent) {
        State initialState = mComponent.newState(0);
        mStates.put(initialState.getLabel(), initialState);
        return initialState;
    }

    public Transition verificaExisteTransicao(String[] trace, int j, State currentState) {

        if (!(j < trace.length)) {
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

    private State percorre(Transition trans) {

        return trans.getDestiny();
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
    private Transition pegandoTransicaoPorStadoOrigEStadoDest(State orig, State dest) {
        for (Transition t : mComponent.getTransitions()) {
            if (t.getSource().getLabel().equals(orig.getLabel()) && t.getDestiny().getLabel().equals(dest.getLabel())) {
                return t;
            }
        }
        return null;
    }

}



