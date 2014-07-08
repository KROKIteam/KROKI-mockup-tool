package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.SystemTask;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class SystemTaskDetails extends TaskDetails {

    /**
     * 
     */
    private static final long serialVersionUID = 5475763120981110928L;

    public static final String IMPLEMENTATION_LABEL = "Implementation:";

    private final SystemTask systemTask = (SystemTask) getElement();

    private JLabel implementationLb;
    private JTextArea implementationTa;
    private JScrollPane implementationScroll;

    public SystemTaskDetails(final SystemTask element) {
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
                SystemTaskDetails.this.systemTask.updateImplementation((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return SystemTaskDetails.this.implementationTa.getText();
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
