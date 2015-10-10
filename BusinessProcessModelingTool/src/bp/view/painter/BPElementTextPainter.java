package bp.view.painter;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import bp.model.graphic.BPComponent;
import bp.model.graphic.BPText;

public class BPElementTextPainter extends BPElementPainter {

    public BPElementTextPainter(final BPComponent component) {
        this(component, null);
    }

    public BPElementTextPainter(final BPComponent component, final String text) {
        super(component);
    }

    public BPElementTextPainter() { }
    
    @Override
    public void paint(final Graphics2D g) {
        super.paint(g);

        if (getComponent() instanceof BPText) {
            final BPComponent comp = getComponent();
            final BPText text = (BPText) comp;

            if (text.getText() == null || text.getText().isEmpty())
                return;

            g.setFont(comp.getFont());
            g.setPaint(comp.getFgColor());
            final FontMetrics metrics = g.getFontMetrics();
            final Integer stringWidth = metrics.stringWidth(text.getText());
            Integer x = comp.getX() + (comp.getWidth() - stringWidth) / 2;
            final Integer y = comp.getY() + comp.getHeight() + metrics.getHeight() + text.getTextSeparatorSize();
            if (x < 0)
                x = 0;

            g.drawString(text.getText(), x, y);

        }

    }

}
