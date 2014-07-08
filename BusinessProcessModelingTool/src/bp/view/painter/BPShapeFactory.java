package bp.view.painter;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

import bp.model.graphic.BPComponent;
import bp.model.graphic.LaneComponent;
import bp.model.graphic.SquareComponent;
import bp.model.graphic.util.ElementHandlers;
import bp.model.graphic.util.Handler;

public class BPShapeFactory {

    private BPShapeFactory() {

    }

    public static Shape task(final BPComponent component) {
        final Integer arcSize = component.getWidth() / 10;
        return new RoundRectangle2D.Float(0, 0, component.getWidth(), component.getHeight(), arcSize, arcSize);
    }

    public static Shape gateway(final SquareComponent component) {
        final Integer w = component.getWidth();
        final Integer h = component.getHeight();
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, h / 2);
        gp.lineTo(w / 2, 0);
        gp.lineTo(w, h / 2);
        gp.lineTo(w / 2, h);
        gp.lineTo(0, h / 2);
        return gp;
    }

    public static Shape event(final SquareComponent component) {
        return new Ellipse2D.Float(0, 0, component.getWidth(), component.getHeight());
    }

    public static Shape handler(final Handler component) {
        return new Rectangle(component.getWidth(), component.getHeight());
    }

    public static Shape handlerBorder(final ElementHandlers component) {
        return new Rectangle(component.getWidth(), component.getHeight());
    }

    public static Shape arrow() {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(-10, -5);
        gp.lineTo(-10, 5);
        gp.lineTo(0, 0);
        return gp;
    }

    public static Shape diamond() {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(10, 5);
        gp.lineTo(20, 0);
        gp.lineTo(10, -5);
        gp.lineTo(0, 0);
        return gp;
    }

    public static Shape lane(final LaneComponent component) {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(component.getTextPanelHeight(), 0);
        gp.lineTo(component.getWidth(), 0);
        gp.lineTo(component.getWidth(), component.getHeight());
        gp.lineTo(component.getTextPanelHeight(), component.getHeight());
        gp.lineTo(component.getTextPanelHeight(), 0);
        gp.lineTo(0, 0);
        gp.lineTo(0, component.getHeight());
        gp.lineTo(component.getTextPanelHeight(), component.getHeight());
        gp.lineTo(component.getTextPanelHeight(), 0);
        return gp;
    }
}
