package br.uece.lotus.designer;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.DefaultWindowManagerPlugin;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.properties.PropertiesEditor;
import br.uece.seed.ext.ExtensionManager;

import java.io.IOException;

/**
 * Created by emerson on 16/04/15.
 */
public class NewDesignerWindowManager extends DefaultWindowManagerPlugin<NewDesignerWindow> {

    private ProjectExplorer mProjectExplorer;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);

        mProjectExplorer.getComponentMenu()
                .newItem("Edit (New)")
                .setWeight(Integer.MIN_VALUE)
                .setAction(() -> {
                    Component c = mProjectExplorer.getSelectedComponent();
                    show(c);
                })
                .create();
    }


    @Override
    protected NewDesignerWindow onCreate() {
        try {
            return new NewDesignerWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onShow(NewDesignerWindow window, Component c) {
        window.setComponent(c);
    }

    @Override
    protected void onHide(NewDesignerWindow window) {

    }
}
