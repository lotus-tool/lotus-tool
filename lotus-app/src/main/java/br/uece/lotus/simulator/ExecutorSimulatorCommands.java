package br.uece.lotus.simulator;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by erickbs7 on 29/01/15.
 */
public class ExecutorSimulatorCommands {
	private Queue<SimulatorCommand> madeOperations;
	private Queue<SimulatorCommand> unmadeOperations;

	public ExecutorSimulatorCommands() {
		madeOperations = new LinkedList<>();
		unmadeOperations = new LinkedList<>();
	}

	public void executeCommand(SimulatorCommand command) {
		command.doOperation();
		madeOperations.offer(command);
		unmadeOperations.clear();
	}

	public void unmakeOperation() {
		if(!madeOperations.isEmpty()) {
			SimulatorCommand command = madeOperations.poll();
			command.undoOperation();
			unmadeOperations.offer(command);
		}
	}

	public void remakeOperation() {
		if(!unmadeOperations.isEmpty()) {
			SimulatorCommand command = unmadeOperations.poll();
			command.doOperation();
			madeOperations.offer(command);
		}
	}

	public void cleanMadeOperations() {
		madeOperations.clear();
	}

	public void cleanUnmadeOperations() {
		unmadeOperations.clear();
	}

}
