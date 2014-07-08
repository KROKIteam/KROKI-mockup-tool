package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Lane;

public class LaneState extends BPState {

    public LaneState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point position = e.getPoint();
        final Lane lane = new Lane(getPanel().getProcess().getNameGenerator().nextLaneName());
        lane.getLaneComponent().setX(position.x);
        lane.getLaneComponent().setY(position.y);
        lane.getLaneComponent().getHandlers().updateHandlers();

        getPanel().getProcess().addElement(lane);
        getPanel().repaint();
    }

    @Override
    public void enteringState() {
        System.out.println("entering lane state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting lane state");
    }

}
