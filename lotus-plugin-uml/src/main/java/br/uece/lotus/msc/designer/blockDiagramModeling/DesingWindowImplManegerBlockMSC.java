package br.uece.lotus.msc.designer.blockDiagramModeling;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.window.DefaultWindowManagerPluginMSC;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
import br.uece.seed.ext.ExtensionManager;

/**
 * Created by Lucas Vieira Alves on 15/01/16.
 */
public class DesingWindowImplManegerBlockMSC extends DefaultWindowManagerPluginMSC<DesingWindowImplBlockMSC> {

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }

    @Override
    protected DesingWindowImplBlockMSC onCreate() {
        try{
        return new DesingWindowImplBlockMSC();}
        catch (Exception e){
        throw new RuntimeException(e);}
    }

    @Override
    protected void onShow(DesingWindowImplBlockMSC window, HmscComponent buildDS) {
    }

    @Override
    protected void onShow(DesingWindowImplBlockMSC window, BmscComponent cds) {window.setBmscComponent(cds);}

    @Override
    protected void onShow(DesingWindowImplBlockMSC window, Component c, ProjectExplorerPluginMSC mProjectExplorerDS) {}

    @Override
    protected void onHide(DesingWindowImplBlockMSC window) {}

    @Override
    protected DesingWindowImplBlockMSC onCreateStandadM(ProjectExplorerPluginMSC pep) {
        return null;
    }
}
