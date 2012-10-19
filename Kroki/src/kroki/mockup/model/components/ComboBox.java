/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.components;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import kroki.mockup.model.Component;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.painters.components.ComboBoxPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ComboBox extends Component {

    private SerializableBufferedImage image;
    private int cols;
    private int gap = 5;
    private int blank = 0;
    private boolean zoom = false;
    /**Minimalan broj kolona*/
    private static final int MIN_COLS = 5;
    /**Minimalan broj redova*/
    private static final int MIN_ROWS = 1;
    /**Dimenzija koju zauzima labela*/
    private Dimension labelDim = new Dimension();
    /**Dimenzija koju zauzima polje za unos teksta*/
    private Dimension fieldDim = new Dimension();
    /**Dimenzija zoom dugmeta*/
    private Dimension zoomDim = new Dimension();

    public ComboBox(String name) {
        super(name);
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDim();
        elementPainter = new ComboBoxPainter(this);
    }

    public ComboBox() {
        super();
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDim();
        elementPainter = new ComboBoxPainter(this);
    }

    public ComboBox(String name, int cols) {
        super();
        this.name = name;
        this.cols = cols;
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDim();
        elementPainter = new ComboBoxPainter(this);
    }

    public ComboBox(int cols) {
        super();
        this.cols = cols;
        image = new SerializableBufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        calculateDim();
        elementPainter = new ComboBoxPainter(this);
    }

    /**Izračunava dimenziju koju zauzima tekst uračunavajući i prazan prostor između teksta i polja za unos*/
    private Dimension calculateLabelDim() {
        Dimension dim = new Dimension();
        if (name != null && !name.equals("")) {
            dim = KrokiTextMeasurer.measureText(name, getFont());
            int cell = 10;
            int mod = dim.width % cell;
            if (mod > 0) {
                blank = cell - mod;
                dim.width += blank;
            }
            dim.width += gap;
        }
        return dim;
    }

    /**Izračunava dimenziju koju zauzima polje za unos teksta*/
    private Dimension calculateFieldDim() {
        Dimension dim = KrokiTextMeasurer.measureText("M", getFont());
        dim.width *= (cols > 0) ? cols : MIN_COLS;
        dim.height *= MIN_ROWS;
        dim.width += image.getSource().getWidth();
        dim.width += margins.left + margins.right;
        dim.height += margins.top + margins.bottom;
        return dim;
    }

    /**Izračunava minimalnu dimenziju polja za unos teksta*/
    private Dimension calculateMinFieldDim() {
        Dimension dim = KrokiTextMeasurer.measureText("M", getFont());
        dim.width *= MIN_COLS;
        dim.height *= MIN_ROWS;
        dim.width += image.getSource().getWidth();
        dim.width += margins.left + margins.right;
        dim.height += margins.top + margins.bottom;
        return dim;
    }

    private void calculateDim() {
        dimension = new Dimension();
        labelDim = calculateLabelDim();
        fieldDim = calculateFieldDim();
        dimension.width += labelDim.width;
        dimension.width += fieldDim.width;
        dimension.height = Math.max(labelDim.height, fieldDim.height);
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
    }

    private void calculateZoomDim() {
        int width;
        int height;
        Dimension textDim = KrokiTextMeasurer.measureText("...", getFont());
        width = textDim.width;
        height = textDim.height;
        width += margins.left + margins.right;
        height += margins.top + margins.bottom;
        zoomDim.setSize(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension max = new Dimension();
        max.width = Integer.MAX_VALUE;
        max.height = getMinimumSize().height;
        return max;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension min = new Dimension();
        Dimension minLabelDim = calculateLabelDim();
        Dimension minFieldDim = calculateMinFieldDim();
        min.width += minLabelDim.width;
        min.width += minFieldDim.width;
        min.height = Math.max(minLabelDim.height, minFieldDim.height);
        min.width += insets.left + insets.right;
        min.height += insets.top + insets.bottom;
        return min;
    }

    @Override
    public void updateComponent() {
        Dimension minSize = getMinimumSize();
        Dimension maxSize = getMaximumSize();
        if (dimension.width < minSize.width) {
            dimension.width = minSize.width;
        } else if (dimension.width > maxSize.width) {
            dimension.width = maxSize.width;
        }
        if (dimension.height < minSize.height) {
            dimension.height = minSize.height;
        } else if (dimension.height > maxSize.width) {
            dimension.height = maxSize.height;
        }
        updateTextField();
        elementPainter.update();
    }

    /**Uzima u obzir promene nad komponentom i izračunava novu dimenziju koju zauzima labela*/
    private void updateLabel() {
        labelDim = calculateLabelDim();
    }

    /**Uzima u obzir promene nad komponentom i izračunava novu dimenziju koju zauzima tekstualno polje*/
    private void updateField() {
        int fieldWidth = dimension.width - labelDim.width - insets.left - insets.right;
        int fieldHeight = dimension.height - insets.top - insets.bottom;
        if (zoom) {
            calculateZoomDim();
            fieldWidth -= (insets.right + zoomDim.width);
        }
//        int cell = 10;
//        int mod = fieldWidth % cell;
//        if (mod > 0) {
//            fieldWidth += cell - mod;
//        }
        fieldDim.width = fieldWidth;
        fieldDim.height = fieldHeight;
    }

    /**Uzima u obzir promene nad komponentom i izračunava novu dimenziju koju zauzima cela komponenta*/
    private void updateTextField() {
        updateLabel();
        updateField();

        int width;
        int height;
        width = insets.left + labelDim.width + fieldDim.width + insets.right;
        height = insets.top + insets.bottom + Math.max(labelDim.height, fieldDim.height);
        if (zoom) {
            calculateZoomDim();
            width = insets.left + labelDim.width + fieldDim.width + insets.right + insets.right + zoomDim.width;
            height = insets.top + insets.bottom + Math.max(Math.max(labelDim.height, fieldDim.height), zoomDim.height);
        }
        dimension.setSize(width, height);
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public Dimension getFieldDim() {
        return fieldDim;
    }

    public void setFieldDim(Dimension fieldDim) {
        this.fieldDim = fieldDim;
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

    public int getBlank() {
        return blank;
    }

    public void setBlank(int blank) {
        this.blank = blank;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public Dimension getZoomDim() {
        return zoomDim;
    }

    public void setZoomDim(Dimension zoomDim) {
        this.zoomDim = zoomDim;
    }
}
