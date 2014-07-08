package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.ConditionalEdge;
import bp.model.data.Element;
import bp.model.data.Vertex;
import bp.model.graphic.BPComponent;

public class ConditionalEdgeState extends BPState {

    private ConditionalEdge edge;

    public ConditionalEdgeState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final Element element = getGraphicPanel().getElementAt(p);
        if (element instanceof Vertex) {
            final Vertex vertex = (Vertex) element;
            if (this.edge == null) {
                this.edge = new ConditionalEdge(getPanel().getProcess().getNameGenerator().nextEdgeName());
                if (vertex.canHaveOutput()) {
                    this.edge.updateSource(vertex, null);
                    final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                    this.edge.getEdgeComponent().setSourceX(p.x);
                    this.edge.getEdgeComponent().setSourceY(p.y);
                    this.edge.getEdgeComponent().setTargetX(p.x);
                    this.edge.getEdgeComponent().setTargetY(p.y);
                    this.edge.getEdgeComponent().updateComponent(vertexComponent, null);

                    getPanel().getProcess().addElement(this.edge);
                    getPanel().repaint();
                }
            } else {
                if (vertex.canHaveInput() && !vertex.equals(this.edge.getSource())) {
                    this.edge.updateTarget(vertex, null);
                    final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                    this.edge.getEdgeComponent().setTargetX(p.x);
                    this.edge.getEdgeComponent().setTargetY(p.y);
                    this.edge.getEdgeComponent().updateComponent(null, vertexComponent);

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

            getPanel().repaint();
        }
    }

    @Override
    public void enteringState() {
        System.out.println("entering conditional edge state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting conditional edge state");
    }

}
