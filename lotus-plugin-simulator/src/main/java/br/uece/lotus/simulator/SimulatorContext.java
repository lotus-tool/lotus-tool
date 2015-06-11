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
