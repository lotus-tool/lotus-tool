/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.probabilisticReach;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.ComponentViewImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ranniery
 */
public class ProbabilisticReachWindow extends AnchorPane{
    private int mStepCount;
    private State mCurrentState;

    private final ToolBar mToolbar;
    private final Button mBtnCalculate;
    private final Button mBtnAdd;
    private final TextField mSrcSttField;
    private final TextField mTgtSttField;
    private final ChoiceBox mChoiceBox;
    private final TextField mOptionalField;
    private final TextField mExcStt;
    private final TextField mAction1;
    private final TextField mAction2;
    private final TextField mOutputField;
    private final ChoiceBox mTemplatesMenu;
    private final Label mTemplateLabel1;
    private final Label mTemplateLabel2;
    private final ComponentViewImpl mViewer;
    private final ScrollPane mScrollPanel;
    private final TableView<Entry> mTableView;
    private final TableColumn<Entry, String> mSourceCol;
    private final TableColumn<Entry, String> mTargetCol;
    private final TableColumn<Entry, String> mInequationCol;
    private final TableColumn<Entry, String> mConditionCol;
    private final TableColumn<Entry, String> mExcSttCol;
    private final TableColumn<Entry, String> mResultCol;
    private final ObservableList<Entry> mEntries = FXCollections.observableArrayList();

