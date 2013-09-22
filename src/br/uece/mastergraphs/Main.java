package br.uece.mastergraphs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	static GrafoView editor;
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FrmPrincipal.fxml"));
		FrmPrincipalController.setEditor((GrafoView) root.getChildrenUnmodifiable().get(1));
		
		stage.setTitle("Master Graphs");
		stage.setScene(new Scene(root, 600, 500));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
