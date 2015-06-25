package br.uece.lotus.simulator;


import br.uece.lotus.Transition;
import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by erickbs7 on 15/05/15.
 */
public class RandomPathCommand extends Thread implements SimulatorCommand {
	private SimulatorContext mSimulatorContext;
	private br.uece.lotus.State mSimulatorCurrentState;
	private Boolean mContinueSimulation = true;
	private Boolean mContinueScheduler = true;

	public RandomPathCommand(SimulatorContext simulatorContext) {
		mSimulatorContext = simulatorContext;
		mSimulatorCurrentState = mSimulatorContext.getmCurrentState();
	}

	@Override
	public void run() {
		while(mContinueSimulation) {
			try {
				while (mContinueSimulation) {
					Thread.sleep(1000);
					Platform.runLater(() -> makeStep());
					makeStep();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
	}


	private void makeStep() {
		Transition t = selectRandomTransition();

		for (Transition tt : mSimulatorCurrentState.getOutgoingTransitionsList()) {
			if (tt == t) { SimulatorUtils.applyEnableStyle(t); }
		}

		mSimulatorCurrentState = t.getDestiny();
		mSimulatorContext.setmCurrentState(mSimulatorCurrentState);

		if (mSimulatorCurrentState.isFinal() || mSimulatorCurrentState.isError()) {
			SimulatorUtils.applyEnableStyle(mSimulatorCurrentState);
			mContinueSimulation = false;
		} else if (mSimulatorCurrentState.getOutgoingTransitionsCount() == 0) {
			SimulatorUtils.applyEnableStyle(mSimulatorCurrentState);
			mContinueSimulation = false;
		} else {
			SimulatorUtils.showChoices(mSimulatorCurrentState);
		}
	}

	private Transition selectRandomTransition() {
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(mSimulatorCurrentState.getOutgoingTransitionsCount());
		return mSimulatorCurrentState.getOutgoingTransitionsList().get(index);
	}

	@Override
	public void doOperation() {
		this.start();
	}

	@Override
	public void undoOperation() {

	}
}
