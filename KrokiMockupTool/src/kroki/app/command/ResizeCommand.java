package kroki.app.command;

import java.awt.Dimension;
import java.awt.Point;

import kroki.profil.VisibleElement;

/**
 * Command for changing the size of the elements
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ResizeCommand implements Command {

    /**Element*/
    private VisibleElement visibleElement;
    /**New position*/
    private Point newPosition;
    /**New dimension*/
    private Dimension newDimension;
    /**Old position*/
    private Point oldPosition;
    /**Old dimension*/
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
