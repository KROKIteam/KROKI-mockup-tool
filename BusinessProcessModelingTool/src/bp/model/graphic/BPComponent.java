package bp.model.graphic;

import java.awt.Dimension;
import java.awt.Shape;

import bp.model.graphic.util.ElementHandlers;
import bp.util.EdgePoints;

public abstract class BPComponent extends BPElement {

    public static final Integer MAX_DIMENSION_COEFFICIENT = 2;
    public static final Integer MIN_DIMENSION_COEFFICIENT = 2;

    /**
     * X Coordinate
     */
    private Integer x;
    /**
     * Y Coordinate
     */
    private Integer y;
    /**
     * Current width
     */
    private Integer width;
    /**
     * Current height
     */
    private Integer height;

    protected ElementHandlers handlers;

    public BPComponent() {
        this.width = getDefaultWidth();
        this.height = getDefaultHeight();
        this.x = 0;
        this.y = 0;
        initializeElementHandlers();
    }

    public BPComponent(final Integer x, final Integer y) {
        this();
        setX(x);
        setY(y);
    }

    public abstract Integer getDefaultHeight();

    public abstract Integer getDefaultWidth();

    public abstract Shape getShape();

    protected abstract void initializeElementHandlers();

    public Integer getMaxDimensionCoefficient() {
        return MAX_DIMENSION_COEFFICIENT;
    }

    public Integer getMinDimensionCoefficient() {
        return MIN_DIMENSION_COEFFICIENT;
    }

    public Integer getMaximumHeight() {
        return getDefaultHeight() * getMaxDimensionCoefficient();
    }

    public Integer getMaximumWidth() {
        return getDefaultWidth() * getMaxDimensionCoefficient();
    }

    public Integer getMinimumHeight() {
        return getDefaultHeight() / getMinDimensionCoefficient();
    }

    public Integer getMinimumWidth() {
        return getDefaultWidth() / getMinDimensionCoefficient();
    }

    public Dimension getMinimumSize() {
        return new Dimension(getMinimumWidth(), getMinimumHeight());
    }

    public Dimension getMaximumSize() {
        return new Dimension(getMaximumWidth(), getMaximumHeight());
    }

    public Dimension getDefaultSize() {
        return new Dimension(getDefaultWidth(), getDefaultHeight());
    }

    public Integer getHeight() {
        return this.height;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setHeight(final Integer height) {
        this.height = height;
        updateComponent();
        getHandlers().updateHandlers();
    }

    public void setWidth(final Integer width) {
        this.width = width;
        updateComponent();
        getHandlers().updateHandlers();
    }

    public Dimension getSize() {
        return new Dimension();
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(final Integer x) {
        this.x = x;
        getHandlers().updateHandlers();
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(final Integer y) {
        this.y = y;
        getHandlers().updateHandlers();
    }

    public EdgePoints[] getValidEdgePoints() {
        return EdgePoints.values();
    }

    @Override
    public ElementHandlers getHandlers() {
        return this.handlers;
    }

    public void updateComponent() {
        if (getWidth() < getMinimumWidth())
            this.width = getMinimumWidth();
        else if (getWidth() > getMaximumWidth())
            this.width = getMaximumWidth();

        if (getHeight() < getMinimumHeight())
            this.height = getMinimumHeight();
        else if (getHeight() > getMaximumHeight())
            this.height = getMaximumHeight();
    }

    public void moveComponent(final Integer diffX, final Integer diffY) {
        if (diffX != null)
            setX(diffX == null ? getX() : getX() + diffX);

        if (diffY != null)
            setY(diffY == null ? getY() : getY() + diffY);
    }

	public void setHandlers(ElementHandlers handlers) {
		this.handlers = handlers;
	}
}