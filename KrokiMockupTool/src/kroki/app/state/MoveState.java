package kroki.app.state;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import kroki.app.command.CommandManager;
import kroki.app.command.MoveCommand;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.profil.panel.VisibleClass;

/**
 * Class represent move state - which allows elements or groups of elements to be moved
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class MoveState extends State {

    private Point previousPosition;
    private Point startPosition;
    private Rectangle2D selectionRec;
    private boolean moved = false;

    public MoveState(Context context) {
        super(context, "app.state.move");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        SelectionModel selectionModel = c.getSelectionModel();
        if (!selectionModel.prepareForMove()) {
            moved = false;
            return;
        }
        Point thisCoord = e.getPoint();
        int dx = thisCoord.x - previousPosition.x;
        int dy = thisCoord.y - previousPosition.y;
        if (selectionRec == null) {
            selectionRec = selectionModel.getSelectionBounds();
        }
        selectionRec.setRect(selectionRec.getX() + dx, selectionRec.getY() + dy, selectionRec.getWidth(), selectionRec.getHeight());
        c.setSelectionRectangleBounds(selectionRec.getX(), selectionRec.getY(), selectionRec.getWidth(), selectionRec.getHeight());
        
        c.setShowSelectionRectangle(true);

        c.repaint();
        previousPosition = (Point) thisCoord.clone();
        moved = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        SelectionModel selectionModel = c.getSelectionModel();
        CommandManager commandManager = c.getCommandManager();
        if (moved && selectionRec != null) {
            Point thisCoord = e.getPoint();

            int dx = thisCoord.x - startPosition.x;
            int dy = thisCoord.y - startPosition.y;

            VisibleClass visibleClass = c.getVisibleClass();

            Point2D selPoint = selectionModel.getRelativePositionFor(selectionRec.getX(), selectionRec.getY());
            int sX = (int) selPoint.getX();
            int sY = (int) selPoint.getY();
            if (sX < 0) {
                dx = dx - sX;
            }
            if (sY < 0) {
                dy = dy - sY;
            }

            MoveCommand moveCommand = new MoveCommand(selectionModel.getVisibleElementList(), dx, dy);
            commandManager.addCommand(moveCommand);

            visibleClass.update();
            c.setShowSelectionRectangle(false);
            selectionRec = null;
            selectionModel.finishMove();
            moved = false;
        }
        c.repaint();
        context.goNext(State.SELECT_STATE);
    }

    public void setPreviousPosition(Point previousPosition) {
        this.previousPosition = previousPosition;
        this.startPosition = (Point) this.previousPosition.clone();
    }
}
