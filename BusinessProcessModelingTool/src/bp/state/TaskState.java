package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Task;

public class TaskState extends BPState {

    public TaskState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point position = e.getPoint();
        System.out.println(position);
        final Task task = new Task(getPanel().getProcess().getNameGenerator().nextTaskName());
        task.getTaskComponent().setX(position.x);
        task.getTaskComponent().setY(position.y);

        getPanel().getProcess().addElement(task);
        getPanel().repaint();
    }

    @Override
    public void enteringState() {
        System.out.println("entering task state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting task state");
    }

}
