/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
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
public class StandardModelingWindowManager extends DefaultWindowManagerPluginDS<StandardModelingWindow>{

    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
    }
    
    @Override
    protected StandardModelingWindow onCreate() {
        System.out.println("Passou no onCreate()");
        StandardModelingWindow c = null;
        try {
            URL location = getClass().getResource("/fxml/SceneStandardModeling.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            c = (StandardModelingWindow) loader.getController();
            c.setNode(root);
            System.out.println("carregou e setou a tela no smw");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    protected void onShow(StandardModelingWindow window, ComponentBuildDS buildDS) {
        window.setComponentBuildDS(buildDS);
        System.out.println("Ligou a window com o buildds");
    }

    @Override
    protected void onHide(StandardModelingWindow window) {
        
    }
    
    @Override
    protected void onShow(StandardModelingWindow window, ComponentDS cds) {}
    @Override
    protected void onShow(StandardModelingWindow window, Component c) {}

    
    
}
