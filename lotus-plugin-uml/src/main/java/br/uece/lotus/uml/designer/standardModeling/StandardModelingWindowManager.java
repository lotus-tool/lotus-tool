/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.viewer.builder.ComponentBuildDSViewImpl;
import br.uece.lotus.uml.api.window.DefaultWindowManagerPluginDS;
import br.uece.seed.ext.ExtensionManager;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

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
    protected StandardModelingWindowImpl onCreate() {
        try{
            return new StandardModelingWindowImpl();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onShow(StandardModelingWindowImpl window, ComponentBuildDS buildDS) {
        window.setComponentBuildDS(buildDS);
    }

    @Override
    protected void onHide(StandardModelingWindowImpl window) {
        
    }
    
    @Override
    protected void onShow(StandardModelingWindowImpl window, ComponentDS cds) {}
    @Override
    protected void onShow(StandardModelingWindowImpl window, Component c) {}

    
    
}
