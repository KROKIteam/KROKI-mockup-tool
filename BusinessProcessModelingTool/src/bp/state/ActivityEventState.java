package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.ActivityEvent;
import bp.model.data.ConditionalActivityEvent;
import bp.model.data.Element;
import bp.model.data.ErrorActivityEvent;
import bp.model.data.MessageActivityEvent;
import bp.model.data.SignalActivityEvent;
import bp.model.data.Task;
import bp.model.data.TimerActivityEvent;
import bp.util.ActivityEventLocationHelper;

public class ActivityEventState extends BPState {

    private final StateType eventType;

    public ActivityEventState(final BPPanel panel, final StateType eventType) {
        super(panel);
        if (eventType == null) {
            throw new IllegalArgumentException("eventType argument can't be null");
        }
        this.eventType = eventType;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final Element el = getGraphicPanel().getElementAt(p);
        if (el != null) {
            if (el instanceof Task) {
                final Task taskElement = (Task) el;
                final ActivityEvent event = createActivityEvent();
                final Point validPoint = ActivityEventLocationHelper.getValidPoint(taskElement.getTaskComponent(),
                        event.getEventComponent(), p);
                event.getEventComponent().setX(validPoint.x);
                event.getEventComponent().setY(validPoint.y);
                event.updateActivity(taskElement, null);

                getPanel().getProcess().addElement(event);
                getPanel().repaint();
            }
        }
    }

    @Override
    public void enteringState() {
        System.out.println("entering activity event state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting activity event state");
    }

    private ActivityEvent createActivityEvent() {
        final String eventName = getPanel().getProcess().getNameGenerator().nextEventName();
        if (this.eventType == StateType.MESSAGE_ACTIVITY_EVENT) {
            return new MessageActivityEvent(eventName);
        }
        if (this.eventType == StateType.TIMER_ACTIVITY_EVENT) {
            return new TimerActivityEvent(eventName);
        }
        if (this.eventType == StateType.CONDITIONAL_ACTIVITY_EVENT) {
            return new ConditionalActivityEvent(eventName);
        }
        if (this.eventType == StateType.SIGNAL_ACTIVITY_EVENT) {
            return new SignalActivityEvent(eventName);
        }
        if (this.eventType == StateType.ERROR_ACTIVITY_EVENT) {
            return new ErrorActivityEvent(eventName);
        }
        return null;
    }

}
