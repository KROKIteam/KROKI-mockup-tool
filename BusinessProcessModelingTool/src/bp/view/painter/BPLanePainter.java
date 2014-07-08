package bp.view.painter;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import bp.model.graphic.LaneComponent;

public class BPLanePainter extends BPElementPainter {

    private final LaneComponent component = (LaneComponent) getElement();

    public BPLanePainter(LaneComponent component) {
        super(component);
    }

    protected LaneComponent getLaneComponent() {
        return component;
    }

    @Override
    public void paint(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();

        g.translate(getLaneComponent().getX(), getLaneComponent().getY());

        g.setPaint(getLaneComponent().getBgColor());
        g.setStroke(getLaneComponent().getBgStroke());
        g.fill(getLaneComponent().getShape());

        g.setPaint(getLaneComponent().getFgColor());
        g.setStroke(getLaneComponent().getFgStroke());
        g.draw(getLaneComponent().getShape());

        g.setTransform(oldTransform);
        
        if (getLaneComponent().getText() != null) {
            g.setFont(getLaneComponent().getFont());
            g.setPaint(getLaneComponent().getFgColor());

            g.translate(getLaneComponent().getX() + getLaneComponent().getTextHeight() + 5, getLaneComponent().getY()
                    + (getLaneComponent().getHeight() + getLaneComponent().getTextWidth()) / 2);
            g.rotate(-Math.PI / 2);

            g.drawString(getLaneComponent().getText(), 0, 0);

            g.setTransform(oldTransform);
        }
    }


}
