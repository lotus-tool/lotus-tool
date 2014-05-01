package br.uece.lotus.app;

import br.uece.lotus.model.ComponentModel;
import br.uece.lotus.model.StateModel;
import br.uece.lotus.model.TransitionModel;
import br.uece.lotus.view.ComponentEditor;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author emerson
 */
public class ComposicaoParalela {

    public ComponentModel composite(ComponentModel cA, ComponentModel cB) {
        ComponentModel p = new ComponentModel();
        List<ParallelState> pilha = new ArrayList<>();

        ParallelState ps = new ParallelState(cA.getVertices().get(0), cB.getVertices().get(0));
        pilha.add(ps);
        ps.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, ps);

        while (!pilha.isEmpty()) {
            ParallelState aux = pilha.remove(0);
            for (TransitionModel t : aux.a.getTransicoesSaida()) {
                ParallelState foo = new ParallelState(t.getDestino(), aux.b);                
                foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, foo);
                
                TransitionModel tt = p.newTransicao(aux.compositeState, foo.compositeState);
                tt.setValue(ComponentEditor.TAG_LABEL, t.getValue(ComponentEditor.TAG_LABEL));

                if (!pilha.contains(foo)) {
                    pilha.add(foo);
                }
            }

            for (TransitionModel t : aux.b.getTransicoesSaida()) {
                ParallelState foo = new ParallelState(aux.a, t.getDestino());
                foo.compositeState = criarOuRecuperarEstadoParaOEstadoParalelo(p, foo);

                TransitionModel tt = p.newTransicao(aux.compositeState, foo.compositeState);
                tt.setValue(ComponentEditor.TAG_LABEL, t.getValue(ComponentEditor.TAG_LABEL));

                if (!pilha.contains(foo)) {
                    pilha.add(foo);
                }
            }
        }
        new SimpleLayouter().run(p);
        return p;
    }

    private StateModel criarOuRecuperarEstadoParaOEstadoParalelo(ComponentModel p, ParallelState ps) {        
        int id = ps.a.getID() * 10 + ps.b.getID();
        StateModel s = p.getVertice(id);
        if (s == null) {
            s = p.newVertice(id);
            s.setValue(ComponentEditor.TAG_LABEL, "<" + ps.a.getID() + ", " + ps.b.getID() + ">");
        }
        return s;
    }
}
class ParallelState {

    public StateModel a;
    public StateModel b;
    public StateModel compositeState;

    public ParallelState(StateModel a, StateModel b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ParallelState)) {
            return false;
        }
        ParallelState aux = (ParallelState) obj;
        return this.a.equals(aux.a) && this.b.equals(b);
    }
}
