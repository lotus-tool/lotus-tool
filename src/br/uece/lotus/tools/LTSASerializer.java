/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class LTSASerializer implements ProjectSerializer {
    
    @Override
    public Project parseStream(InputStream stream) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
