package kroki.mockup.model.components;

import java.awt.Dimension;

import kroki.mockup.model.Component;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.view.painters.components.LinkPainter;

/**
 * Component which represents a link
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class Link extends Component {

    public Link(String name) {
        super(name);
        calculateDim();
        elementPainter = new LinkPainter(this);
    }

    public Link() {
        super("Link");
        calculateDim();
        elementPainter = new LinkPainter(this);
    }

    private void calculateDim() {
        dimension = KrokiTextMeasurer.measureText(name, font);
        dimension.width += insets.left;
        dimension.width += insets.right;
        dimension.height += insets.bottom;
        dimension.height += insets.top;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension min = KrokiTextMeasurer.measureText(name, font);
        min.width += insets.left;
        min.width += insets.right;
        min.height += insets.bottom;
        min.height += insets.top;
        return min;
    }

    @Override
    public Dimension getMaximumSize() {
        return getMinimumSize();
    }

    @Override
    public void updateComponent() {
        dimension = getMinimumSize();
        elementPainter.update();
    }
}
