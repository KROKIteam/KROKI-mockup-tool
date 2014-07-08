package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.SystemTask;

public class SystemTaskState extends BPState {

    public SystemTaskState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final SystemTask task = new SystemTask(getPanel().getProcess().getNameGenerator().nextTaskName());
        task.getTaskComponent().setX(p.x);
        task.getTaskComponent().setY(p.y);

        getPanel().getProcess().addElement(task);
        getPanel().repaint();
    }

    @Override
    public void enteringState() {
        System.out.println("entering system task state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting system task state");
    }

}
