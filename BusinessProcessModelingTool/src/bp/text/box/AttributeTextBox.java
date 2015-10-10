package bp.text.box;

import bp.model.util.BPKeyWords;

public class AttributeTextBox extends BasicTextBox {

    public AttributeTextBox(final BPKeyWords key, final Object value, final TextBox owner) {
        super(key, value, owner);
    }
    
    public AttributeTextBox() { }
    
    @Override
    public String generateText() {
        return String.format("%s%s: %s", getIndentation(), getKey().getName(),
                getKey().getType().generateText(getValue()));
    }

}
