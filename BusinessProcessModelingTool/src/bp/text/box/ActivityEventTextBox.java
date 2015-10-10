package bp.text.box;

import bp.model.data.ActivityEvent;
import bp.model.util.BPKeyWords;

public class ActivityEventTextBox extends ElementTextBox {

    public ActivityEventTextBox(final ActivityEvent aEvent, final BPKeyWords keyWord, final TextBox owner) {
        super(aEvent, keyWord, owner);
    }

    public ActivityEventTextBox() { }
    
    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (getKey() == BPKeyWords.MESSAGE_ACTIVITY_EVENT) {
                if (keyWord == BPKeyWords.DATA_FORMAT || keyWord == BPKeyWords.STOP_ACTIVITY) {
                    updateAttribute(keyWord, value);
                }
            } else if (getKey() == BPKeyWords.CONDITIONAL_ACTIVITY_EVENT) {
                if (keyWord == BPKeyWords.CONDITION || keyWord == BPKeyWords.STOP_ACTIVITY) {
                    updateAttribute(keyWord, value);
                }
            } else if (getKey() == BPKeyWords.TIMER_ACTIVITY_EVENT) {
                if (keyWord == BPKeyWords.TIME_FORMAT || keyWord == BPKeyWords.STOP_ACTIVITY) {
                    updateAttribute(keyWord, value);
                }
            } else if (getKey() == BPKeyWords.SIGNAL_ACTIVITY_EVENT) {
                if (keyWord == BPKeyWords.DATA_FORMAT || keyWord == BPKeyWords.SIGNAL_NAME
                        || keyWord == BPKeyWords.STOP_ACTIVITY) {
                    updateAttribute(keyWord, value);
                }
            } else if (getKey() == BPKeyWords.ERROR_ACTIVITY_EVENT) {
                if (keyWord == BPKeyWords.ERROR_NAME) {
                    updateAttribute(keyWord, value);
                }
            }
        }
    }

    public ActivityEvent getActivityEvent() {
        return (ActivityEvent) getElement();
    }

}
