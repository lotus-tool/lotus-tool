package br.uece.gamut.app;

import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import br.uece.gamut.app.editor.GrafoEditor;
import br.uece.gamut.app.editor.MapEditor;
import br.uece.gamut.parser.GrafoMarshaller;
import br.uece.gamut.parser.GrafoParserFacade;
import br.uece.gamut.parser.GrafoUnmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

public class MainSceneController implements Initializable, GrafoEditor.OnSelectionChange {

    @FXML
    protected ToggleButton btnDefault;
    @FXML
    protected ToggleButton btnVertice;
    @FXML
    protected ToggleButton btnTransicao;
    @FXML
    protected ToggleButton btnApagar;
    @FXML
    protected GrafoEditor editor;
    @FXML
    protected VBox pnlPropriedadesTransicao;
    @FXML
    protected TextField edtRotulo;
    @FXML
    protected VBox pnlPropriedadesVertice;
    @FXML
    protected CheckBox ckbDefault;
    private Vertice mVerticeEmEdicao;
    private Transicao mTransicaoEmEdicao;
    @FXML
    private TextField edtProbabilidade;

    @FXML
    protected void handleNovo(ActionEvent event) {
        editor.clear();
    }

    private void mostrarDialogoErro(Exception e) {
        JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
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
    protected void handleDefault(ActionEvent event) {
        editor.setModo(GrafoEditor.MODO_NENHUM);
        editor.setCursor(Cursor.DEFAULT);
    }

    @FXML
    protected void handleVertice(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_VERTICE);
    }

    @FXML
    protected void handleTransicao(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_TRANSICAO);
    }

    @FXML
    protected void handleApagar(ActionEvent event) {
        editor.setCursor(Cursor.DEFAULT);
        editor.setModo(GrafoEditor.MODO_REMOVER);
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

    @FXML
    protected void handleSair(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    protected void handleSobre(ActionEvent event) {
        System.out.println("handle sobre");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editor.setOnSelectionChange(this);
        
        ToggleGroup g = new ToggleGroup();
        btnDefault.setToggleGroup(g);
        btnVertice.setToggleGroup(g);
        btnTransicao.setToggleGroup(g);
        btnApagar.setToggleGroup(g);
        
//        ckbDefault.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
//                mVerticeEmEdicao.setTag(GrafoEditor.TAG_DEFAULT, ckbDefault.isSelected());
//            }
//        });
        edtRotulo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                mTransicaoEmEdicao.setTag(GrafoEditor.TAG_LABEL, edtRotulo.getText());
            }
        });
        edtProbabilidade.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Double d = (Double) mTransicaoEmEdicao.getTag(GrafoEditor.TAG_PROBABILIDADE);
                try {
                    d = Double.parseDouble(edtProbabilidade.getText());
                } catch (Exception e) {
                    //ignora
                }
                mTransicaoEmEdicao.setTag(GrafoEditor.TAG_PROBABILIDADE, d);
            }
        });
        pnlPropriedadesTransicao.setVisible(mTransicaoEmEdicao != null);
    }

    @Override
    public void onOnSelectionChange(GrafoEditor editor, int objectKind) {
        mTransicaoEmEdicao = editor.getTransicaoSelecionada();        
        pnlPropriedadesTransicao.setVisible(mTransicaoEmEdicao != null);
        System.out.println("t: " + mTransicaoEmEdicao);
        if (mTransicaoEmEdicao != null) {
            exibirPropriedadesDaTransicao();
        }
    }

    private void exibirPropriedadesDaTransicao() {
        edtRotulo.setText((String) mTransicaoEmEdicao.getTag(GrafoEditor.TAG_LABEL));
        edtProbabilidade.setText(mTransicaoEmEdicao.getTag(GrafoEditor.TAG_PROBABILIDADE).toString());
    }
}
