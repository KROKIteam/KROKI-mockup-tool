package bp.details;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import bp.app.AppCore;
import bp.model.data.LinkCatchEvent;
import bp.model.data.LinkThrowEvent;
import bp.model.data.Process;
import bp.model.util.BPKeyWords;

public class LinkThrowEventDetails extends ElementDetails{

    /**
     * 
     */
    private static final long serialVersionUID = -6302256921897072748L;

    public static final String CATCH_LINK_LABEL = "Catch link:";

    private final LinkThrowEvent event = (LinkThrowEvent) getElement();

    private JLabel catchLinkLb;
    private JComboBox<String> catchLinkCb;

    public LinkThrowEventDetails(final LinkThrowEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.catchLinkLb = new JLabel(CATCH_LINK_LABEL);
        this.catchLinkCb = new JComboBox<>();
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createBasic();

        getBasic().add(this.catchLinkLb);
        getBasic().add(this.catchLinkCb);
    }

    @Override
    protected void addActions() {
        super.addActions();

        final Process process = AppCore.getInstance().getBpPanel().getProcess();
        // TODO
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.LINK) {
                final LinkCatchEvent lce = (LinkCatchEvent) value;
                final Process process = AppCore.getInstance().getBpPanel().getProcess();
                if (lce.getUniqueName() != null) {
                    // TODO
                }
            }
        }
    }

}
