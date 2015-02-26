package kroki.mockup.model.components;

import java.awt.Dimension;

import kroki.mockup.model.Component;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.view.painters.components.TextAreaPainter;

/**
 * Component which represent a text area
 * @author Vladan MarseniÄ‡ (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class TextArea extends Component {

    /**Broj kolona*/
    private int cols;
    /**Broj redova*/
    private int rows;
    /**Minimalni razmak izmedju labele i polja za unos teksta*/
    private int gap = 5;
    /**Prazan prostor koji zauzima labela nakon njenog ispisivanja*/
    private int blank = 0;
    /**Minimalan broj kolona*/
    private static final int MIN_COLS = 5;
    /**Minimalan broj redova*/
    private static final int MIN_ROWS = 3;
    /**Dimenzija koju zauzima labela*/
    private Dimension labelDim = new Dimension();
    /**Dimenzija koju zauzima polje za unos teksta*/
    private Dimension fieldDim = new Dimension();

    public TextArea(String name) {
        super(name);
        calculateDim();
        elementPainter = new TextAreaPainter(this);
    }

    public TextArea() {
        super();
        calculateDim();
        elementPainter = new TextAreaPainter(this);
    }

    public TextArea(String name, int cols, int rows) {
        super();
        this.name = name;
        this.cols = cols;
        this.rows = rows;
        calculateDim();
        elementPainter = new TextAreaPainter(this);
    }

    public TextArea(int cols, int rows) {
        super();
        this.cols = cols;
        this.rows = rows;
        calculateDim();
        elementPainter = new TextAreaPainter(this);
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
    public Dimension getMaximumSize() {
        Dimension max = new Dimension();
        max.width = Integer.MAX_VALUE;
        max.height = Integer.MAX_VALUE;
        return max;
    }

    /**IzraÄ�unava dimenziju koju zauzima tekst uraÄ�unavajuÄ‡i i prazan prostor izmeÄ‘u teksta i polja za unos*/
    private Dimension calculateLabelDim() {
        Dimension dim = new Dimension();
        if (name != null && !name.equals("")) {
            dim = KrokiTextMeasurer.measureText(name, getFont());
            /*
            int cell = 10;
            int mod = dim.width % cell;
            if (mod > 0) {
                blank = cell - mod;
                dim.width += blank;
            }
            dim.width += gap;
            */
            dim.width += gap+blank;
        }
        return dim;
    }

    /**IzraÄ�unava dimenziju koju zauzima polje za unos teksta*/
    private Dimension calculateFieldDim() {
        Dimension dim = KrokiTextMeasurer.measureText("M", getFont());
        dim.width *= (cols > 0) ? cols : MIN_COLS;
        dim.height *= (rows > 0) ? rows : MIN_ROWS;
        dim.width += margins.left + margins.right;
        dim.height += margins.top + margins.bottom;
        return dim;
    }

    /**IzraÄ�unava minimalnu dimenziju polja za unos teksta*/
    private Dimension calculateMinFieldDim() {
        Dimension dim = KrokiTextMeasurer.measureText("M", getFont());
        dim.width *= MIN_COLS;
        dim.height *= MIN_ROWS;
        dim.width += margins.left + margins.right;
        dim.height += margins.top + margins.bottom;
        return dim;
    }

    /**IzraÄ�unava ukupnu dimenziju komponente*/
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
        updateTextArea();
        elementPainter.update();
    }

    /**Uzima u obzir promene nad komponentom i izraÄ�unava novu dimenziju koju zauzima labela*/
    private void updateLabel() {
        labelDim = calculateLabelDim();
    }

    /**Uzima u obzir promene nad komponentom i izraÄ�unava novu dimenziju koju zauzima tekstualno polje*/
    private void updateField() {
        int fieldWidth = dimension.width - labelDim.width - insets.left - insets.right;
        int fieldHeight = dimension.height - insets.top - insets.bottom;

//        int cell = 10;
//        int mod = 0;
//        mod = fieldWidth % cell;
//        if (mod > 0) {
//            fieldWidth += cell - mod;
//        }
        fieldDim.width = fieldWidth;
        fieldDim.height = fieldHeight;
    }

    /**Uzima u obzir promene nad komponentom i izraÄ�unava novu dimenziju koju zauzima cela komponenta*/
    private void updateTextArea() {
        updateLabel();
        updateField();
        dimension.width = insets.left + labelDim.width + fieldDim.width + insets.right;
        dimension.height = insets.top + insets.bottom + Math.max(labelDim.height, fieldDim.height);
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Dimension getFieldDim() {
        return fieldDim;
    }

    public void setFieldDim(Dimension fieldDim) {
        this.fieldDim = fieldDim;
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
}
