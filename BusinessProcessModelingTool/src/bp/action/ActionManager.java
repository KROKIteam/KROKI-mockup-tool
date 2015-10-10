package bp.action;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import bp.state.StateType;

public class ActionManager {

    private static final Action newDiagram = new NewDiagramAction("New Diagram", "Creates new diagram",
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));

    private static final Action open = new OpenAction("Open", "Open existing business process diagram", 
    		KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

    private static final Action save = new SaveAction("Save", "Save Changes Made", 
    		KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    
    private static final Action exit = new ExitAction("Exit", "Close Application");
    
    private static final Action select = new BPAction("Select", "Select Item", StateType.SELECT);

    private static final Action addTask = new BPAction("Task", "Add Task", StateType.TASK);
    private static final Action addUserTask = new BPAction("User Task", "Add User Task", StateType.USER_TASK);
    private static final Action addSystemTask = new BPAction("System Task", "Add System Task", StateType.SYSTEM_TASK);
    private static final Action addLane = new BPAction("Lane", "Add Lane", StateType.LANE);

    private static final Action addEdge = new BPAction("Edge", "Add Edge", StateType.EDGE);
    private static final Action addConditionalEdge = new BPAction("Con. Edge", "Add Conditional Edge",
            StateType.CONDITIONAL_EDGE);
    private static final Action addGateway = new BPAction("Gateway", "Add Gateway", StateType.GATEWAY);

    private static final Action addStartEvent = new BPAction("Normal", "Add Normal Start Event", StateType.START_EVENT);
    private static final Action addTimerStartEvent = new BPAction("Timer", "Add Timer Start Event",
            StateType.TIMER_START_EVENT);
    private static final Action addConditionalStartEvent = new BPAction("Condition", "Add Conditional Start Event",
            StateType.CONDITIONAL_START_EVENT);
    private static final Action addMessageStartEvent = new BPAction("Message", "Add Message Start Event",
            StateType.MESSAGE_START_EVENT);
    private static final Action addSignalStartEvent = new BPAction("Signal", "Add Signal Start Event",
            StateType.SIGNAL_START_EVENT);
    private static final Action addErrorStartEvent = new BPAction("Error", "Add Error Start Event",
            StateType.ERROR_START_EVENT);

    private static final Action addEndEvent = new BPAction("Normal", "Add Normal End Event", StateType.END_EVENT);
    private static final Action addMessageEndEvent = new BPAction("Message", "Add Message End Event",
            StateType.MESSAGE_END_EVENT);
    private static final Action addErrorEndEvent = new BPAction("Error", "Add Error End Event",
            StateType.ERROR_END_EVENT);
    private static final Action addSignalEndEvent = new BPAction("Signal", "Add Signal End Event",
            StateType.SIGNAL_END_EVENT);

    private static final Action addCatchTimerEvent = new BPAction("Timer", "Add Catch Timer Intermediate Event",
            StateType.TIMER_CATCH_EVENT);
    private static final Action addCatchConditionEvent = new BPAction("Condition",
            "Add Catch Conditional Intermediate Event", StateType.CONDITIONAL_CATCH_EVENT);
    private static final Action addCatchMessageEvent = new BPAction("Message", "Add Catch Message Intermediate Event",
            StateType.MESSAGE_CATCH_EVENT);
    private static final Action addCatchSignalEvent = new BPAction("Signal", "Add Catch Signal Intermediate Event",
            StateType.SIGNAL_CATCH_EVENT);
    private static final Action addCatchLinkEvent = new BPAction("Link", "Add Catch Link Event",
            StateType.LINK_CATCH_EVENT);

    private static final Action addThrowMessageEvent = new BPAction("Message", "Add Throw Message Intermediate Event",
            StateType.MESSAGE_THROW_EVENT);
    private static final Action addThrowSignalEvent = new BPAction("Signal", "Add Throw Signal Intermediate Event",
            StateType.SIGNAL_THROW_EVENT);
    private static final Action addThrowLinkEvent = new BPAction("Link", "Add Throw Link Event",
            StateType.LINK_THROW_EVENT);

    private static final Action addActivityMessageEvent = new BPAction("Message", "Add Message Activity Event",
            StateType.MESSAGE_ACTIVITY_EVENT);
    private static final Action addActivityTimerEvent = new BPAction("Timer", "Add Timer Activity Event",
            StateType.TIMER_ACTIVITY_EVENT);
    private static final Action addActivityConditionEvent = new BPAction("Condition", "Add Conditional Activity Event",
            StateType.CONDITIONAL_ACTIVITY_EVENT);
    private static final Action addActivitySignalEvent = new BPAction("Signal", "Add Signal Activity Event",
            StateType.SIGNAL_ACTIVITY_EVENT);
    private static final Action addActivityErrorEvent = new BPAction("Error", "Add Error Activity Event",
            StateType.ERROR_ACTIVITY_EVENT);

    public static Action getNewDiagram() {
        return newDiagram;
    }

    public static Action getExit() {
        return exit;
    }

    public static Action getSave() {
    	return save;
    }
    
    public static Action getOpen() {
    	return open;
    }

    public static Action getSelect() {
        return select;
    }

    public static Action getAddTask() {
        return addTask;
    }

    public static Action getAddUserTask() {
        return addUserTask;
    }

    public static Action getAddSystemTask() {
        return addSystemTask;
    }

    public static Action getAddLane() {
        return addLane;
    }

    public static Action getAddEdge() {
        return addEdge;
    }

    public static Action getAddConditionalEdge() {
        return addConditionalEdge;
    }

    public static Action getAddGateway() {
        return addGateway;
    }

    public static Action getAddStartEvent() {
        return addStartEvent;
    }

    public static Action getAddTimerStartEvent() {
        return addTimerStartEvent;
    }

    public static Action getAddConditionalStartEvent() {
        return addConditionalStartEvent;
    }

    public static Action getAddMessageStartEvent() {
        return addMessageStartEvent;
    }

    public static Action getAddSignalStartEvent() {
        return addSignalStartEvent;
    }

    public static Action getAddErrorStartEvent() {
        return addErrorStartEvent;
    }

    public static Action getAddEndEvent() {
        return addEndEvent;
    }

    public static Action getAddMessageEndEvent() {
        return addMessageEndEvent;
    }

    public static Action getAddErrorEndEvent() {
        return addErrorEndEvent;
    }

    public static Action getAddSignalEndEvent() {
        return addSignalEndEvent;
    }

    public static Action getAddCatchTimerEvent() {
        return addCatchTimerEvent;
    }

    public static Action getAddCatchConditionEvent() {
        return addCatchConditionEvent;
    }

    public static Action getAddCatchMessageEvent() {
        return addCatchMessageEvent;
    }

    public static Action getAddCatchSignalEvent() {
        return addCatchSignalEvent;
    }

    public static Action getAddCatchLinkEvent() {
        return addCatchLinkEvent;
    }

    public static Action getAddThrowMessageEvent() {
        return addThrowMessageEvent;
    }

    public static Action getAddThrowSignalEvent() {
        return addThrowSignalEvent;
    }

    public static Action getAddThrowLinkEvent() {
        return addThrowLinkEvent;
    }

    public static Action getAddActivityMessageEvent() {
        return addActivityMessageEvent;
    }

    public static Action getAddActivityTimerEvent() {
        return addActivityTimerEvent;
    }

    public static Action getAddActivityConditionEvent() {
        return addActivityConditionEvent;
    }

    public static Action getAddActivitySignalEvent() {
        return addActivitySignalEvent;
    }

    public static Action getAddActivityErrorEvent() {
        return addActivityErrorEvent;
    }

}
