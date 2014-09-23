/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.components;

import java.awt.Dimension;
import java.awt.FlowLayout;

import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.view.painters.components.ComboZoomPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ComboZoom extends Composite {

    private Button button;
    private ComboBox comboBox;

    public ComboZoom(String name) {
        super(name);
        setLayoutManager(new FlowLayoutManager(0,0,FlowLayout.LEFT));
        comboBox = new ComboBox(name, 15);
        button = new Button("...");
        addChild(comboBox);
        addChild(button);
        layout();
        elementPainter = new ComboZoomPainter(this);
    }

    public ComboZoom() {
        super();
        setLayoutManager(new FlowLayoutManager());
        comboBox = new ComboBox(10);
        button = new Button("...");
        addChild(comboBox);
        addChild(button);
        layout();
        elementPainter = new ComboZoomPainter(this);
    }

    @Override
    public void updateComponent() {
        Dimension minSize = getMinimumSize();
        Dimension maxSize = getMaximumSize();
        if (dimension.width < minSize.width) {
            dimension.width = minSize.width + insets.left + insets.bottom;
        } else if (dimension.width > maxSize.width) {
            dimension.width = maxSize.width + insets.left + insets.bottom;
        }
        if (dimension.height < minSize.height) {
            dimension.height = minSize.height + insets.top + insets.bottom;
        } else if (dimension.height > maxSize.width) {
            dimension.height = maxSize.height + insets.top + insets.bottom;
        }
        elementPainter.update();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }
}