    public ProbabilisticReachWindow() {
        mViewer = new ComponentViewImpl();
        mScrollPanel = new ScrollPane(mViewer);
        AnchorPane.setTopAnchor(mScrollPanel, 38D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);
        mViewer.minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.minWidthProperty().bind(mScrollPanel.widthProperty());
        getChildren().add(mScrollPanel);        

        mSrcSttField = new TextField();
        mSrcSttField.setPromptText("Source");
        mSrcSttField.setPrefWidth(58.0);
        
        mTgtSttField = new TextField();
        mTgtSttField.setPromptText("Target");
        mTgtSttField.setPrefWidth(58.0);
        
        mChoiceBox = new ChoiceBox();
        mChoiceBox.getItems().addAll(">", ">=", "<", "<=", "=", "!=");
        mChoiceBox.setPrefWidth(30.0);
        mChoiceBox.setTooltip(new Tooltip("Inequation"));
        
        mOptionalField = new TextField();
        mOptionalField.setPromptText("Condition");
        mOptionalField.setPrefWidth(70.0);

        mExcStt = new TextField();
        mExcStt.setPromptText("Excluding States");
        mExcStt.setPrefWidth(70.0);
        
        mBtnCalculate = new Button("Calculate");
        mBtnCalculate.setOnAction((ActionEvent e) -> {
            calculate(false, null);
        });
        
        mBtnAdd = new Button("+");
        mBtnAdd.setOnAction((ActionEvent e) -> {
            addEntry();            
        });

        mTemplateLabel1 = new Label();
        mTemplateLabel1.setVisible(false);

        mTemplateLabel2 = new Label();
        mTemplateLabel2.setVisible(false);

        mTemplatesMenu = new ChoiceBox(FXCollections.observableArrayList(
                "Default", "P(Action1)", "P(Action1 ^ ~Action2)", "P(Action1 after Action2)", "P(Action1 in X steps)")
        );
        mTemplatesMenu.setPrefWidth(120.0);
        mTemplatesMenu.setTooltip(new Tooltip("Templates"));
        mTemplatesMenu.getSelectionModel().selectFirst();

        mAction1 = new TextField();
        mAction1.setPromptText("Action1");
        mAction1.setPrefWidth(70.0);
        mAction1.setVisible(false);

        mAction2 = new TextField();
        mAction2.setPromptText("Action2");
        mAction2.setPrefWidth(70.0);
        mAction2.setVisible(false);

        mTemplatesMenu.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int chosenTemplate = newValue.intValue();

                mSrcSttField.setText("");
                mTgtSttField.setText("");
                mExcStt.setText("");

                switch(chosenTemplate){
                    case 0: // Default
                        mTemplateLabel1.setText("");
                        mTemplateLabel2.setText("");

                        mAction1.setText("");
                        mAction2.setText("");

                        mAction1.setVisible(false);
                        mTemplateLabel1.setVisible(false);
                        mAction2.setVisible(false);
                        mTemplateLabel2.setVisible(false);

                        mAction2.setPromptText("Action2");
                        break;

                    case 1: // "P(Action1)"
                        mTemplateLabel1.setText("Probability of doing ");
                        mTemplateLabel2.setText(".");

                        mAction1.setText("");
                        mAction2.setText("");

                        mAction1.setVisible(true);
                        mTemplateLabel1.setVisible(true);
                        mAction2.setVisible(false);
                        mTemplateLabel2.setVisible(true);

                        mAction2.setPromptText("Action2");
                        break;

                    case 2: // "P(Action1 ^ ~Action2)"
                        mTemplateLabel1.setText("Probability of doing ");
                        mTemplateLabel2.setText(" and not performing ");

                        mAction1.setText("");
                        mAction2.setText("");

                        mAction1.setVisible(true);
                        mTemplateLabel1.setVisible(true);
                        mAction2.setVisible(true);
                        mTemplateLabel2.setVisible(true);

                        mAction2.setPromptText("Action2");
                        break;

                    case 3: // "P(Action1 after Action2)"
                        mTemplateLabel1.setText("Probability of doing ");
                        mTemplateLabel2.setText(" after ");

                        mAction1.setText("");
                        mAction2.setText("");

                        mAction1.setVisible(true);
                        mTemplateLabel1.setVisible(true);
                        mAction2.setVisible(true);
                        mTemplateLabel2.setVisible(true);

                        mAction2.setPromptText("Action2");
                        break;

                    case 4: // "P(Action1 in X steps)"
                        mTemplateLabel1.setText("Probability of doing ");
                        mTemplateLabel2.setText(" in up to ");

                        mAction1.setText("");
                        mAction2.setText("");

                        mAction1.setVisible(true);
                        mTemplateLabel1.setVisible(true);
                        mAction2.setVisible(true);
                        mTemplateLabel2.setVisible(true);

                        mAction2.setPromptText("X steps");
                        break;
                }
            }
        });

        mOutputField = new TextField();
        mOutputField.setPromptText("Result");
        mOutputField.setPrefWidth(58.0);
        mOutputField.setEditable(false);
        mOutputField.setFocusTraversable(false);
        mOutputField.setMouseTransparent(true);
        mOutputField.setStyle("-fx-background-color: yellow; -fx-prompt-text-fill: black;");
        
        mTableView = new TableView();
        mTableView.setPrefHeight(0);
        mTableView.setVisible(false);

        AnchorPane.setLeftAnchor(mTableView, 0D);
        AnchorPane.setRightAnchor(mTableView, 0D);
        AnchorPane.setBottomAnchor(mTableView, 0D);
        getChildren().add(mTableView);
        
        mSourceCol = new TableColumn<>("Source");
        mSourceCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("source"));
        mSourceCol.setPrefWidth(100);
        mTargetCol = new TableColumn<>("Target");
        mTargetCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("target"));
        mTargetCol.setPrefWidth(100);
        mInequationCol = new TableColumn<>("Inequation");
        mInequationCol.setPrefWidth(100);
        mInequationCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("inequation"));
        mConditionCol = new TableColumn<>("Condition");
        mConditionCol.setPrefWidth(100);
        mConditionCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("condition"));
        mExcSttCol = new TableColumn<>("Excluding States");
        mExcSttCol.setPrefWidth(160);
        mExcSttCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("excstt"));
        mResultCol = new TableColumn<>("Result");
        mResultCol.setPrefWidth(100);
        mResultCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("result"));
        
        mTableView.getColumns().addAll(mSourceCol, mTargetCol, mInequationCol, mConditionCol, mExcSttCol, mResultCol);
        mTableView.setItems(mEntries);
        ContextMenu menu = new ContextMenu();
        MenuItem verify = new MenuItem("Verify Property");
        verify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Entry selectedEntry = (Entry) mTableView.getSelectionModel().getSelectedItem();
                calculate(true, selectedEntry);
                refresh();
                System.out.println();
            }
        });
        MenuItem verifyAll = new MenuItem("Verify All");
        verifyAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Entry> allEntries = mTableView.getItems();
                for(Entry selectedEntry : allEntries){
                    calculate(true, selectedEntry);
                    refresh();
                    System.out.println();
                }
            }
        });
        
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Entry selectedEntry = mTableView.getSelectionModel().getSelectedItem();
                mEntries.remove(selectedEntry);
                mTableView.getSelectionModel().clearSelection();
                refresh();
                System.out.println();
            }
        });
        menu.getItems().add(verify);
        menu.getItems().add(verifyAll);
        menu.getItems().add(remove);
        mTableView.setContextMenu(menu);

        mToolbar = new ToolBar();
        mToolbar.getItems().addAll(mSrcSttField);
        mToolbar.getItems().addAll(mTgtSttField);
        mToolbar.getItems().addAll(mChoiceBox);
        mToolbar.getItems().addAll(mOptionalField);
        mToolbar.getItems().addAll(mExcStt);
        mToolbar.getItems().addAll(mBtnCalculate);
        mToolbar.getItems().addAll(mOutputField);
        mToolbar.getItems().addAll(mBtnAdd);
        mToolbar.getItems().addAll(mTemplatesMenu);
        mToolbar.getItems().addAll(mTemplateLabel1);
        mToolbar.getItems().addAll(mAction1);
        mToolbar.getItems().addAll(mTemplateLabel2);
        mToolbar.getItems().addAll(mAction2);
        AnchorPane.setTopAnchor(mToolbar, 0D);
        AnchorPane.setLeftAnchor(mToolbar, 0D);
        AnchorPane.setRightAnchor(mToolbar, 0D);
        getChildren().add(mToolbar);
    }
    
    private void refresh(){
        mTableView.setItems(null);
        mTableView.layout();
        mTableView.setItems(mEntries);
    }
    
    private void addEntry(){
        mTableView.setVisible(true);
        mTableView.setPrefHeight(150);
        AnchorPane.setBottomAnchor(mScrollPanel, 150D);
        calculate(false, null);
        String sourceStt = mSrcSttField.getText();
        String targetStt = mTgtSttField.getText();
        String inequation = (String) mChoiceBox.getSelectionModel().getSelectedItem();
        String optField = mOptionalField.getText();
        String excStt = mExcStt.getText();
        String outputField = mOutputField.getText();
        Entry newEntry = new Entry(sourceStt, targetStt, inequation, optField, excStt, outputField);
        
        if(optField == null || optField.trim().isEmpty()){
            newEntry.setInequation("");
            newEntry.setCondition("");
        }
        if(!TableContainsRow(newEntry, mEntries)){
            mEntries.add(newEntry);
        }else{
            JOptionPane.showMessageDialog(null, "Row already added!");
            return;
        }
        
    }

    private boolean TableContainsRow (Entry newEntry, ObservableList<Entry> mEntries){
        boolean contains = false;
        for(Entry aux : mEntries){
            if(SameEntry(aux, newEntry)){
                contains = true;
            }
        }
        return contains;
    }

    private boolean SameEntry(Entry aux, Entry newEntry){
        if(aux.getSource().equals(newEntry.getSource()) && aux.getTarget().equals(newEntry.getTarget()) && aux.getInequation().equals(newEntry.getInequation()) && aux.getExcstt().equals(newEntry.getExcstt()) && aux.getCondition().equals(newEntry.getCondition()) && aux.getResult().equals(newEntry.getResult())){
            return true;
        }else{
            return false;
        }
    }

    private String calculate(boolean fromMenu, Entry selectedEntry){
        mBtnCalculate.setText("Calculate");
        Component a = null;

        try {
            a = mViewer.getComponent().clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ProbabilisticReachPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

        String template = (String) mTemplatesMenu.getSelectionModel().getSelectedItem();
        int steps = Integer.MAX_VALUE;

        switch(template){
            case "P(Action1)":
                mSrcSttField.setText("0");
                mTgtSttField.setText(mAction1.getText());
                break;

            case "P(Action1 ^ ~Action2)":
                mSrcSttField.setText("0");
                mTgtSttField.setText(mAction1.getText());
                mExcStt.setText(mAction2.getText());
                break;

            case "P(Action1 after Action2)":
                String action2Destiny = a.getTransitionByLabel(mAction2.getText()).getDestiny().getLabel();
                mSrcSttField.setText(action2Destiny);
                mTgtSttField.setText(mAction1.getText());
                break;

            case "P(Action1 in X steps)":
                mSrcSttField.setText("0");
                mTgtSttField.setText(mAction1.getText());
                steps = Integer.parseInt(mAction2.getText());
                break;
        }

        String sourceStt = mSrcSttField.getText();
        String targetStt = mTgtSttField.getText();
        String inequation = (String) mChoiceBox.getSelectionModel().getSelectedItem();
        String optField = mOptionalField.getText();
        String excStt = mExcStt.getText();
        
        if(excStt != null && !excStt.trim().isEmpty()){
            excStt = excStt.replaceAll("\\s", "");
            String[] excStates = excStt.split(",");
            List<Integer> IDs = new ArrayList<>();
            List<String> Actions = new ArrayList<>();

            for(String label : excStates){
                if(label.matches("\\d+")){
                    IDs.add(Integer.parseInt(label));
                }else{
                    Actions.add(label);
                }
            }

            State undesirableStt;
            for(int id : IDs){
                undesirableStt = a.getStateByID(id);
                List<Transition> undesirableTransitions = undesirableStt.getOutgoingTransitionsList();
                for(Transition t : undesirableTransitions){
                    t.setProbability(0.0);
                }
            }

            Transition undesirableAction;
            for(String action : Actions){
                undesirableAction = a.getTransitionByLabel(action);
                undesirableAction.setProbability(0.0);
            }
        }
        
        if(fromMenu){
            sourceStt = selectedEntry.getSource();
            mSrcSttField.setText(sourceStt);
            targetStt = selectedEntry.getTarget();
            mTgtSttField.setText(targetStt);
            inequation = selectedEntry.getInequation();
            if(inequation == null || inequation.trim().isEmpty()){
                mChoiceBox.getSelectionModel().clearSelection();
            }else{
                mChoiceBox.getSelectionModel().select(inequation);
            }
            optField = selectedEntry.getCondition();
            mOptionalField.setText(optField);
        }
        
        if(sourceStt == null || sourceStt.trim().isEmpty() || targetStt == null || targetStt.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Provide source and target states!");
            return null;
        }

        int sourceID = -1;
        int targetID = -1;
        int actionTargetID = -1;
        double actionP = 1;
        double p = 0.0;
        double probabilityTotal = 0.0;
        String result = "0";
        sourceStt = sourceStt.trim();
        targetStt = targetStt.trim();

        //State or Action?
        if(sourceStt.matches("\\d+")){
            sourceID = Integer.parseInt(sourceStt);
            if(sourceID >= a.getStatesCount()) sourceID = -1;
        }else{
            sourceID = a.getTransitionByLabel(sourceStt).getSource().getID();
        }

        if(sourceID == -1){
            JOptionPane.showMessageDialog(null, "State/Transition " + sourceStt + " is not in this component.",
                    "Probabilistic Reachability", JOptionPane.WARNING_MESSAGE);
            return "0";
        }

        targetStt = targetStt.replaceAll("\\s", "");
        targetStt = targetStt.trim();
        String[] targetStates = targetStt.split(",");

        // Checar se o estado alvo pedido foi E (final) ou -1 (erro).
        // Se sim, achar qual estado (ID) corresponde ao final/erro.

        for(String target : targetStates){
            targetID = -1;
            actionTargetID = -1;
            actionP = 1;

            switch (target){
                case "E":
                case "e":
                    targetID = a.getFinalState().getID();
                    break;

                case "-1":
                    targetID = a.getErrorState().getID();
                    break;

                default:
                    //System.out.println("executei 1 vez.");
                    if(target.matches("\\d+")){
                        actionTargetID = targetID = Integer.parseInt(target);
                        if(targetID >= a.getStatesCount()){ targetID = -1;}
                        //System.out.println("Entrei no PRIMEIRO IF. TargetID: " + targetID);
                    }else{
                        targetID = a.getTransitionByLabel(target).getSource().getID();
                        actionP = a.getTransitionByLabel(target).getProbability();
                        actionTargetID = a.getTransitionByLabel(target).getDestiny().getID();
                        //System.out.println("Entrei no SEGUNDO IF. TargetID: " + targetID);
                    }
            }

            if(targetID == -1){
                JOptionPane.showMessageDialog(null, "State/Transition " + targetStt + " is not in this component.",
                        "Probabilistic Reachability", JOptionPane.WARNING_MESSAGE);
                return "0";
            }

            p = new ProbabilisticReachAlgorithm().probabilityBetween(a, sourceID, targetID, steps, actionTargetID);
            p = p * actionP;
            probabilityTotal += p;

        }


        result = String.valueOf(probabilityTotal);
        
        if(optField == null || optField.trim().isEmpty()){
            mOutputField.setText(result);
            mOutputField.setStyle("-fx-background-color: yellow");
            if(fromMenu){ selectedEntry.setResult(result + " ok!");}
        }else{
            double optional = Double.parseDouble(optField);
            switch (inequation){
                case ">":
                    if(probabilityTotal > optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;
                case ">=":
                    if(probabilityTotal >= optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;
                case "<":
                    if(probabilityTotal < optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;
                case "<=":
                    if(probabilityTotal <= optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;
                case "=":
                    if(probabilityTotal == optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;
                case "!=":
                    if(probabilityTotal != optional){
                        mOutputField.setText("True");
                        mOutputField.setStyle("-fx-background-color: limegreen");
                        if(fromMenu){selectedEntry.setResult("True" + " ok!");}
                    }else{
                        mOutputField.setText("False");
                        mOutputField.setStyle("-fx-background-color: red");
                        if(fromMenu){selectedEntry.setResult("False" + " ok!");}
                    }
                    break;    
            }
        }
        
        return result;
        
    }

    private void start() {
        mBtnCalculate.setText("Calculate");
    }

    public void setComponent(Component c) {
        mViewer.setComponent(c);
        start();
    }

    private void applyChoiceStyle(Transition t) {
        t.setColor("blue");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setTextColor("blue");
        t.setWidth(2);
    }

    private void applyChoiceStyle(State s) {
        s.setColor(null);
        s.setBorderColor("blue");
        s.setTextSyle(Transition.TEXTSTYLE_BOLD);
        s.setTextColor("blue");
        s.setBorderWidth(2);
    }

    public static class Entry {

        private SimpleStringProperty  mSource;
        private SimpleStringProperty mTarget;
        private SimpleStringProperty mInequation;
        private SimpleStringProperty mCondition;
        private SimpleStringProperty mExcStt;
        private SimpleStringProperty mResult;

        Entry(String source, String target, String inequation, String condition, String excstt, String result) {
            this.mSource = new SimpleStringProperty(source);
            this.mTarget = new SimpleStringProperty(target);
            this.mInequation = new SimpleStringProperty(inequation);
            this.mCondition = new SimpleStringProperty(condition);
            this.mExcStt = new SimpleStringProperty(excstt);
            this.mResult = new SimpleStringProperty(result);
        }

        public String getSource() { return mSource.get(); }
        public void setSource(String source) {
            mSource.set(source);
        }

        public String getTarget() {
            return mTarget.get();
        }
        public void setTarget(String target) {
            mTarget.set(target);
        }

        public String getInequation() {
            return mInequation.get();
        }
        public void setInequation(String inequation) {
            mInequation.set(inequation);
        }
        
        public String getCondition() {
            return mCondition.get();
        }
        public void setCondition(String condition) {
            mCondition.set(condition);
        }

        public String getExcstt(){ return mExcStt.get(); }
        public void setExcstt(String excstt){ mExcStt.set(excstt); }
        
        public String getResult() {
            return mResult.get();
        }
        public void setResult(String result) {
            mResult.set(result);
        }

    }

}
