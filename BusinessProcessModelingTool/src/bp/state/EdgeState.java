package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.Vertex;
import bp.model.graphic.BPComponent;

public class EdgeState extends BPState {

    private Edge edge;

    public EdgeState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final Element element = getGraphicPanel().getElementAt(p);
        if (element instanceof Vertex) {
            final Vertex vertex = (Vertex) element;
            if (this.edge == null) {
                this.edge = new Edge(getPanel().getProcess().getNameGenerator().nextEdgeName());
                if (vertex.canHaveOutput()) {
                    final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                    this.edge.getEdgeComponent().setSourceX(p.x);
                    this.edge.getEdgeComponent().setSourceY(p.y);
                    this.edge.getEdgeComponent().setTargetX(p.x);
                    this.edge.getEdgeComponent().setTargetY(p.y);
                    this.edge.getEdgeComponent().updateComponent(vertexComponent, null);

                    getPanel().getProcess().addElement(this.edge);
                    this.edge.updateSource(vertex, null);
                    // TODO: limit repaint region
                    getPanel().repaint();
                }
            } else {
                if (vertex.canHaveInput() && !vertex.equals(this.edge.getSource())) {
                    this.edge.updateTarget(vertex, null);
                    final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                    this.edge.getEdgeComponent().setTargetX(p.x);
                    this.edge.getEdgeComponent().setTargetY(p.y);
                    this.edge.getEdgeComponent().updateComponent(null, vertexComponent);

                    // TODO: limit repaint region
                    getPanel().repaint();

                    this.edge = null;
                }
            }
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        final Point p = e.getPoint();
        if (this.edge != null) {
            this.edge.getEdgeComponent().setTargetX(p.x);
            this.edge.getEdgeComponent().setTargetY(p.y);

            // TODO: limit repaint region
            getPanel().repaint();
        }
    }

    @Override
    public void enteringState() {
        System.out.println("entering edge state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting edge state");
    }

    protected Edge getEdge() {
        return this.edge;
    }

}
