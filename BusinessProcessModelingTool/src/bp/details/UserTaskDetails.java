package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.UserTask;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class UserTaskDetails extends TaskDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -5037434410805225376L;

    public static final String IMPLEMENTATION_LABEL = "Implementation:";

    private final UserTask userTask = (UserTask) getElement();

    private JLabel implementationLb;
    private JTextArea implementationTa;
    private JScrollPane implementationScroll;

    public UserTaskDetails(final UserTask element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.implementationLb = new JLabel(IMPLEMENTATION_LABEL);
        this.implementationTa = new JTextArea(5, 20);
        this.implementationScroll = new JScrollPane(this.implementationTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.implementationLb);
        getAdvanced().add(this.implementationScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.implementationTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                UserTaskDetails.this.userTask.updateImplementation((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return UserTaskDetails.this.implementationTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.IMPLEMENTATION) {
                this.implementationTa.setText((String) value);
            }
        }
    }

}
