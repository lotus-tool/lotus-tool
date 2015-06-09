package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.helpers.window.DefaultWindowManagerPlugin;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

import javax.script.ScriptException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by emerson on 09/06/15.
 */
public class RunnerWindowManager extends DefaultWindowManagerPlugin<RunnerWindow> {

    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;
    private final Runnable mRunComponent = () -> {
        Component c = mProjectExplorer.getSelectedComponent();
        if (c == null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "Select a component!");
            });
            return;
        }
        show(c);
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);

        mUserInterface.getMainMenu()
                .newItem("Run/Run component")
                .setWeight(Integer.MIN_VALUE + 2)
                .setAccelerator(KeyCode.R, KeyCombination.CONTROL_DOWN)
                .setAction(mRunComponent)
                .create();

        mUserInterface.getToolBar().newItem(null)
                .setGraphic(getClass().getResourceAsStream("/images/ic_run.png"))
                .setWeight(Integer.MAX_VALUE)
                .setAction(mRunComponent)
                .create();

        mProjectExplorer.getComponentMenu().newItem("Run component")
                .setAction(mRunComponent)
                .create();
    }

    @Override
    protected RunnerWindow onCreate() {
        RunnerWindow c = null;
        try {
            URL location = getClass().getResource("/fxml/runner.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            c = (RunnerWindow) loader.getController();
            c.setNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    protected void onShow(RunnerWindow window, Component c) {
        try {
            window.setComponent(c.clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RunnerWindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onHide(RunnerWindow window) {

    }
}
