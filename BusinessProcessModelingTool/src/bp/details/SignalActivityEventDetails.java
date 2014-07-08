package bp.details;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.BPFocusListener;
import bp.model.data.SignalActivityEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class SignalActivityEventDetails extends ActivityEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 6817615890997648745L;

    public static final String SIGNAL_NAME_LABEL = "Signal name:";
    public static final String DATA_FORMAT_LABEL = "Data format:";
    public static final String STOP_ACTIVITY_LABEL = "Stop activity:";

    private final SignalActivityEvent event = (SignalActivityEvent) getElement();

    private JLabel signalNameLb;
    private JLabel dataFormatLb;
    private JLabel stopActivityLb;

    private JTextField signalNameTf;
    private JTextArea dataFormatTa;
    private JScrollPane dataFormatScroll;
    private JCheckBox stopActivityCb;

    public SignalActivityEventDetails(final SignalActivityEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.signalNameLb = new JLabel(SIGNAL_NAME_LABEL);
        this.dataFormatLb = new JLabel(DATA_FORMAT_LABEL);
        this.stopActivityLb = new JLabel(STOP_ACTIVITY_LABEL);

        this.signalNameTf = new JTextField(20);
        this.dataFormatTa = new JTextArea(5, 20);
        this.dataFormatScroll = new JScrollPane(this.dataFormatTa);
        this.stopActivityCb = new JCheckBox();
        this.stopActivityCb.setSelected(false);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.signalNameLb);
        getAdvanced().add(this.signalNameTf);
        getAdvanced().add(this.dataFormatLb);
        getAdvanced().add(this.dataFormatScroll);
        getAdvanced().add(this.stopActivityLb);
        getAdvanced().add(this.stopActivityCb);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.signalNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalActivityEventDetails.this.event.updateSignalName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalActivityEventDetails.this.signalNameTf.getText();
            }
        });

        this.dataFormatTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                SignalActivityEventDetails.this.event.updateDataFormat((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SignalActivityEventDetails.this.dataFormatTa.getText();
            }
        });

        this.stopActivityCb.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                SignalActivityEventDetails.this.event.updateStopActivity(
                        SignalActivityEventDetails.this.stopActivityCb.isSelected(), Controller.DETAILS);
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
            } else if (keyWord == BPKeyWords.STOP_ACTIVITY) {
                this.stopActivityCb.setSelected((Boolean) value);
            }
        }
    }
}
