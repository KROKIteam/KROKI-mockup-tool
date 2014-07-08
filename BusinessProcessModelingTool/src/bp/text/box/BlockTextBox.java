package bp.text.box;

import bp.model.util.BPKeyWords;

public class BlockTextBox extends CompositeTextBox {

    public BlockTextBox(final BPKeyWords key, final Object value, final TextBox owner) {
        super(key, value, owner);
    }

    public BlockTextBox() { }
    
    @Override
    public String generateText() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%s %s {", getIndentation(), getKey().getName(),
                getKey().getType().generateText(getValue())));
        sb.append(super.generateText());
        sb.append(String.format("\n%s}", getIndentation()));
        return sb.toString();
    }

}
