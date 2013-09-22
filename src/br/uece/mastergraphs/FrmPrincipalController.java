package br.uece.mastergraphs;

import br.uece.mastergraphs.algoritmos.BuscaEmLargura;
import br.uece.mastergraphs.algoritmos.BuscaEmProfundidade;
import br.uece.mastergraphs.algoritmos.Coloracao;
import br.uece.mastergraphs.algoritmos.Conexao;
import br.uece.mastergraphs.algoritmos.Kruskal;
import br.uece.mastergraphs.algoritmos.MinimoCaminho;
import br.uece.mastergraphs.algoritmos.OrdenacaoTopologica;
import br.uece.mastergraphs.algoritmos.Prim;
import br.uece.mastergraphs.algoritmos.ResetaCor;
import br.uece.mastergraphs.model.Grafo2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;

public class FrmPrincipalController implements Initializable {

	private Algoritmo[] algoritmos = {
            new ResetaCor(),
            new BuscaEmLargura(), 
            new BuscaEmProfundidade(),
            new Coloracao(),
            new Conexao(),
            new Kruskal(),
            new MinimoCaminho(),
            new OrdenacaoTopologica(),
            new Prim()                        
        };
                
        @FXML protected MenuButton btnAlgoritmos;
        
        private static GrafoView editor;
        private EventHandler<ActionEvent> aplicarAlgoritmoHandle = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
               Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Algoritmo algoritmo = (Algoritmo) ((MenuItem)event.getSource()).getUserData();
                        Object resultado = algoritmo.rodar(editor.getGrafo());
                        System.out.println(resultado);
                    }
                };
                Thread t = new Thread(run);
                t.start();   
            }
    };
	              
	@FXML protected void handleVertice(ActionEvent event) {
		editor.setModo(GrafoView.Modo.VERTICE);
	}
	@FXML protected void handleLigacao(ActionEvent event) {
		editor.setModo(GrafoView.Modo.LIGACAO);
	}
	@FXML protected void handleMover(ActionEvent event) {
		editor.setModo(GrafoView.Modo.MOVER);
	}
	@FXML protected void handleSelecionar(ActionEvent event) {
		editor.setModo(GrafoView.Modo.SELECAO);
	}
	@FXML protected void handleApagar(ActionEvent event) {
		editor.setModo(GrafoView.Modo.APAGAR);
	}
	@FXML protected void handleNovo(ActionEvent event) {            
            editor.getGrafo().clear();
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
            try {
                FileInputStream in = new FileInputStream(file);
                GrafoParser parser = new GrafoParser();
                editor.getGrafo().clear();
                parser.load(editor.getGrafo(), in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }                     
        }
        
        @FXML protected void handleSalvar(ActionEvent event) {            
            FileChooser fileChooser = new FileChooser();
 
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MasterGraphs (*.gph)", "*.gph");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show open file dialog
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                GrafoParser parser = new GrafoParser();
                parser.save(editor.getGrafo(), out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }            
        }
        @FXML protected void handleSobre(ActionEvent event) {            
                     
        }
	
	public static void setEditor(GrafoView e) {
		editor = e;	
                editor.setGrafo(new Grafo2());
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAlgoritmos.getItems().clear();
        for (Algoritmo alg: algoritmos) {
                    MenuItem menu = new MenuItem(alg.getClass().getSimpleName());
                    menu.setOnAction(aplicarAlgoritmoHandle);
                    menu.setUserData(alg);
                    btnAlgoritmos.getItems().add(menu);
        }
        //editor.setGrafo(new Grafo2());
    }
	
}
