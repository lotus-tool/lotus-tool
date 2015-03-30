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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

/**
 *
 * @author emerson
 */
public class SimulatorWindow extends AnchorPane implements Window {
    private final int MOUSE_STEP = 1;
    private final int RANDOM_PROBABILISTIC_STEP = 2;
    private final int RANDOM_STEP = 3;

    private SimulatorContext mSimulatorContext;

    private final ToolBar mToolbar;
    private final Button mBtnStart;
    private final Button mBtnMakeStep;
    private final Button mBtnUnmakeStep;
    private final BasicComponentViewer mViewer;
    private final TableView<Step> mTableView;
    private final ExecutorSimulatorCommands mExecutorCommands;

    private final EventHandler<? super MouseEvent> onMouseClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            View v = mViewer.getViewByMouseCoordinates(e.getX(), e.getY());
            if (!(v instanceof StateView)) {
                return;
            }

            State mCurrentState = mSimulatorContext.getmCurrentState();
            State s = ((StateView) v).getState();
            Transition t = mSimulatorContext.getmCurrentState().getTransitionTo(s);

            if (t == null) {
                System.out.println(mSimulatorContext.getmCurrentState().getLabel());
                JOptionPane.showMessageDialog(null, "Select a valid state!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (mCurrentState.isFinal() || mCurrentState.isError() || mCurrentState.getOutgoingTransitionsCount() == 0) {
                mBtnStart.setText("Start");
                JOptionPane.showMessageDialog(null, "Error state or final reached!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            } else if (mCurrentState.onlySelfTransition() && mCurrentState.getmVisitedStatesCount() > 1) {
                JOptionPane.showMessageDialog(null, "Only self transition available!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            } else {
                MakeStepCommand madeStep = new MakeStepCommand(mSimulatorContext, s, MOUSE_STEP);
                mExecutorCommands.executeCommand(madeStep);
                mSteps.add(new Step(madeStep.getmTransistionLabel(), madeStep.getmPreviousStateLabel(), madeStep.getmStateLabel()));
            }
        }
    };

    private final TableColumn<Step, String> mActionCol;
    private final TableColumn<Step, String> mFromCol;
    private final TableColumn<Step, String> mToCol;
    private final ObservableList<Step> mSteps = FXCollections.observableArrayList();
    private final ScrollPane mScrollPanel;

    public SimulatorWindow() {
//        AnchorPane.setTopAnchor(mViewer, 38D);
//        AnchorPane.setLeftAnchor(mViewer, 0D);
//        AnchorPane.setRightAnchor(mViewer, 0D);
//        AnchorPane.setBottomAnchor(mViewer, 222D);

        mViewer = new BasicComponentViewer();
        mViewer.setOnMouseClicked(onMouseClick);
        mScrollPanel = new ScrollPane(mViewer);

        mSimulatorContext = new SimulatorContext();

        mExecutorCommands = new ExecutorSimulatorCommands();

        AnchorPane.setTopAnchor(mScrollPanel, 38D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 222D);
        mViewer.minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.minWidthProperty().bind(mScrollPanel.widthProperty());
        getChildren().add(mScrollPanel);

        mSimulatorContext.setmPathLabel(new Label(""));
        mSimulatorContext.getmPathLabel().setPrefHeight(22);
        AnchorPane.setLeftAnchor(mSimulatorContext.getmPathLabel(), 0D);
        AnchorPane.setRightAnchor(mSimulatorContext.getmPathLabel(), 0D);
        AnchorPane.setBottomAnchor(mSimulatorContext.getmPathLabel(), 200D);
        getChildren().add(mSimulatorContext.getmPathLabel());

        mBtnStart = new Button("Start");
        mBtnStart.setOnAction((ActionEvent e) -> {
            start();
        });

        mBtnUnmakeStep = new Button("Previous Step");
        mBtnUnmakeStep.setOnAction((ActionEvent e) -> {
            if (!mSimulatorContext.getmCurrentState().isInitial()) {
                mExecutorCommands.unmakeOperation();
                mSteps.remove(mSteps.size() - 1);
            }
            else {
                JOptionPane.showMessageDialog(null, "Initial state reached!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            }
        });

        mBtnMakeStep = new Button("Random Step");
        mBtnMakeStep.setOnAction((ActionEvent e) -> {

            State mCurrentState = mSimulatorContext.getmCurrentState();
            if (mCurrentState.isFinal() || mCurrentState.isError() || mCurrentState.getOutgoingTransitionsCount() == 0) {
                mBtnStart.setText("Start");
                JOptionPane.showMessageDialog(null, "Error state or final reached!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            } else if (mCurrentState.onlySelfTransition() && mCurrentState.getmVisitedStatesCount() > 1) {
                JOptionPane.showMessageDialog(null, "Only self transition available!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            } else {
                MakeStepCommand madeStep = new MakeStepCommand(mSimulatorContext, RANDOM_PROBABILISTIC_STEP);
                mExecutorCommands.executeCommand(madeStep);
                mSteps.add(new Step(madeStep.getmTransistionLabel(), madeStep.getmPreviousStateLabel(), madeStep.getmStateLabel()));
            }
        });



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
        mTableView.setItems(mSteps);
    }

    private void start() {
        mBtnStart.setText("Restart");
        mExecutorCommands.cleanMadeOperations();
        mExecutorCommands.cleanUnmadeOperations();
//        mStepCount = 0;
        mSteps.clear();
//        mPathLabel.setText("");
//        mCurrentState = mViewer.getComponent().getInitialState();

        mViewer.getComponent().clearVisitedStatesCount();

        mSimulatorContext.setmStepCount(0);
        mSimulatorContext.getmPathLabel().setText("");
        mSimulatorContext.setmCurrentState(mViewer.getComponent().getInitialState());

        if (mSimulatorContext.getmCurrentState() == null) {
            JOptionPane.showMessageDialog(null, "O componente não possui um estado inicial!");
            return;
        }

        for (State s : mViewer.getComponent().getStates()) {
            SimulatorUtils.applyDisabledStyle(s);
        }

        for (Transition t : mViewer.getComponent().getTransitions()) {
            SimulatorUtils.applyDisabledStyle(t);
        }

        SimulatorUtils.showChoices(mSimulatorContext.getmCurrentState());
        mSteps.add(new Step("", "", mSimulatorContext.getmCurrentState().getLabel()));
        mSimulatorContext.getmPathLabel().setText(mSimulatorContext.getmCurrentState().getLabel());
    }

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
