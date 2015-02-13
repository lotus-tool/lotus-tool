package br.uece.lotus.simulator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;

/**
 * Created by erickbs7 on 02/02/15.
 */
public class SimulatorUtils {

	public static void showChoices(State currentState) {
		applyEnableStyle(currentState);
		for (Transition t : currentState.getOutgoingTransitions()) {
			applyChoiceStyle(t);
			applyChoiceStyle(t.getDestiny());
		}
	}

	public static void hideChoices(State currentState) {
		applyDisabledStyle(currentState);
		for (Transition t : currentState.getOutgoingTransitionsList()) {
			applyDisabledStyle(t);
			applyDisabledStyle(t.getDestiny());
		}
	}

	public static void applyEnableStyle(State s) {
		s.setColor(null);
		s.setTextColor("black");
		s.setTextSyle(State.TEXTSTYLE_NORMAL);
		s.setBorderColor("black");
		s.setBorderWidth(1);
	}

	public static void applyEnableStyle(Transition t) {
		t.setColor("black");
		t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
		t.setTextColor("black");
		t.setWidth(1);
	}

	public static void applyDisabledStyle(State s) {
		s.setColor("#d0d0d0");
		s.setTextColor("#c0c0c0");
		s.setTextSyle(State.TEXTSTYLE_NORMAL);
		s.setBorderColor("gray");
		s.setBorderWidth(1);
	}

	public static void applyDisabledStyle(Transition t) {
		t.setColor("#d0d0d0");
		t.setTextColor("#c0c0c0");
		t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
		t.setWidth(1);
	}

	public static void applyChoiceStyle(Transition t) {
		t.setColor("blue");
		t.setTextSyle(Transition.TEXTSTYLE_BOLD);
		t.setTextColor("blue");
		t.setWidth(2);
	}

	public static void applyChoiceStyle(State s) {
		s.setColor(null);
		s.setBorderColor("blue");
		s.setTextSyle(Transition.TEXTSTYLE_BOLD);
		s.setTextColor("blue");
		s.setBorderWidth(2);
	}
}
