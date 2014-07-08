package bp.state;

import bp.gui.BPPanel;

public class StateManager {

    private final BPPanel panel;

    private State currentState;
    private final State defaultState;

    private final SelectState selectState;
    private final TaskState taskState;
    private final UserTaskState userTaskState;
    private final SystemTaskState systemTaskState;
    private final EdgeState edgeState;
    private final ConditionalEdgeState conditionalEdgeState;
    private final GatewayState gatewayState;
    private final LaneState laneState;
    private final MoveState moveState;
    private final EdgeMoveState moveEdgeState;
    private final ResizeState resizeState;
    private final EventState startEventState;
    private final EventState timerStartEventState;
    private final EventState conditionalStartEventState;
    private final EventState messageStartEventState;
    private final EventState signalStartEventState;
    private final EventState errorStartEventState;
    private final EventState endEventState;
    private final EventState messageEndEventState;
    private final EventState errorEndEventState;
    private final EventState signalEndEventState;
    private final EventState timerCatchEventState;
    private final EventState conditionalCatchEventState;
    private final EventState messageCatchEventState;
    private final EventState signalCatchEventState;
    private final EventState linkCatchEventState;
    private final EventState messageThrowEventState;
    private final EventState signalThrowEventState;
    private final EventState linkThrowEventState;
    private final ActivityEventState messageActivityEventState;
    private final ActivityEventState timerActivityEventState;
    private final ActivityEventState conditionalActivityEventState;
    private final ActivityEventState signalActivityEventState;
    private final ActivityEventState errorActivityEventState;

