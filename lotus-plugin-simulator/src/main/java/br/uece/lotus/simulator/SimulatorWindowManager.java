package br.uece.lotus.simulator;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.DefaultWindowManagerPlugin;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.ExtensibleToolbar;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emerson
 */
public class SimulatorWindowManager extends DefaultWindowManagerPlugin<SimulatorWindow> {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;    
    private final Runnable mOpenSimulator = () -> {
        Component c = mProjectExplorer.getSelectedComponent();
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Select a component!");
            return;
        }
        try{
            show(c.clone());
        }catch(CloneNotSupportedException e){}
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        
        final ExtensibleMenu componentMenu = mProjectExplorer.getComponentMenu();
        componentMenu.newItem("-")
        .setWeight(Integer.MAX_VALUE)
        .showSeparator(true)
        .create();
        componentMenu.newItem("Simulator")
        .setWeight(Integer.MAX_VALUE)
        .setAction(mOpenSimulator)
        .create();
        
        final ExtensibleToolbar toolBar = mUserInterface.getToolBar();
        toolBar.newItem("-")
                .setWeight(Integer.MIN_VALUE + 1)
                .showSeparator(true)                
                .create();
        toolBar.newItem("Simulate")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("/images/ic_simulator.png"))
                .setWeight(Integer.MAX_VALUE)
                .setAction(mOpenSimulator)
                .setTooltip("Simulate")
                .create();
    }

    @Override
    protected SimulatorWindow onCreate() {
        SimulatorWindow c = null;
        try {
            URL location = getClass().getResource("/fxml/simulator.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            c = (SimulatorWindow) loader.getController();
            c.setNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    protected void onShow(SimulatorWindow window, Component c) {
        try {
            window.setComponent(c.clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SimulatorWindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onHide(SimulatorWindow window) {

    }
}
