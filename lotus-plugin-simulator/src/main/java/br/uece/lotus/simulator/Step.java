package br.uece.lotus.simulator;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by erickbs7 on 10/03/15.
 */
public class Step {
	private final SimpleStringProperty mAction;
	private final SimpleStringProperty mFrom;
	private final SimpleStringProperty mTo;

	public Step(String action, String from, String to) {
		mAction = new SimpleStringProperty(action);
		mFrom = new SimpleStringProperty(from);
		mTo = new SimpleStringProperty(to);
	}

	public String getmAction() {
		return mAction.get();
	}

	public SimpleStringProperty mActionProperty() {
		return mAction;
	}

	public void setmAction(String mAction) {
		this.mAction.set(mAction);
	}

	public String getmFrom() {
		return mFrom.get();
	}

	public SimpleStringProperty mFromProperty() {
		return mFrom;
	}

	public void setmFrom(String mFrom) {
		this.mFrom.set(mFrom);
	}

	public String getmTo() {
		return mTo.get();
	}

	public SimpleStringProperty mToProperty() {
		return mTo;
	}

	public void setmTo(String mTo) {
		this.mTo.set(mTo);
	}
}
