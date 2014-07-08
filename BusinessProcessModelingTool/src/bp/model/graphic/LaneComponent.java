package bp.model.graphic;

import java.awt.Font;
import java.awt.Shape;

import bp.app.AppCore;
import bp.event.AttributeChangeListener;
import bp.model.data.Lane;
import bp.model.graphic.util.ElementHandlers;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.StringUtils;
import bp.view.BasicPainter;
import bp.view.painter.BPLanePainter;
import bp.view.painter.BPShapeFactory;

public class LaneComponent extends BPComponent {

    public static final Integer MIN_DIMENSION_COEFFICIENT = 1;
    public static final Integer MAX_DIMENSION_COEFFICIENT = 10;

    public static final Integer REGULAR_FONT = 0;
    public static final Integer ITALIC_FONT = 1;

    private String text;
    private Integer textType;
    private BPLanePainter painter;
    private Lane lane;
    private Integer fontType = REGULAR_FONT;

    public LaneComponent(final Lane lane) {
        this.lane = lane;
        this.painter = new BPLanePainter(this);
        addDataListener();
    }

    public LaneComponent() {
		super();
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public Integer getFontType() {
		return fontType;
	}

	public void setFontType(Integer fontType) {
		this.fontType = fontType;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setPainter(BPLanePainter painter) {
		this.painter = painter;
	}

	@Override
    public Integer getDefaultHeight() {
        return 100;
    }

    @Override
    public Integer getDefaultWidth() {
        return 300;
    }

    @Override
    public Integer getMinimumHeight() {
        final Integer textWidth = getTextPanelWidth();
        if (textWidth > super.getMinimumHeight())
            return textWidth;
        else
            return super.getMinimumHeight();
    }

    @Override
    public Shape getShape() {
        return BPShapeFactory.lane(this);
    }

    @Override
    protected void initializeElementHandlers() {
        this.handlers = new ElementHandlers(this, ElementHandlers.RECTANGLE_HANDLERS);

    }

    @Override
    public BasicPainter getPainter() {
        return this.painter;
    }

    @Override
    public Integer getMaxDimensionCoefficient() {
        return MAX_DIMENSION_COEFFICIENT;
    }

    @Override
    public Integer getMinDimensionCoefficient() {
        return MIN_DIMENSION_COEFFICIENT;
    }

    public Integer getTextHeight() {
        return StringUtils.calculateStringHeight(getFont(), this.text);
    }

    public Integer getTextWidth() {
        return StringUtils.calculateStringWidth(getFont(), this.text);
    }

    public Integer getTextPanelHeight() {
        return getTextHeight() + 10;
    }

    public Integer getTextPanelWidth() {
        return getTextWidth() + 10;
    }

    public String getText() {
        return this.text;
    }

    protected void updateText(final String text) {
        this.text = text;
        AppCore.getInstance().getBpPanel().getGraphicsPanel().repaint();
    }

    public Integer getTextType() {
        return this.textType;
    }

    public void setTextType(final Integer textType) {
        this.textType = textType;
    }

    protected void addDataListener() {
        this.lane.addAttributeChangeListener(new AttributeChangeListener() {

            @Override
            public Controller getController() {
                return Controller.GRAPHIC;
            }

            @Override
            public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
                if (value != null) {
                    if (keyWord == BPKeyWords.NAME) {
                        updateText((String) value);
                        LaneComponent.this.fontType = REGULAR_FONT;
                        if (getText() == null || getText().isEmpty()) {
                            updateText(LaneComponent.this.lane.getUniqueName());
                            LaneComponent.this.fontType = ITALIC_FONT;
                        }
                    } else if (keyWord == BPKeyWords.UNIQUE_NAME) {
                        if (LaneComponent.this.lane.getName() == null || LaneComponent.this.lane.getName().isEmpty()) {
                            updateText((String) value);
                            LaneComponent.this.fontType = ITALIC_FONT;
                        }
                    }
                }
            }
        });
    }

    public Font getRegularFont() {
        return super.getFont();
    }

    public Font getItalicFont() {
        return new Font(super.getFont().getName(), Font.ITALIC, super.getFont().getSize());
    }

    @Override
    public Font getFont() {
        if (this.fontType == REGULAR_FONT)
            return getRegularFont();
        if (this.fontType == ITALIC_FONT)
            return getItalicFont();
        return super.getFont();
    }
}
