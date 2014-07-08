package bp.text.box;

import bp.event.AttributeChangeListener;
import bp.model.data.Element;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public abstract class ElementTextBox extends BlockTextBox {

    private Element element;

    public ElementTextBox(final Element element, final BPKeyWords keyWord, final TextBox owner) {
        super(keyWord, element.getUniqueName(), owner);
        this.element = element;

        addDataListener();
    }
    
    public ElementTextBox() { }

    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        if (value != null) {
            if (keyWord == BPKeyWords.UNIQUE_NAME) {
                setValue(value);
                textChanged();
            } else if (keyWord == BPKeyWords.NAME || keyWord == BPKeyWords.DESCRIPTION) {
                updateAttribute(keyWord, value);
            }
        }
    }

    protected void updateAttribute(final BPKeyWords keyWord, final Object value) {
        final AttributeTextBox attTextBox = getAttributeTextBox(keyWord);
        if (attTextBox != null) {
            attTextBox.setValue(value);
            textChanged();
        } else {
            appendTextBox(new AttributeTextBox(keyWord, value, this));
            textChanged();
        }
    }

    private void addDataListener() {
        this.element.addAttributeChangeListener(new AttributeChangeListener() {

            @Override
            public Controller getController() {
                return Controller.TEXT;
            }

            @Override
            public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
                dataAttributeChanged(keyWord, value);
            }
        });
    }

    public Element getElement() {
        return this.element;
    }

}
