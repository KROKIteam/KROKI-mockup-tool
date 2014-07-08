package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.MessageStartEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class MessageStartEventDetails extends StartEventDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -7009102447049998279L;

    public static final String DATA_FORMAT = "Data format:";

    private final MessageStartEvent event = (MessageStartEvent) getElement();

    private JLabel dataFormatLb;
    private JTextArea dataFormatTa;
    private JScrollPane dataFormatScroll;

    public MessageStartEventDetails(final MessageStartEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.dataFormatLb = new JLabel(DATA_FORMAT);
        this.dataFormatTa = new JTextArea(5, 20);
        this.dataFormatScroll = new JScrollPane(this.dataFormatTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.dataFormatLb);
        getAdvanced().add(this.dataFormatScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.dataFormatTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                MessageStartEventDetails.this.event.updateDataFormat((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return MessageStartEventDetails.this.dataFormatTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.DATA_FORMAT) {
                this.dataFormatTa.setText((String) value);
            }
        }
    }

}
