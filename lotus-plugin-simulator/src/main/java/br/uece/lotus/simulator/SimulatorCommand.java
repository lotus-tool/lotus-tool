package br.uece.lotus.simulator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;

/**
 * Created by erickbs7 on 29/01/15.
 */
public interface SimulatorCommand {
	public void doOperation();
	public void undoOperation();
}

