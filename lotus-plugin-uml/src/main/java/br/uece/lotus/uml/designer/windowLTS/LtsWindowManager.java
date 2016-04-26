/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.windowLTS;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.window.DefaultWindowManagerPluginDS;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.seed.ext.ExtensionManager;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsWindowManager extends DefaultWindowManagerPluginDS<LtsWindowImpl>{
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }
    
    @Override
    protected void onShow(LtsWindowImpl window, Component c) {
        window.setComponentLTS(c);
    }
    
    @Override
    protected LtsWindowImpl onCreate() {
        try{
            return new LtsWindowImpl();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected LtsWindowImpl onCreateStandadM(ProjectExplorerPluginDS pep) {return null;}

    @Override
    protected void onShow(LtsWindowImpl window, StandardModeling buildDS) {}

    @Override
    protected void onShow(LtsWindowImpl window, ComponentDS cds) {}

    @Override
    protected void onHide(LtsWindowImpl window) {}
    
}
