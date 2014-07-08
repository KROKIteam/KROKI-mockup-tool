package bp.state;

import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Element;
import bp.model.graphic.BPComponent;
import bp.model.graphic.BPElement;

public class MoveState extends BPState {

    private BPComponent draggedComponent;
    private Integer startX;
    private Integer startY;

    public MoveState(final BPPanel panel) {
        super(panel);
        this.draggedComponent = null;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.draggedComponent == null) {
            final Element el = getGraphicPanel().getElementAt(e.getPoint());
            if (el != null) {
                final BPElement bpEl = el.getComponent();
                if (bpEl instanceof BPComponent) {
                    this.draggedComponent = (BPComponent) bpEl;
                    this.startX = e.getPoint().x;
                    this.startY = e.getPoint().y;
                }

            }
        }

        if (this.draggedComponent == null) {
            getPanel().getStateManager().moveToState(StateType.SELECT);
        } else {
            final Integer diffX = e.getPoint().x - this.startX;
            final Integer diffY = e.getPoint().y - this.startY;
            this.draggedComponent.moveComponent(diffX, diffY);

            getGraphicPanel().repaint();

            this.startX = e.getPoint().x;
            this.startY = e.getPoint().y;
        }

    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        this.draggedComponent = null;
        getPanel().getStateManager().moveToState(StateType.SELECT);
    }

    @Override
    public void enteringState() {
        System.out.println("entering move state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting move state");

    }

}
