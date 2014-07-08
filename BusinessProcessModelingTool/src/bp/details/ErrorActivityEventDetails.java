package bp.details;

import javax.swing.JLabel;
import javax.swing.JTextField;

import bp.event.BPFocusListener;
import bp.model.data.ErrorActivityEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ErrorActivityEventDetails extends ActivityEventDetails{

    /**
     * 
     */
    private static final long serialVersionUID = 1096834131210529218L;

    public static final String ERROR_NAME_LABEL = "Error name:";

    private final ErrorActivityEvent event = (ErrorActivityEvent) getElement();

    private JLabel errorNameLb;
    private JTextField errorNameTf;

    public ErrorActivityEventDetails(final ErrorActivityEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.errorNameLb = new JLabel(ERROR_NAME_LABEL);
        this.errorNameTf = new JTextField(20);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.errorNameLb);
        getAdvanced().add(this.errorNameTf);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.errorNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                ErrorActivityEventDetails.this.event.updateErrorName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ErrorActivityEventDetails.this.errorNameTf.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.ERROR_NAME) {
                this.errorNameTf.setText((String) value);
            }
        }
    }

}
