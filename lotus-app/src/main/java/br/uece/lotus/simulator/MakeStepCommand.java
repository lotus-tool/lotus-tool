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
	private State mState;
	private String mPreviousPathLabel;

	private State mSimulatorCurrentState;
	private State mStateSelectedByMouse;
	private Label mPathLabel;
	private SimulatorContext mSimulatorContext;

	public MakeStepCommand(SimulatorContext simulatorContext) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = simulatorContext.getmCurrentState();
		mPathLabel = simulatorContext.getmPathLabel();
		mPreviousPathLabel = mPathLabel.getText();
	}

	public MakeStepCommand(SimulatorContext simulatorContext, State stateSelectedByMouse) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = simulatorContext.getmCurrentState();
		mPathLabel = simulatorContext.getmPathLabel();
		mPreviousPathLabel = mPathLabel.getText();
		mStateSelectedByMouse = stateSelectedByMouse;
	}

	@Override
	public void doOperation() {
		Transition t;

		if (mStateSelectedByMouse == null) {
			t = selectTransitionByProbability();
		} else {
			t = mSimulatorCurrentState.getTransitionTo(mStateSelectedByMouse);
		}

		for (Transition tt : mSimulatorCurrentState.getOutgoingTransitionsList()) {
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
		mState = t.getDestiny();
		mPreviousState = t.getSource();
		mSimulatorContext.setmCurrentState(mState);

		if (mState.isFinal() || mState.isError()) {
			mPathLabel.setText(mPathLabel.getText() + " > ENDED");
			SimulatorUtils.applyEnableStyle(mState);
		} else if (mState.getOutgoingTransitionsCount() == 0) {
			mPathLabel.setText(mPathLabel.getText() + " > DEADLOCK!");
			SimulatorUtils.applyEnableStyle(mState);
		} else {
			SimulatorUtils.showChoices(mState);
		}
	}

	@Override
	public void undoOperation() {
		if (mState.isFinal() || mState.isError() || mState.getOutgoingTransitionsCount() == 0) {
			mPathLabel.setText(mPreviousPathLabel);
			SimulatorUtils.applyDisabledStyle(mState);
		} else {
			mPathLabel.setText(mPreviousPathLabel);
			SimulatorUtils.hideChoices(mState);
		}

		mSimulatorContext.setmCurrentState(mPreviousState);
		SimulatorUtils.showChoices(mSimulatorCurrentState);
		// mSteps.remove(mStep.size());

	}

	//Não há verificação de consistência na probabilidade das transições.
	//O método não seleciona uma transição de fato aleatória para o caso de ocorrer probabilidades de igual valor.
	private Transition selectTransitionByProbability() {
		List<Transition> transitionList = new ArrayList<>();
		Random randomGenerator = new Random();

		for (Transition t : mSimulatorCurrentState.getOutgoingTransitionsList()) {
			transitionList.add(t);
		}

//		Collections.sort(transitionList, new Comparator<Transition>() {
//			@Override
//			public int compare(Transition t1, Transition t2) {
//				if (t1.getProbability() < t2.getProbability()) return -1;
//				if (t1.getProbability() > t2.getProbability()) return 1;
//				return 0;
//			}
//		});

		int randomProbability = randomGenerator.nextInt(101);

		for (Transition t : transitionList) {
			if (t.getProbability() == null) {
				System.out.println("Não há probabilidade na transição.");
				System.out.println(randomProbability);
			}
			if (randomProbability <= t.getProbability()) {
				return t;
			}
		}

		return transitionList.get(transitionList.size() - 1);
	}
}