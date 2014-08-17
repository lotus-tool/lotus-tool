package br.uece.lotus.model;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParallelComposition {
  

//    private Layouter mLayouter;
//    private UserInterface mUI;

//    @Override
//    public void onStart(ExtensionManager extensionManager) throws Exception {
//        mUI = extensionManager.get(UserInterface.class);
//        mLayouter = extensionManager.get(Layouter.class);
//        
//        mUI.addMainMenuEntry("Modelo/Composição paralela", () -> {
//            if (mUI.getSelectedComponents().size() < 2) {
//                throw new RuntimeException("Selecione pelo menos dois componentes!");
//            }             
//            Component a = mUI.getSelectedComponents().get(0);
//            Component b = mUI.getSelectedComponents().get(1);
//            Component c = composite(a, b);
//            c.setName("||" + a.getName() + "_" + b.getName());
//            Project mProject = mUI.getSelectedProjects().get(0);
//            mProject.addComponent(c);
//        });        
//    }
    
    public Component composite(Component cA, Component cB) {
        Component p = new Component();
        List<ParallelState> pilha = new ArrayList<>();
        List<ParallelState> mVisitedStates = new ArrayList<>();

        System.out.println("ca: " + cA.getInitialState() + " cb:" + cB.getInitialState());
        
        ParallelState ps = new ParallelState(cA.getInitialState(), cB.getInitialState());
        pilha.add(ps);
        ps.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, ps);        
        p.setInitialState(ps.compositeState);
        
        while (!pilha.isEmpty()) {
            ParallelState aux = pilha.remove(0);
            if (mVisitedStates.contains(aux)) {
                continue;
            }
            for (Transition t : aux.a.getOutgoingTransitions()) {
                ParallelState foo = new ParallelState(t.getDestiny(), aux.b);
                foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, foo);

                Transition tt = p.newTransition(aux.compositeState, foo.compositeState);
                tt.setLabel(t.getLabel());                
                
                if (!pilha.contains(foo)) {
                    pilha.add(foo);
                }
            }

            for (Transition t : aux.b.getOutgoingTransitions()) {
                ParallelState foo = new ParallelState(aux.a, t.getDestiny());
                foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, foo);

                Transition tt = p.newTransition(aux.compositeState, foo.compositeState);
                tt.setLabel(t.getLabel());

                if (!pilha.contains(foo)) {
                    pilha.add(foo);
                }
            }
            mVisitedStates.add(aux);
        }
        return p;
    }

    private State criarOuRecuperarEstadoParaOEstadoParalelo(Component p, ParallelState ps) {
        int id = ps.a.getID() * 10 + ps.b.getID();
        State s = p.getStateByID(id);
        if (s == null) {
            s = p.newState(id);
//            if (ps.a.isInitial() || ps.b.isInitial()) {
//                p.setInitialState(s);
//            } else if (ps.a.isFinal() || ps.b.isFinal()) {
//                p.setFinalState(s);
//            } else if (ps.a.isError() || ps.b.isError()) {
//                p.setErrorState(s);
//            }
//            s.setLabel("<" + ps.a.getID() + ", " + ps.b.getID() + ">");           
        }
        return s;
    }

    static class ParallelState {

        public State a;
        public State b;
        public State compositeState;

        public ParallelState(State a, State b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.a);
            hash = 97 * hash + Objects.hashCode(this.b);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ParallelState other = (ParallelState) obj;
            if (!Objects.equals(this.a, other.a)) {
                return false;
            }
            return Objects.equals(this.b, other.b);
        }
    }
}
