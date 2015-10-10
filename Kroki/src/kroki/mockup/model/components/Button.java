package kroki.mockup.model.components;

import java.awt.Dimension;

import kroki.mockup.model.Component;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.painters.components.ButtonPainter;

/**Component which represents a button
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class Button extends Component {

    private SerializableBufferedImage image;
    private Dimension labelDim;
    private int gap = 5;

    public Button(String name) {
        super(name);
        calculateDimension();
        elementPainter = new ButtonPainter(this);
    }

    public Button() {
        super();
        calculateDimension();
        elementPainter = new ButtonPainter(this);
    }

    public Button(SerializableBufferedImage image) {
        this.image = image;
        this.name = null;
        calculateDimension();
        elementPainter = new ButtonPainter(this);
    }

    public Button(String name, SerializableBufferedImage image) {
        super();
        this.name = name;
        this.image = image;
        calculateDimension();
        elementPainter = new ButtonPainter(this);
    }

    private void calculateDimension() {
        dimension = getMinimumSize();
        dimension.width += insets.left;
        dimension.width += insets.right;
        dimension.height += insets.top;
        dimension.height += insets.bottom;
    }

    @Override
    public final Dimension getMinimumSize() {
        Dimension min = new Dimension();
        min.width = 0;
        min.height = 0;

        
        if (image != null) {
            min.width += image.getSource().getWidth();
            min.height += image.getSource().getHeight();
        }
        if (name != null && !name.equals("")) {
            Dimension nameDim = KrokiTextMeasurer.measureText(name, getFont());
            if (image != null) {
                min.width += gap;
                if (nameDim.height > image.getSource().getHeight()) {
                    int dif = nameDim.height - image.getSource().getHeight();
                    min.height += dif;
                }
            } else {
                min.height += nameDim.height;
            }
            min.width += nameDim.width;
        }
        return min;
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
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public SerializableBufferedImage getImage() {
        return image;
    }

    public void setImage(SerializableBufferedImage image) {
        this.image = image;
    }

    public Dimension getLabelDim() {
        return labelDim;
    }

    public void setLabelDim(Dimension labelDim) {
        this.labelDim = labelDim;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
}
