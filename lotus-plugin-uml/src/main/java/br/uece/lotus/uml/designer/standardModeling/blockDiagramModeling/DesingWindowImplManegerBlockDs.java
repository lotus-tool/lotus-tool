package br.uece.lotus.uml.designer.standardModeling.blockDiagramModeling;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.window.DefaultWindowManagerPluginDS;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindow;
import br.uece.seed.ext.ExtensionManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

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
        DesingWindowImplBlockDs dwImpDS = null;
        try {
            URL location = getClass().getResource("/fxml/SceneDesingWindowImplBlockDs.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            dwImpDS = (DesingWindowImplBlockDs) loader.getController();
            dwImpDS.setNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dwImpDS;
    }

    @Override
    protected void onShow(DesingWindowImplBlockDs window, ComponentBuildDS buildDS) {}

    @Override
    protected void onShow(DesingWindowImplBlockDs window, ComponentDS cds) {window.setComponentDS(cds);}

    @Override
    protected void onShow(DesingWindowImplBlockDs window, Component c) {}

    @Override
    protected void onHide(DesingWindowImplBlockDs window) {}
}
