package graphedit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// Invoker
public class CommandManager extends Observable { 

	protected List<Command> commands = new ArrayList<Command>();
	protected int currentCommandIndex = -1;

	public void executeCommand(Command command) {
		for (int i = commands.size() - 1; i > currentCommandIndex; i--)
			commands.remove(i); 
		commands.add(command);
		command.execute();
		currentCommandIndex++;
		fireUpdates();
	}

	public void undo() {
		if (currentCommandIndex > -1) {
			Command currentCommandToUndo = commands.get(currentCommandIndex);
			currentCommandToUndo.undo();
			currentCommandIndex--;
			fireUpdates();
		}
	}

	public void redo() {
		if (currentCommandIndex < commands.size() - 1) {
			Command currentCommandToRedo = commands.get(currentCommandIndex + 1);
			currentCommandToRedo.execute();
			currentCommandIndex++;
			fireUpdates();
		}
	}
	
	public void fireUpdates() {
		this.setChanged();
		this.notifyObservers();
		}
		
	public boolean isRedoable() { return currentCommandIndex < commands.size() - 1; }
	
	public boolean isUndoable() { return currentCommandIndex > -1; }
	
	/**
	 * This method restores persistent diagram state.
	 * @author specijalac
	 */
	public void restoreDiagram() {
		while (isUndoable()) undo();
		commands.clear();
		currentCommandIndex = -1;
	}

}