    public StateManager(final BPPanel panel) {
        this.panel = panel;

        this.selectState = new SelectState(panel);
        this.taskState = new TaskState(panel);
        this.userTaskState = new UserTaskState(panel);
        this.systemTaskState = new SystemTaskState(panel);
        this.edgeState = new EdgeState(panel);
        this.conditionalEdgeState = new ConditionalEdgeState(panel);
        this.gatewayState = new GatewayState(panel);
        this.laneState = new LaneState(panel);
        this.moveState = new MoveState(panel);
        this.moveEdgeState = new EdgeMoveState(panel);
        this.resizeState = new ResizeState(panel);
        this.startEventState = new EventState(panel, StateType.START_EVENT);
        this.timerStartEventState = new EventState(panel, StateType.TIMER_START_EVENT);
        this.conditionalStartEventState = new EventState(panel, StateType.CONDITIONAL_START_EVENT);
        this.messageStartEventState = new EventState(panel, StateType.MESSAGE_START_EVENT);
        this.signalStartEventState = new EventState(panel, StateType.SIGNAL_START_EVENT);
        this.errorStartEventState = new EventState(panel, StateType.ERROR_START_EVENT);
        this.endEventState = new EventState(panel, StateType.END_EVENT);
        this.messageEndEventState = new EventState(panel, StateType.MESSAGE_END_EVENT);
        this.errorEndEventState = new EventState(panel, StateType.ERROR_END_EVENT);
        this.signalEndEventState = new EventState(panel, StateType.SIGNAL_END_EVENT);
        this.timerCatchEventState = new EventState(panel, StateType.TIMER_CATCH_EVENT);
        this.conditionalCatchEventState = new EventState(panel, StateType.CONDITIONAL_CATCH_EVENT);
        this.messageCatchEventState = new EventState(panel, StateType.MESSAGE_CATCH_EVENT);
        this.signalCatchEventState = new EventState(panel, StateType.SIGNAL_CATCH_EVENT);
        this.linkCatchEventState = new EventState(panel, StateType.LINK_CATCH_EVENT);
        this.messageThrowEventState = new EventState(panel, StateType.MESSAGE_THROW_EVENT);
        this.signalThrowEventState = new EventState(panel, StateType.SIGNAL_THROW_EVENT);
        this.linkThrowEventState = new EventState(panel, StateType.LINK_THROW_EVENT);
        this.messageActivityEventState = new ActivityEventState(panel, StateType.MESSAGE_ACTIVITY_EVENT);
        this.timerActivityEventState = new ActivityEventState(panel, StateType.TIMER_ACTIVITY_EVENT);
        this.conditionalActivityEventState = new ActivityEventState(panel, StateType.CONDITIONAL_ACTIVITY_EVENT);
        this.signalActivityEventState = new ActivityEventState(panel, StateType.SIGNAL_ACTIVITY_EVENT);
        this.errorActivityEventState = new ActivityEventState(panel, StateType.ERROR_ACTIVITY_EVENT);

        this.defaultState = this.selectState;
        this.currentState = this.defaultState;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void moveToState(final StateType state) {
        // TODO: check if move from current state to next one is possible

        this.currentState.exitingState();

        final State newState = getStateObject(state);
        this.currentState = newState;
        this.currentState.enteringState();
    }

    public BPPanel getPanel() {
        return this.panel;
    }

    private State getStateObject(final StateType stateType) {
        if (stateType == StateType.SELECT)
            return this.selectState;
        else if (stateType == StateType.TASK)
            return this.taskState;
        else if (stateType == StateType.EDGE)
            return this.edgeState;
        else if (stateType == StateType.CONDITIONAL_EDGE)
            return this.conditionalEdgeState;
        else if (stateType == StateType.GATEWAY)
            return this.gatewayState;
        else if (stateType == StateType.LANE)
            return this.laneState;
        else if (stateType == StateType.MOVE)
            return this.moveState;
        else if (stateType == StateType.MOVE_EDGE)
            return this.moveEdgeState;
        else if (stateType == StateType.RESIZE)
            return this.resizeState;
        else if (stateType == StateType.USER_TASK)
            return this.userTaskState;
        else if (stateType == StateType.SYSTEM_TASK)
            return this.systemTaskState;
        else if (stateType == StateType.START_EVENT)
            return this.startEventState;
        else if (stateType == StateType.TIMER_START_EVENT)
            return this.timerStartEventState;
        else if (stateType == StateType.CONDITIONAL_START_EVENT)
            return this.conditionalStartEventState;
        else if (stateType == StateType.MESSAGE_START_EVENT)
            return this.messageStartEventState;
        else if (stateType == StateType.SIGNAL_START_EVENT)
            return this.signalStartEventState;
        else if (stateType == StateType.ERROR_START_EVENT)
            return this.errorStartEventState;
        else if (stateType == StateType.END_EVENT)
            return this.endEventState;
        else if (stateType == StateType.MESSAGE_END_EVENT)
            return this.messageEndEventState;
        else if (stateType == StateType.ERROR_END_EVENT)
            return this.errorEndEventState;
        else if (stateType == StateType.SIGNAL_END_EVENT)
            return this.signalEndEventState;
        else if (stateType == StateType.TIMER_CATCH_EVENT)
            return this.timerCatchEventState;
        else if (stateType == StateType.CONDITIONAL_CATCH_EVENT)
            return this.conditionalCatchEventState;
        else if (stateType == StateType.MESSAGE_CATCH_EVENT)
            return this.messageCatchEventState;
        else if (stateType == StateType.SIGNAL_CATCH_EVENT)
            return this.signalCatchEventState;
        else if (stateType == StateType.LINK_CATCH_EVENT)
            return this.linkCatchEventState;
        else if (stateType == StateType.MESSAGE_THROW_EVENT)
            return this.messageThrowEventState;
        else if (stateType == StateType.SIGNAL_THROW_EVENT)
            return this.signalThrowEventState;
        else if (stateType == StateType.LINK_THROW_EVENT)
            return this.linkThrowEventState;
        else if (stateType == StateType.MESSAGE_ACTIVITY_EVENT)
            return this.messageActivityEventState;
        else if (stateType == StateType.TIMER_ACTIVITY_EVENT)
            return this.timerActivityEventState;
        else if (stateType == StateType.CONDITIONAL_ACTIVITY_EVENT)
            return this.conditionalActivityEventState;
        else if (stateType == StateType.SIGNAL_ACTIVITY_EVENT)
            return this.signalActivityEventState;
        else if (stateType == StateType.ERROR_ACTIVITY_EVENT)
            return this.errorActivityEventState;

        return this.defaultState;
    }
}
