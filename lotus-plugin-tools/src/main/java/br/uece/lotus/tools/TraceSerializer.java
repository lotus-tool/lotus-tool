package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by lucas on 04/03/15.
 */
public class TraceSerializer implements ProjectSerializer {

        @Override
        public Project parseStream(InputStream stream) throws Exception {
            //analizar
            Project p = new Project();
            TraceParser parser = new TraceParser();
            Component c = parser.parseFile(stream);
            p.addComponent(c);
            return p;
        }


    @Override
    public void toStream(Project p, OutputStream stream) throws Exception {
        PrintStream out = new PrintStream(stream);
        int componentIndex = 0;
        for (Component c : p.getComponents()) {
            out.printf("%s = Q%d,", c.getName().toUpperCase(), c.getStateIndex(c.getInitialState()));

            int stateIndex = 0;
            for (State s : c.getStates()) {
                if (stateIndex > 0) {
                    out.printf(",\n");
                }
//                int transitionsCount = s.getOutgoingTransitionsCount();
//                if (transitionsCount == 0) {
//                    continue;
//                }
                out.printf("\nQ%d = ", stateIndex);
                if (s.isFinal()) {
                    out.printf("STOP");
                } else {
                    int transitionIndex = 0;
                    out.printf("(");
                    for (Transition t : s.getOutgoingTransitions()) {
                        if (transitionIndex > 0) {
                            out.printf("\n|");
                        }
                        int aux = c.getStateIndex(t.getDestiny());
                        out.printf("%s -> Q%d", t.getLabel(), aux);
                        transitionIndex++;
                    }
                    out.printf(")");
                }
                stateIndex++;
            }
            out.println(".");
        }
    }


}
