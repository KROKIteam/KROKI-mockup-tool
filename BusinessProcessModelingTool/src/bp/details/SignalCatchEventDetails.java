package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.BPFocusListener;
import bp.model.data.SignalCatchEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class SignalCatchEventDetails extends IntermediateEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -671378795026308653L;

    public static final String SIGNAL_NAME = "Signal name:";
    public static final String DATA_FORMAT = "Data format:";

    private final SignalCatchEvent event = (SignalCatchEvent) getElement();

    private JLabel signalNameLb;
    private JLabel dataFormatLb;

    private JTextField signalNameTf;
    private JTextArea dataFormatTa;
    private JScrollPane dataFormatScroll;

    public SignalCatchEventDetails(final SignalCatchEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.signalNameLb = new JLabel(SIGNAL_NAME);
        this.dataFormatLb = new JLabel(DATA_FORMAT);

        this.signalNameTf = new JTextField(20);
        this.dataFormatTa = new JTextArea(5, 20);
        this.dataFormatScroll = new JScrollPane(this.dataFormatTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.signalNameLb);
        getAdvanced().add(this.signalNameTf);
        getAdvanced().add(this.dataFormatLb);
        getAdvanced().add(this.dataFormatScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.signalNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalCatchEventDetails.this.event.updateSignalName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalCatchEventDetails.this.signalNameTf.getText();
            }
        });

        this.dataFormatTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalCatchEventDetails.this.event.updateDataFormat((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalCatchEventDetails.this.dataFormatTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.SIGNAL_NAME) {
                this.signalNameTf.setText((String) value);
            } else if (keyWord == BPKeyWords.DATA_FORMAT) {
                this.dataFormatTa.setText((String) value);
            }
        }
    }

}
