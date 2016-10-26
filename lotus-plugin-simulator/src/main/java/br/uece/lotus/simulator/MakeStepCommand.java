package br.uece.lotus.simulator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import javafx.scene.control.Label;

import javax.script.ScriptException;
import java.util.*;

/**
 * Created by erickbs7 on 29/01/15.
 */
public class MakeStepCommand implements SimulatorCommand {

	private final int MOUSE_STEP = 1;
	private final int RANDOM_PROBABILISTIC_STEP = 2;
	private final int RANDOM_STEP = 3;
	private int TYPE_STEP;

	private State mPreviousState;
	private State mState;
	private Transition mTransition;
	private String mPreviousPathLabel;
	private State mStateSelectedByMouse;
        private Transition mTransitionSelectedByMouse;

	private SimulatorContext mSimulatorContext;
	private State mSimulatorCurrentState;
	private Label mSimulatorPathLabel;

	public MakeStepCommand(SimulatorContext simulatorContext, int typeStep) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = simulatorContext.getmCurrentState();
		mSimulatorPathLabel = simulatorContext.getmPathLabel();
		mPreviousPathLabel = mSimulatorPathLabel.getText();
		TYPE_STEP = typeStep;
	}

	public MakeStepCommand(SimulatorContext simulatorContext, State stateSelectedByMouse, int typeStep) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = simulatorContext.getmCurrentState();
		mSimulatorPathLabel = simulatorContext.getmPathLabel();
		mPreviousPathLabel = mSimulatorPathLabel.getText();
		mStateSelectedByMouse = stateSelectedByMouse;
		TYPE_STEP = typeStep;
	}
        
        public MakeStepCommand(SimulatorContext simulatorContext, Transition transitionSelectedByMouse, int typeStep) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = simulatorContext.getmCurrentState();
		mSimulatorPathLabel = simulatorContext.getmPathLabel();
		mPreviousPathLabel = mSimulatorPathLabel.getText();
		mTransitionSelectedByMouse = transitionSelectedByMouse;
		TYPE_STEP = typeStep;
	}

	@Override
	public void doOperation() {
		Transition t = null;

		switch (TYPE_STEP) {
			case MOUSE_STEP:
                                if(mStateSelectedByMouse != null){
                                    t = mSimulatorCurrentState.getTransitionTo(mStateSelectedByMouse);
                                }else{
                                    t = mTransitionSelectedByMouse;
                                }
                                break;
			case RANDOM_PROBABILISTIC_STEP:
				t = selectRandomTransitionByProbability();
				break;
			case RANDOM_STEP:
				t = selectRandomTransition();
				break;
			default:
				System.out.println("Invalid type of step!");
				return;
		}

		for (Transition tt : mSimulatorCurrentState.getOutgoingTransitionsList()) {
			if (tt == t) {
				SimulatorUtils.applyEnableStyle(t);
			} else {
				if (tt.getDestiny().getmVisitedStatesCount() < 1) {
					SimulatorUtils.applyDisabledStyle(tt.getDestiny());
				} else {
					SimulatorUtils.applyEnableStyle(tt.getDestiny());
				}

				if (tt.getmTransitionsStatesCount() < 1) {
					SimulatorUtils.applyDisabledStyle(tt);
				} else {
					SimulatorUtils.applyEnableStyle(tt);
				}
			}
		}

		mSimulatorPathLabel.setText(mSimulatorPathLabel.getText() + " > " + t.getLabel());
		mState = t.getDestiny();
		mState.incrementStatesCount();
		mPreviousState = t.getSource();
		mTransition = t;
		mTransition.incrementTransitionsCount();
		mSimulatorContext.setmCurrentState(mState);

		try {
			mSimulatorContext.getmEngine().eval(t.getLabel());
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		if (mState.isFinal() || mState.isError()) {
			mSimulatorPathLabel.setText(mSimulatorPathLabel.getText() + " > ENDED");
			SimulatorUtils.applyEnableStyle(mState);
		} else if (mState.getOutgoingTransitionsCount() == 0) {
			mSimulatorPathLabel.setText(mSimulatorPathLabel.getText() + " > DEADLOCK!");
			SimulatorUtils.applyEnableStyle(mState);
		} else {
			SimulatorUtils.showChoices(mState, mSimulatorContext);
		}
	}

	@Override
	public void undoOperation() {
		mState.decrementStatesCount();
		mTransition.decrementTransitionsCount();

		if (mState.isFinal() || mState.isError() || mState.getOutgoingTransitionsCount() == 0) {
			mSimulatorPathLabel.setText(mPreviousPathLabel);

			if (mState.getmVisitedStatesCount() < 1) {
				SimulatorUtils.applyDisabledStyle(mState);
				mState.setmVisitedStatesCount(0);
			}
			if (mTransition.getmTransitionsStatesCount() < 1) {
				SimulatorUtils.applyDisabledStyle(mPreviousState.getTransitionTo(mState));
				mTransition.setmTransitionsStatesCount(0);
			}
		} else {
			mSimulatorPathLabel.setText(mPreviousPathLabel);
			SimulatorUtils.hideChoices(mState);
		}

		mSimulatorContext.setmCurrentState(mPreviousState);
		SimulatorUtils.showChoices(mSimulatorCurrentState, mSimulatorContext);
	}

	private Transition selectRandomTransitionByProbability() {
		Transition selectedTransition = null;
		Iterator it = mSimulatorCurrentState.getOutgoingTransitions().iterator();
		Boolean transitionWithNullProbability = false;
		double probabilityCounter = 0;
		Random randomGenerator = new Random();
		double randomProbability = randomGenerator.nextDouble();

		while(it.hasNext()) {
			Transition t = (Transition) it.next();

			if (t.getProbability() != null) {
				probabilityCounter += t.getProbability();
				//Test if the randomProbability resides on the probability range of the current transition.
				if ((randomProbability <= probabilityCounter) && (selectedTransition == null)) selectedTransition = t;
			} else {
				transitionWithNullProbability = true;
				break;
			}
		}

		if ( transitionWithNullProbability || (probabilityCounter != 1) ) selectedTransition = selectRandomTransition();

		return selectedTransition;
	}

	private Transition selectRandomTransition() {
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(mSimulatorCurrentState.getOutgoingTransitionsCount());
		return mSimulatorCurrentState.getOutgoingTransitionsList().get(index);
	}

	public String getmTransistionLabel() { return mTransition.getLabel(); }

	public String getmPreviousStateLabel() { return mPreviousState.getLabel(); }

	public String getmStateLabel() { return mState.getLabel(); }

}