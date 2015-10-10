package bp.model.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.io.Serializable;

import bp.view.Strokes;

public class BPGraphElement implements Serializable {

    private Color bgColor = Color.WHITE;
    private Color fgColor = Color.BLACK;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Stroke bgStroke = Strokes.getLine(Strokes.THIN_LINE);
    private Stroke fgStroke = Strokes.getLine(Strokes.THIN_LINE);
    private Integer zIndex = 0;

    public BPGraphElement() { }

    public Color getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(final Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getFgColor() {
        return this.fgColor;
    }

    public void setFgColor(final Color fgColor) {
        this.fgColor = fgColor;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(final Font font) {
        this.font = font;
    }

    public Stroke getBgStroke() {
        return this.bgStroke;
    }

    public void setBgStroke(final Stroke bgStroke) {
        this.bgStroke = bgStroke;
    }

    public Stroke getFgStroke() {
        return this.fgStroke;
    }

    public void setFgStroke(final Stroke fgStroke) {
        this.fgStroke = fgStroke;
    }

    public Integer getzIndex() {
        return this.zIndex;
    }

    public void setzIndex(final Integer zIndex) {
        this.zIndex = zIndex;
    }

}
