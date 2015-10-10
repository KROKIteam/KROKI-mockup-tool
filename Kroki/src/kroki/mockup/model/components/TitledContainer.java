package kroki.mockup.model.components;

import kroki.mockup.model.Composite;
import kroki.mockup.model.Insets;
import kroki.mockup.view.painters.components.TitledContainerPainter;

/**
 * Component represent a titled container, where the title is located in the top part of the panel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class TitledContainer extends Composite {

    public TitledContainer() {
        insets = new Insets(40, 4, 4, 4);
        elementPainter = new TitledContainerPainter(this);
    }

    public TitledContainer(String name) {
        super(name);
        insets = new Insets(40, 4, 4, 4);
        elementPainter = new TitledContainerPainter(this);
    }
}
