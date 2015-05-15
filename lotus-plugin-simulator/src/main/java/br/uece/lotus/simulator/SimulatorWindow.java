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
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import br.uece.lotus.viewer.StateView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author emerson
 */
public class SimulatorWindow extends AnchorPane implements Window, Initializable {

    private final int MOUSE_STEP = 1;
    private final int RANDOM_PROBABILISTIC_STEP = 2;
    private final int RANDOM_STEP = 3;

    private SimulatorContext mSimulatorContext;

    @FXML
    private ToolBar mToolbar;
    @FXML
    private Button mBtnStart;
    @FXML
    private Button mBtnMakeStep;
    @FXML
    private Button mBtnUnmakeStep;
    @FXML
    private TableView<Step> mTableView;
    @FXML
    private TableColumn<Step, String> mActionCol;
    @FXML
    private TableColumn<Step, String> mFromCol;
    @FXML
    private TableColumn<Step, String> mToCol;
    @FXML
    private ScrollPane mScrollPanel;

    private ComponentView mViewer;
    private ExecutorSimulatorCommands mExecutorCommands;

    private final EventHandler<? super MouseEvent> onMouseClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            StateView v = mViewer.locateStateView(new Point2D(e.getSceneX(), e.getSceneY()));
            if (v == null) {
                return;
            }

            State mCurrentState = mSimulatorContext.getmCurrentState();
            State s = v.getState();
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

    private final ObservableList<Step> mSteps = FXCollections.observableArrayList();
    private Node mNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentViewImpl();
        mViewer.getNode().setOnMouseClicked(onMouseClick);
        mScrollPanel.setContent((Node) mViewer);

        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());

        mSimulatorContext.setmPathLabel(new Label(""));

        mBtnStart.setOnAction((ActionEvent e) -> {
            start();
        });

        mBtnUnmakeStep.setOnAction((ActionEvent e) -> {
            if (!mSimulatorContext.getmCurrentState().isInitial()) {
                mExecutorCommands.unmakeOperation();
                mSteps.remove(mSteps.size() - 1);
            } else {
                JOptionPane.showMessageDialog(null, "Initial state reached!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
            }
        });

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

        mActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        mFromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        mToCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        mTableView.setItems(mSteps);
    }

    public SimulatorWindow() {
        mSimulatorContext = new SimulatorContext();
        mExecutorCommands = new ExecutorSimulatorCommands();
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
        return mNode;
    }

    public void setNode(Parent node) {
        this.mNode = node;
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
