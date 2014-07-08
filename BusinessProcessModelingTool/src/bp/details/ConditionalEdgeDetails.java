package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.ConditionalEdge;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ConditionalEdgeDetails extends EdgeDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -4714739722708880413L;

    public static final String CONDITION_LABEL = "Condition:";

    private final ConditionalEdge edge = (ConditionalEdge) getElement();

    private JLabel conditionLb;
    private JTextArea conditionTa;
    private JScrollPane conditionScroll;

    public ConditionalEdgeDetails(final ConditionalEdge edge) {
        super(edge);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.conditionLb = new JLabel(CONDITION_LABEL);
        this.conditionTa = new JTextArea(5, 20);
        this.conditionScroll = new JScrollPane(this.conditionTa);
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
                ConditionalEdgeDetails.this.edge.updateCondition((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ConditionalEdgeDetails.this.conditionTa.getText();
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
