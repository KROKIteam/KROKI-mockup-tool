package bp.event;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class BPFocusListener implements FocusListener {

    private Object oldValue;

    @Override
    public void focusGained(final FocusEvent e) {
        this.oldValue = getValue();
    }

    @Override
    public void focusLost(final FocusEvent e) {
        if (this.oldValue != null && !this.oldValue.equals(getValue())) {
            updateValue();
        }
    }

    public abstract void updateValue();

    public abstract Object getValue();

}
