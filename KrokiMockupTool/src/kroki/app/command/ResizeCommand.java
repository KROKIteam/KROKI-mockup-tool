/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.awt.Dimension;
import java.awt.Point;
import kroki.profil.VisibleElement;

/**
 * Komanda promene veličine elementa.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ResizeCommand implements Command {

    /**Element*/
    private VisibleElement visibleElement;
    /**Nova pozicija*/
    private Point newPosition;
    /**Nova dimenzija*/
    private Dimension newDimension;
    /**Stara pozicija*/
    private Point oldPosition;
    /**Stara dimenzija*/
    private Dimension oldDimension;

    public ResizeCommand(VisibleElement visibleElement, Point newPosition, Dimension newDimension) {
        this.visibleElement = visibleElement;
        this.newPosition = newPosition;
        this.newDimension = newDimension;
        this.oldDimension = (Dimension) visibleElement.getComponent().getDimension().clone();
        this.oldPosition = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
    }

    public void doCommand() {
        visibleElement.getComponent().setAbsolutePosition(newPosition);
        visibleElement.getComponent().setDimension(newDimension);
        visibleElement.update();
    }

    public void undoCommand() {
        visibleElement.getComponent().setAbsolutePosition(oldPosition);
        visibleElement.getComponent().setDimension(oldDimension);
        visibleElement.update();
    }
}
