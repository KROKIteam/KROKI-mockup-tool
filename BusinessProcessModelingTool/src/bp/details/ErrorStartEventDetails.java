package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.ErrorStartEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ErrorStartEventDetails extends StartEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -7374097101193825991L;

    public static final String ERROR_NAME = "Error name:";

    private final ErrorStartEvent event = (ErrorStartEvent) getElement();

    private JLabel errorNameLb;
    private JTextArea errorNameTa;
    private JScrollPane errorNameScroll;

    public ErrorStartEventDetails(final ErrorStartEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.errorNameLb = new JLabel(ERROR_NAME);
        this.errorNameTa = new JTextArea(5, 20);
        this.errorNameScroll = new JScrollPane(this.errorNameTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.errorNameLb);
        getAdvanced().add(this.errorNameScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.errorNameTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ErrorStartEventDetails.this.event.updateErrorName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ErrorStartEventDetails.this.errorNameTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.ERROR_NAME) {
                this.errorNameTa.setText((String) value);
            }
        }
    }
}
