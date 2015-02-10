/*
 * The MIT License
 *
 * Copyright 2014 emerson.
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
package br.uece.lotus.model;

import br.uece.lotus.Component;
import br.uece.lotus.designer.DesignerWindowImpl;
import br.uece.lotus.designer.DesignerWindowManager;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author emerson
 */
public class AlgorithmsPlugins extends Plugin {

    private ProjectExplorer mProjectExplorer;
    private DesignerWindowManager mDesignerWindow;
    private final Runnable mDoParallelComposition = () -> {
        List<Component> c = mProjectExplorer.getSelectedComponents();
        if (c.size() != 2) {
            JOptionPane.showMessageDialog(null, "select at lest 2 components!");
            return;
        }
        Component r = new ParallelComposition().composite(c.get(0), c.get(1));
        r.setName("||" + c.get(0).getName() + "_" + c.get(1).getName());
        new BasicLayouterImpl().layout(r);
        mProjectExplorer.getSelectedProject().addComponent(r);
        mDesignerWindow.show(r);
    };    

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {        
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mDesignerWindow = extensionManager.get(DesignerWindowManager.class);

        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE + 1, "-", null);
        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE + 1, "Parallel composition", mDoParallelComposition);
    }

}
