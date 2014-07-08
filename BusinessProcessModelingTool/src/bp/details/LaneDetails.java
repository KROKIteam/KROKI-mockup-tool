package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bp.event.BPFocusListener;
import bp.model.data.Lane;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class LaneDetails extends ElementDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -5480446616251138679L;

    public static final String PARENT_LABEL = "Parent:";
    public static final String ACTOR_LABEL = "Actor:";

    private final Lane lane = (Lane) getElement();

    private JLabel parentLb;
    private JLabel actorLb;
    private JTextField parentTf;
    private JTextArea actorTa;
    private JScrollPane actorScroll;

    public LaneDetails(final Lane lane) {
        super(lane);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.parentLb = new JLabel(PARENT_LABEL);
        this.actorLb = new JLabel(ACTOR_LABEL);

        this.parentTf = new JTextField(20);
        this.actorTa = new JTextArea(5, 20);
        this.actorScroll = new JScrollPane(this.actorTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createBasic();

        getBasic().add(this.actorLb);
        getBasic().add(this.actorScroll);
        getBasic().add(this.parentLb);
        getBasic().add(this.parentTf);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.actorTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                LaneDetails.this.lane.updateActor((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return LaneDetails.this.actorTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.PARENT) {
                // TODO
            } else if (keyWord == BPKeyWords.ACTOR) {
                this.actorTa.setText((String) value);
            }
        }
    }
}
