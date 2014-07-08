package bp.details;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.ConditionalActivityEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ConditionalActivityEventDetails extends ActivityEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -1132192828474462860L;

    public static final String CONDITION_LABEL = "Condition:";
    public static final String STOP_ACTIVITY_LABEL = "Stop activity:";

    private final ConditionalActivityEvent event = (ConditionalActivityEvent) getElement();

    private JLabel conditionLb;
    private JLabel stopActivityLb;

    private JTextArea conditionTa;
    private JScrollPane conditionScroll;
    private JCheckBox stopActivityCb;

    public ConditionalActivityEventDetails(final ConditionalActivityEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.conditionLb = new JLabel(CONDITION_LABEL);
        this.stopActivityLb = new JLabel(STOP_ACTIVITY_LABEL);

        this.conditionTa = new JTextArea(5, 20);
        this.conditionScroll = new JScrollPane(this.conditionTa);
        this.stopActivityCb = new JCheckBox();
        this.stopActivityCb.setSelected(false);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.conditionLb);
        getAdvanced().add(this.conditionScroll);
        getAdvanced().add(this.stopActivityLb);
        getAdvanced().add(this.stopActivityCb);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.conditionTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ConditionalActivityEventDetails.this.event.updateCondition((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ConditionalActivityEventDetails.this.conditionTa.getText();
            }
        });

        this.stopActivityCb.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                ConditionalActivityEventDetails.this.event.updateStopActivity(
                        ConditionalActivityEventDetails.this.stopActivityCb.isSelected(), Controller.DETAILS);
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.CONDITION) {
                this.conditionTa.setText((String) value);
            } else if (keyWord == BPKeyWords.STOP_ACTIVITY) {
                this.stopActivityCb.setSelected((Boolean) value);
            }
        }
    }

}
