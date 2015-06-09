package br.uece.lotus.tools.layout;

import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

/**
 * Created by emerson on 09/06/15.
 */
public class LayoutsPlugins extends Plugin {

    private UserInterface mUserInterface;
    private final Runnable mTreeLayout = new Runnable() {
        @Override
        public void run() {
            new TreeLayouter().layout(mProjectExplorer.getSelectedComponent());
        }
    };
    private ProjectExplorer mProjectExplorer;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);
        ExtensibleMenu mMainMenu = mUserInterface.getMainMenu();
        mMainMenu.newItem("Model/Layout/Tree layout")
                .setWeight(Integer.MIN_VALUE + 1)
                .setAction(mTreeLayout)
                .create();
    }
}
