package br.uece.lotus.simulator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by erickbs7 on 10/03/15.
 */
public class Step {
	private final StringProperty mAction;
	private final StringProperty mFrom;
	private final StringProperty mTo;

	public Step(String action, String from, String to) {
		mAction = new SimpleStringProperty(action);
		mFrom = new SimpleStringProperty(from);
		mTo = new SimpleStringProperty(to);
	}

	public String getAction() {
		return mAction.get();
	}

	public void setAction(String action) {
		mAction.set(action);
	}

	public String getFrom() {
		return mFrom.get();
	}

	public void setFrom(String from) {
		mFrom.set(from);
	}

	public String getTo() {
		return mTo.get();
	}

	public void setTo(String to) {
		mTo.set(to);
	}
}
