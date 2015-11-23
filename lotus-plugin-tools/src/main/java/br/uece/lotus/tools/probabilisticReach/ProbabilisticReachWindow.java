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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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
    private final TextField mOutputField;
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
        AnchorPane.setBottomAnchor(mScrollPanel, 200D);
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
        Tooltip mToolTip = new Tooltip("Inequation");
        mChoiceBox.setTooltip(mToolTip);
        
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
        
        mOutputField = new TextField();
        mOutputField.setPromptText("Result");
        mOutputField.setPrefWidth(58.0);
        mOutputField.setEditable(false);
        mOutputField.setFocusTraversable(false);
        mOutputField.setMouseTransparent(true);
        mOutputField.setStyle("-fx-background-color: yellow; -fx-prompt-text-fill: black;");
        
        mTableView = new TableView();
        mTableView.setPrefHeight(200);
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

        String aux5 = mExcStt.getText();
        if(aux5 != null && !aux5.trim().isEmpty()){
            aux5 = aux5.replaceAll("\\s", "");
            String[] excStates = aux5.split(",");
            List<Integer> ids = new ArrayList<Integer>();
            for(String label : excStates){
                ids.add(Integer.parseInt(label));
            }
            State undesirable;
            for(int id : ids){
                undesirable = a.getStateByID(id);
                List<Transition> undesirableTransitions = undesirable.getOutgoingTransitionsList();
                for(Transition t : undesirableTransitions){
                    t.setProbability(0.0);
                }
            }
        }

        String aux1 = mSrcSttField.getText();
        String aux2 = mTgtSttField.getText();
        String aux3 = (String) mChoiceBox.getSelectionModel().getSelectedItem();
        String aux4 = mOptionalField.getText();
        
        if(fromMenu){
            aux1 = selectedEntry.getSource();
            mSrcSttField.setText(aux1);
            aux2 = selectedEntry.getTarget();
            mTgtSttField.setText(aux2);
            aux3 = selectedEntry.getInequation();
            if(aux3 == null || aux3.trim().isEmpty()){
                mChoiceBox.getSelectionModel().clearSelection();
            }else{
                mChoiceBox.getSelectionModel().select(aux3);
            }
            aux4 = selectedEntry.getCondition();
            mOptionalField.setText(aux4);
        }
        
        if(aux1 == null || aux1.trim().isEmpty() || aux2 == null || aux2.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Provide source and target states!");
            return null;
        }

        int sourceID = -1;
        int targetID = -1;

        //State?
        if(aux1.matches("\\d+")){
            sourceID = Integer.parseInt(aux1);
        }

        // Checar se o estado alvo pedido foi E (final) ou -1 (erro).
        // Se sim, achar qual estado (ID) corresponde ao final/erro.
        aux2 = aux2.trim();
        switch (aux2){
            case "E":
                for(State finalState : a.getStates()){
                    if(finalState.isFinal()){
                        targetID = finalState.getID();
                        break;
                    }
                }
                break;

            case "e":
                for(State finalState : a.getStates()){
                    if(finalState.isFinal()){
                        targetID = finalState.getID();
                        break;
                    }
                }
                break;

            case "-1":
                for(State errorState : a.getStates()){
                    if(errorState.isError()){
                        targetID = errorState.getID();
                        break;
                    }
                }
                break;

            default:
                if(aux2.matches("\\d+")){
                    targetID = Integer.parseInt(aux2);
                }
        }

        State source = a.getStateByID(0);//Initialization

        //sourceID = -1 => not state, transition.
        if(sourceID == -1){
            aux1 = aux1.trim();
            source = a.getTransitionByLabel(aux1).getDestiny();
        }else{
            source = a.getStateByID(sourceID);
        }

        State target = a.getStateByID(0); //Initialization

        //targetID = -1 => not state, transition.
        if(targetID == -1){
            target = a.getTransitionByLabel(aux2).getDestiny();
        }else{
            target = a.getStateByID(targetID);
        }


        double p = new ProbabilisticReachAlgorithm().probabilityBetween(a, source, target);
        String result = String.valueOf(p);
        
        if(aux4 == null || aux4.trim().isEmpty()){
            mOutputField.setText(result);
            mOutputField.setStyle("-fx-background-color: yellow");
            if(fromMenu){ selectedEntry.setResult(result + " ok!");}
        }else{
            double optional = Double.parseDouble(aux4);
            switch (aux3){
                case ">":
                    if(p > optional){
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
                    if(p >= optional){
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
                    if(p < optional){
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
                    if(p <= optional){
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
                    if(p == optional){
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
                    if(p != optional){
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
