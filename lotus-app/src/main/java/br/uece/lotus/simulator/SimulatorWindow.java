/*
 * The MIT License
 *
 * Copyright 2014 emerson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.simulator;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.viewer.BasicComponentViewer;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

/**
 *
 * @author emerson
 */
public class SimulatorWindow extends AnchorPane implements Window {

    private int mStepCount;
    private State mCurrentState;

    private final ToolBar mToolbar;
    private final Button mBtnStart;
    private final Button mBtnMakeStep;
    private final Button mBtnUnmakeStep;
    private final BasicComponentViewer mViewer;
    private final TableView<Step> mTableView;
    private final Label mPathLabel;
    private final ExecutorSimulatorCommands mExecutorCommands;

    private final EventHandler<? super MouseEvent> onMouseClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            View v = mViewer.getViewByMouseCoordinates(e.getX(), e.getY());
            if (!(v instanceof StateView)) {
                return;
            }
            State s = ((StateView) v).getState();
            Transition t = mCurrentState.getTransitionTo(s);
            if (t == null) {
                System.out.println("-- selecione um estado valido!");
                return;
            }

            mExecutorCommands.executeCommand(new MakeStepCommand(mCurrentState, s, mPathLabel));

//            for (Transition tt : mCurrentState.getOutgoingTransitions()) {
//                if (tt == t) {
//                    SimulatorUtils.applyEnableStyle(t);
//                } else {
//                    SimulatorUtils.applyDisabledStyle(tt);
//                    SimulatorUtils.applyDisabledStyle(tt.getDestiny());
//                }
//            }
//
//            mSteps.add(new Step(t.getLabel(), mCurrentState.getLabel(), s.getLabel()));
//            mPathLabel.setText(mPathLabel.getText() + " > " + t.getLabel());
//            mCurrentState = s;
//            if (s.isFinal() || s.isError()) {
//                mPathLabel.setText(mPathLabel.getText() + " > ENDED");
//                SimulatorUtils.applyEnableStyle(mCurrentState);
//                mBtnStart.setText("Start");
//            } else if (s.getOutgoingTransitionsCount() == 0) {
//                mPathLabel.setText(mPathLabel.getText() + " DEADLOCK!");
//                SimulatorUtils.applyEnableStyle(mCurrentState);
//                mBtnStart.setText("Start");
//            } else {
//                SimulatorUtils.showChoices(mCurrentState);
//            }
        }
    };

    private final TableColumn<Step, String> mActionCol;
    private final TableColumn<Step, String> mFromCol;
    private final TableColumn<Step, String> mToCol;
    private final ObservableList<Step> mSteps = FXCollections.observableArrayList();
    private final ScrollPane mScrollPanel;

    public SimulatorWindow() {
        mViewer = new BasicComponentViewer();
//        AnchorPane.setTopAnchor(mViewer, 38D);
//        AnchorPane.setLeftAnchor(mViewer, 0D);
//        AnchorPane.setRightAnchor(mViewer, 0D);
//        AnchorPane.setBottomAnchor(mViewer, 222D);
        mExecutorCommands = new ExecutorSimulatorCommands();
        mViewer.setOnMouseClicked(onMouseClick);
        mScrollPanel = new ScrollPane(mViewer);0
        AnchorPane.setTopAnchor(mScrollPanel, 38D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 222D);
        mViewer.minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.minWidthProperty().bind(mScrollPanel.widthProperty());
        getChildren().add(mScrollPanel);

        mPathLabel = new Label("");
        mPathLabel.setPrefHeight(22);
        AnchorPane.setLeftAnchor(mPathLabel, 0D);
        AnchorPane.setRightAnchor(mPathLabel, 0D);
        AnchorPane.setBottomAnchor(mPathLabel, 200D);
        getChildren().add(mPathLabel);

        mBtnStart = new Button("Start");
        mBtnStart.setOnAction((ActionEvent e) -> {
            start();
        });

        mBtnUnmakeStep = new Button("Previous Step");
        mBtnUnmakeStep.setOnAction((ActionEvent e) -> {
            if (mCurrentState.isInitial()) {
                JOptionPane.showMessageDialog(null, "Estado inicial atingido!");
            } else {
                mExecutorCommands.unmakeOperation();
            }
        });

        mBtnMakeStep = new Button("Next Step");
        mBtnMakeStep.setOnAction((ActionEvent e) -> {
            mExecutorCommands.executeCommand(new MakeStepCommand(mCurrentState, mPathLabel));
            if (mCurrentState.isFinal() || mCurrentState.isError() || mCurrentState.getOutgoingTransitionsCount() == 0) {
                mBtnStart.setText("Start");
                mBtnMakeStep.setVisible(false);
                mBtnUnmakeStep.setVisible(false);
            }
        });

        mBtnMakeStep.setVisible(false);
        mBtnUnmakeStep.setVisible(false);

        mToolbar = new ToolBar();
        mToolbar.getItems().addAll(mBtnStart, mBtnMakeStep, mBtnUnmakeStep);
        AnchorPane.setTopAnchor(mToolbar, 0D);
        AnchorPane.setLeftAnchor(mToolbar, 0D);
        AnchorPane.setRightAnchor(mToolbar, 0D);
        getChildren().add(mToolbar);

        mTableView = new TableView();
        mTableView.setPrefHeight(200);
        AnchorPane.setLeftAnchor(mTableView, 0D);
        AnchorPane.setRightAnchor(mTableView, 0D);
        AnchorPane.setBottomAnchor(mTableView, 0D);
        getChildren().add(mTableView);

        mActionCol = new TableColumn<>("Action");
        mActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        mActionCol.setPrefWidth(100);
        mFromCol = new TableColumn<>("From");
        mFromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        mFromCol.setPrefWidth(100);
        mToCol = new TableColumn<>("To");
        mToCol.setPrefWidth(100);
        mToCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        mTableView.getColumns().addAll(mActionCol, mFromCol, mToCol);
//        mTableView.setItems(mSteps);
    }

    private void start() {
        mBtnStart.setText("Restart");
        mExecutorCommands.cleanMadeOperations();
        mExecutorCommands.cleanUnmadeOperations();
        mBtnMakeStep.setVisible(true);
        mBtnUnmakeStep.setVisible(true);
        mStepCount = 0;
        mSteps.clear();
        mPathLabel.setText("");
        mCurrentState = mViewer.getComponent().getInitialState();

        if (mCurrentState == null) {
            JOptionPane.showMessageDialog(null, "O componente não possui um estado inicial!");
            return;
        }

        for (State s : mViewer.getComponent().getStates()) {
            SimulatorUtils.applyDisabledStyle(s);
        }

        for (Transition t : mViewer.getComponent().getTransitions()) {
            SimulatorUtils.applyDisabledStyle(t);
        }

        SimulatorUtils.showChoices(mCurrentState);
        mSteps.add(new Step("", "", mCurrentState.getLabel()));
        mPathLabel.setText(mCurrentState.getLabel());
    }

