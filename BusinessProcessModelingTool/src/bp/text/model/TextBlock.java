package bp.text.model;

public abstract class TextBlock {

    private TextBlock owner;
    private TextBlock precessor;
    private TextBlock successor;

    public TextBlock() {

    }

    public abstract Boolean isLeaf();

    public abstract String getText();

    public abstract Integer getLength();

    public abstract void insertText(Integer position, String text);

    public abstract void removeText(Integer startPos, Integer endPos);

    // public abstract Boolean appendable();

    public Integer getPosition() {
        if (getPrecessor() != null) {
            return getPrecessor().getPosition() + getPrecessor().getLength();
        }
        if (getOwner() != null) {
            return getOwner().getPosition();
        }
        return 0;
    }

    public void removeBlock() {
        if (getPrecessor() != null) {
            getPrecessor().setSuccessor(getSuccessor());
        }
        if (getSuccessor() != null) {
            getSuccessor().setPrecessor(getPrecessor());
        }
    }

    public TextBlock getOwner() {
        return this.owner;
    }

    public void setOwner(final TextBlock owner) {
        this.owner = owner;
    }

    public TextBlock getPrecessor() {
        return this.precessor;
    }

    public void setPrecessor(final TextBlock precessor) {
        this.precessor = precessor;
    }

    public TextBlock getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(final TextBlock successor) {
        this.successor = successor;
    }

}
