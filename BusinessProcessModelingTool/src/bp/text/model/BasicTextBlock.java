package bp.text.model;

public class BasicTextBlock extends TextBlock {

    private final StringBuilder text;

    public BasicTextBlock() {
        this.text = new StringBuilder();
    }

    @Override
    public Boolean isLeaf() {
        return true;
    }

    @Override
    public String getText() {
        return this.text.toString();
    }

    @Override
    public Integer getLength() {
        return this.text.length();
    }

    @Override
    public void insertText(final Integer position, final String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        if (position < getPosition() || position > getPosition() + getLength() + 1) {
            return;
        }
        final Integer relativePosition = position - getPosition();
        this.text.insert(relativePosition, text);
    }

    @Override
    public void removeText(final Integer startPos, final Integer endPos) {
        final Integer relativeStartPos = startPos - getPosition();
        final Integer relativeEndPos = endPos - getPosition();

        this.text.delete(relativeStartPos, relativeEndPos);
    }

}
