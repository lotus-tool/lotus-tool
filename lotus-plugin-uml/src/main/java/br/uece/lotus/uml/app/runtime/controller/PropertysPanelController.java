package br.uece.lotus.uml.app.runtime.controller;


import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.app.runtime.app.MyHandler;
import br.uece.lotus.uml.app.runtime.config.Configuration;
import br.uece.lotus.uml.app.runtime.model.Equation;
import br.uece.lotus.uml.app.runtime.utils.checker.ConditionalOperator;
import br.uece.lotus.uml.app.runtime.utils.checker.Property;
import br.uece.lotus.uml.app.runtime.utils.checker.Template;
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
    ChoiceBox<String> firstHMSCChoiseBox, secondHMSCChoiseBox, templateChoiseBox;

    @FXML
    private
    ChoiceBox<String> operationChoiseBox;

    @FXML
    TableView equationsTable;

    @FXML
    TableColumn firstHMSCColumn, templateColumn, secondHMSCColumn, operationColumn, probabilityColumn;

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
                    .sourceHMSCId(equation.getFirstHMSC().getID())
                    .targetHMSCId(equation.getSecondHMSC().getID())
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

        firstHMSCChoiseBox.setItems(FXCollections.observableArrayList(itemsHMSCChoiseBox));
        secondHMSCChoiseBox.setItems(FXCollections.observableArrayList(itemsHMSCChoiseBox));


        ObservableList<String> itemsOperationChoiseBox = FXCollections.observableArrayList();
        for(ConditionalOperator operation : ConditionalOperator.values()){
            itemsOperationChoiseBox.add(operation.toString());
        }
        ObservableList<String> itemsTemplateChoiseBox = FXCollections.observableArrayList();

        for(Template template: Template.values()){
            itemsTemplateChoiseBox.add(template.toString());
        }




        operationChoiseBox.setItems(FXCollections.observableArrayList(itemsOperationChoiseBox));

        templateChoiseBox.setItems(FXCollections.observableArrayList(itemsTemplateChoiseBox));
    }

    private void configureTableColumn() {
        firstHMSCColumn.setCellValueFactory(new PropertyValueFactory<>("firstHMSCProperty"));
        secondHMSCColumn.setCellValueFactory(new PropertyValueFactory<>("secondHMSCProperty"));
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("probabilityProperty"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("conditionalOperationProperty"));
        templateColumn.setCellValueFactory(new PropertyValueFactory<>("templateProperty"));

    }

    private void buildEquation() {
        //Hmsc srcHMSCSelected = (HMSC) getSelectedItem(firstHMSCChoiseBox, HMSCs);
        Hmsc srcHMSCSelected = getSelectedHMSC(firstHMSCChoiseBox, HMSCs);
        String selectedTemplateItem = templateChoiseBox.getValue();
        Hmsc dstHMSCSelected = getSelectedHMSC(secondHMSCChoiseBox, HMSCs);

        ConditionalOperator selectedOperationItem =  getSelectedOperation(operationChoiseBox, operations);

        String probabilityInString = probabilityTextField.getText();
        Double probabilityInDouble = Double.valueOf(probabilityInString);

        System.out.println("Item selecionado "+ srcHMSCSelected.getLabel()+ "  "+ dstHMSCSelected.getLabel()+ " "+selectedOperationItem.toString()+ " "+probabilityInString);

        Equation currentEquation = new Equation();

        currentEquation.setFirstHMSC(srcHMSCSelected).setTemplate(selectedTemplateItem).setSecondHMSC(dstHMSCSelected)
                .setConditionalOperation(selectedOperationItem).setProbability(probabilityInDouble);
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
