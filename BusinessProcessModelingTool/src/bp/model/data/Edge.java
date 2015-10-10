package bp.model.data;

import java.awt.Point;

import bp.details.EdgeDetails;
import bp.model.graphic.BPEdge;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;


/**
 * Edges of Business Process. They connect vertices.
 * 
 * @author Sholy
 * 
 */
public class Edge extends Element {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vertex source;
    private Vertex target;

    public Edge(final String uniqueName) {
        super(uniqueName);
    }
    
    public Edge() { }

    public Vertex getSource() {
        return this.source;
    }

    public void updateSource(final Vertex source, final Controller controller) {
        if (this.source != null) {
            this.source.getOutputEdges().remove(this);
        }
        this.source = source;
        this.source.getOutputEdges().add(this);
        fireAttributeChanged(BPKeyWords.SOURCE, this.source, controller);
    }

    public Vertex getTarget() {
        return this.target;
    }

    public void updateTarget(final Vertex target, final Controller controller) {
        if (this.target != null) {
            this.target.getInputEdges().remove(this);
        }
        this.target = target;
        this.target.getInputEdges().add(this);
        fireAttributeChanged(BPKeyWords.TARGET, this.target, controller);
    }

    public BPEdge getEdgeComponent() {
        return (BPEdge) getComponent();
    }

    @Override
    protected void initializeComponent() {
        this.component = new BPEdge(BPEdge.REGULAR_EDGE);
        this.component.setzIndex(301);
    }

    @Override
    protected void initializeDetails() {
        this.details = new EdgeDetails(this);
    }

    @Override
    public boolean isElementAt(final Point p) {
        if (this.source == null || this.target == null) {
            return false;
        }
        return super.isElementAt(p);
    }

	public void setSource(Vertex source) {
		this.source = source;
	}

	public void setTarget(Vertex target) {
		this.target = target;
	}

}
