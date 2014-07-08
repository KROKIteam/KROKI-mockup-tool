package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bp.event.BPFocusListener;
import bp.model.data.Activity;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public abstract class ActivityDetails extends ElementDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 8765453750320541702L;

    public static final String DATA_LABEL = "Data:";
    public static final String LOOP_EXPRESSION_LABEL = "Loop expression:";
    public static final String MIN_INPUT_LABEL = "Minimal input:";

    private final Activity activity = (Activity) getElement();

    private JLabel dataLb;
    private JLabel loopExpressionLb;
    private JLabel minInputLb;
    private JTextArea dataTa;
    private JTextArea loopExpressionTa;
    private JScrollPane dataScroll;
    private JScrollPane loopExpressionScroll;
    private JSpinner minInputSp;

    public ActivityDetails(final Activity activity) {
        super(activity);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.dataLb = new JLabel(DATA_LABEL);
        this.loopExpressionLb = new JLabel(LOOP_EXPRESSION_LABEL);
        this.minInputLb = new JLabel(MIN_INPUT_LABEL);

        this.dataTa = new JTextArea(5, 20);
        this.loopExpressionTa = new JTextArea(5, 20);

        this.dataScroll = new JScrollPane(this.dataTa);
        this.loopExpressionScroll = new JScrollPane(this.loopExpressionTa);

        final SpinnerModel sm = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        this.minInputSp = new JSpinner(sm);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.dataLb);
        getAdvanced().add(this.dataScroll);
        getAdvanced().add(this.loopExpressionLb);
        getAdvanced().add(this.loopExpressionScroll);
        getAdvanced().add(this.minInputLb);
        getAdvanced().add(this.minInputSp);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.dataTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ActivityDetails.this.activity.updateData((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ActivityDetails.this.dataTa.getText();
            }
        });

        this.loopExpressionTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ActivityDetails.this.activity.updateLoopExpression((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ActivityDetails.this.loopExpressionTa.getText();
            }
        });

        this.minInputSp.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                ActivityDetails.this.activity.updateMinInput((Integer) ActivityDetails.this.minInputSp.getValue(),
                        Controller.DETAILS);
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.DATA) {
                this.dataTa.setText((String) value);
            } else if (keyWord == BPKeyWords.LOOP_EXPRESSION) {
                this.loopExpressionTa.setText((String) value);
            } else if (keyWord == BPKeyWords.MIN_INPUT) {
                this.minInputSp.setValue(value);
            }
        }
    }

}
