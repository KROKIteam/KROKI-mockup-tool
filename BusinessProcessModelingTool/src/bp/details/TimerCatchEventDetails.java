package bp.details;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bp.event.BPFocusListener;
import bp.model.data.TimerCatchEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class TimerCatchEventDetails extends IntermediateEventDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -883507948100426558L;

    public static final String TIME_FORMAT = "Time format:";

    private final TimerCatchEvent event = (TimerCatchEvent) getElement();

    private JLabel timeFormatLb;
    private JTextArea timeFormatTa;
    private JScrollPane timeFormatScroll;

    public TimerCatchEventDetails(final TimerCatchEvent element) {
        super(element);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.timeFormatLb = new JLabel(TIME_FORMAT);
        this.timeFormatTa = new JTextArea(5, 20);
        this.timeFormatScroll = new JScrollPane(this.timeFormatTa);
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createAdvanced();

        getAdvanced().add(this.timeFormatLb);
        getAdvanced().add(this.timeFormatScroll);
    }

    @Override
    protected void addActions() {
        super.addActions();

        this.timeFormatTa.addFocusListener(new BPFocusListener() {

            @Override
            public void updateValue() {
                TimerCatchEventDetails.this.event.updateTimeFormat((String) getValue(), Controller.DETAILS);
            }

            @Override
            public Object getValue() {
                return TimerCatchEventDetails.this.timeFormatTa.getText();
            }
        });
    }

    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.TIME_FORMAT) {
                this.timeFormatTa.setText((String) value);
            }
        }
    }

}
