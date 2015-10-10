package bp.details;

import javax.swing.JLabel;
import javax.swing.JTextField;

import bp.event.AttributeChangeListener;
import bp.model.data.Edge;
import bp.model.data.Vertex;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class EdgeDetails extends ElementDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -4876745930351443176L;

    public static final String TARGET_LABEL = "Target:";
    public static final String SOURCE_LABEL = "Source:";

    private Vertex sourceVertex;
    private Vertex targetVertex;
    private AttributeChangeListener sourceChangeListener;
    private AttributeChangeListener targetChangeListener;

    private JLabel targetLb;
    private JLabel sourceLb;
    private JTextField targetTf;
    private JTextField sourceTf;

    private Edge edge = (Edge) getElement();
    
    public EdgeDetails(Edge edge) {
        super(edge);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.targetLb = new JLabel(TARGET_LABEL);
        this.sourceLb = new JLabel(SOURCE_LABEL);

        this.targetTf = new JTextField(20);
        this.sourceTf = new JTextField(20);

        this.targetTf.setEnabled(false);
        this.sourceTf.setEnabled(false);

        this.sourceVertex = null;
        this.targetVertex = null;
        
        // Set the texts if available
        edge = (Edge) getElement();
        if (edge.getTarget() != null) targetTf.setText(edge.getTarget().getUniqueName());
        if (edge.getSource() != null) sourceTf.setText(edge.getSource().getUniqueName());
    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();

        createBasic();

        getBasic().add(this.sourceLb);
        getBasic().add(this.sourceTf);
        getBasic().add(this.targetLb);
        getBasic().add(this.targetTf);
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
                    this.sourceTf.setText(this.sourceVertex.getUniqueName());
                }
            } else if (keyWord == BPKeyWords.TARGET) {
                if (this.targetVertex != null) {
                    this.targetVertex.removeAttributeChangeListener(this.targetChangeListener);
                }
                this.targetVertex = (Vertex) value;
                this.targetChangeListener = new TargetChangeListener();
                this.targetVertex.addAttributeChangeListener(this.targetChangeListener);
                if (this.targetVertex.getUniqueName() != null) {
                    this.targetTf.setText(this.targetVertex.getUniqueName());
                }
            }
        }
    }

    private class SourceChangeListener extends AttributeChangeListener {

        @Override
        public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
            if (keyWord == BPKeyWords.UNIQUE_NAME) {
                EdgeDetails.this.sourceTf.setText((String) value);
            }
        }

        @Override
        public Controller getController() {
            return null;
        }

    }

    private class TargetChangeListener extends AttributeChangeListener {

        @Override
        public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
            if (keyWord == BPKeyWords.UNIQUE_NAME) {
                EdgeDetails.this.targetTf.setText((String) value);
            }
        }

        @Override
        public Controller getController() {
            return null;
        }

    }

}
