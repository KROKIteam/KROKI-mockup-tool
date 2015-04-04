package kroki.app.command;

/**
 * Interface which represents a command. Every implementation has to implement undo and redo methods
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface Command {

    public void doCommand();

    public void undoCommand();
}
