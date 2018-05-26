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
package br.uece.lotus.tools.probabilisticReach;

import br.uece.lotus.tools.probabilisticReach.ProbabilisticReach;
import br.uece.lotus.tools.probabilisticReach.ProbabilisticReachPlugin;
import br.uece.lotus.tools.probabilisticReach.ProbabilisticReachWindow;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.tools.modelcheck.DeadlockDetection;
import br.uece.lotus.model.ParallelCompositor;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.properties.PropertiesEditor;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ranniery
 */
public class ProbabilisticReachPlugin extends Plugin implements ProbabilisticReach {
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private PropertiesEditor mPropertiesEditor;
    private final Runnable mOpenProbabilisticReach = () -> {
        try {
            Component c = mProjectExplorer.getSelectedComponent();
            if (c == null) {
                JOptionPane.showMessageDialog(null, "Select a component!");
                return;
            }
            show(c.clone(), true);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ProbabilisticReachPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        
        mUserInterface.getMainMenu().newItem("Verification/Probabilistic Reachability")
                .setWeight(Integer.MIN_VALUE+20)
                .setAction(mOpenProbabilisticReach)
                .create();

    }

    @Override
    public void show(Component c, boolean editable) {
        ProbabilisticReachWindow w = new ProbabilisticReachWindow();
        w.setComponent(c);
        int id = mUserInterface.getCenterPanel().newTab(c.getName() + " - [Probabilistic Reachability]", w,3, true);
        mUserInterface.getCenterPanel().showTab(id);
    }
}
