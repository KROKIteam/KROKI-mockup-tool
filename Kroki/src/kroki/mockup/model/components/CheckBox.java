package kroki.mockup.model.components;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import kroki.mockup.model.Component;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.painters.components.CheckBoxPainter;

/**
 * Component which represents a check box.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class CheckBox extends Component {

    private boolean checked;
    private SerializableBufferedImage image;
    private int gap = 5;

    public CheckBox(String name, boolean checked) {
        super(name);
        this.checked = checked;
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDimension();
        elementPainter = new CheckBoxPainter(this);
    }

    public CheckBox(boolean checked) {
        this.checked = checked;
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDimension();
        elementPainter = new CheckBoxPainter(this);
    }

    public CheckBox(String name) {
        this.checked = false;
        this.name = name;
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDimension();
        elementPainter = new CheckBoxPainter(this);
    }

    private void calculateDimension() {
        dimension = getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension min = new Dimension();
        min.width = 0;
        min.height = 0;

        min.width += image.getSource().getWidth();
        min.height += image.getSource().getHeight();

        Dimension nameDim = new Dimension();
        if (name != null && !name.equals("")) {
            nameDim = KrokiTextMeasurer.measureText(name, getFont());
            min.width += gap;
            min.width += nameDim.width;
        }
        min.height = Math.max(image.getSource().getHeight(), nameDim.height);

        min.width += insets.getLeft() + insets.getRight();
        min.height += insets.getTop() + insets.getBottom();
        return min;
    }

    @Override
    public void updateComponent() {
        dimension = getMinimumSize();
        elementPainter.update();
    }

    @Override
    public Dimension getMaximumSize() {
        return getMinimumSize();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public SerializableBufferedImage getImage() {
        return image;
    }

    public void setImage(SerializableBufferedImage image) {
        this.image = image;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
}
