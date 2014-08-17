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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectSerializer;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author emerson
 */
public class PrismSerializer implements ProjectSerializer {

    private static final String[] varNames = {"x", "y", "z"};

    public void toStream(Component c, OutputStream stream) {
        PrintStream out = new PrintStream(stream);
        out.println("dtmc");
        out.println("");
        serializeComponent(c, out, varNames[0]);
    }

    @Override
    public void toStream(Project p, OutputStream stream) throws Exception {
        PrintStream out = new PrintStream(stream);
        out.println("dtmc");
        out.println("");
        int componentIndex = 0;
        for (Component c : p.getComponents()) {
            serializeComponent(c, out, varNames[componentIndex++]);            
        }

    }

    @Override
    public Project parseStream(InputStream stream) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void serializeComponent(Component c, PrintStream out, String stateVarName) {
        String nome = c.getName();
        nome = nome.substring(0, nome.length() - 4);
        out.println("module " + nome);
        out.println("");       
        int indiceDefaultState = c.getStateIndex(c.getInitialState()) + 1;
        out.printf("%s: [1..%d] init %d;\n", stateVarName, c.getStatesCount(), indiceDefaultState);
        out.println("");

        int stateIndex = 0;
        for (State s : c.getStates()) {
            int transitionsCount = s.getOutgoingTransitionsCount();
            if (transitionsCount == 0) {
                continue;
            }
            out.printf("[] %s=%d ->", stateVarName, stateIndex);
            double defaultProbability = 1.0 / transitionsCount;
            int transitionIndex = 0;
            for (Transition t : s.getOutgoingTransitions()) {
                double prob = t.getProbability() == null ? defaultProbability : t.getProbability();
//                    double prob = t.getProbability();
                int aux = c.getStateIndex(t.getDestiny()) + 1;
                if (transitionIndex > 0) {
                    out.print(" +");
                }
                out.printf(" %s:(%s' = %d)", ("" + prob).replaceAll(",", "."), stateVarName, aux);
                transitionIndex++;
            }
            out.printf(";\n");
            stateIndex++;
        }
        out.println("");
        out.println("endmodule");
        out.println("");
    }

}
