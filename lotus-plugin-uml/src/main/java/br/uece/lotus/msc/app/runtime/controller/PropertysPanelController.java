package br.uece.lotus.msc.app.runtime.controller;


import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.app.ParallelComponentController;
import br.uece.lotus.msc.app.runtime.app.MyHandler;
import br.uece.lotus.msc.app.runtime.config.Configuration;
import br.uece.lotus.msc.app.runtime.model.Equation;
import br.uece.lotus.msc.app.runtime.monitor.ProbabilisticAnnotator;
import br.uece.lotus.msc.app.runtime.utils.checker.ConditionalOperator;
import br.uece.lotus.msc.app.runtime.utils.checker.Property;
import br.uece.lotus.msc.app.runtime.utils.checker.Template;
import br.uece.lotus.msc.app.runtime.utils.component_service.Runtime;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lucas Vieira Alves
 * 03/09/2018
 */


public class PropertysPanelController {

    private HmscComponent standardModeling;
    private Component parallelComponent;
    private final HmscWindowMSCImpl standardModelingWindow;
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
    ChoiceBox<String> firstHMSCChoiseBox, secondHMSCChoiseBox, templateChoiseBox/*, action1ChoiseBox,action2ChoiseBox*/;

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
    Button startButton, removeButton;

    @FXML
    TextArea resultingEquationTextArea;

    @FXML
    TextField numberStepsInput;

    @FXML
    TextField frequencyTxtField;

    private Stage stage;
    private List<ConditionalOperator> operations;
    private List<Equation> addedEquations = new ArrayList<>();




    private final List<HmscBlock> HMSCHmscBlocks;
    private Runtime runTime;

    public PropertysPanelController(HmscWindowMSCImpl standardModelingWindow) {
        this.standardModelingWindow = standardModelingWindow;
        this.standardModeling = standardModelingWindow.getHmscComponent();
        this.parallelComponent = standardModelingWindow.getComponentLTS();
        this.HMSCHmscBlocks = standardModeling.getHmscBlockList();
        this.operations = new ArrayList<>(Arrays.asList(ConditionalOperator.values()));



    }


    public void onCreatedView(){

        configureTableColumn();
        addItemsInChoiseBoxs();
        changeListeners();

        stage = (Stage) anchorPane.getScene().getWindow();
        chooseButton.setOnAction(onClickChooseButton());

        addButton.setOnAction(event ->{

            removeButton.setDisable(true);
            buildEquation();

            firstHMSCChoiseBox.getSelectionModel().clearSelection();
            secondHMSCChoiseBox.getSelectionModel().clearSelection();
            templateChoiseBox.getSelectionModel().selectFirst();
            operationChoiseBox.getSelectionModel().clearSelection();
            probabilityTextField.setText("");
                });


        templateChoiseBox.setOnAction(onSelectedTemplate());
        startButton.setOnAction(event -> {
            start();
        });

        removeButton.setOnAction(event -> {

            firstHMSCChoiseBox.getSelectionModel().clearSelection();
            secondHMSCChoiseBox.getSelectionModel().clearSelection();
            templateChoiseBox.getSelectionModel().selectFirst();
            operationChoiseBox.getSelectionModel().clearSelection();
            probabilityTextField.setText("");

            removeButton.setDisable(true);
            removeProperty();
        });

    }

    private void removeProperty() {
        Equation equationForDelete = (Equation) equationsTable.getSelectionModel().getSelectedItem();
        equationsTable.getItems().remove(equationForDelete);
        addedEquations.remove(equationForDelete);

        if(addedEquations.isEmpty()){
            removeButton.setDisable(true);
        }
    }


