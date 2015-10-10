package bp.text.box;

import bp.model.data.Activity;
import bp.model.util.BPKeyWords;

public abstract class ActivityTextBox extends ElementTextBox {

    public ActivityTextBox(final Activity activity, final BPKeyWords keyWord, final TextBox owner) {
        super(activity, keyWord, owner);
    }

    public ActivityTextBox() { }
    
    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.DATA || keyWord == BPKeyWords.LOOP_EXPRESSION || keyWord == BPKeyWords.MIN_INPUT) {
                updateAttribute(keyWord, value);
            }
        }
    }

    public Activity getActivity() {
        return (Activity) getElement();
    }
}
