package bp.text.box;

import bp.model.data.MessageActivityEvent;
import bp.model.util.BPKeyWords;

public class MessageActivityEventTextBox extends ElementTextBox {

    public MessageActivityEventTextBox(final MessageActivityEvent maEvent, final BPKeyWords keyWord, final TextBox owner) {
        super(maEvent, keyWord, owner);
    }

    public MessageActivityEventTextBox() { }
    
    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.DATA_FORMAT || keyWord == BPKeyWords.STOP_ACTIVITY) {
                updateAttribute(keyWord, value);
            }
        }
    }

    public MessageActivityEvent getMessageActivityEvent() {
        return (MessageActivityEvent) getElement();
    }

}
