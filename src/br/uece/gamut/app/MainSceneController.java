package br.uece.gamut.app;

import br.uece.gamut.app.editor.GrafoEditor;
import br.uece.gamut.parser.GrafoMarshaller;
import br.uece.gamut.parser.GrafoParserFacade;
import br.uece.gamut.parser.GrafoUnmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.event.ActionEvent;
import br.uece.gamut.Grafo;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

public class MainSceneController {

    @FXML
    protected GrafoEditor editor;
    @FXML
    protected Button btnNovo;
    @FXML
    protected Button btnAbrir;
    @FXML
    protected Button btnSalvar;
    @FXML
    protected ToggleButton btnDefault;
    @FXML
    protected ToggleButton btnMover;
    @FXML
    protected ToggleButton btnAddEstado;
    @FXML
    protected ToggleButton btnAddLigacao;
    @FXML
    protected ToggleButton btnDelEstado;
    @FXML
    protected ToggleButton btnDelLigacao;
    ToggleButton group;

    @FXML
    protected void handleNovo(ActionEvent event) {
        editor.clear();
    }

    @FXML
    protected void handleAbrir(ActionEvent event) {
        File file = selecionarArquivo();
        GrafoUnmarshaller parser = GrafoParserFacade.getUnmarshallerByFile(file);
        try (FileInputStream in = new FileInputStream(file)) {
            editor.clear();
            parser.unmarshaller(in, editor);
            editor.layoutGrafo();
        } catch (Exception e) {
            mostrarDialogoErro(e);
        }
    }

    @FXML
    protected void handleSalvar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MasterGraphs (*.gph)", "*.gph");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            GrafoMarshaller parser = GrafoParserFacade.getMarshallerByFile(file);
            parser.marshaller(editor, out);
            out.close();
        } catch (Exception e) {
            mostrarDialogoErro(e);
        }
    }

    @FXML
    protected void handleMouseDefault(ActionEvent event) {
        editor.setModo(GrafoEditor.MODO_NENHUM);
        editor.setCursor(Cursor.DEFAULT);
    }

    @FXML
    protected void handleMover(ActionEvent event) {
        editor.setModo(GrafoEditor.MODO_MOVER_VERTICE);
        editor.setCursor(Cursor.MOVE);
    }

    @FXML
    protected void handleAddVertice(ActionEvent event) {        
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_ADICIONAR_VERTICE);        
    }

    @FXML
    protected void handleAddLigacao(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_ADICIONAR_LIGACAO);
    }

    @FXML
    protected void handleDelVertice(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_REMOVER_VERTICE);
    }

    @FXML
    protected void handleDelLigacao(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_REMOVER_LIGACAO);
    }

    private void mostrarDialogoErro(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
    }

    private File selecionarArquivo() {
        editor.setCursor(Cursor.DEFAULT);
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MasterGraphs (*.gph)", "*.gph");
        //fileChooser.getExtensionFilters().add(extFilter);
        //Show open file dialog
        return fileChooser.showOpenDialog(null);
    }
}
