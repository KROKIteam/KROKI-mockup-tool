package bp.details;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.MessageActivityEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class MessageActivityEventDetails extends ActivityEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 1719722878457909960L;

    public static final String DATA_FORMAT_LABEL = "Data format:";
    public static final String STOP_ACTIVITY_LABEL = "Stop activity:";

    private final MessageActivityEvent event = (MessageActivityEvent) getElement();

    private JLabel dataFormatLb;
    private JLabel stopActivityLb;

    private JTextArea dataFormatTa;
    private JScrollPane dataFormatScroll;
    private JCheckBox stopActivityCb;

    public MessageActivityEventDetails(final MessageActivityEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.dataFormatLb = new JLabel(DATA_FORMAT_LABEL);
        this.stopActivityLb = new JLabel(STOP_ACTIVITY_LABEL);

        this.dataFormatTa = new JTextArea(5, 20);
        this.dataFormatScroll = new JScrollPane(this.dataFormatTa);
        this.stopActivityCb = new JCheckBox();
        this.stopActivityCb.setSelected(false);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.dataFormatLb);
        getAdvanced().add(this.dataFormatScroll);
        getAdvanced().add(this.stopActivityLb);
        getAdvanced().add(this.stopActivityCb);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.dataFormatTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                MessageActivityEventDetails.this.event.updateDataFormat((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return MessageActivityEventDetails.this.dataFormatTa.getText();
            }
        });

        this.stopActivityCb.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                MessageActivityEventDetails.this.event.updateStopActivity(
                        MessageActivityEventDetails.this.stopActivityCb.isSelected(), Controller.DETAILS);
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.DATA_FORMAT) {
                this.dataFormatTa.setText((String) value);
            } else if (keyWord == BPKeyWords.STOP_ACTIVITY) {
                this.stopActivityCb.setSelected((Boolean) value);
            }
        }
    }

}
