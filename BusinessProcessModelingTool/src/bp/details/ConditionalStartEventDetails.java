package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.ConditionalStartEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ConditionalStartEventDetails extends StartEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -3830283509661106420L;

    public static final String CONDITION = "Condition:";

    private ConditionalStartEvent event = (ConditionalStartEvent) getElement();

    private JLabel conditionLb;
    private JTextArea conditionTa;
    private JScrollPane conditionScroll;

    public ConditionalStartEventDetails(ConditionalStartEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.conditionLb = new JLabel(CONDITION);
        this.conditionTa = new JTextArea(5, 20);
        this.conditionScroll = new JScrollPane(this.conditionTa);
        
        // Set the texts if available
        event = (ConditionalStartEvent) getElement();
        if (event.getComponent() != null) conditionTa.setText(event.getCondition());
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.conditionLb);
        getAdvanced().add(this.conditionScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.conditionTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ConditionalStartEventDetails.this.event.updateCondition((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ConditionalStartEventDetails.this.conditionTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.CONDITION) {
                this.conditionTa.setText((String) value);
            }
        }
    }

}
