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
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.layout.TreeLayouter;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.util.Callback;

/**
 *
 * @author Bruno Barbosa && Yan Gurgel
 */
public class ImplicitScenarioWindowController implements Initializable {

    @FXML
    private ScrollPane mScrollPane;

    @FXML
    private TableView<ScenarioTableView> mTableView;

    private ComponentViewImpl mViewer;
    private ObservableList<ScenarioTableView> data = FXCollections.observableArrayList();
    private Component mComponent;
    public Component mComponentAlterado;
    private ProjectExplorer mProjectExplorer;
    private ArrayList<String> pathsFromTraceModel;
    private ArrayList<String> pathsScenarioImplied;

    private Boolean sinalizar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            mViewer = new ComponentViewImpl();
            mScrollPane.setContent(mViewer);

            mComponent = (Component) resources.getObject("component");
            mComponentAlterado = mComponent.clone();
            mViewer.setComponent(mComponent);
            mProjectExplorer = (ProjectExplorer) resources.getObject("mProjectExplorer");
            pathsFromTraceModel = (ArrayList<String>) resources.getObject("TraceModelo");
            pathsScenarioImplied = (ArrayList<String>) resources.getObject("CenariosImplicitos");

            System.out.println("Trace entrada: " + pathsFromTraceModel.size());
            System.out.println("Implieds: " + pathsScenarioImplied.size());

            for (String s : pathsScenarioImplied) {
                data.add(new ScenarioTableView(s));
            }

