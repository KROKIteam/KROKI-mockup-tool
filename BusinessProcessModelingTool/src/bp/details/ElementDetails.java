package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.AttributeChangeListener;
import bp.event.BPFocusListener;
import bp.model.data.Element;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ElementDetails extends AbstractDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -7999313490510402309L;

    public static final String UNIQUE_NAME_LABEL = "Unique name:";
    public static final String NAME_LABEL = "Name:";
    public static final String DESCRIPTION_LABEL = "Description:";

    private final Element element;

    private JLabel uniqueNameLb;
    private JLabel nameLb;
    private JLabel descriptionLb;
    private JTextField uniqueNameTf;
    private JTextField nameTf;
    private JTextArea descriptionTa;
    private JScrollPane descriptionScroll;

    public ElementDetails(final Element element) {
        this.element = element;

        initComponents();
        layoutComponents();
        addActions();
        addDataListener();
    }

    protected void initComponents() {
        this.uniqueNameLb = new JLabel(UNIQUE_NAME_LABEL);
        this.nameLb = new JLabel(NAME_LABEL);
        this.descriptionLb = new JLabel(DESCRIPTION_LABEL);

        this.uniqueNameTf = new JTextField(20);
        this.nameTf = new JTextField(20);

        this.descriptionTa = new JTextArea(5, 20);
        this.descriptionScroll = new JScrollPane(this.descriptionTa);
    }

    protected void layoutComponents() {
        createBasic();

        getBasic().add(this.uniqueNameLb);
        getBasic().add(this.uniqueNameTf);
        getBasic().add(this.nameLb);
        getBasic().add(this.nameTf);
        getBasic().add(this.descriptionLb);
        getBasic().add(this.descriptionScroll);
    }

    protected void addActions() {
        this.uniqueNameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getElement().updateUniqueName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ElementDetails.this.uniqueNameTf.getText();
            }
        });

        this.nameTf.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getElement().updateName((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ElementDetails.this.nameTf.getText();
            }
        });

        this.descriptionTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                getElement().updateDescription((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return ElementDetails.this.descriptionTa.getText();
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
            }
        }
    }

    private void addDataListener() {
        getElement().addAttributeChangeListener(new AttributeChangeListener() {

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

    protected Element getElement() {
        return this.element;
    }

}
