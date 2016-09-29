/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     
        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);
        
        mComponent = (Component) resources.getObject("component");
        mViewer.setComponent(mComponent);
        
        List<String> paths = (List<String>) resources.getObject("paths");
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
                            HBox box = new HBox(5);
                            Button btnOk = new Button();
                            Button btnCancel = new Button();
                            
                            String selecionado = getTableView().getItems().get(getIndex()).implicitScenarioProperty().get();
                            btnOk.setOnAction((ActionEvent event) -> {
                                acaoOK(selecionado);
                            });
                            btnOk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/Ok_Scenario.png"))));
                            btnCancel.setOnAction((ActionEvent event) -> {
                                acaoCancel(selecionado);
                            });
                            btnCancel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/Cancel_Scenario.png"))));
                            box.getChildren().addAll(btnOk,btnCancel);
                            setGraphic(box);
                            setText(null);
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
    
    private void acaoOK(String cenarioSelecionado){
        System.out.println("ativou o botao ok, cenario: "+cenarioSelecionado);
    }
    
    private void acaoCancel(String cenarioSelecionado){
        System.out.println("ativou o botao cancel, cenario: "+cenarioSelecionado);
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
        
        applyDisableAll();
        
        String[] ordem = caminho.split(",");
        State state = mComponent.getInitialState();
        
        for(int i=0;i<ordem.length;i++){
            applyEnableStyle(state);
            String labelTransition = ordem[i];
            Transition prox = null;
            List<Transition> transitions = state.getOutgoingTransitionsList();
            for(Transition t : transitions){
                if(t.getLabel().equals(labelTransition)){
                    prox = t;
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
        s.setColor(null);
        s.setTextColor("black");
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor("black");
        s.setBorderWidth(1);
    }

    protected void applyEnableStyle(Transition t){
        t.setColor("black");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor("black");
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
}
