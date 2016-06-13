package br.uece.lotus.tools.randomComponentGenerator;

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
import br.uece.lotus.tools.randomComponentGenerator.RCG_Algorithm;
import br.uece.lotus.model.ParallelCompositor;

/**
 * Created by Ranniery on 04/05/2016.
 */
public class RCG_Plugin extends Plugin{

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);

        mUserInterface.getMainMenu().newItem("Model/Generate Random Component")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                    String statesInput = (String) JOptionPane.showInputDialog(
                            null, "How many states?:\n",
                            "Number of States", JOptionPane.QUESTION_MESSAGE);

                    if (!statesInput.matches("\\d+")) {
                        JOptionPane.showMessageDialog(null, "Please provide an integer!",
                                "Number of States", JOptionPane.WARNING_MESSAGE);
                    }

                    int tam = Integer.parseInt(statesInput);

                    Component randomComponent = RCG_Algorithm.generateRandomComponent(tam);
                    randomComponent.setName("RandomComponent" + tam);
                    br.uece.lotus.model.ParallelCompositor.layout(randomComponent);

                    mProjectExplorer.getSelectedProject().addComponent(randomComponent);


                })
                .create();

    }
}
