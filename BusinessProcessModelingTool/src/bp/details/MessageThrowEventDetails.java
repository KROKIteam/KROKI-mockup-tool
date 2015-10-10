package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.MessageThrowEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class MessageThrowEventDetails extends IntermediateEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 7972577841282926873L;

    public static final String MESSAGE_LABEL = "Message label:";

    private MessageThrowEvent event = (MessageThrowEvent) getElement();

    private JLabel messageLb;
    private JTextArea messageTa;
    private JScrollPane messageScroll;

    public MessageThrowEventDetails(MessageThrowEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.messageLb = new JLabel(MESSAGE_LABEL);
        this.messageTa = new JTextArea(5, 20);
        this.messageScroll = new JScrollPane(this.messageTa);
        
        // Set the texts if available
        event = (MessageThrowEvent) getElement();
        if (event.getMessage() != null)
        	messageTa.setText(event.getMessage());
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.messageLb);
        getAdvanced().add(this.messageScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.messageTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                MessageThrowEventDetails.this.event.updateMessage((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return MessageThrowEventDetails.this.messageTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.MESSAGE) {
                this.messageTa.setText((String) value);
            }
        }
    }

}
