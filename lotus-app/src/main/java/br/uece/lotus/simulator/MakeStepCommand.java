package br.uece.lotus.simulator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import javafx.scene.control.Label;

import java.util.*;

/**
 * Created by erickbs7 on 29/01/15.
 */
public class MakeStepCommand implements SimulatorCommand {

	private State mPreviousState;
	private State mCurrentState;
	private State mStateSelectedByMouse;
	private Label mPathLabel;
//	private String previousPathLabel;


	public MakeStepCommand(State previousState, Label pathLabel) {
		mPreviousState = previousState;
		mPathLabel = pathLabel;
		// previousPathLabel = mPathLabel.getText();
	}

	public MakeStepCommand(State previousState, State stateSelectedByMouse, Label pathLabel) {
		mPreviousState = previousState;
		mStateSelectedByMouse = stateSelectedByMouse;
		mPathLabel = pathLabel;
		// previousPathLabel = mPathLabel.getText();
	}

	@Override
	public void doOperation() {
		Transition t;

		if (mStateSelectedByMouse == null) {
			t = selectTransitionByProbability();
		} else {
			t = mPreviousState.getTransitionTo(mStateSelectedByMouse);
		}

		for (Transition tt : mPreviousState.getOutgoingTransitionsList()) {
			if (tt == t) {
				SimulatorUtils.applyEnableStyle(t);
			} else {
				SimulatorUtils.applyDisabledStyle(tt);
				SimulatorUtils.applyDisabledStyle(tt.getDestiny());
			}
		}
		// Criar classe separada para Step ????
		//		mSteps.add(new Step(t.getLabel(), mCurrentState.getLabel(), s.getLabel()));
		mPathLabel.setText(mPathLabel.getText() + " > " + t.getLabel());
		mCurrentState = t.getDestiny();

		if (mCurrentState.isFinal() || mCurrentState.isError()) {
			mPathLabel.setText(mPathLabel.getText() + " > ENDED");
			SimulatorUtils.applyEnableStyle(mCurrentState);
		} else if (mCurrentState.getOutgoingTransitionsCount() == 0) {
			mPathLabel.setText(mPathLabel.getText() + " > DEADLOCK!");
			SimulatorUtils.applyEnableStyle(mCurrentState);
		} else {
			SimulatorUtils.showChoices(mCurrentState);
		}
	}

	@Override
	public void undoOperation() {
		if (mCurrentState.isFinal() || mCurrentState.isError() || mCurrentState.getOutgoingTransitionsCount() == 0) {
			// Retirar palavra do final até ">". (Expressões regulares?)
			SimulatorUtils.applyDisabledStyle(mCurrentState);
		} else {
			SimulatorUtils.hideChoices(mCurrentState);
		}

		mCurrentState = mPreviousState;
		// Retornar mPathLabel para o estado anterior. (Expressões regulares?)
		// mSteps.remove(mStep.size());

		for (Transition tt : mCurrentState.getOutgoingTransitionsList()) {
			SimulatorUtils.applyEnableStyle(tt);
		}
	}

	private Transition selectTransitionByProbability() {
		List<Transition> transitionList = mPreviousState.getOutgoingTransitionsList();
		Random randomGenerator = new Random();

		Collections.sort(transitionList, new Comparator<Transition>() {
			@Override
			public int compare(Transition t1, Transition t2) {
				if (t1.getProbability() < t2.getProbability()) return -1;
				if (t1.getProbability() > t2.getProbability()) return 1;
				return 0;
			}
		});

		double randomProbability = randomGenerator.nextDouble();

		for (Transition t : transitionList) {
			if (randomProbability <= t.getProbability()) return t;
		}

		return null;
	}

}
