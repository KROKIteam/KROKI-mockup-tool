package bp.text.box;

import bp.event.AttributeChangeListener;
import bp.model.data.Edge;
import bp.model.data.Vertex;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class EdgeTextBox extends ElementTextBox {

    private Vertex sourceVertex;
    private Vertex targetVertex;
    private transient AttributeChangeListener sourceChangeListener;
    private transient AttributeChangeListener targetChangeListener;

    public EdgeTextBox(final Edge edge, final BPKeyWords keyWord, final TextBox owner) {
        super(edge, keyWord, owner);
    }

    public EdgeTextBox() { 
    	sourceChangeListener = new SourceChangeListener();
    	targetChangeListener = new TargetChangeListener();
    }
    
    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.SOURCE) {
                if (this.sourceVertex != null) {
                    this.sourceVertex.removeAttributeChangeListener(this.sourceChangeListener);
                }
                this.sourceVertex = (Vertex) value;
                this.sourceChangeListener = new SourceChangeListener();
                this.sourceVertex.addAttributeChangeListener(this.sourceChangeListener);
                if (this.sourceVertex.getUniqueName() != null) {
                    updateAttribute(keyWord, this.sourceVertex.getUniqueName());
                }
            } else if (keyWord == BPKeyWords.TARGET) {
                if (this.targetVertex != null) {
                    this.targetVertex.removeAttributeChangeListener(this.targetChangeListener);
                }
                this.targetVertex = (Vertex) value;
                this.targetChangeListener = new TargetChangeListener();
                this.targetVertex.addAttributeChangeListener(this.targetChangeListener);
                if (this.targetVertex.getUniqueName() != null) {
                    updateAttribute(keyWord, this.targetVertex.getUniqueName());
                }
            }
        }
    }

    public Edge getEdge() {
        return (Edge) getElement();
    }

    private class SourceChangeListener implements AttributeChangeListener {

    	SourceChangeListener() { }
    	
        @Override
        public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
            if (value != null) {
                if (keyWord == BPKeyWords.UNIQUE_NAME) {
                    updateAttribute(BPKeyWords.SOURCE, value);
                }
            }
        }

        @Override
        public Controller getController() {
            return null;
        }
    }

    private class TargetChangeListener implements AttributeChangeListener {

        @Override
        public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
            if (value != null) {
                if (keyWord == BPKeyWords.UNIQUE_NAME) {
                    updateAttribute(BPKeyWords.TARGET, value);
                }
            }
        }

        @Override
        public Controller getController() {
            return null;
        }

    }

}
