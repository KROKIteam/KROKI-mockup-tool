package bp.view.painter;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import bp.model.graphic.ActivityEventComponent;
import bp.model.graphic.BPComponent;
import bp.model.graphic.BPImage;
import bp.model.graphic.EventComponent;
import bp.model.graphic.TaskComponent;
import bp.model.graphic.util.ElementHandlers;
import bp.view.BasicPainter;

public class BPElementPainter extends BasicPainter {

    private BPComponent component = (BPComponent) getElement();

    public BPElementPainter(final BPComponent component) {
        super(component);
    }

    public BPElementPainter() { }
    
    protected BPComponent getComponent() {
        return this.component;
    }

    public void setComponent(BPComponent component) {
		this.component = component;
	}

	@Override
    public void paint(final Graphics2D g) {
        final AffineTransform oldTransform = g.getTransform();

        g.translate(getComponent().getX(), getComponent().getY());

        g.setPaint(getComponent().getFgColor());
        g.setStroke(getComponent().getFgStroke());
        g.draw(getComponent().getShape());

        g.setPaint(getComponent().getBgColor());
        g.setStroke(getComponent().getBgStroke());
        g.fill(getComponent().getShape());

        g.setTransform(oldTransform);

        if (this.component instanceof BPImage) {
            final BPImage img = (BPImage) this.component;
            if (this.component instanceof TaskComponent) {
                if (img.getImage() != null) {
                    g.translate(
                            getComponent().getX() + getComponent().getWidth() - img.getImageWidth()
                            - img.getImageMargins(), getComponent().getY() + img.getImageMargins());

                    g.drawImage(img.getImage(), 0, 0, img.getImageWidth(), img.getImageHeight(), null);

                    g.setTransform(oldTransform);
                }
            } else if (this.component instanceof EventComponent || this.component instanceof ActivityEventComponent) {
                if (img.getImage() != null) {

                    g.translate(getComponent().getX() + getComponent().getWidth() / 2 - img.getImageWidth() / 2,
                            getComponent().getY() + getComponent().getHeight() / 2 - img.getImageHeight() / 2);

                    g.drawImage(img.getImage(), 0, 0, img.getImageWidth(), img.getImageHeight(), null);

                    g.setTransform(oldTransform);
                }
            }
        }
    }

    @Override
    public boolean isElementAt(final Point pos) {
        final Integer x = getComponent().getX();
        final Integer y = getComponent().getY();
        final Integer w = getComponent().getWidth();
        final Integer h = getComponent().getHeight();
        final Rectangle2D area = new Rectangle2D.Double(x, y, w, h);
        return area.contains(pos);
    }

    @Override
    public void paintHandlers(final Graphics2D g) {
        final ElementHandlers handlers = getComponent().getHandlers();
        if (handlers == null)
            return;

        final AffineTransform oldTransform = g.getTransform();

        g.translate(handlers.getX(), handlers.getY());

        g.setPaint(handlers.getLineColor());
        g.setStroke(handlers.getLineStroke());
        g.draw(handlers.getShape());

        g.setTransform(oldTransform);

        if (handlers.getNw() != null)
            paintHandler(g, handlers.getNw());
        if (handlers.getNe() != null)
            paintHandler(g, handlers.getNe());
        if (handlers.getSe() != null)
            paintHandler(g, handlers.getSe());
        if (handlers.getSw() != null)
            paintHandler(g, handlers.getSw());
        if (handlers.getN() != null)
            paintHandler(g, handlers.getN());
        if (handlers.getE() != null)
            paintHandler(g, handlers.getE());
        if (handlers.getS() != null)
            paintHandler(g, handlers.getS());
        if (handlers.getW() != null)
            paintHandler(g, handlers.getW());

    }
}
