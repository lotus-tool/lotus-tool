package br.uece.lotus.runner;

import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerson on 27/03/15.
 */
public class RunnerPlugin extends Plugin {

    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);

        mProjectExplorer.getComponentMenu().newItem("Run")
                .setAction(() -> {
                    try {
                        Runner r = new Runner(mProjectExplorer.getSelectedComponent());
                        RunnerWindow w = new RunnerWindow(r);
                        w.show(mUserInterface);
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                })
                .create();
    }

}
