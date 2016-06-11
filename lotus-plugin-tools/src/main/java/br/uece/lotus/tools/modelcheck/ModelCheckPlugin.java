/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.modelcheck;

import br.uece.lotus.tools.probabilisticReach.ProbabilisticReachAlgorithm;
import br.uece.lotus.tools.modelcheck.UnreachableStates;
import br.uece.lotus.tools.modelcheck.ProbabilitiesCheck;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author emerson
 */

public class ModelCheckPlugin extends Plugin {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    public Component ParallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
        Component c = new ParallelCompositor().compor(a, b);
        String name = a.getName() + " || " + b.getName();
        for(int i = 2; i < tam; i++){
            c = new ParallelCompositor().compor(c, b);
            name += " || " + b.getName();
        }
        c.setName(name);
        return c;
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);

        mProjectExplorer.getComponentMenu().newItem("Probabilities Check")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                    if (mProjectExplorer.getSelectedComponents().size() != 1) {
                        throw new RuntimeException("Select exactly ONE component!");
                    }
                    Component a = mProjectExplorer.getSelectedComponents().get(0);
                    List<State> inconsistentStates = new ProbabilitiesCheck().checkProbabilities(a);

                    String output = new String();
                    int tam = inconsistentStates.size();
                    if(tam > 0){
                        output += inconsistentStates.get(0).getLabel();
                        for (int i = 1; i < tam; i++) {
                            output += ", " + inconsistentStates.get(i).getLabel();
                        }
                        JOptionPane.showMessageDialog(null, "The sum of the probabilities of transitions from State(s) "
                                                            + output + " is lower/greater than 1.",
                                "Inconsistent States Detection", JOptionPane.WARNING_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "No inconsistent states!",
                                "Inconsistent States Detection", JOptionPane.INFORMATION_MESSAGE);
                    }

                })
                .create();

        mProjectExplorer.getComponentMenu().newItem("Parallel Composition")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                    Component newPC = ParallelComposition(mProjectExplorer.getSelectedComponents());
                    mProjectExplorer.getSelectedProject().addComponent(newPC);
                })
                .create();

        mUserInterface.getMainMenu().newItem("Verification/Unreachable States")
            .setWeight(Integer.MIN_VALUE+20)
            .setAction(() -> {
            if (mProjectExplorer.getSelectedComponents().size() != 1) {
                throw new RuntimeException("Select exactly ONE component!");
                }     
                Component a = mProjectExplorer.getSelectedComponents().get(0);
                List<State> unreachables = new UnreachableStates().detectUnreachableStates(a);
                String output = new String();
                int tam = unreachables.size();
                if(unreachables.size() > 0){
                    output += unreachables.get(0).getLabel();
                    for (int i = 1; i < unreachables.size(); i++) {
                        output += ", " + unreachables.get(i).getLabel();
                    }
                    output += ".";
                    Object[] options = {"Yes", "No"};
                    int dialogResult = JOptionPane.showOptionDialog(null, "Unreachable States: " + output + "\n" + 
                                                                    "Would you like to remove those states? ", "Unreachable States",
                                                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                    null, options, options[0]);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        new UnreachableStates().remove(unreachables, a);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "No unreachable states!");
                }
            })
                .create();

        mUserInterface.getMainMenu().newItem("Verification/Deadlock Detection")
                .setWeight(Integer.MIN_VALUE + 20)
                .setAction(() -> {
                    if (mProjectExplorer.getSelectedComponents().size() != 1) {
                        throw new RuntimeException("Select exactly ONE component!");
                    }
                    Component a = mProjectExplorer.getSelectedComponents().get(0);
                    List<State> deadlocks = new DeadlockDetection().detectDeadlocks(a);
                    String output = new String();
                    int tam = deadlocks.size();
                    if (deadlocks.size() > 0) {
                        output += deadlocks.get(0).getLabel();
                        for (int i = 1; i < deadlocks.size(); i++) {
                            output += ", " + deadlocks.get(i).getLabel();
                        }
                        output += ".";
                        JOptionPane.showMessageDialog(null, "Deadlock States: " + output,
                                "Deadlock Detection", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No deadlocks!",
                                "Deadlock Detection", JOptionPane.INFORMATION_MESSAGE);
                    }
                })
                .create();
        
    }

}
