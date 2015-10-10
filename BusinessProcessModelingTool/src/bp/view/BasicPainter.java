package bp.view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import bp.model.graphic.BPGraphElement;
import bp.model.graphic.util.Handler;

public abstract class BasicPainter {

    private BPGraphElement element;

    public BasicPainter(BPGraphElement element) {
        this.element = element;
    }
    
    public BasicPainter() { }

    protected BPGraphElement getElement() {
        return element;
    }

    public void setElement(BPGraphElement element) {
		this.element = element;
	}

	public abstract void paint(Graphics2D g);

    public abstract boolean isElementAt(Point pos);

    public abstract void paintHandlers(Graphics2D g);

    protected void paintHandler(Graphics2D g, Handler h) {
        AffineTransform oldTransform = g.getTransform();

        g.translate(h.getX(), h.getY());

        g.setPaint(h.getFgColor());
        g.setStroke(h.getFgStroke());
        g.draw(h.getShape());
        g.fill(h.getShape());

        g.setTransform(oldTransform);
    }
}