//    private void showChoices() {
//        applyEnableStyle(mCurrentState);
//        for (Transition t : mCurrentState.getOutgoingTransitions()) {
//            applyChoiceStyle(t);
//            applyChoiceStyle(t.getDestiny());
//        }
//    }
//
//    private void applyEnableStyle(State s) {
//        s.setColor(null);
//        s.setTextColor("black");
//        s.setTextSyle(State.TEXTSTYLE_NORMAL);
//        s.setBorderColor("black");
//        s.setBorderWidth(1);
//    }
//
//    private void applyEnableStyle(Transition t) {
//        t.setColor("black");
//        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
//        t.setTextColor("black");
//        t.setWidth(1);
//    }
//
//    private void applyDisabledStyle(State s) {
//        s.setColor("#d0d0d0");
//        s.setTextColor("#c0c0c0");
//        s.setTextSyle(State.TEXTSTYLE_NORMAL);
//        s.setBorderColor("gray");
//        s.setBorderWidth(1);
//    }
//
//    private void applyDisabledStyle(Transition t) {
//        t.setColor("#d0d0d0");
//        t.setTextColor("#c0c0c0");
//        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
//        t.setWidth(1);
//    }
//
//    private void applyChoiceStyle(Transition t) {
//        t.setColor("blue");
//        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
//        t.setTextColor("blue");
//        t.setWidth(2);
//    }
//
//    private void applyChoiceStyle(State s) {
//        s.setColor(null);
//        s.setBorderColor("blue");
//        s.setTextSyle(Transition.TEXTSTYLE_BOLD);
//        s.setTextColor("blue");
//        s.setBorderWidth(2);
//    }

    @Override
    public Component getComponent() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponent(Component c) {        
        mViewer.setComponent(c);
        start();        
    }

    @Override
    public String getTitle() {
        Component c = mViewer.getComponent();
        return c.getName() + " [Simulator]";
    }

    @Override
    public Node getNode() {
        return this;
    }

    public static class Step {

        private String mAction;
        private String mFrom;
        private String mTo;

        Step(String action, String from, String to) {
            mAction = action;
            mFrom = from;
            mTo = to;
        }

        public String getAction() {
            return mAction;
        }

        public void setAction(String action) {
            this.mAction = action;
        }

        public String getFrom() {
            return mFrom;
        }

        public void setFrom(String from) {
            this.mFrom = from;
        }

        public String getTo() {
            return mTo;
        }

        public void setTo(String to) {
            this.mTo = to;
        }

    }

}
