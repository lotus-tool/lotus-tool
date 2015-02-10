package br.uece.lotus.simulator;

import br.uece.lotus.State;
import javafx.scene.control.Label;

/**
 * Created by erickbs7 on 04/02/15.
 */
public class SimulatorContext {
	private State mCurrentState;
	private Label mPathLabel;
	private int mStepCount;
	// Observable list with step objects stay in this class?

	public State getmCurrentState() {
		return mCurrentState;
	}

	public void setmCurrentState(State mCurrentState) {
		this.mCurrentState = mCurrentState;
	}

	public Label getmPathLabel() {
		return mPathLabel;
	}

	public void setmPathLabel(Label mPathLabel) {
		this.mPathLabel = mPathLabel;
	}

	public int getmStepCount() {
		return mStepCount;
	}

	public void setmStepCount(int mStepCount) {
		this.mStepCount = mStepCount;
	}
}
