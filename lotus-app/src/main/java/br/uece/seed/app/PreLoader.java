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

    public static Stage stagePreloader;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stagePreloader = primaryStage;
        
        Parent splash = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));
            
        stagePreloader.initStyle(StageStyle.TRANSPARENT);
        Scene cena = new Scene(splash);
        cena.setFill(Color.TRANSPARENT);
        stagePreloader.setScene(cena);
        stagePreloader.setResizable(false);
        stagePreloader.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.png")));
        stagePreloader.show();
    }
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt){
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (stagePreloader.isShowing()) {
                PauseTransition pt = new PauseTransition(Duration.seconds(3));
                FadeTransition ft = new FadeTransition(
                    Duration.millis(4000), stagePreloader.getScene().getRoot());
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    final Stage s = stagePreloader;
                    EventHandler<ActionEvent> eh = (ActionEvent t) -> {
                        s.close();
                };
                ft.setOnFinished(eh);
                    
                SequentialTransition st = new SequentialTransition(pt,ft);
                st.play();
                
            } else {
                stagePreloader.hide();
            }
        }
    }
    
}
