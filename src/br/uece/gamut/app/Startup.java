package br.uece.gamut.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {
    
    private static final String TITULO_APP = "GAMuT";
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));		
		stage.setTitle(TITULO_APP);
		stage.setScene(new Scene(root, 600, 500));
		stage.show();
	}	
	
}
