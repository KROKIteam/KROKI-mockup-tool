package bp.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import bp.action.ActionManager;

public class SideToolbar extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -1021087398755442257L;

    public static final Integer START_EVENT_STATE = 0;
    public static final Integer END_EVENT_STATE = 1;
    public static final Integer INTERMEDIATE_EVENT_STATE = 2;
    public static final Integer ACTIVITY_EVENT_STATE = 3;
    public static final Integer MIN_STATE_VALUE = 0;
    public static final Integer MAX_STATE_VALUE = 3;

    private final String[] bpStrings = {"Start", "End", "Intermediate", "Activity"};
    private JComboBox<String> bpOptions;

    private Integer currentEventState;

    /** common view **/
    private JButton select;

    /** activity view **/
    private JButton task;
    private JButton userTask;
    private JButton systemTask;
    private JButton lane;

    /** flow control view **/
    private JButton edge;
    private JButton conditionalEdge;
    private JButton gateway;

    /** start **/
    private JButton start;
    private JButton timerStart;
    private JButton conditionalStart;
    private JButton messageStart;
    private JButton signalStart;
    private JButton errorStart;

    /** end **/
    private JButton end;
    private JButton messageEnd;
    private JButton errorEnd;
    private JButton signalEnd;

    /** intermediate **/
    private JButton cTimer;
    private JButton cCondition;
    private JButton cMessage;
    private JButton cSignal;
    private JButton cLink;
    private JButton tMessage;
    private JButton tSignal;
    private JButton tLink;

    /** activity event **/
    private JButton aMessage;
    private JButton aTimer;
    private JButton aCondition;
    private JButton aSignal;
    private JButton aError;

    /** labels **/
    private JLabel commonLb;
    private JLabel activityLb;
    private JLabel controlFlowLb;
    private JLabel eventsLb;
    private JLabel catchLb;
    private JLabel throwLb;

    public SideToolbar() {
        setBorder(new EmptyBorder(10, 10, 10, 10)); // (N, W, S, E)
        final LayoutManager layout = new MigLayout("wrap 1", "[fill]", "[]");
        this.setLayout(layout);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        this.select = new JButton(ActionManager.getSelect());

        this.task = new JButton(ActionManager.getAddTask());
        this.userTask = new JButton(ActionManager.getAddUserTask());
        this.systemTask = new JButton(ActionManager.getAddSystemTask());
        this.lane = new JButton(ActionManager.getAddLane());

        this.edge = new JButton(ActionManager.getAddEdge());
        this.conditionalEdge = new JButton(ActionManager.getAddConditionalEdge());
        this.gateway = new JButton(ActionManager.getAddGateway());

        this.start = new JButton(ActionManager.getAddStartEvent());
        this.timerStart = new JButton(ActionManager.getAddTimerStartEvent());
        this.conditionalStart = new JButton(ActionManager.getAddConditionalStartEvent());
        this.messageStart = new JButton(ActionManager.getAddMessageStartEvent());
        this.signalStart = new JButton(ActionManager.getAddSignalStartEvent());
        this.errorStart = new JButton(ActionManager.getAddErrorStartEvent());

        this.end = new JButton(ActionManager.getAddEndEvent());
        this.messageEnd = new JButton(ActionManager.getAddMessageEndEvent());
        this.errorEnd = new JButton(ActionManager.getAddErrorEndEvent());
        this.signalEnd = new JButton(ActionManager.getAddSignalEndEvent());

        this.cTimer = new JButton(ActionManager.getAddCatchTimerEvent());
        this.cCondition = new JButton(ActionManager.getAddCatchConditionEvent());
        this.cMessage = new JButton(ActionManager.getAddCatchMessageEvent());
        this.cSignal = new JButton(ActionManager.getAddCatchSignalEvent());
        this.cLink = new JButton(ActionManager.getAddCatchLinkEvent());
        this.tMessage = new JButton(ActionManager.getAddThrowMessageEvent());
        this.tSignal = new JButton(ActionManager.getAddThrowSignalEvent());
        this.tLink = new JButton(ActionManager.getAddThrowLinkEvent());

        this.aMessage = new JButton(ActionManager.getAddActivityMessageEvent());
        this.aTimer = new JButton(ActionManager.getAddActivityTimerEvent());
        this.aCondition = new JButton(ActionManager.getAddActivityConditionEvent());
        this.aSignal = new JButton(ActionManager.getAddActivitySignalEvent());
        this.aError = new JButton(ActionManager.getAddActivityErrorEvent());

        this.commonLb = new JLabel("Common:");
        this.activityLb = new JLabel("Activity:");
        this.controlFlowLb = new JLabel("Flow Control:");
        this.eventsLb = new JLabel("Events:");
        this.catchLb = new JLabel("Catch Event:");
        this.throwLb = new JLabel("Throw Event:");

        this.bpOptions = new JComboBox<>(this.bpStrings);
        this.bpOptions.setSelectedIndex(0);
        this.bpOptions.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                updateEventButtons(SideToolbar.this.bpOptions.getSelectedIndex());
            }
        });
        this.currentEventState = 0;
    }

    private void layoutComponents() {
        add(this.commonLb);
        add(this.select);

        add(this.activityLb);
        add(this.task);
        add(this.userTask);
        add(this.systemTask);
        add(this.lane);

        add(this.controlFlowLb);
        add(this.edge);
        add(this.conditionalEdge);
        add(this.gateway);

        add(this.eventsLb);
        add(this.bpOptions);

        addStartEventButtons();
    }

    private void addStartEventButtons() {
        add(this.start);
        add(this.timerStart);
        add(this.conditionalStart);
        add(this.messageStart);
        add(this.signalStart);
        add(this.errorStart);
    }

    private void removeStartEventButtons() {
        remove(this.start);
        remove(this.timerStart);
        remove(this.conditionalStart);
        remove(this.messageStart);
        remove(this.signalStart);
        remove(this.errorStart);
    }

    private void addEndEventButtons() {
        add(this.end);
        add(this.messageEnd);
        add(this.errorEnd);
        add(this.signalEnd);
    }

    private void removeEndEventButtons() {
        remove(this.end);
        remove(this.messageEnd);
        remove(this.errorEnd);
        remove(this.signalEnd);
    }

    private void addIntermediateEventButtons() {
        add(this.catchLb);
        add(this.cTimer);
        add(this.cCondition);
        add(this.cMessage);
        add(this.cSignal);
        add(this.cLink);

        add(this.throwLb);
        add(this.tMessage);
        add(this.tSignal);
        add(this.tLink);
    }

    private void removeIntermediateEventButtons() {
        remove(this.catchLb);
        remove(this.cTimer);
        remove(this.cCondition);
        remove(this.cMessage);
        remove(this.cSignal);
        remove(this.cLink);

        remove(this.throwLb);
        remove(this.tMessage);
        remove(this.tSignal);
        remove(this.tLink);
    }

    private void addActivityEventButtons() {
        add(this.aMessage);
        add(this.aTimer);
        add(this.aCondition);
        add(this.aSignal);
        add(this.aError);
    }

    private void removeActivityEventButtons() {
        remove(this.aMessage);
        remove(this.aTimer);
        remove(this.aCondition);
        remove(this.aSignal);
        remove(this.aError);
    }

    private void updateEventButtons(final Integer newState) {
        if (this.currentEventState.equals(newState) || newState < MIN_STATE_VALUE || newState > MAX_STATE_VALUE)
            return;

        if (this.currentEventState == START_EVENT_STATE) {
            removeStartEventButtons();
        } else if (this.currentEventState == END_EVENT_STATE) {
            removeEndEventButtons();
        } else if (this.currentEventState == INTERMEDIATE_EVENT_STATE) {
            removeIntermediateEventButtons();
        } else if (this.currentEventState == ACTIVITY_EVENT_STATE) {
            removeActivityEventButtons();
        }

        if (newState == START_EVENT_STATE) {
            addStartEventButtons();
        } else if (newState == END_EVENT_STATE) {
            addEndEventButtons();
        } else if (newState == INTERMEDIATE_EVENT_STATE) {
            addIntermediateEventButtons();
        } else if (newState == ACTIVITY_EVENT_STATE) {
            addActivityEventButtons();
        }

        this.currentEventState = newState;

        updateUI();
    }

}
