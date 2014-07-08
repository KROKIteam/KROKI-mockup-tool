package bp.text.box;

import bp.model.util.BPKeyWords;

public abstract class BasicTextBox extends TextBox {

    public BasicTextBox(final BPKeyWords key, final Object value, final TextBox owner) {
        super(key, value, owner);
    }

    public BasicTextBox() { }
}
