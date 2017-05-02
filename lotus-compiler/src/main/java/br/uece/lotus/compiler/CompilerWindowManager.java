package br.uece.lotus.compiler;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.helpers.window.DefaultWindowManagerPlugin;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.viewer.ComponentView;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.ExtensibleToolbar;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 *
 * @author Yan Gurgel
 */
public class CompilerWindowManager extends DefaultWindowManagerPlugin<CompilerWindow> {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private ComponentView mViewer;
    private final Runnable mOpenCompiler = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        Component c = p.getComponent(0);
        if (c == null) {
            //Component c1 = new Component();
            //Project p = mProjectExplorer.getSelectedProject();
            //p.addComponent(c1);

            //*** CHAMAR TELA***
            //System.out.println(""); 
            //onCreate();
            JOptionPane.showMessageDialog(null, "Component NULL...");

            return;
        }
        try {
            show(c.clone());
        } catch (CloneNotSupportedException e) {
        }
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);

        /**
         * *CLIQUE NO PROJETO
         */
        final ExtensibleMenu projectMenu = mProjectExplorer.getProjectMenu();
        projectMenu.newItem("-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        projectMenu.newItem("Compiler")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mOpenCompiler)
                .create();
        projectMenu.newItem("-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        projectMenu.newItem("-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        /**
         * *CLIQUE NO COMPONENT
         */
//        final ExtensibleMenu componentMenu = mProjectExplorer.getComponentMenu();
//        componentMenu.newItem("-")
//                .setWeight(Integer.MAX_VALUE)
//                .showSeparator(true)
//                .create();
//        componentMenu.newItem("Compiler")
//                .setWeight(Integer.MAX_VALUE)
//                .setAction(mOpenCompiler)
//                .create();
        /**
         * *BOTÃO
         */
//        final ExtensibleToolbar toolBar = mUserInterface.getToolBar();
//        toolBar.newItem("-")
//                .setWeight(Integer.MIN_VALUE + 1)
//                .showSeparator(true)
//                .create();
//        toolBar.newItem("Compiler")
//                .hideText(true)
//                .setGraphic(getClass().getResourceAsStream("/images/ic_compiler.png"))
//                .setWeight(Integer.MAX_VALUE)
//                .setAction(mOpenCompiler)
//                .setTooltip("Compiler")
//                .create();
    }

    @Override
    protected CompilerWindow onCreate() {
        CompilerWindow c = null;
        ResourceBundle bundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                if (key.equals("userInterface")) {
                    return mUserInterface;
                }
                if (key.equals("projectMenu")) {
                    return mProjectExplorer;
                }
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
        try {
            URL location = getClass().getResource("/fxml/Compiler.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setResources(bundle);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load(location.openStream());
            c = (CompilerWindow) loader.getController();
            c.setNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    protected void onShow(CompilerWindow window, Component c) {
        try {
            window.setComponent(c.clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CompilerWindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onHide(CompilerWindow window) {

    }

}
