package bp.model.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Vertex elements of Business Process
 * 
 * @author Sholy
 * 
 */
public abstract class Vertex extends Element {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Edge> inputEdges;
    private List<Edge> outputEdges;

    public Vertex(final String uniqueName) {
        super(uniqueName);
        initializeEdges();
    }

    public Vertex() { }
    
    public void setInputEdges(List<Edge> inputEdges) {
		this.inputEdges = inputEdges;
	}

	public void setOutputEdges(List<Edge> outputEdges) {
		this.outputEdges = outputEdges;
	}

	private void initializeEdges() {
        this.inputEdges = new ArrayList<Edge>();
        this.outputEdges = new ArrayList<Edge>();
    }

    /**
     * Returns true if vertex can have input (edge can end in this element)
     * 
     * @return
     */
    public abstract boolean canHaveInput();

    /**
     * Returns true if vertex can have output (edge can start from this element)
     * 
     * @return
     */
    public abstract boolean canHaveOutput();

    /**
     * Returns true if it has at least one input edge
     * 
     * @return
     */
    public boolean hasInput() {
        return !this.inputEdges.isEmpty();
    }

    /**
     * Returns true if it has at least one output edge
     * 
     * @return
     */
    public boolean hasOutput() {
        return !this.outputEdges.isEmpty();
    }

    public List<Edge> getInputEdges() {
        return this.inputEdges;
    }

    public List<Edge> getOutputEdges() {
        return this.outputEdges;
    }

    public void addOutputEdge(final Edge edge) {
        if (canHaveOutput()) {
            this.outputEdges.add(edge);
        }
    }

    public void addInputEdge(final Edge edge) {
        if (canHaveInput()) {
            this.inputEdges.add(edge);
        }
    }

}
