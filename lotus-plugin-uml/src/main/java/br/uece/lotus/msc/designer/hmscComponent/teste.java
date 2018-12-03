package br.uece.lotus.msc.designer.hmscComponent;

import br.uece.lotus.Component;
import br.uece.lotus.model.ParallelCompositor;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import java.util.List;

public class teste extends Plugin {
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    private Runnable mAbout = () -> {
        Component newPC = ParallelComposition(mProjectExplorer.getSelectedComponents());
        mProjectExplorer.getSelectedProject().addComponent(newPC);
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);
        mUserInterface = extensionManager.get(UserInterface.class);
        mUserInterface.getMainMenu().newItem("Help/teste")
                .setWeight(Integer.MAX_VALUE)
                .setAction(mAbout).create();

    }


    public Component ParallelComposition(List<Component> Components){
        int tam = Components.size();
        if (tam < 2) {
            throw new RuntimeException("Select at least 2(two) components!");
        }
        Component a = Components.get(0);
        Component b = Components.get(1);
        Component c = new ParallelCompositor().compor(a, b);
        String name = a.getName() + " || " + b.getName();
        for(int i = 2; i < tam; i++){
            b = Components.get(i);
            c = new ParallelCompositor().compor(c, b);
            name += " || " + b.getName();
        }
        c.setName(name);
        return c;
    }

    @Override
    public void onStop(ExtensionManager extensionManager) throws Exception {
        super.onStop(extensionManager);
    }
}