    private void start() {

        if(runTime != null) {
            try {
                runTime.stop();
                restartStandardModeling();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        Path traceFile = Paths.get(pathTraceTxtField.getText());

        String frequencyInString = frequencyTxtField.getText();
        Long frequencyInLong = Long.valueOf(frequencyInString);


        ParallelComponentController parallelComponentController = new ParallelComponentController(standardModeling);

        try {
            List<Component> createdComponentsWithIndividualLTS = parallelComponentController.buildIndividualComponents();
            parallelComponentController.addIndividualComponentsInLeftPanel(standardModelingWindow,createdComponentsWithIndividualLTS);

            List<Component> createdComponentsWithLifeLTS = parallelComponentController.buildLifeComponents();
            parallelComponentController.addLifeComponentsInLeftPanel(standardModelingWindow, createdComponentsWithLifeLTS);

            parallelComponent = parallelComponentController.buildParallelComponent();

            standardModelingWindow.setComponentLTS(parallelComponent);

            parallelComponentController.addParallelComponentInLeftPanel(standardModelingWindow, parallelComponent);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Condictions that I want to verify
		List<Property> properties = new ArrayList<>();


		for(Equation equation : addedEquations){
            Integer firstStateId = null;
            Integer secondStateId = null;

             if(equation.getTemplate().equals(Template.AFTER.toString())){

                firstStateId = HMSCLTSMapper.getStateIdInComponetAboutInitialHMSC(parallelComponent, equation.getFirstHMSCHmscBlock());

                if(equation.getFirstHMSCHmscBlock() == equation.getSecondHMSCHmscBlock()){
                    secondStateId = firstStateId;
                }else {
                    secondStateId = HMSCLTSMapper.getStateIdInComponetAboutFinalHMSC(parallelComponent, equation.getSecondHMSCHmscBlock());
                }
             }else if(equation.getTemplate().equals(Template.DEFAULT.toString())){
                 firstStateId = HMSCLTSMapper.getStateIdInComponetAboutInitialHMSC(parallelComponent, equation.getFirstHMSCHmscBlock());

                 if(equation.getFirstHMSCHmscBlock() == equation.getSecondHMSCHmscBlock()){
                     secondStateId = firstStateId;
                 }else {
                     secondStateId = HMSCLTSMapper.getStateIdInComponetAboutFinalHMSC(parallelComponent, equation.getSecondHMSCHmscBlock());
                 }
             }else if(equation.getTemplate().equals(Template.AND_NOT.toString())){
                 firstStateId = HMSCLTSMapper.getStateIdInComponetAboutInitialHMSC(parallelComponent,equation.getFirstHMSCHmscBlock());

                 if(equation.getFirstHMSCHmscBlock() == equation.getSecondHMSCHmscBlock()){
                     // todo fazer lançamento de exception
                 }else {
                     secondStateId = HMSCLTSMapper.getStateIdInComponetAboutInitialHMSC(parallelComponent, equation.getSecondHMSCHmscBlock());
                 }
             }



            System.out.println(" probabilidade do cenario "+ equation.getFirstHMSCHmscBlock().getLabel() + "para p cenário "+ equation.getSecondHMSCHmscBlock().getLabel() );
            System.out.println(" é a probabilidade do estado " + firstStateId +"para o estado"+ secondStateId);
            System.out.println("<<><><><><><>>");

            if(firstStateId == null || secondStateId == null){
                try {
                    throw new Exception("Not found stateInBase! ");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }

            properties.add(new Property.PropertyBuilder()
                    .firstStateId(firstStateId)
                    .template(equation.getTemplate())
                    .secondStateId(secondStateId)
                    .conditionalOperator(equation.getConditionalOperator())
                    .probability(equation.getProbability())
                    .build());
        }





		MyHandler handler = new MyHandler();


		Configuration configuration = new Configuration.ConfigurationBuilder()
				.traceFile(traceFile.toString())
				.milliseconds(frequencyInLong)
				.project(standardModelingWindow.projectExplorerPluginMSC.getSelectedProjectDS())
                .parallelComponent(parallelComponent)
				.properties(properties)
				.build();




        try {
            runTime = new Runtime(configuration, handler);
            runTime.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restartStandardModeling() {
        for(TransitionMSC transitionMSC : standardModeling.getTransitionMSCList()){
            transitionMSC.setProbability(0.0);
            transitionMSC.putValue(ProbabilisticAnnotator.VISIT_COUNT, null);
            ((HmscBlock)transitionMSC.getSource()).putValue(ProbabilisticAnnotator.VISIT_COUNT, null);
            ((HmscBlock)transitionMSC.getDestiny()).putValue(ProbabilisticAnnotator.VISIT_COUNT, null);
        }
    }



    /* Optional<Transition>  transitionOptional = parallelComponent.getTransitionsList().stream().filter(transition
             -> (transition.getLabel().split("[.]"))[1].equals(labelHMSC)).findAny();

     Transition transition = transitionOptional.get();*/


   //  return transition.getSource().getID();



    private void addItemsInChoiseBoxs() {
        ObservableList<String> itemsHMSCChoiseBox = FXCollections.observableArrayList();
        ObservableList<String> itemsActionsChoiseBox  = FXCollections.observableArrayList();

        for(HmscBlock hmscHmscBlock : HMSCHmscBlocks){
            itemsHMSCChoiseBox.add(hmscHmscBlock.getLabel());
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

        templateChoiseBox.getSelectionModel().selectFirst();

        for(TransitionMSC transitionMSC : standardModeling.getTransitionMSCList()){
            itemsActionsChoiseBox.add(transitionMSC.getLabel());
        }

//        action1ChoiseBox.setItems(FXCollections.observableArrayList(itemsActionsChoiseBox));
//        action2ChoiseBox.setItems(FXCollections.observableArrayList(itemsActionsChoiseBox));


    }

    private void configureTableColumn() {
        firstHMSCColumn.setCellValueFactory(new PropertyValueFactory<>("firstHMSCProperty"));
        secondHMSCColumn.setCellValueFactory(new PropertyValueFactory<>("secondHMSCProperty"));
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("probabilityProperty"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("conditionalOperationProperty"));
        templateColumn.setCellValueFactory(new PropertyValueFactory<>("templateProperty"));

    }

    private void buildEquation() {
        //HmscBlock srcHMSCHmscBlockSelected = (HMSC) getSelectedItem(firstHMSCChoiseBox, HMSCHmscBlocks);
        HmscBlock srcHMSCHmscBlockSelected = getSelectedHMSC(firstHMSCChoiseBox, HMSCHmscBlocks);
        if(srcHMSCHmscBlockSelected == null){
            JOptionPane.showMessageDialog(null, "Source bMSC not be null", "Source bMSC Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(templateChoiseBox.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Template not be null", "Template HmscBlock Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String selectedTemplateItem = templateChoiseBox.getValue();

        ConditionalOperator selectedOperationItem =  getSelectedOperation(operationChoiseBox, operations);
        if(selectedOperationItem == null){
            JOptionPane.showMessageDialog(null, "Operation not be null", "Operation Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String probabilityInString = probabilityTextField.getText();
        if(probabilityInString.equals("")){
            JOptionPane.showMessageDialog(null, "Probability not be null", "Probability Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Double probabilityInDouble = Double.valueOf(probabilityInString);

       // System.out.println("Item selecionado "+ srcHMSCHmscBlockSelected.getLabel()+ "  "+ selectedTemplateItem +" "+dstHMSCSelected.getLabel()+ " "+selectedOperationItem.toString()+ " "+probabilityInString);

        Equation currentEquation = new Equation();


        if(secondHMSCChoiseBox.isVisible()) {
            HmscBlock dstHMSCHmscBlockSelected = getSelectedHMSC(secondHMSCChoiseBox, HMSCHmscBlocks);
            if(dstHMSCHmscBlockSelected == null){
                JOptionPane.showMessageDialog(null, "Destiny bMSC not be null", "Destiny bMSC Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentEquation.setFirstHMSCHmscBlock(srcHMSCHmscBlockSelected).setTemplate(selectedTemplateItem).setSecondHMSCHmscBlock(dstHMSCHmscBlockSelected)
                    .setConditionalOperation(selectedOperationItem).setProbability(probabilityInDouble);
            addedEquations.add(currentEquation);
        }else{
            String nSteps = numberStepsInput.getText();
            if(nSteps.equals("")){
                JOptionPane.showMessageDialog(null, "Number of Steps not be null", "Nº Steps Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentEquation.setFirstHMSCHmscBlock(srcHMSCHmscBlockSelected).setTemplate(selectedTemplateItem).set2HMSCProperty(nSteps)
                    .setConditionalOperation(selectedOperationItem).setProbability(probabilityInDouble);
            addedEquations.add(currentEquation);
        }

        ObservableList<Equation> data = FXCollections.observableArrayList(addedEquations);
        equationsTable.setItems(data);


        startButton.setDisable(false);


    }

    private ConditionalOperator getSelectedOperation(ChoiceBox<String> choiceBox, List<ConditionalOperator> operations) {
        if(choiceBox.getSelectionModel().isEmpty()){
            return null;
        }
        return operations.get(choiceBox.getSelectionModel().getSelectedIndex());
    }

    private HmscBlock getSelectedHMSC(ChoiceBox<String> choiceBox, List<HmscBlock> HMSCHmscBlocks) {
        if(choiceBox.getSelectionModel().isEmpty()){
            return null;
        }
        return HMSCHmscBlocks.get(choiceBox.getSelectionModel().getSelectedIndex());
    }


    private EventHandler<ActionEvent> onClickChooseButton() {
        return event -> showChooser();
    }

    private EventHandler<ActionEvent> onSelectedTemplate(){
            return event -> {
                firstHMSCChoiseBox.getSelectionModel().clearSelection();
                secondHMSCChoiseBox.getSelectionModel().clearSelection();
            };

    }



    private void changeListeners() {
        equationsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> tableListener(observable, oldValue, newValue));
        firstHMSCChoiseBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        secondHMSCChoiseBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        numberStepsInput.textProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        templateChoiseBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        operationChoiseBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        probabilityTextField.textProperty().addListener((observable, oldValue, newValue) -> updateResultingEquation());
        probabilityTextField.textProperty().addListener((observable, oldValue, newValue) -> patternProbability(oldValue, newValue));


        /*
        secondHMSCChoiseBox.setOnInputMethodTextChanged(event -> {
            updateResultingEquation();
        });
        numberStepsInput.setOnInputMethodTextChanged(event -> {
            updateResultingEquation();
        });
        templateChoiseBox.setOnInputMethodTextChanged(event -> {
            updateResultingEquation();
        });
        operationChoiseBox.setOnInputMethodTextChanged(event -> {
            updateResultingEquation();
        });
        probabilityTextField.setOnInputMethodTextChanged(event -> {
            updateResultingEquation();
        });
        */
    }

    private void tableListener(ObservableValue observable, Object oldValue, Object newValue) {
        removeButton.setDisable(false);
    }

    private void patternProbability(String oldValue, String newValue) {
        System.out.println("pattern: "+oldValue+" : "+newValue);
        String valorDoField = newValue.trim();
        String auxValor = "";
        double valor;
      //  if(valorDoField.matches(" null | [0-1] \\.? [0-9]* | [0-1] \\,{0,1} [0-9] | [0-9]* ")){
            if(valorDoField.matches("[0-9]+%")) {
                auxValor = valorDoField.replaceAll("%","");
                valor = Double.parseDouble(auxValor);
                if(valor > 100){
                    valorDoField = oldValue;
                    JOptionPane.showMessageDialog(null, "Imput probability need 0% to 100%", "Erro[1]", JOptionPane.ERROR_MESSAGE);

                }else {
                    valor = valor / 100;
                    valorDoField = "" + valor;
                }
            }else if(valorDoField.matches("[0-1]\\,[0-9]+")) {
                auxValor = valorDoField.replaceAll("," , ".");
                valor = Double.parseDouble(auxValor);
                if(valor > 1.0){
                    valorDoField = oldValue;
                    JOptionPane.showMessageDialog(null, "Imput probability need 0,0 to 1,0", "Erro[2]", JOptionPane.ERROR_MESSAGE);
                }else{
                    valorDoField = ""+valor;
                }
            }else if (valorDoField.matches("[0-1]\\.[0-9]+")){
                valor = Double.parseDouble(valorDoField);
                if(valor > 1.0){
                    valorDoField = oldValue;
                    JOptionPane.showMessageDialog(null, "Imput probability need 0.0 to 1.0", "Erro[3]", JOptionPane.ERROR_MESSAGE);
                }else{
                    valorDoField = ""+valor;
                }
            }else if(valorDoField.matches("[0-9]*\\.?|[0-9]*\\,?|")){

            }
       // }
        else{
            JOptionPane.showMessageDialog(null, "Imput probability must be in the format: 0.0 or 0,0 or 0% ", "Erro[4]", JOptionPane.ERROR_MESSAGE);
            valorDoField = oldValue;
        }

        probabilityTextField.setText(valorDoField);

/*
        if(valorDoField.contains(",")){
            auxValor = valorDoField.replaceAll(",", ".");
            double teste = Double.parseDouble(auxValor);
            if(teste<0 || teste >1){
                JOptionPane.showMessageDialog(null, "Input probability between 0 and 1", "Erro[1]", JOptionPane.ERROR_MESSAGE);
                probabilityTextField.setText("");
            }
        }
        else if(valorDoField.contains(".")){
            auxValor = valorDoField;
            double teste = Double.parseDouble(auxValor);
            if(teste<0 || teste >1){
                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro[2]", JOptionPane.ERROR_MESSAGE);
                probabilityTextField.setText("");
            }
        }
        else if(valorDoField.contains("%")){
            double valorEntre0e1;
            auxValor = valorDoField.replaceAll("%", "");
            valorEntre0e1 = (Double.parseDouble(auxValor))/100;
            auxValor = String.valueOf(valorEntre0e1);
            double teste = Double.parseDouble(auxValor);
            if(teste<0 || teste >1){
                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro[3]", JOptionPane.ERROR_MESSAGE);
                probabilityTextField.setText("");
            }
        }
        else{
            if(valorDoField.matches("[0-1][0-9]*")){
                if(!probabilityTextField.isFocused()){
                    if( !valorDoField.contains("%") ){

                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Imput probability need 0 to 1", "Erro[4]", JOptionPane.ERROR_MESSAGE);
                probabilityTextField.setText("");
            }
        }
        */
    }

    private void updateResultingEquation(){
        String secondTarget;
        String first = firstHMSCChoiseBox.getSelectionModel().getSelectedItem();
        String template = templateChoiseBox.getSelectionModel().getSelectedItem();

        String operation = operationChoiseBox.getSelectionModel().getSelectedItem();
        String probability = probabilityTextField.getText();
        if(secondHMSCChoiseBox.isVisible()) {
            secondTarget = secondHMSCChoiseBox.getSelectionModel().getSelectedItem();
            resultingEquationTextArea.setText(first + " " + template + " " + secondTarget + " " + operation + " " + probability);
        }else{
            secondTarget = numberStepsInput.getText();
            resultingEquationTextArea.setText(first + " "  + "in " + secondTarget + " steps" + operation + " " + probability);
        }

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
