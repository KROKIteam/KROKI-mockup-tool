package bp.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import bp.gui.BPPanel;
import bp.model.data.Gateway;

public class GatewayState extends BPState {

    public GatewayState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final Point p = e.getPoint();
        final Gateway gateway = new Gateway(getPanel().getProcess().getNameGenerator().nextGatewayName());
        gateway.getGatewayComponent().setX(p.x);
        gateway.getGatewayComponent().setY(p.y);

        getPanel().getProcess().addElement(gateway);
        getPanel().repaint();
    }

    @Override
    public void enteringState() {
        System.out.println("entering gateway state");
    }

    @Override
    public void exitingState() {
        System.out.println("exiting gateway state");
    }

}
