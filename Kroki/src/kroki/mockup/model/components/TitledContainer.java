/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.components;

import kroki.mockup.model.Composite;
import kroki.mockup.model.Insets;
import kroki.mockup.view.painters.components.TitledContainerPainter;

/**
 * Komponenta koja predstavlja kontejner sa naslovom (koji se nalazi u gornjem delu panela).
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
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
