package bp.state;

import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Activity;
import bp.model.data.ActivityEvent;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.Lane;
import bp.model.data.Vertex;
import bp.model.graphic.BPComponent;
import bp.model.graphic.BPElement;
import bp.model.graphic.SquareComponent;
import bp.model.graphic.util.HandlerPosition;

public class ResizeState extends BPState {

    private Integer diffX;
    private Integer diffY;
    private BPComponent draggedComponent;
    private Vertex draggedVertex;
    private Lane draggedLane;
    private HandlerPosition handlerPos;

    private Integer start;
    private Integer end;

    public ResizeState(final BPPanel panel) {
        super(panel);
        this.draggedComponent = null;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.draggedComponent == null) {
            for (final Element el : getGraphicPanel().getSelectionManager().getSelectedElements()) {
                if (el.getComponent().getHandlers().isHandlerAt(e.getPoint())) {
                    final BPElement bpEl = el.getComponent();
                    if (bpEl instanceof BPComponent) {
                        this.draggedComponent = (BPComponent) bpEl;
                        this.handlerPos = this.draggedComponent.getHandlers().getHandlerAt(e.getPoint()).getHandlerPosition();
                        if (el instanceof Vertex) {
                            this.draggedVertex = (Vertex) el;
                        } else if (el instanceof Lane) {
                            this.draggedLane = (Lane) el;
                        }
                        this.diffX = e.getPoint().x;
                        this.diffY = e.getPoint().y;
                        break;
                    }
                }
            }
        }

        if (this.draggedComponent == null) {
            getPanel().getStateManager().moveToState(StateType.SELECT);
        } else {
            this.diffX = e.getPoint().x - this.diffX;
            this.diffY = e.getPoint().y - this.diffY;

            if (this.handlerPos == HandlerPosition.SOUTH_EAST) {
                this.draggedComponent.setWidth(this.draggedComponent.getWidth() + this.diffX);
                this.draggedComponent.setHeight(this.draggedComponent.getHeight() + this.diffY);
            } else if (this.handlerPos == HandlerPosition.EAST) {
                this.draggedComponent.setWidth(this.draggedComponent.getWidth() + this.diffX);
            } else if (this.handlerPos == HandlerPosition.SOUTH) {
                this.draggedComponent.setHeight(this.draggedComponent.getHeight() + this.diffY);
            } else if (this.handlerPos == HandlerPosition.NORTH_WEST) {
                this.start = this.draggedComponent.getWidth();
                this.draggedComponent.setWidth(this.draggedComponent.getWidth() - this.diffX);
                this.end = this.draggedComponent.getWidth();

                this.draggedComponent.setX(this.draggedComponent.getX() + this.start - this.end);

                this.start = this.draggedComponent.getHeight();
                this.draggedComponent.setHeight(this.draggedComponent.getHeight() - this.diffY);
                this.end = this.draggedComponent.getHeight();

                this.draggedComponent.setY(this.draggedComponent.getY() + this.start - this.end);
                if (this.draggedComponent instanceof SquareComponent) {
                    this.draggedComponent.setX(this.draggedComponent.getX() + this.start - this.end);
                }
            } else if (this.handlerPos == HandlerPosition.NORTH) {
                this.start = this.draggedComponent.getHeight();
                this.draggedComponent.setHeight(this.draggedComponent.getHeight() - this.diffY);
                this.end = this.draggedComponent.getHeight();

                this.draggedComponent.setY(this.draggedComponent.getY() + this.start - this.end);
            } else if (this.handlerPos == HandlerPosition.WEST) {
                this.start = this.draggedComponent.getWidth();
                this.draggedComponent.setWidth(this.draggedComponent.getWidth() - this.diffX);
                this.end = this.draggedComponent.getWidth();

                this.draggedComponent.setX(this.draggedComponent.getX() + this.start - this.end);
            } else if (this.handlerPos == HandlerPosition.NORTH_EAST) {
                this.draggedComponent.setWidth(this.draggedComponent.getWidth() + this.diffX);

                this.start = this.draggedComponent.getHeight();
                this.draggedComponent.setHeight(this.draggedComponent.getHeight() - this.diffY);
                this.end = this.draggedComponent.getHeight();

                this.draggedComponent.setY(this.draggedComponent.getY() + this.start - this.end);
            } else if (this.handlerPos == HandlerPosition.SOUTH_WEST) {
                if (this.draggedComponent instanceof SquareComponent) {
                    this.start = this.draggedComponent.getWidth();
                    this.draggedComponent.setHeight(this.draggedComponent.getHeight() + this.diffY);
                    this.end = this.draggedComponent.getWidth();

                    this.draggedComponent.setX(this.draggedComponent.getX() + this.start - this.end);
                } else {
                    this.draggedComponent.setHeight(this.draggedComponent.getHeight() + this.diffY);

                    this.start = this.draggedComponent.getWidth();
                    this.draggedComponent.setWidth(this.draggedComponent.getWidth() - this.diffX);
                    this.end = this.draggedComponent.getWidth();

                    this.draggedComponent.setX(this.draggedComponent.getX() + this.start - this.end);
                }
            }

            if (this.draggedVertex != null) {
                for (final Edge edge : this.draggedVertex.getInputEdges()) {
                    edge.getEdgeComponent().updateComponent(null, this.draggedComponent);
                }
                for (final Edge edge : this.draggedVertex.getOutputEdges()) {
                    edge.getEdgeComponent().updateComponent(this.draggedComponent, null);
                }
                if (this.draggedVertex instanceof Activity) {
                    final Activity activity = (Activity) this.draggedVertex;
                    for (final ActivityEvent aEvent : activity.getActivityEvents()) {
                        aEvent.getEventComponent().updatePosition();
                    }
                }
            }
            this.diffX = e.getPoint().x;
            this.diffY = e.getPoint().y;

            getGraphicPanel().repaint();
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        this.draggedVertex = null;
        this.draggedLane = null;
        this.draggedComponent = null;
        getPanel().getStateManager().moveToState(StateType.SELECT);
    }

    @Override
    public void enteringState() {
        System.out.println("entering resize state");

    }

    @Override
    public void exitingState() {
        System.out.println("exiting resize state");
    }

}
