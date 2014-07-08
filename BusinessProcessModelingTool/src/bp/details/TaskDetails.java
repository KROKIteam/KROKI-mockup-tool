package bp.details;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bp.event.BPFocusListener;
import bp.model.data.ExecutionType;
import bp.model.data.Task;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class TaskDetails extends ActivityDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -8344599583920127541L;

    public static final String ACTOR_LABEL = "Actor:";
    public static final String LANE_ACTOR_LABEL = "Actor[Lane]:";
    public static final String AUTO_ASSIGN = "Auto assign:";
    public static final String MULTIPLE_EXECUTION = "Multiple Execution";
    public static final String MULTIPLE_EXECUTION_TYPE = "Multiple Execution Type";

    private final Task task = (Task) getElement();

    private JLabel actorLb;
    private JLabel laneActorLb;
    private JLabel autoAssignLb;
    private JLabel multipleExecutionLb;
    private JLabel multipleExecutionTypeLb;
    private JTextArea actorTa;
    private JScrollPane actorScroll;
    private JTextField laneActorTf;
    private JCheckBox autoAssignCb;
    private JSpinner multipleExecutionSp;
    private JComboBox<String> multipleExecutionTypeCb;

    public TaskDetails(final Task element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.actorLb = new JLabel(ACTOR_LABEL);
        this.laneActorLb = new JLabel(LANE_ACTOR_LABEL);
        this.autoAssignLb = new JLabel(AUTO_ASSIGN);
        this.multipleExecutionLb = new JLabel(MULTIPLE_EXECUTION);
        this.multipleExecutionTypeLb = new JLabel(MULTIPLE_EXECUTION_TYPE);

        this.actorTa = new JTextArea(5, 20);
        this.actorScroll = new JScrollPane(this.actorTa);

        this.laneActorTf = new JTextField(20);
        this.laneActorTf.setEnabled(false);

        this.autoAssignCb = new JCheckBox();

        final SpinnerModel sm = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);
        this.multipleExecutionSp = new JSpinner(sm);

        final String[] elements = new String[ExecutionType.values().length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = ExecutionType.values()[i].getName();
        }
        this.multipleExecutionTypeCb = new JComboBox<>(elements);
        this.multipleExecutionTypeCb.setSelectedIndex(0);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createBasic();

        getBasic().add(this.actorLb);
        getBasic().add(this.actorScroll);
        getBasic().add(this.laneActorLb);
        getBasic().add(this.laneActorTf);

        createAdvanced();

        getAdvanced().add(this.autoAssignLb);
        getAdvanced().add(this.autoAssignCb);
        getAdvanced().add(this.multipleExecutionLb);
        getAdvanced().add(this.multipleExecutionSp);
        getAdvanced().add(this.multipleExecutionTypeLb);
        getAdvanced().add(this.multipleExecutionTypeCb);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.actorTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                TaskDetails.this.task.updateActor((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return TaskDetails.this.actorTa.getText();
            }
        });

        this.autoAssignCb.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                TaskDetails.this.task.updateAutoAssign(TaskDetails.this.autoAssignCb.isSelected(), Controller.DETAILS);
            }
        });

        this.multipleExecutionSp.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                TaskDetails.this.task.updateMultipleExecution((Integer) TaskDetails.this.multipleExecutionSp.getValue(), Controller.DETAILS);
            }
        });

        this.multipleExecutionTypeCb.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                TaskDetails.this.task.updateMultipleExecutionType(ExecutionType.getEnumValue((String) TaskDetails.this.multipleExecutionTypeCb.getSelectedItem()), Controller.DETAILS);
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.ACTOR) {
                this.actorTa.setText((String) value);
            } else if (keyWord == BPKeyWords.LANE_ACTOR) {
                // TODO
            } else if (keyWord == BPKeyWords.AUTO_ASSIGN) {
                this.autoAssignCb.setSelected((Boolean) value);
            } else if (keyWord == BPKeyWords.MULTIPLE_EXECUTION) {
                this.multipleExecutionSp.setValue(value);
            } else if (keyWord == BPKeyWords.MULTIPLE_EXECUTION_TYPE) {
                this.multipleExecutionTypeCb.setSelectedItem(((ExecutionType) value).getName());
            }
        }
    }

}
