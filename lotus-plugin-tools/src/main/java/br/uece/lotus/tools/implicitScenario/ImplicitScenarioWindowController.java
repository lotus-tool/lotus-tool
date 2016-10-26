/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Aggregator;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Refiner;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Trie;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 *
 * @author Bruno Barbosa
 */

public class ImplicitScenarioWindowController implements Initializable{
    
    @FXML
    private ScrollPane mScrollPane;

    @FXML
    private TableView<ScenarioTableView> mTableView;
    
    private ComponentViewImpl mViewer;
    private ObservableList<ScenarioTableView> data = FXCollections.observableArrayList();
    private Component mComponent;
    private Refiner refiner;
    public Component modificadComponet;
    private ProjectDialogsHelper mProjectDialogsHelper;
    private ProjectExplorer mProjectExplorer;
    private List<String> traceRealModel;
    
    private Boolean sinalizar = false;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);

        mComponent = (Component) resources.getObject("component");
        mViewer.setComponent(mComponent);
        refiner = (Refiner) resources.getObject("Refiner");
        mProjectDialogsHelper = (ProjectDialogsHelper) resources.getObject("mProjectDialogsHelper");
        mProjectExplorer = (ProjectExplorer) resources.getObject("mProjectExplorer");
        traceRealModel = (List<String>) resources.getObject("RealModelTrace");
        List<String> paths = (List<String>) resources.getObject("ListRefined");
        for(String s : paths){
            data.add(new ScenarioTableView(s));
        }
        
        iniciarTable();
    }    

    private void iniciarTable() {
        
        TableColumn scenario = new TableColumn("Implied Scenarios");
        scenario.setCellValueFactory(new PropertyValueFactory<>("implicitScenario"));
        scenario.prefWidthProperty().bind(mTableView.widthProperty().subtract(105));
        
        TableColumn operation = new TableColumn("Operation");
        operation.setCellValueFactory(new PropertyValueFactory<>("Operation"));
        operation.setPrefWidth(90);
        
        Callback<TableColumn<ScenarioTableView,String>,TableCell<ScenarioTableView,String>> cellFactory = new Callback<TableColumn<ScenarioTableView, String>, TableCell<ScenarioTableView,String>>() {
            @Override
            public TableCell call(TableColumn<ScenarioTableView, String> param) {
                final TableCell<ScenarioTableView,String> cell = new TableCell<ScenarioTableView,String>(){
                    
                    @Override
                    public void updateItem( String item, boolean empty ){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                            setText(null);
                        }else{
                            Button btnDelete = new Button();
                            
                            String selecionado = getTableView().getItems().get(getIndex()).implicitScenarioProperty().get();
                            
                            btnDelete.setOnAction((ActionEvent event) -> {
                                actionDelete(selecionado);
                            });
                            ImageView viewImage = new ImageView(new Image(getClass().getResourceAsStream("/image/remove-icon.png")));
                            viewImage.setFitWidth(28);
                            viewImage.setFitHeight(22);
                            
                            btnDelete.setGraphic(viewImage);
                            
                            setGraphic(btnDelete);
                            setText(null);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        };
        
        operation.setCellFactory(cellFactory);
        
        mTableView.setOnMouseClicked(selecionarPath);
        mTableView.setItems(data);
        mTableView.getColumns().addAll(scenario,operation);
        ordenarTabela(mTableView, scenario);
    }
    
    private void actionDelete(String cenarioSelecionado){
        if(cenarioSelecionado.equals("")){
            refiner.removeAllImplicitedScenary();
        }else {
            removeScenary(cenarioSelecionado);
        }

    }

    private void removeScenary( String cenarioSelecionado) {
        ArrayList<String> mListCenariosImplicitos = refiner.getListCenariosImplicitos();
        refiner.removeImplicitedScenary(cenarioSelecionado);

        ArrayList<String> mListClearOneLoopPath = refiner.getListCleanOneLoopPath();
        makePrintFromList(mListCenariosImplicitos,"Scenary:","S.I");
        makePrintFromList(mListClearOneLoopPath,"CLEAN ONE LOOP PATH:","Clear-O-L-P");

        //This segment is doing the builder the Component
        Trie trie = new Trie();
         modificadComponet = trie.createComponet(mListClearOneLoopPath); // return a component witiout scenarys, but it is all open

        //try join Transitons the same label without generate scenarys
        ArrayList<String> mListTraceFromRealModel = refiner.getListTrace();
        Aggregator aggregator = new Aggregator(modificadComponet, mListTraceFromRealModel);
        modificadComponet=aggregator.aggregate();

       
        Project p =  mProjectExplorer.getSelectedProject();
        modificadComponet.setName(p.getName().substring(6, p.getName().length())+countScenarios());
        p.addComponent(modificadComponet);
    }
    
    private int countScenarios() {
        int count = 1;
        Project p = mProjectExplorer.getSelectedProject();
        List<Component> lista = (List<Component>) p.getComponents();
        for(Component c : lista){
            if(c.getName().contains(p.getName().substring(6, p.getName().length()))){
                count++;
            }
        }
        return count;
    }

    private void makePrintFromList(ArrayList<String> list, String title, String tag) {
        System.out.println("\n\n");
        System.out.println(title);
        for (String s : list) {
            System.out.println(tag + " " + s);
        }
    }
    
    private void ordenarTabela(TableView tv, TableColumn tc){
        TableColumn.SortType st = null;
        if (tv.getSortOrder().size()>0) {
            tc = (TableColumn) tv.getSortOrder().get(0);
            st = tc.getSortType().ASCENDING;
        }
        if (tc!=null) {
            tv.getSortOrder().add(tc);
            tc.setSortType(st);
            tc.setSortable(true);
        }
    }
    
    private EventHandler<? super MouseEvent> selecionarPath = (MouseEvent event) -> {
        ScenarioTableView pathView = mTableView.getSelectionModel().getSelectedItem();
        String caminho = pathView.implicitScenarioProperty().get();
        
        String caminhoBom = verificarPontoRuim(caminho);
        String[] partesCaminhoBom = caminhoBom.split(",");
        
        applyDisableAll();
        
        String[] ordem = caminho.split(",");
        State state = mComponent.getInitialState();
        
        for(int i=0;i<ordem.length-1;i++){
            String labelTransition = ordem[i];
            sinalizar = !(i <= partesCaminhoBom.length-1 && labelTransition.contains(partesCaminhoBom[i]));
            
            applyEnableStyle(state);
            
            Transition prox = null;
            List<Transition> transitions = state.getOutgoingTransitionsList();
            for(Transition t : transitions){
                if(state.isInitial()){
                    if(t.getLabel().equals(labelTransition)){
                        prox = t;
                    }
                }else{
                    if(t.getLabel().equals(labelTransition.substring(1, labelTransition.length()))){
                        prox = t;
                    }
                }
            }
            if(prox != null){
                applyEnableStyle(prox);
                state = prox.getDestiny();
            }
            applyEnableStyle(state);
        }
    };
    
    protected void applyEnableStyle(State s){
        String cor;
        if(!sinalizar){
            cor = "black";
        }else{
            cor = "red";
        }
        s.setColor(null);
        s.setTextColor(cor);
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor(cor);
        s.setBorderWidth(1);
    }

    protected void applyEnableStyle(Transition t){
        String cor;
        if(!sinalizar){
            cor = "black";
        }else{
            cor = "red";
        }
        t.setColor(cor);
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor(cor);
        t.setWidth(1);
    }

    protected void applyDisabledStyle(State s){
        s.setColor("#d0d0d0");
        s.setTextColor("#c0c0c0");
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor("gray");
        s.setBorderWidth(1);
    }

    protected void applyDisabledStyle(Transition t){
        t.setColor("#d0d0d0");
        t.setTextColor("#c0c0c0");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setWidth(1);
    }
    
    protected void applyDisableAll(){
        
        State s = mComponent.getInitialState();
        ArrayList<State> stateList = new ArrayList<>();
        ArrayList<Transition> visitedTransitions = new ArrayList<>();
        int i = 0;
        stateList.add(s);

        while (i < stateList.size()){
            
            s = stateList.get(i);
            applyDisabledStyle(s);
            
            for (Transition t : s.getOutgoingTransitions()){
                
                if (!stateList.contains(t.getDestiny())){
                    stateList.add(t.getDestiny());
                }
                
                if (!visitedTransitions.contains(t)){
                    applyDisabledStyle(t);
                    visitedTransitions.add(t);
                }
            }
            ++i;
        }
    }

    private String verificarPontoRuim(String caminho) {
        List<String> selecionados = new ArrayList<>();
        String parteBoa = "";
        
        String[] partesCaminho = caminho.split(",");
        
        for(String model : traceRealModel){
            String pedacoCompativel = "";
            String[] partesModel = model.split(",");
            for(int i=0;i<partesModel.length;i++){
                for(int j=0;j<partesCaminho.length-1;j++){
                    if(i==j){
                        if(partesModel[i].contains(partesCaminho[j])){
                            pedacoCompativel += partesCaminho[j]+",";
                        }
                    }
                }
            }
            selecionados.add(pedacoCompativel);
        }
        
        if(!selecionados.isEmpty()){
            parteBoa = selecionados.get(0);
        }
        
        for(String selecionado : selecionados){
            if(caminho.startsWith(selecionado)){
                parteBoa = selecionado;
            }
        }
        
        return parteBoa;
    }
}
