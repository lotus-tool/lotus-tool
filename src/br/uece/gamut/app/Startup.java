package br.uece.gamut.app;

import br.uece.mastergraphs.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

    static GrafoView editor;
    private static final String TITULO_APP = "GAMuT";
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FrmPrincipal.fxml"));
		FrmPrincipalController.setEditor((GrafoView) root.getChildrenUnmodifiable().get(1));
		
		stage.setTitle(TITULO_APP);
		stage.setScene(new Scene(root, 600, 500));
		stage.show();
	}	
	
}
