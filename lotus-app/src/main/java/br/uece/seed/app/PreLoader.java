/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.seed.app;

import static com.sun.javafx.PlatformUtil.isEmbedded;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Bruno Barbosa
 */
public class PreLoader extends Preloader{

    private Stage stage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        
        Parent splash = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));
            
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene cena = new Scene(splash);
        cena.setFill(Color.TRANSPARENT);
        stage.setScene(cena);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.png")));
        stage.show();
    }
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt){
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (stage.isShowing()) {
                PauseTransition pt = new PauseTransition(Duration.seconds(3));
                FadeTransition ft = new FadeTransition(
                    Duration.millis(4000), stage.getScene().getRoot());
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    final Stage s = stage;
                    EventHandler<ActionEvent> eh = (ActionEvent t) -> {
                        s.close();
                };
                ft.setOnFinished(eh);
                    
                SequentialTransition st = new SequentialTransition(pt,ft);
                st.play();
                
            } else {
                stage.hide();
            }
        }
    }
    
}
