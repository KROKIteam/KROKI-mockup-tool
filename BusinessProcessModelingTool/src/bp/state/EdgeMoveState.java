package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import bp.gui.BPPanel;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.Vertex;
import bp.model.graphic.BPComponent;
import bp.model.graphic.BPEdge;
import bp.model.graphic.util.HandlerPosition;

public class EdgeMoveState extends BPState {

    private Integer startX;
    private Integer startY;
    private Edge draggedEdge;
    private BPEdge draggedComponent;
    private HandlerPosition handlerPos;

    public EdgeMoveState(final BPPanel panel) {
        super(panel);
        this.draggedComponent = null;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.draggedComponent == null) {
            for (final Element el : getGraphicPanel().getSelectionManager().getSelectedElements()) {
                if (el.getComponent().getHandlers().isHandlerAt(e.getPoint())) {
                    if (el instanceof Edge) {
                        this.draggedEdge = (Edge) el;
                        this.draggedComponent = this.draggedEdge.getEdgeComponent();
                        this.handlerPos = this.draggedComponent.getHandlers().getHandlerAt(e.getPoint()).getHandlerPosition();
                        if (this.handlerPos == HandlerPosition.SOURCE) {
                            this.startX = this.draggedComponent.getSourceX();
                            this.startY = this.draggedComponent.getSourceY();
                        } else if (this.handlerPos == HandlerPosition.TARGET) {
                            this.startX = this.draggedComponent.getTargetX();
                            this.startY = this.draggedComponent.getTargetY();
                        } else {
                            this.startX = e.getPoint().x;
                            this.startY = e.getPoint().y;
                        }
                        break;
                    }
                }
            }
        }

        if (this.draggedComponent == null) {
            getPanel().getStateManager().moveToState(StateType.SELECT);
        } else {
            if (this.handlerPos == HandlerPosition.SOURCE) {
                this.draggedComponent.setSourceX(e.getPoint().x);
                this.draggedComponent.setSourceY(e.getPoint().y);
            } else if (this.handlerPos == HandlerPosition.TARGET) {
                this.draggedComponent.setTargetX(e.getPoint().x);
                this.draggedComponent.setTargetY(e.getPoint().y);
            }
            getGraphicPanel().repaint();
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.draggedComponent != null) {
            final Point p = e.getPoint();
            final Set<Element> ignoreSelection = new HashSet<>();
            ignoreSelection.add(this.draggedEdge);
            final Element element = getGraphicPanel().getElementAt(p, ignoreSelection);
            if (element instanceof Vertex) {
                System.out.println("edge move: element hit");
                final Vertex vertex = (Vertex) element;
                if (this.handlerPos == HandlerPosition.SOURCE) {
                    if (vertex.canHaveOutput()) {
                        this.draggedEdge.updateSource(vertex, null);
                        final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                        this.draggedComponent.setSourceX(p.x);
                        this.draggedComponent.setSourceY(p.y);
                        this.draggedComponent.updateComponent(vertexComponent, null);
                    }
                } else if (this.handlerPos == HandlerPosition.TARGET) {
                    if (vertex.canHaveInput()) {
                        this.draggedEdge.updateTarget(vertex, null);
                        final BPComponent vertexComponent = (BPComponent) vertex.getComponent();
                        this.draggedComponent.setTargetX(p.x);
                        this.draggedComponent.setTargetY(p.y);
                        this.draggedComponent.updateComponent(null, vertexComponent);
                    }
                }
            } else {
                if (this.handlerPos == HandlerPosition.SOURCE) {
                    this.draggedComponent.setSourceX(this.startX);
                    this.draggedComponent.setSourceY(this.startY);
                } else if (this.handlerPos == HandlerPosition.TARGET) {
                    this.draggedComponent.setTargetX(this.startX);
                    this.draggedComponent.setTargetY(this.startY);
                }
            }
            getGraphicPanel().repaint();
        }


        this.draggedEdge = null;
        this.draggedComponent = null;
        getPanel().getStateManager().moveToState(StateType.SELECT);
    }

    @Override
    public void enteringState() {
        System.out.println("entering edge move state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting edge move state");

    }

}
