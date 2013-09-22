package br.uece.gamut.app;

import br.uece.gamut.app.editor.GrafoEditor;
import br.uece.gamut.app.editor.GrafoView;
import br.uece.gamut.parser.GrafoMarshaller;
import br.uece.gamut.parser.GrafoParserFacade;
import br.uece.gamut.parser.GrafoUnmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class MainSceneController implements Initializable {

        private GrafoEditor editor;        
    
        @FXML private GrafoView view;        
	              
	@FXML protected void handleVertice(ActionEvent event) {
		editor.setModo(GrafoEditor.MODO_ADICIONAR_VERTICE);
	}
                
        @FXML protected void handleAbrir(ActionEvent event) {            
            FileChooser fileChooser = new FileChooser(); 
            //Set extension filter
            //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MasterGraphs (*.gph)", "*.gph");
            //fileChooser.getExtensionFilters().add(extFilter);
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                return;
            }
            GrafoUnmarshaller parser = GrafoParserFacade.getUnmarshallerByFile(file);
            //FileInputStream in = new FileInputStream(file);
            try (FileInputStream in = new FileInputStream(file)) {                
                view.clear();
                parser.unmarshaller(in, view);
                in.close();
            } catch (Exception e) {                
                mostrarDialogoErro(e);
            }                     
        }
        
        @FXML protected void handleSalvar(ActionEvent event) {            
            FileChooser fileChooser = new FileChooser();
             
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MasterGraphs (*.gph)", "*.gph");
            fileChooser.getExtensionFilters().add(extFilter);
            
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            try (FileOutputStream out = new FileOutputStream(file)) {                
                GrafoMarshaller parser = GrafoParserFacade.getMarshallerByFile(file);                
                parser.marshaller(view, out);
                out.close();
            } catch (Exception e) {
                mostrarDialogoErro(e);
            }            
        }
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editor = new GrafoEditor();
        editor.setGrafoView(view);
    }

    private void mostrarDialogoErro(Exception e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}
