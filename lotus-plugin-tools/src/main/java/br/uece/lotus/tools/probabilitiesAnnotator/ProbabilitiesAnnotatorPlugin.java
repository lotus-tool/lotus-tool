package br.uece.lotus.tools.probabilitiesAnnotator;

import br.uece.lotus.tools.probabilitiesAnnotator.ProbabilitiesAnnotatorAlgorithm;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import java.util.List;
import java.util.Random;

/**
 * Created by Ranniery on 25/11/2015.
 */
public class ProbabilitiesAnnotatorPlugin extends Plugin{

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);

       mUserInterface.getMainMenu().newItem("Model/Probabilities Annotator/Equitably")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                    if (mProjectExplorer.getSelectedComponents().size() != 1) {
                        throw new RuntimeException("Select exactly ONE components!");
                    }
                    Component a = mProjectExplorer.getSelectedComponents().get(0);

                    ProbabilitiesAnnotatorAlgorithm.Equitably(a);
                })
                .create();

        mUserInterface.getMainMenu().newItem("Model/Probabilities Annotator/Random")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                    if (mProjectExplorer.getSelectedComponents().size() != 1) {
                        throw new RuntimeException("Select exactly ONE components!");
                    }
                    Component a = mProjectExplorer.getSelectedComponents().get(0);

                    ProbabilitiesAnnotatorAlgorithm.Random(a);
                })
                .create();
    }

}
