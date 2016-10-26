package br.uece.lotus.simulator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import javafx.scene.control.Label;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by erickbs7 on 04/02/15.
 */
public class SimulatorContext {

	private final ScriptEngine mEngine;
	private State mCurrentState;
	private Label mPathLabel;
	private int mStepCount;
        private Transition mCurrentTransition;

	public ScriptEngine getmEngine() {
		return mEngine;
	}

	public SimulatorContext() {
		ScriptEngineManager manager = new ScriptEngineManager();
		mEngine = manager.getEngineByName("JavaScript");
	}
	
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

    public Transition getmCurrentTransition() {
        return mCurrentTransition;
    }

    public void setmCurrentTransition(Transition mCurrentTransition) {
        this.mCurrentTransition = mCurrentTransition;
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
