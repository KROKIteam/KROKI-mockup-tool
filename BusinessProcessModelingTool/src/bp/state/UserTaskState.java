package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.UserTask;

public class UserTaskState extends BPState {

    public UserTaskState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final UserTask task = new UserTask(getPanel().getProcess().getNameGenerator().nextTaskName());
        task.getTaskComponent().setX(p.x);
        task.getTaskComponent().setY(p.y);

        getPanel().getProcess().addElement(task);
        getPanel().repaint();
    }

    @Override
    public void enteringState() {
        System.out.println("entering user task state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting user task state");
    }

}
