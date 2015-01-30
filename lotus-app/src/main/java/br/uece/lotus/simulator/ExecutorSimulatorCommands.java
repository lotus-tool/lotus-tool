import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author erick
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
		unmadeOpeartions.clear();
	}

	public void unmakeOperation() {
		if(!madeOperations.isEmpty()) {
			SimulatorCommand command = madeOperations.pool();
			command.undoOperations();
			unmadeOperations.offer(command);
		}
	}

	public void remakeOperation() {
		if(!unmadeOperations.isEmpty()) {
			SimulatorCommand command = new SimulatorCommand();
			command.doOperation();
			madeOperations.offer(command);
		}
	}

}
