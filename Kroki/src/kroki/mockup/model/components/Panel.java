package kroki.mockup.model.components;

import java.awt.Dimension;

import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.view.painters.components.PanelPainter;

/**
 * Component which represents a panel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class Panel extends Composite {

    public Panel() {
        super();
        elementPainter = new PanelPainter(this);
        layoutManager = new FlowLayoutManager();
        dimension = new Dimension(100, 50);
    }

    public Panel(String name) {
        super(name);
        elementPainter = new PanelPainter(this);
        layoutManager = new FlowLayoutManager();
        dimension = new Dimension(100, 50);
    }


    @Override
    public void updateComponent() {
        Dimension minSize = getMinimumSize();
        if (dimension.width < minSize.width) {
            dimension.width = minSize.width + insets.left + insets.bottom;
        }
        if (dimension.height < minSize.height) {
            dimension.height = minSize.height + insets.top + insets.bottom;
        }
        elementPainter.update();
    }

}
