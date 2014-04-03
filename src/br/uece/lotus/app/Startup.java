package br.uece.lotus.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

    private static Stage mStage;
    
    public static void setAppTitle(String text) {
        mStage.setTitle(text == null ? "LoTuS" : (text + " - LoTuS"));
    }    

    @Override
    public void start(Stage stage) throws Exception {
        mStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }
}