            iniciarTable();

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ImplicitScenarioWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void iniciarTable() {

        TableColumn scenario = new TableColumn("Implied Scenarios");
        scenario.setCellValueFactory(new PropertyValueFactory<>("implicitScenario"));
        scenario.prefWidthProperty().bind(mTableView.widthProperty().subtract(105));

        TableColumn operation = new TableColumn("Operation");
        operation.setCellValueFactory(new PropertyValueFactory<>("Operation"));
        operation.setPrefWidth(90);

        Callback<TableColumn<ScenarioTableView, String>, TableCell<ScenarioTableView, String>> cellFactory = new Callback<TableColumn<ScenarioTableView, String>, TableCell<ScenarioTableView, String>>() {
            @Override
            public TableCell call(TableColumn<ScenarioTableView, String> param) {
                final TableCell<ScenarioTableView, String> cell = new TableCell<ScenarioTableView, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Button btnDelete = new Button();

                            String selecionado = getTableView().getItems().get(getIndex()).implicitScenarioProperty().get();

                            btnDelete.setOnAction((ActionEvent event) -> {
                                try {
                                    actionDelete(selecionado);
                                } catch (CloneNotSupportedException ex) {
                                    Logger.getLogger(ImplicitScenarioWindowController.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
        mTableView.getColumns().addAll(scenario, operation);
        ordenarTabela(mTableView, scenario);
    }

    private void actionDelete(String cenarioSelecionado) throws CloneNotSupportedException {
        actionWindow(cenarioSelecionado, "RemoveScenario");
        Project p = mProjectExplorer.getSelectedProject();
        mComponentAlterado.setName(p.getName().substring(6, p.getName().length()) + countScenarios());
        resetarPosicoes();
        new TreeLayouter().layout(mComponentAlterado);
        p.addComponent(mComponentAlterado.clone());
    }

    private int countScenarios() {
        int count = 1;
        Project p = mProjectExplorer.getSelectedProject();
        List<Component> lista = (List<Component>) p.getComponents();
        for (Component c : lista) {
            if (c.getName().contains(p.getName().substring(6, p.getName().length()))) {
                count++;
            }
        }
        return count;
    }

    private void ordenarTabela(TableView tv, TableColumn tc) {
        TableColumn.SortType st = null;
        if (tv.getSortOrder().size() > 0) {
            tc = (TableColumn) tv.getSortOrder().get(0);
            st = TableColumn.SortType.ASCENDING;
        }
        if (tc != null) {
            tv.getSortOrder().add(tc);
            tc.setSortType(st);
            tc.setSortable(true);
        }
    }

    private final EventHandler<? super MouseEvent> selecionarPath = (MouseEvent event) -> {
        ScenarioTableView pathView = mTableView.getSelectionModel().getSelectedItem();
        String caminho = pathView.implicitScenarioProperty().get();
        actionWindow(caminho, "viewTrace");
    };

    protected void applyEnableStyle(State s) {
        String cor;
        if (!sinalizar) {
            cor = "black";
        } else {
            cor = "red";
        }
        s.setColor(null);
        s.setTextColor(cor);
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor(cor);
        s.setBorderWidth(1);
    }

    protected void applyEnableStyle(Transition t) {
        String cor;
        if (!sinalizar) {
            if (t.getColor().equals("red")) {
                cor = "red";
            } else {
                cor = "black";
            }
        } else {
            cor = "red";
        }
        t.setColor(cor);
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor(cor);
        t.setWidth(1);
    }

    protected void applyDisabledStyle(State s) {
        s.setColor("#d0d0d0");
        s.setTextColor("#c0c0c0");
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor("gray");
        s.setBorderWidth(1);
    }

    protected void applyDisabledStyle(Transition t) {
        t.setColor("#d0d0d0");
        t.setTextColor("#c0c0c0");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setWidth(1);
    }

    protected void applyDisableAll() {

        State s = mComponent.getInitialState();
        ArrayList<State> stateList = new ArrayList<>();
        ArrayList<Transition> visitedTransitions = new ArrayList<>();
        int i = 0;
        stateList.add(s);

        while (i < stateList.size()) {

            s = stateList.get(i);
            applyDisabledStyle(s);

            for (Transition t : s.getOutgoingTransitions()) {

                if (!stateList.contains(t.getDestiny())) {
                    stateList.add(t.getDestiny());
                }

                if (!visitedTransitions.contains(t)) {
                    applyDisabledStyle(t);
                    visitedTransitions.add(t);
                }
            }
            ++i;
        }
    }

    private HashMap<Integer, Transition> transitionsPathSelected;

    private void actionWindow(String pathSelected, String type) {
        switch (type) {

            case "viewTrace": {
                if (transitionsPathSelected == null) {
                    transitionsPathSelected = new HashMap<>();
                } else {
                    transitionsPathSelected.clear();
                }
                State currentState = mComponent.getInitialState();
                Transition currentTransition = null;
                String[] partesCaminho = pathSelected.split(",");
                int tamPedacoCaminho = partesCaminho.length;
                int ponteiroPosicao = 0;
                boolean caminhoOK = false;
                applyDisableAll();
                System.out.println("Caminho selecionado : " + pathSelected);
                System.out.println("Tamanho do caminho selecionado: " + tamPedacoCaminho);
                System.out.println("Ponteiro posicao: " + ponteiroPosicao);
                while (transitionsPathSelected.size() < tamPedacoCaminho) {
                    System.out.println("Dentro do while hashMap menor que tamanho do caminho");
                    CopyOnWriteArrayList<Transition> currentStateSaidas = new CopyOnWriteArrayList<>();
                    currentStateSaidas.addAll(currentState.getOutgoingTransitionsList());
                    boolean encontrou = false;
                    for (Transition t : currentStateSaidas) {
                        if (t.getLabel().equals(partesCaminho[ponteiroPosicao].trim()) && t != currentTransition || currentTransition == null) {
                            System.out.println("Encontrou a Transicao: " + t.getLabel());
                            transitionsPathSelected.put(ponteiroPosicao, t);
                            System.out.println("Inserio no hashmap");
                            currentTransition = t;
                            System.out.println("Transicao corrente: " + t.getLabel());
                            currentState = t.getDestiny();
                            System.out.println("Proximo state: " + currentState.getLabel());
                            ponteiroPosicao++;
                            System.out.println("Proxima posicao do ponteiro: " + ponteiroPosicao);
                            encontrou = true;
                            break;
                        }
                    }
                    if (!encontrou && currentTransition != null) {
                        System.out.println("Nao encontrou a transicao");
                        currentState = currentTransition.getSource();
                        System.out.println("Voltou ao state: " + currentState.getLabel());
                        ponteiroPosicao--;
                        System.out.println("Ponteiro volto para: " + ponteiroPosicao);
                        transitionsPathSelected.remove(ponteiroPosicao);
                        System.out.println("Removi a transicao do hashmap: " + currentTransition);
                    }
                }
                System.out.println("-------------------------------------------------");
                ponteiroPosicao = 0;
                System.out.println("Ponteiro posicao resetado: " + ponteiroPosicao);
                Transition implicita = null;
                while (!caminhoOK) {
                    System.out.println("Dentro do while do caminhoOK");
                    System.out.println("Transicao Implicita: " + implicita);
                    String pathTemp = "";
                    for (int i = 0; i <= ponteiroPosicao; i++) {
                        pathTemp += partesCaminho[i].trim() + ", ";
                    }
                    System.out.println("PathTemp: " + pathTemp);
                    if (verificarTraceEntrada(pathTemp)) {
                        System.out.println("PathTempo permitido nos traces de entrada");
                        if (ponteiroPosicao < tamPedacoCaminho) {
                            ponteiroPosicao++;
                            System.out.println("Proxima posicao do ponteiro: " + ponteiroPosicao);
                        }
                    } else {
                        if (implicita == null) {
                            implicita = transitionsPathSelected.get(ponteiroPosicao);
                            System.out.println("Achou a implicita: " + implicita.getLabel());
                            ponteiroPosicao = tamPedacoCaminho;
                        }
                    }
                    if (ponteiroPosicao == tamPedacoCaminho) {
                        System.out.println("Ponteiro igual ao tamanho do caminho");
                        caminhoOK = true;
                        
                    }
                }
                for (Transition t : transitionsPathSelected.values()) {
                    for (Transition tComponent : mComponent.getTransitions()) {
                        if (t == tComponent) {
                            sinalizar = t == implicita && implicita != null;
                            applyEnableStyle(tComponent.getSource());
                            applyEnableStyle(tComponent.getDestiny());
                            applyEnableStyle(tComponent);
                            System.out.println("Sinalizar = " + sinalizar + " pintou os states e transition: " + tComponent.getLabel());
                        }
                    }
                }
                System.out.println("terminou a pintura");
            }
            break;

            case "RemoveScenario": {
                try {
                    String[] partesCaminho = pathSelected.split(",");
                    String currentCaminho = partesCaminho[0].trim();
                    State currentState = mComponentAlterado.getInitialState();
                    String pathTemp = currentCaminho + ", ";
                    ramoPorPronfundidade(currentState, currentCaminho, pathTemp);
                } catch (ConcurrentModificationException e) {
                    //Cai nessa exception quando volta pro state inicial, pois ja modifiquei ele criando o novo ramo a partir dele
                    System.out.println("Caiu na exception Concurrent");
                }

                stateVisitados.clear();
                ligacaoRamoComOriginal.clear();

                if (transitionInicialSelecionada != null) {
                    State destino = transitionInicialSelecionada.getDestiny();
                    mComponentAlterado.remove(transitionInicialSelecionada);
                    List<Transition> caminhosIniciais = mComponentAlterado.getInitialState().getTransitionsTo(destino);
                    if (caminhosIniciais == null || caminhosIniciais.isEmpty()) {
                        mComponentAlterado = recuperarRamosCriados();
                        statesInicialEntrada.clear();
                    }
                }
            }
            break;
        }
    }

    private final HashMap<State, Integer> stateVisitados = new HashMap<>();
    private final HashMap<State, State> ligacaoRamoComOriginal = new HashMap<>();
    private final List<State> statesInicialEntrada = new CopyOnWriteArrayList<>();
    private Transition transitionInicialSelecionada = null;

    private void ramoPorPronfundidade(State currentState, String currentCaminho, String pathTemp) {
        boolean caminhoInicial = false;
        //verifica se ja foi visitado
        if (stateVisitados.containsKey(currentState)) {
            int oldValue = stateVisitados.get(currentState);
            stateVisitados.replace(currentState, oldValue, oldValue++);
        } else {
            stateVisitados.put(currentState, 1);
        }
        //preenche a ligacao com o state inicial
        if (currentState.isInitial()) {
            ligacaoRamoComOriginal.put(currentState, currentState);
            caminhoInicial = true;
        }
        //percorre as saidas
        for (Transition tOut : currentState.getOutgoingTransitions()) {
            if (caminhoInicial) {
                System.out.println("Eh inicial");
                if (tOut.getLabel().equals(currentCaminho.trim())) {
                    transitionInicialSelecionada = tOut;
                    if (verificarTraceEntrada(pathTemp)) {
                        //criar ramo
                        State dstRamo = mComponentAlterado.newState(mComponentAlterado.getStatesCount() + 1);
                        ligacaoRamoComOriginal.put(tOut.getDestiny(), dstRamo);
                        mComponentAlterado.buildTransition(ligacaoRamoComOriginal.get(tOut.getSource()), dstRamo)
                                .setLabel(tOut.getLabel())
                                .setViewType(0)
                                .create();
                        ramoPorPronfundidade(tOut.getDestiny(), null, pathTemp); // falta fazer as verificacoes de visitados
                    } else {
                        return;
                    }
                }
            } else {
                String pathTemp2 = pathTemp + tOut.getLabel() + ", ";
                if (verificarTraceEntrada(pathTemp2)) {
                    //criar ramo
                    if (stateVisitados.containsKey(tOut.getDestiny())) {
                        if (stateVisitados.containsKey(tOut.getSource())) {
                            State srcLig = ligacaoRamoComOriginal.get(tOut.getSource());
                            State dstLig = ligacaoRamoComOriginal.get(tOut.getDestiny());
                            Transition tEntreVisitados = srcLig.getTransitionTo(dstLig);
                            if (tEntreVisitados != null) {
                                String labelEntreVisitados = tEntreVisitados.getLabel();
                                if (labelEntreVisitados.equals(tOut.getLabel())) {
                                    ramoPorPronfundidade(tOut.getDestiny(), null, pathTemp2);
                                }
                            } else {
                                mComponentAlterado.buildTransition(srcLig, dstLig)
                                        .setLabel(tOut.getLabel())
                                        .setViewType(1)
                                        .create();
                                ramoPorPronfundidade(tOut.getDestiny(), null, pathTemp2);
                            }

                        } else {
                            State dstRamo = ligacaoRamoComOriginal.get(tOut.getDestiny());
                            mComponentAlterado.buildTransition(ligacaoRamoComOriginal.get(tOut.getSource()), dstRamo)
                                    .setLabel(tOut.getLabel())
                                    .setViewType(1)
                                    .create();
                            ramoPorPronfundidade(tOut.getDestiny(), null, pathTemp2);
                        }
                    } else {
                        State dstRamo = mComponentAlterado.newState(mComponentAlterado.getStatesCount() + 1);
                        ligacaoRamoComOriginal.put(tOut.getDestiny(), dstRamo);
                        mComponentAlterado.buildTransition(ligacaoRamoComOriginal.get(tOut.getSource()), dstRamo)
                                .setLabel(tOut.getLabel())
                                .setViewType(0)
                                .create();
                        ramoPorPronfundidade(tOut.getDestiny(), null, pathTemp2);
                    }
                }
            }
        }
    }

    private boolean verificarTraceEntrada(String pathTemp) {
        for (String s : pathsFromTraceModel) {
            if (s.startsWith(pathTemp.substring(0, pathTemp.length() - 2))) {
                return true;
            }
        }
        return false;
    }

    private Component recuperarRamosCriados() {

        Component ramos = new Component();

        Component cIni = null;
        Project p = mProjectExplorer.getSelectedProject();
        for (Component c : p.getComponents()) {
            if (c.getName().equals(p.getName().substring(6, p.getName().length()))) {
                cIni = c;
            }
        }
        if (cIni.getStates() != null) {
            statesInicialEntrada.addAll((Collection<? extends State>) cIni.getStates());
            for (State s : statesInicialEntrada) {
                if (s.isInitial()) {
                    statesInicialEntrada.remove(s);
                }
            }
            System.out.println("----------------States iniciais recuperados-----------------");
            statesInicialEntrada.stream().forEach((s) -> {
                System.out.println("State label: " + s.getLabel() + "  ID: " + s.getID());
            });
            System.out.println("----------------States do component todo-----------------");
            for (State s : mComponentAlterado.getStates()) {
                System.out.println("State label: " + s.getLabel() + "  ID: " + s.getID());
            }
        }

        //Guardando a diferenca
        for (State s : mComponentAlterado.getStates()) {
            if (!statesInicialEntrada.contains(s)) {
                ramos.add(s);
                if (s.isInitial()) {
                    ramos.setInitialState(s);
                }
                List<Transition> t = new CopyOnWriteArrayList<>();
                t.addAll(s.getOutgoingTransitionsList());
                t.stream().forEach((tr) -> {
                    ramos.add(tr);
                });
            }
        }

        return ramos;
    }

    private void resetarPosicoes() {
        for (State s : mComponentAlterado.getStates()) {
            s.setLayoutX(0);
            s.setLayoutY(0);
        }
    }
}
