/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class CommandManager {

    private List<Command> commandList;
    private int currentCommand = 0;

    public CommandManager() {
        commandList = new ArrayList<Command>();
    }

    public void addCommand(Command command) {
        while (currentCommand < commandList.size()) {
            commandList.remove(currentCommand);
        }
        commandList.add(command);
        doCommand();
    }

    /**
     * Ponavlja operaciju
     */
    public void doCommand() {
        if (currentCommand < commandList.size()) {
            commandList.get(currentCommand++).doCommand();
        }
    }

    /**
     * Ponistava operaciju
     */
    public void undoCommand() {
        if (currentCommand > 0) {
            commandList.get(--currentCommand).undoCommand();
        }
    }
}
