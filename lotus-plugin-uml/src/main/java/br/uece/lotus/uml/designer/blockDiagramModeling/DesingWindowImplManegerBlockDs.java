package br.uece.lotus.uml.designer.blockDiagramModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.window.DefaultWindowManagerPluginDS;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.seed.ext.ExtensionManager;

/**
 * Created by lva on 15/01/16.
 */
public class DesingWindowImplManegerBlockDs extends DefaultWindowManagerPluginDS<DesingWindowImplBlockDs> {

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }

    @Override
    protected DesingWindowImplBlockDs onCreate() {
        try{
        return new DesingWindowImplBlockDs();}
        catch (Exception e){
        throw new RuntimeException(e);}
    }

    @Override
    protected void onShow(DesingWindowImplBlockDs window, StandardModeling buildDS) {
    }

    @Override
    protected void onShow(DesingWindowImplBlockDs window, ComponentDS cds) {window.setComponentDS(cds);}

    @Override
    protected void onShow(DesingWindowImplBlockDs window, Component c, ProjectExplorerPluginDS mProjectExplorerDS) {}

    @Override
    protected void onHide(DesingWindowImplBlockDs window) {}

    @Override
    protected DesingWindowImplBlockDs onCreateStandadM(ProjectExplorerPluginDS pep) {
        System.out.println("Metodo nao deve ser utilizado ainda");
        return null;
    }
}
