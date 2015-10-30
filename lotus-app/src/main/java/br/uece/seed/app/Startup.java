/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.seed.app;

import br.uece.lotus.Project;
import br.uece.lotus.about.AboutPlugin;
import br.uece.lotus.project.*;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.JarModule;
import br.uece.seed.ext.Module;
import br.uece.seed.ext.Plugin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Startup extends Application {

    private static final Logger logger = Logger.getLogger(Startup.class
            .getName());
    private static Stage mStage;
    private static File extensionsPath;
    private ExtensionManager extensionManager;
    private ProjectExplorer mProjectExplorer ;
    private UserInterface mUserInterface ;
    private ProjectDialogsHelper mProjectDialogsHelper;
    private static final String EXTENSION_DESCRIPTION = "LoTuS files (*.xml)";
    private static final String EXTENSION =  "*.xml";

    private ProjectSerializer mProjectSerializer = new ProjectXMLSerializer();


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return mStage;
    }


    @Override
    public void start(Stage stage) throws Exception {
        mStage = stage;

        URL location = getClass().getResource("/fxml/MainScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        MainSceneController controller = fxmlLoader.getController();

        extensionManager = new ExtensionManager();
        Plugin p = (Plugin) controller;
        extensionManager.registerPlugin(p);
        extensionManager.registerPlugin(new DialogsHelper());
        extensionManager.registerPlugin(new ProjectDialogsHelper());

        //extensionManager.registerPlugin(new PropertiesEditorPlugin());
        extensionManager.registerPlugin(new ProjectExplorerPlugin());
        extensionManager.registerPlugin(new AboutPlugin());
        //extensionManager.registerPlugin(new DesignerWindowManager());
        extensionManager.registerPlugin(new BasicPlugin());
        //extensionManager.registerPlugin(new ToolsPlugin());
        //extensionManager.registerPlugin(new SimulatorWindowManager());
        //extensionManager.registerPlugin(new AnnotatorPlugin());
        // extensionManager.registerPlugin(new AlgorithmsPlugins());

        registerModules(extensionManager);
        extensionManager.start();

        Scene scene = new Scene(root, 700, 500);

        mStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.png")));
        mStage.setMaximized(true);
        mStage.setScene(scene);
        mStage.show();
        mStage.setTitle("LoTuS 3.0");

        mProjectExplorer =extensionManager.get(ProjectExplorer.class);
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectDialogsHelper= extensionManager.get(ProjectDialogsHelper.class);


        mStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to exit LoTuS");
            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeExit = new ButtonType("Not Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeSave,buttonTypeExit, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()== buttonTypeSave){
               /* ExtensibleMenu mMainMenu = mUserInterface.getMainMenu();
                mMainMenu.*/
                for (Project projeto : mProjectExplorer.getAllProjects()) {
                    mProjectDialogsHelper.save(projeto, mProjectSerializer, "Save project", EXTENSION_DESCRIPTION, EXTENSION/*,false*/);

                }
                try {
                    extensionManager.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }

            if (result.get() == buttonTypeExit) {
                try {
                    extensionManager.stop();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result.get() == buttonTypeCancel) {
                Thread.currentThread().run();


            }

        });

        PreLoader.stagePreloader.toFront();
    }


    private void registerModules(ExtensionManager extensionManager) {
        // String aux = System.getProperty("lotus.extensions.path");
        // if (aux == null) {
        // logger.log(Level.INFO, "No extension path defined");
        // return;
        // }
        File homePath;
        try {
            homePath = new File(Startup.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath()).getParentFile();

            extensionsPath = new File(homePath, "extensions");
            logger.log(Level.INFO, "Searching for plugins at {0}...",
                    extensionsPath.getAbsolutePath());
            File[] arqs = extensionsPath.listFiles();
            if (arqs == null) {
                return;
            }
            for (File arq : arqs) {
                if (arq.getName().endsWith(".jar")) {
                    try {
                        Module m = new JarModule(arq);
                        extensionManager.registerModule(m);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Fail at module loading!", e);
                    }
                }
            }
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
