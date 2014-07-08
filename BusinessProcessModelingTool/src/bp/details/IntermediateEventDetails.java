package bp.details;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bp.model.data.IntermediateEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class IntermediateEventDetails extends ElementDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 6013637898199048759L;

    public static final String MIN_INPUT_LABEL = "Minimal input count:";

    private final IntermediateEvent event = (IntermediateEvent) getElement();

    private JLabel minInputLb;
    private JSpinner minInputSp;

    public IntermediateEventDetails(final IntermediateEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.minInputLb = new JLabel(MIN_INPUT_LABEL);
        final SpinnerModel sm = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        this.minInputSp = new JSpinner(sm);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.minInputLb);
        getAdvanced().add(this.minInputSp);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.minInputSp.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent arg0) {
                IntermediateEventDetails.this.event.updateMinInput(
                        (Integer) IntermediateEventDetails.this.minInputSp.getValue(), Controller.DETAILS);
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.MIN_INPUT) {
                this.minInputSp.setValue(value);
            }
        }
    }

}
