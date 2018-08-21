package br.uece.lotus.uml.app.runtime.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChooserController {

    public static File selectFile(Stage stage) throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Trace File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File traceFile = fileChooser.showOpenDialog(stage);

        if(traceFile == null){
            throw new Exception("Trace File Null!");
        }else {
            return traceFile;
        }

    }


}
