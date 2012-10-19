/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

/**
 * Interfejs koji predstavlja komandu. Svaka konkretna implementacija implementira metode undo i redo.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface Command {

    public void doCommand();

    public void undoCommand();
}
