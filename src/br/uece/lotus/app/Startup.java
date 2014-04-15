package br.uece.lotus.app;

import java.net.URI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.JSPFProperties;

public class Startup extends Application {

    private static Stage mStage;

    public static void setAppTitle(String text) {
        mStage.setTitle(text == null ? "LoTuS" : (text + " - LoTuS"));
    }

    @Override
    public void start(Stage stage) throws Exception {
//        final JSPFProperties props = new JSPFProperties();
//        props.setProperty(PluginManager.class, "cache.enabled", "true");
//        props.setProperty(PluginManager.class, "cache.mode", "weak"); //optional
//        props.setProperty(PluginManager.class, "cache.file", "jspf.cache");

        PluginManager pm = PluginManagerFactory.createPluginManager();
        pm.addPluginsFrom(new URI("classpath://*"));        
//        pm.addPluginsFrom(new URI("classpath://br.uece.lotus.plugins/*"));  

        mStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }
}
