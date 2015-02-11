package br.uece.lotus.simulator;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by erickbs7 on 29/01/15.
 */
public class ExecutorSimulatorCommands {
	private Deque<SimulatorCommand> madeOperations;
	private Deque<SimulatorCommand> unmadeOperations;

	public ExecutorSimulatorCommands() {
		madeOperations = new LinkedList<>();
		unmadeOperations = new LinkedList<>();
	}

	public void executeCommand(SimulatorCommand command) {
		command.doOperation();
		madeOperations.addLast(command);
		unmadeOperations.clear();
	}

	public void unmakeOperation() {
		if(!madeOperations.isEmpty()) {
			SimulatorCommand command = madeOperations.removeLast();
			command.undoOperation();
			unmadeOperations.addLast(command);
		}
	}

	public void remakeOperation() {
		if(!unmadeOperations.isEmpty()) {
			SimulatorCommand command = unmadeOperations.removeLast();
			command.doOperation();
			madeOperations.addLast(command);
		}
	}

	public void cleanMadeOperations() {
		madeOperations.clear();
	}

	public void cleanUnmadeOperations() {
		unmadeOperations.clear();
	}

}
