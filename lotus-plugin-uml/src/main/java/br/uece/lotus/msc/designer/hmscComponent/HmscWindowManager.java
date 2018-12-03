/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.window.DefaultWindowManagerPluginMSC;
import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
import br.uece.seed.ext.ExtensionManager;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscWindowManager extends DefaultWindowManagerPluginMSC<HmscWindowMSCImpl> {

    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }
    
    @Override
    protected HmscWindowMSCImpl onCreateStandadM(ProjectExplorerPluginMSC projectExplorerPluginMSC) {
        try{
            return new HmscWindowMSCImpl(projectExplorerPluginMSC);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected HmscWindowMSCImpl onCreate() {
        return null;
    }

    @Override
    protected void onShow(HmscWindowMSCImpl window, HmscComponent hmscComponent) {
        window.setHmscComponent(hmscComponent);
    }

    @Override
    protected void onHide(HmscWindowMSCImpl window) {
        
    }
    
    @Override
    protected void onShow(HmscWindowMSCImpl window, BmscComponent bmscComponent) {}
    @Override
    protected void onShow(HmscWindowMSCImpl window, Component c, ProjectExplorerPluginMSC projectExplorerPluginMSC) {}

    

    
    
}
