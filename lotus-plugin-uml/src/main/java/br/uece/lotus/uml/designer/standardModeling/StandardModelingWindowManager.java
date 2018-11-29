/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.window.DefaultWindowManagerPluginDS;
import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
import br.uece.seed.ext.ExtensionManager;

/**
 *
 * @author Bruno Barbosa
 */
public class StandardModelingWindowManager extends DefaultWindowManagerPluginDS<StandardModelingWindowImpl>{

    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }
    
    @Override
    protected StandardModelingWindowImpl onCreateStandadM(ProjectExplorerPluginDS pep) {
        try{
            return new StandardModelingWindowImpl(pep);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected StandardModelingWindowImpl onCreate() {
        return null;
    }

    @Override
    protected void onShow(StandardModelingWindowImpl window, StandardModeling buildDS) {
        window.setComponentBuildDS(buildDS);
    }

    @Override
    protected void onHide(StandardModelingWindowImpl window) {
        
    }
    
    @Override
    protected void onShow(StandardModelingWindowImpl window, ComponentDS cds) {}
    @Override
    protected void onShow(StandardModelingWindowImpl window, Component c, ProjectExplorerPluginDS mProjectExplorerDS) {}

    

    
    
}
