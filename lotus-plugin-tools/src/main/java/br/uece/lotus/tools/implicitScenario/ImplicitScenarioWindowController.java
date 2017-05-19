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
import br.uece.lotus.viewer.TransitionView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    private List<String> pathsFromTraceModel;
    private List<String> pathsScenarioImplied = new CopyOnWriteArrayList<>();
    private List<String> pathsOLP = new CopyOnWriteArrayList<>();

    private Boolean sinalizar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            mViewer = new ComponentViewImpl();
            mScrollPane.setContent(mViewer);

            mComponent = (Component) resources.getObject("component");
            mComponentAlterado = new Component();
            mViewer.setComponent(mComponent);
            mProjectExplorer = (ProjectExplorer) resources.getObject("mProjectExplorer");
            pathsFromTraceModel = (ArrayList<String>) resources.getObject("TraceModelo");
            pathsScenarioImplied.addAll((Collection<? extends String>) resources.getObject("CenariosImplicitos"));
            pathsOLP.addAll((Collection<? extends String>) resources.getObject("OLP"));

            System.out.println("Trace entrada: " + pathsFromTraceModel.size());
            System.out.println("Implieds: " + pathsScenarioImplied.size());

            for (String s : pathsScenarioImplied) {
                data.add(new ScenarioTableView(s));
            }

            iniciarTable();

        } catch (Exception ex) {
            Logger.getLogger(ImplicitScenarioWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actionDelete(String cenarioSelecionado) throws CloneNotSupportedException {
        actionWindow(cenarioSelecionado, "RemoveScenario");
        Project p = mProjectExplorer.getSelectedProject();
        mComponentAlterado.setName(p.getName().substring(6, p.getName().length()) + countScenarios());
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

    private final EventHandler<? super MouseEvent> selecionarPath = (MouseEvent event) -> {
        ScenarioTableView pathView = mTableView.getSelectionModel().getSelectedItem();
        String caminho = pathView.implicitScenarioProperty().get();
        actionWindow(caminho, "viewTrace");
    };

    private HashMap<Integer, Transition> transitionsPathSelected;

    private void actionWindow(String pathSelected, String type) {
        switch (type) {
//------------------------------------------------------PINTURA------------------------------------------------------------------------------------------
            case "viewTrace": {
                if (transitionsPathSelected == null) {
                    transitionsPathSelected = new HashMap<>();
                } else {
                    transitionsPathSelected.clear();
                }
                State currentState = mComponent.getInitialState();
                Transition currentTransition = null;
                String[] partesCaminho = pathSelected.split(", ");
                int tamPedacoCaminho = partesCaminho.length;
                int ponteiroPosicao = 0;
                boolean caminhoOK = false;
                applyDisableAll();
//                System.out.println("Caminho selecionado : " + pathSelected);
//                System.out.println("Tamanho do caminho selecionado: " + tamPedacoCaminho);
//                System.out.println("Ponteiro posicao: " + ponteiroPosicao);
                while (transitionsPathSelected.size() < tamPedacoCaminho) {
                    //System.out.println("Dentro do while hashMap menor que tamanho do caminho");
                    CopyOnWriteArrayList<Transition> currentStateSaidas = new CopyOnWriteArrayList<>();
                    currentStateSaidas.addAll(currentState.getOutgoingTransitionsList());
                    boolean encontrou = false;
                    for (Transition t : currentStateSaidas) {
//                        System.out.println("t currentstatesaidas: ----------> "+t.getLabel());
//                        System.out.println("parte do caminho na posicao: "+ponteiroPosicao+" com label: "+partesCaminho[ponteiroPosicao]);
//                        System.out.println(t.getLabel()+" eh igual a "+partesCaminho[ponteiroPosicao]+" ?: "+(t.getLabel().equals(partesCaminho[ponteiroPosicao])));
                        if (t.getLabel().equals(partesCaminho[ponteiroPosicao]) && (t != currentTransition || currentTransition == null)) {
                            //System.out.println("Encontrou a Transicao: " + t.getLabel());
                            transitionsPathSelected.put(ponteiroPosicao, t);
                            //System.out.println("Inserio no hashmap");
                            currentTransition = t;
                            //System.out.println("Transicao corrente: " + t.getLabel());
                            currentState = t.getDestiny();
                            //System.out.println("Proximo state: " + currentState.getLabel());
                            ponteiroPosicao++;
                            //System.out.println("Proxima posicao do ponteiro: " + ponteiroPosicao);
                            encontrou = true;
                            break;
                        }
                    }
                    if (!encontrou && currentTransition != null) {
                        //System.out.println("Nao encontrou a transicao");
                        currentState = currentTransition.getSource();
                        //System.out.println("Voltou ao state: " + currentState.getLabel());
                        ponteiroPosicao--;
                        //System.out.println("Ponteiro volto para: " + ponteiroPosicao);
                        transitionsPathSelected.remove(ponteiroPosicao);
                        //System.out.println("Removi a transicao do hashmap: " + currentTransition);
                    }
                }
                //System.out.println("-------------------------------------------------");
                ponteiroPosicao = 0;
                //System.out.println("Ponteiro posicao resetado: " + ponteiroPosicao);
                Transition implicita = null;
                while (!caminhoOK) {
//                    System.out.println("Dentro do while do caminhoOK");
//                    System.out.println("Transicao Implicita: " + implicita);
                    String pathTemp = "";
                    for (int i = 0; i <= ponteiroPosicao; i++) {
                        pathTemp += partesCaminho[i].trim() + ", ";
                    }
                    //System.out.println("PathTemp: " + pathTemp);
                    if (verificarTraceEntrada(pathTemp)) {
                        //System.out.println("PathTempo permitido nos traces de entrada");
                        if (ponteiroPosicao < tamPedacoCaminho) {
                            ponteiroPosicao++;
                            //System.out.println("Proxima posicao do ponteiro: " + ponteiroPosicao);
                        }
                    } else {
                        if (implicita == null) {
                            implicita = transitionsPathSelected.get(ponteiroPosicao);
                            //System.out.println("Achou a implicita: " + implicita.getLabel());
                            ponteiroPosicao = tamPedacoCaminho;
                        }
                    }
                    if (ponteiroPosicao == tamPedacoCaminho) {
                        //System.out.println("Ponteiro igual ao tamanho do caminho");
                        caminhoOK = true;

                    }
                }
                for (Transition t : transitionsPathSelected.values()) {
                    for (Transition tComponent : mComponent.getTransitions()) {
                        if (t.equals(tComponent)) {
                            sinalizar = t == implicita && implicita != null;
                            applyEnableStyle(tComponent.getSource());
                            applyEnableStyle(tComponent.getDestiny());
                            applyEnableStyle(tComponent);
                            //System.out.println("Sinalizar = " + sinalizar + " pintou os states e transition: " + tComponent.getLabel());
                        }
                    }
                }
                //System.out.println("terminou a pintura");
            }
            break;
//-------------------------------------------------------REMOCAO-----------------------------------------------------------------------------------------------
            case "RemoveScenario": {
                pathsOLP = pathsOLP.stream().filter(p-> !p.equals(pathSelected)).collect(Collectors.toList());
                pathsScenarioImplied = pathsScenarioImplied.stream().filter(p-> !p.equals(pathSelected)).collect(Collectors.toList());
            //----------------------Step 1 Separar em listas de caminhos com loop e sem loop -----------------------------    
                List<String> pathsSemLoop = new CopyOnWriteArrayList<>();
                List<String> pathsComLoop = new CopyOnWriteArrayList<>();
                pathsOLP.stream().forEach(path -> {
                    boolean contemLoop = verificarLoopInPath(path);
                    if(contemLoop){
                        pathsComLoop.add(path);
                    }else{
                        pathsSemLoop.add(path);
                    }
                }); 
                mostrarCaminhos("SemLoops", pathsSemLoop);
                mostrarCaminhos("ComLoops", pathsComLoop);
            //--------------------Step 2 Analizar Loops em Ciclos e Montar Linha do tempo Transformando em LTS-------------
                boolean processo = false;
                while(!processo){
                    Map<Integer,CicloOuNao> linhaDoTempo = new HashMap<>();
                    int indexMax = 0;
                    //primeiro os com loop
                    if(!pathsComLoop.isEmpty()){
                        String pathMenor = verificarPathMenor(pathsComLoop);
                        preencherLinhaDoTempo(pathMenor, linhaDoTempo, indexMax);
                        //remover path do pathComLoop
                        pathsComLoop.remove(pathMenor);
                    }
                    //Depois de finalizar o pathsComLoop pode verificar os semLoop
                    if(pathsComLoop.isEmpty()){
                        String pathMenor = verificarPathMenor(pathsSemLoop);
                        preencherLinhaDoTempo(pathMenor, linhaDoTempo, indexMax);
                        //remover path do pathSemLoop
                        pathsSemLoop.remove(pathMenor);
                    }
                    //montar LTS
                    montarLTS(linhaDoTempo);
                    
                    linhaDoTempo.clear();
                    //finalizar
                    if(pathsComLoop.isEmpty() && pathsSemLoop.isEmpty()){
                        processo = true;
                    }
                }
            }
            break;
        }
    }
    
    private void montarLTS(Map<Integer, CicloOuNao> linhaDoTempo){
        //Verifica se existe state inicial
        if(mComponentAlterado.getInitialState() == null){
            State inicial = mComponentAlterado.newState(0);
            mComponentAlterado.setInitialState(inicial);
        }
        State currentState = mComponentAlterado.getInitialState();
        int tamanhoLindaDoTempo = linhaDoTempo.size();
        int posicao = 1;
        while(posicao <= tamanhoLindaDoTempo){
            CicloOuNao cicloOuNao = linhaDoTempo.get(posicao);
            //Nao Ciclo
            if(!cicloOuNao.isCiclo()){
                State destinoExisteProximo = procurarNoComponente(currentState, cicloOuNao.getTransition());
                if(destinoExisteProximo == null){
                    List<State> destinoLonge = procurarDestinoLonge(linhaDoTempo,tamanhoLindaDoTempo,posicao);
                    if(destinoLonge.isEmpty()){
                        if(!verificarAutoLoop(currentState,cicloOuNao.getTransition())){
                            int labelState = mComponentAlterado.getStatesCount();
                            State prox = mComponentAlterado.newState(labelState);
                            mComponentAlterado.buildTransition(currentState, prox)
                                    .setLabel(cicloOuNao.getTransition().trim())
                                    .setViewType(TransitionView.Geometry.LINE)
                                    .create();
                            currentState = prox;
                        }else{
                            mComponentAlterado.buildTransition(currentState, currentState)
                                    .setLabel(cicloOuNao.getTransition().trim())
                                    .create();
                        }
                    }else{
                        boolean transicaoCriada = false;
                        for(State candidato : destinoLonge){
                            if(!geraCenario(currentState,candidato,cicloOuNao.getTransition())){
                                mComponentAlterado.buildTransition(currentState, candidato)
                                        .setLabel(cicloOuNao.getTransition().trim())
                                        .setViewType(TransitionView.Geometry.CURVE)
                                        .create();
                                transicaoCriada = true;
                                currentState = candidato;
                                break;
                            }
                        }
                        if(!transicaoCriada){
                            int labelState = mComponentAlterado.getStatesCount();
                            State prox = mComponentAlterado.newState(labelState);
                            mComponentAlterado.buildTransition(currentState, prox)
                                    .setLabel(cicloOuNao.getTransition().trim())
                                    .setViewType(TransitionView.Geometry.LINE)
                                    .create();
                            currentState = prox;
                        }
                    }
                }else{
                    currentState = destinoExisteProximo;
                }
            }
            //Ciclo
            else{
                State inicioLoop = currentState;
                State finalLoop = verificarLoopExistente(inicioLoop,cicloOuNao.getTransitionsCiclo());
                if(finalLoop == null){
                    for(int iLoop=0;iLoop<(cicloOuNao.getTransitionsCiclo().size()-1);iLoop++){
                        String labelTransiton = cicloOuNao.getTransitionsCiclo().get(iLoop);

                        if(iLoop != cicloOuNao.getTransitionsCiclo().size()-2){
                            if(!verificarAutoLoop(currentState, labelTransiton)){
                                int labelState = mComponentAlterado.getStatesCount();
                                State prox = mComponentAlterado.newState(labelState);
                                mComponentAlterado.buildTransition(currentState, prox)
                                        .setLabel(labelTransiton.trim())
                                        .setViewType(TransitionView.Geometry.LINE)
                                        .create();
                                currentState = prox;
                            }
                            else{
                                mComponentAlterado.buildTransition(currentState, currentState)
                                        .setLabel(labelTransiton.trim())
                                        .create();
                            }
                        }
                        else{
                            if(!verificarAutoLoop(currentState, labelTransiton)){
                                mComponentAlterado.buildTransition(currentState, inicioLoop)
                                        .setLabel(labelTransiton.trim())
                                        .setViewType(TransitionView.Geometry.CURVE)
                                        .create();
                            }
                            else{
                                mComponentAlterado.buildTransition(currentState, currentState)
                                        .setLabel(labelTransiton.trim())
                                        .create();
                            }
                        }
                    }
                }else{
                    currentState = finalLoop;
                }
            }
            posicao++;
        }
    }
    
    private boolean geraCenario(State currentState, State candidato, String transition) {
        boolean gera = false;
        try {
            //FIXME falta consertar essa parte!!!!!!!!!!!
            Component aux = mComponentAlterado.clone();
            State srcAux = aux.getStateByID(currentState.getID());
            State dstAux = aux.getStateByID(candidato.getID());
            aux.buildTransition(srcAux, dstAux).setLabel(transition.trim()).create();
            OneLoopPath oneLoopPath = new OneLoopPath();
            List<String> olpAux = oneLoopPath.createOneLoopPath(aux);
            List<String> implicitosAux = olpAux.stream().filter(olp-> !pathsFromTraceModel.contains(olp)).collect(Collectors.toList());
            gera = !implicitosAux.stream().allMatch(p -> pathsScenarioImplied.contains(p));
            
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ImplicitScenarioWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return gera;
    }
    
    private State procurarNoComponente(State currentState, String transition) {
        State destino = null;
        for(Transition t : currentState.getOutgoingTransitionsList()){
            if(t.getLabel().trim().equals(transition.trim())){
                destino = t.getDestiny();
            }
        }        
        return destino;
    }
    
    private List<State> procurarDestinoLonge(Map<Integer, CicloOuNao> linhaDoTempo, int tamanhoLindaDoTempo, int posicao) {
        List<State> destino = new CopyOnWriteArrayList<>();
        if(posicao<tamanhoLindaDoTempo){
           CicloOuNao cicloOuNaoProximo = linhaDoTempo.get(posicao+1);
           String label = cicloOuNaoProximo.isCiclo() ? cicloOuNaoProximo.getTransitionsCiclo().get(0) : cicloOuNaoProximo.getTransition();
           List<Transition> transitionsComponent = (List<Transition>) mComponentAlterado.getTransitions();
           destino.addAll(transitionsComponent.stream().filter(t -> t.getLabel().trim().equals(label.trim())).map(Transition::getSource).collect(Collectors.toList()));
        }
        return destino;
    }
    
    private State verificarLoopExistente(State inicioLoopState, List<String> transitions){
        State destino = null;
        State auxiliar = inicioLoopState;
        for(int i = 0; i<transitions.size();i++){
            String labelTransition = transitions.get(i);
            if(auxiliar.getOutgoingTransitionsList().stream().map(Transition::getLabel).anyMatch(label -> label.trim().equals(labelTransition.trim()))){
                auxiliar = procurarNoComponente(auxiliar, labelTransition);
                if(i == transitions.size()-1){
                    destino = auxiliar;
                }
            }
            else{
                break;
            }
        }
        
        return destino;
    }
    
    private boolean verificarAutoLoop(State state, String labelTransition){
        return state.getIncomingTransitionsList().stream().map(Transition::getLabel).anyMatch(label-> label.trim().equals(labelTransition.trim()));
    }
    
    private void preencherLinhaDoTempo(String pathMenor, Map<Integer, CicloOuNao> linhaDoTempo, int maxIndex) {
        System.out.println("preenchendo linha do tempo com o path: "+pathMenor);
        String[] partes = pathMenor.split(",");
        for(int i = 0; i < partes.length; i++){
            System.out.println("O i eh igual a: "+i);
            CicloOuNao cicloOuNao = new CicloOuNao();
            String label = partes[i].trim().toLowerCase();
            int fimLoop = -1;
            if(i < partes.length-1){
                for(int j = (i+1); j < partes.length; j++){
                    String labelAfter = partes[j].trim().toLowerCase();
                    if(label.equals(labelAfter) && j > (i+1)){ //ciclo tem que ser n>2
                        fimLoop = j;
                        break;
                    }
                }
            }
            System.out.println("Label: "+label);
            if(fimLoop == -1){
                System.out.println("Nao tem ciclo");
                cicloOuNao.setCiclo(false);
                cicloOuNao.setTransition(partes[i]);
            }else{
                System.out.println("Tem ciclo");
                List<String> caminhosCiclo = new ArrayList<>();
                for(int k = i; k <= fimLoop; k++){
                    caminhosCiclo.add(partes[k]);
                    System.out.println("Caminho ciclo: "+partes[k]);
                }
                cicloOuNao.setCiclo(true);
                cicloOuNao.setTransitionsCiclo(caminhosCiclo);
                System.out.println("Atribui ao i o fim do loop");
                i = fimLoop;
            }
           
            linhaDoTempo.put(++maxIndex, cicloOuNao);
            System.out.println("Adiciona parte do caminho com index: "+maxIndex);
        }
    }
    
    private String verificarPathMenor(List<String> pathsComLoop) {
        String menor = pathsComLoop.get(0);
        for(String caminho : pathsComLoop){
            if(caminho.length() < menor.length()){
                menor = caminho;
            }
        }
        return menor;
    }

    private boolean verificarLoopInPath(String path) {
        boolean contem = false;
        String[] partesDoCaminho = path.split(",");
        for(int i = 0; i < partesDoCaminho.length; i++){
            String transition = partesDoCaminho[i].trim().toLowerCase();
            if(i < partesDoCaminho.length-1){
                for(int j = (i+1); j < partesDoCaminho.length; j++){
                    String transitionAfter = partesDoCaminho[j].trim().toLowerCase();
                    if(transition.equals(transitionAfter)){
                        contem = true;
                        break;
                    }
                }
            }
            if(contem){
                break;
            }
        }
        return contem;
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

    private boolean verificarTraceEntrada(String pathTemp) {
        for (String s : pathsFromTraceModel) {
            if (s.startsWith(pathTemp.substring(0, pathTemp.length() - 2))) {
                return true;
            }
        }
        return false;
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

    private void mostrarLinhaDoTempo(Map<Integer, CicloOuNao> linhaDoTempo, int indexMax) {
        System.out.println("------------Linha do Tempo---------------");
        for(int i = 1; i <= indexMax;i++){
            CicloOuNao cicloOuNao = linhaDoTempo.get(i);
            if(!cicloOuNao.isCiclo()){
                System.out.println("Index: "+i+" Caminho: "+cicloOuNao.getTransition());
            }else{
                System.out.print("\nIndex: "+i+" Caminho: ");
                for(String s : cicloOuNao.getTransitionsCiclo()){
                    System.out.print(s+", ");
                }
                System.out.println("\n");
            }
        }
    }
    
    private void mostrarCaminhos(String titulo, List<String> lista){
        System.out.println("------------"+titulo+"---------------");
        for(String s : lista){
            System.out.println(s);
        }
        System.out.println("-------------------------------");
    }
}
