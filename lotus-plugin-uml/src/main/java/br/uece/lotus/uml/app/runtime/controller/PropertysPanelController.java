package br.uece.lotus.uml.app.runtime.controller;


import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.app.runtime.app.MyHandler;
import br.uece.lotus.uml.app.runtime.config.Configuration;
import br.uece.lotus.uml.app.runtime.model.Equation;
import br.uece.lotus.uml.app.runtime.utils.checker.ConditionalOperator;
import br.uece.lotus.uml.app.runtime.utils.checker.Property;
import br.uece.lotus.uml.app.runtime.utils.component_service.Runtime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PropertysPanelController {

    private final StandardModeling standardModeling;
    @FXML
    private
    AnchorPane anchorPane;

    @FXML
    private
    TextField pathTraceTxtField;

    @FXML
    private
    Button chooseButton, addButton;

    @FXML
    private
    ChoiceBox<String> srcHMSCChoiseBox, dstHMSCChoiseBox;

    @FXML
    private
    ChoiceBox<String>operationHMSCChoiseBox;

    @FXML
    TableView equationsTable;

    @FXML
    TableColumn srcCol,dstCol, opCol, probCol;

    @FXML
    TextField probabilityTextField;

    @FXML
    Button startButton;

    private Stage stage;


    private List<ConditionalOperator> operations;
    private List<Equation> addedEquations = new ArrayList<>();



    private final List<Hmsc> HMSCs;

    public PropertysPanelController(StandardModeling standardModeling) {
        this.standardModeling = standardModeling;
        this.HMSCs = standardModeling.getBlocos();
        operations = new ArrayList<>(Arrays.asList(ConditionalOperator.values()));
    }


    public void onCreatedView(){

        configureTableColumn();
        addItemsInChoiseBoxs();

        stage = (Stage) anchorPane.getScene().getWindow();
        chooseButton.setOnAction(onClickChooseButton());

        addButton.setOnAction(event ->
                buildEquation());

        startButton.setOnAction(event -> start());

    }

    private void start() {
        Path traceFile = Paths.get(pathTraceTxtField.getText());
//		Path lotusFile = Paths.get("/home/lucas-vieira/Desktop/exemplo-2/exemplo2.xml");


		// Condictions that I want to verify
		List<Property> properties = new ArrayList<>();

		for(Equation equation :addedEquations){
            properties.add(new Property.PropertyBuilder()
                    .sourceHMSCId(equation.getSourceHMSC().getID())
                    .targetHMSCId(equation.getDestinyHMSC().getID())
                    .conditionalOperator(equation.getConditionalOperator())
                    .probability(equation.getProbability())
                    .build());
        }





		MyHandler handler = new MyHandler();


		Configuration configuration = new Configuration.ConfigurationBuilder()
				.traceFile(traceFile.toString())
				.milliseconds(2000L)
				.project(standardModeling)
				.properties(properties)
				.build();

        Runtime runTime = new Runtime(configuration, handler);

        try {
            runTime.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItemsInChoiseBoxs() {
        ObservableList<String> itemsHMSCChoiseBox = FXCollections.observableArrayList();

        for(Hmsc hmsc : HMSCs){
            itemsHMSCChoiseBox.add(hmsc.getLabel());
        }

        srcHMSCChoiseBox.setItems(FXCollections.observableArrayList(itemsHMSCChoiseBox));
        dstHMSCChoiseBox.setItems(FXCollections.observableArrayList(itemsHMSCChoiseBox));


        ObservableList<String> itemsOperationChoiseBox = FXCollections.observableArrayList();
        for(ConditionalOperator operation : ConditionalOperator.values()){
            itemsOperationChoiseBox.add(operation.toString());
        }

        operationHMSCChoiseBox.setItems(FXCollections.observableArrayList(itemsOperationChoiseBox));
    }

    private void configureTableColumn() {
        srcCol.setCellValueFactory(new PropertyValueFactory<>("sourceProperty"));
        dstCol.setCellValueFactory(new PropertyValueFactory<>("destinyProperty"));
        probCol.setCellValueFactory(new PropertyValueFactory<>("probabilityProperty"));
        opCol.setCellValueFactory(new PropertyValueFactory<>("conditionalOperationProperty"));

    }

    private void buildEquation() {
        //Hmsc srcHMSCSelected = (HMSC) getSelectedItem(srcHMSCChoiseBox, HMSCs);
        Hmsc srcHMSCSelected = getSelectedHMSC(srcHMSCChoiseBox, HMSCs);

        Hmsc dstHMSCSelected = getSelectedHMSC(dstHMSCChoiseBox, HMSCs);

        ConditionalOperator opItemSelect =  getSelectedOperation(operationHMSCChoiseBox, operations);

        String probabilityInString = probabilityTextField.getText();
        Double probabilityInDouble = Double.valueOf(probabilityInString);

        System.out.println("Item selecionado "+ srcHMSCSelected.getLabel()+ "  "+ dstHMSCSelected.getLabel()+ " "+opItemSelect.toString()+ " "+probabilityInString);

        Equation currentEquation = new Equation();
        currentEquation.setSource(srcHMSCSelected).setDestiny(dstHMSCSelected).setConditionalOperation(opItemSelect).setProbability(probabilityInDouble);
        addedEquations.add(currentEquation);

        ObservableList<Equation> data = FXCollections.observableArrayList(addedEquations);
        equationsTable.setItems(data);


        startButton.setDisable(false);


    }

    private ConditionalOperator getSelectedOperation(ChoiceBox<String> choiceBox, List<ConditionalOperator> operations) {
        return operations.get(choiceBox.getSelectionModel().getSelectedIndex());
    }

    private Hmsc getSelectedHMSC(ChoiceBox<String> choiceBox, List<Hmsc> HMSCs) {
        return HMSCs.get(choiceBox.getSelectionModel().getSelectedIndex());
    }


    private EventHandler<ActionEvent> onClickChooseButton() {
        return event -> showChooser();
    }


//    private ChangeListener<Number> onSelectedSrcHMSCChoiseBox(){
//        return (observable, oldValue, newValue) -> {
//            System.out.println("");
//        };
//    }
//
//    private ChangeListener<? super Number> onSelectedOperationHMSCChoiseBox() {
//        return (observable, oldValue, newValue) -> {
//            System.out.println("");
//        };
//    }
//
//    private ChangeListener<Number> onSelectedDstHMSCChoiseBox() {
//        return (observable, oldValue, newValue) -> {
//            System.out.println("");
//        };
//    }




    private void showChooser() {

        File traceFile = trySelectFile();

        String pathTraceFileInString = traceFile.getPath();

        pathTraceTxtField.setText(pathTraceFileInString);

    }

    private File trySelectFile() {
        File traceFile = null;
        try {
           traceFile = ChooserController.selectFile(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traceFile;
    }


}
