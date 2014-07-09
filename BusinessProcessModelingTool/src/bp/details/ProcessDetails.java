package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.AttributeChangeListener;
import bp.event.BPFocusListener;
import bp.model.data.Process;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ProcessDetails extends AbstractDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -8731164849167955775L;

    public static final String UNIQUE_NAME_LABEL = "Unique name:";
    public static final String NAME_LABEL = "Name:";
    public static final String DESCRIPTION_LABEL = "Description:";
    public static final String DATA_LABEL = "Data:";

    //private final Process process;
    private transient final Process process;

    private JLabel uniqueNameLb;
    private JLabel nameLb;
    private JLabel descriptionLb;
    private JLabel dataLb;
    private JTextField uniqueNameTf;
    private JTextField nameTf;
    private JTextArea descriptionTa;
    private JTextArea dataTa;
    private JScrollPane descriptionScroll;
    private JScrollPane dataScroll;

    public ProcessDetails(Process process) {
        this.process = process;

        initComponents();
        layoutComponents();
        addActions();
        addDataListener();
    }

    private void initComponents() {
        this.uniqueNameLb = new JLabel(UNIQUE_NAME_LABEL);
        this.nameLb = new JLabel(NAME_LABEL);
        this.descriptionLb = new JLabel(DESCRIPTION_LABEL);
        this.dataLb = new JLabel(DATA_LABEL);

        this.uniqueNameTf = new JTextField(20);
        this.nameTf = new JTextField(20);

        this.descriptionTa = new JTextArea(5, 20);
        this.descriptionScroll = new JScrollPane(this.descriptionTa);
        this.dataTa = new JTextArea(5, 20);
        this.dataScroll = new JScrollPane(this.dataTa);
        
        // Set the texts if available
        if (process.getUniqueName() != null)
        	uniqueNameTf.setText(process.getUniqueName());
        if (process.getName() != null)
        	nameTf.setText(process.getName());
        if (process.getDescription() != null) 
        	descriptionTa.setText(process.getDescription());
        if (process.getData() != null)
        	dataTa.setText(process.getData());
    }

    private void layoutComponents() {
        createBasic();

        getBasic().add(this.uniqueNameLb);
        getBasic().add(this.uniqueNameTf);
        getBasic().add(this.nameLb);
        getBasic().add(this.nameTf);
        getBasic().add(this.descriptionLb);
        getBasic().add(this.descriptionScroll);

        createAdvanced();

        getAdvanced().add(this.dataLb);
        getAdvanced().add(this.dataScroll);
    }

    private void addActions() {
        this.uniqueNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getProcess().updateUniqueName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ProcessDetails.this.uniqueNameTf.getText();
            }
        });

        this.nameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getProcess().updateName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ProcessDetails.this.nameTf.getText();
            }
        });

        this.descriptionTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getProcess().updateDescription((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ProcessDetails.this.descriptionTa.getText();
            }
        });

        this.dataTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getProcess().updateData((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ProcessDetails.this.dataTa.getText();
            }
        });
    }

    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        if (value != null) {
            if (keyWord == BPKeyWords.UNIQUE_NAME) {
                this.uniqueNameTf.setText((String) value);
            } else if (keyWord == BPKeyWords.NAME) {
                this.nameTf.setText((String) value);
            } else if (keyWord == BPKeyWords.DESCRIPTION) {
                this.descriptionTa.setText((String) value);
            } else if (keyWord == BPKeyWords.DATA) {
                this.dataTa.setText((String) value);
            }
        }
    }

    private void addDataListener() {
        getProcess().addAttributeChangeListener(new AttributeChangeListener() {

            @Override
            public Controller getController() {
                return Controller.DETAILS;
            }

            @Override
            public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
                dataAttributeChanged(keyWord, value);
            }
        });
    }

    protected Process getProcess() {
        return this.process;
    }

}
