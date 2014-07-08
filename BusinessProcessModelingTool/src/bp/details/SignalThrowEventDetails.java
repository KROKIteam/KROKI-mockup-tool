package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.BPFocusListener;
import bp.model.data.SignalThrowEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class SignalThrowEventDetails extends IntermediateEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 4885224931704133360L;

    public static final String SIGNAL_NAME_LABEL = "Signal name:";
    public static final String SIGNAL_DATA_LABEL = "Signal data:";

    private final SignalThrowEvent event = (SignalThrowEvent) getElement();

    private JLabel signalNameLb;
    private JLabel signalDataLb;

    private JTextField signalNameTf;
    private JTextArea signalDataTa;
    private JScrollPane signalDataScroll;

    public SignalThrowEventDetails(final SignalThrowEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.signalNameLb = new JLabel(SIGNAL_NAME_LABEL);
        this.signalDataLb = new JLabel(SIGNAL_DATA_LABEL);

        this.signalNameTf = new JTextField(20);
        this.signalDataTa = new JTextArea(5, 20);
        this.signalDataScroll = new JScrollPane(this.signalDataTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.signalNameLb);
        getAdvanced().add(this.signalNameTf);
        getAdvanced().add(this.signalDataLb);
        getAdvanced().add(this.signalDataScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.signalNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalThrowEventDetails.this.event.updateSignalName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalThrowEventDetails.this.signalNameTf.getText();
            }
        });

        this.signalDataTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalThrowEventDetails.this.event.updateSignalData((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalThrowEventDetails.this.signalDataTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.SIGNAL_NAME) {
                this.signalNameTf.setText((String) value);
            } else if (keyWord == BPKeyWords.SIGNAL_DATA) {
                this.signalDataTa.setText((String) value);
            }
        }
    }

}
