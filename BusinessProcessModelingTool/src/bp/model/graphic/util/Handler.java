package bp.model.graphic.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;

import bp.model.graphic.BPGraphElement;
import bp.view.painter.BPShapeFactory;

public class Handler extends BPGraphElement {

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    private HandlerPosition handlerPosition;

    public Handler(final HandlerPosition handlerPosition) {
        this.handlerPosition = handlerPosition;

        setFgColor(Color.BLACK);
        setBgColor(Color.BLACK);

        setX(0);
        setY(0);
        this.width = 6;
        this.height = 6;

    }

    public Handler(final Integer x, final Integer y, final HandlerPosition handlerPosition) {
        this(handlerPosition);
        setX(x);
        setY(y);
    }

    public Handler() {
		super();
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setHandlerPosition(HandlerPosition handlerPosition) {
		this.handlerPosition = handlerPosition;
	}

	public Shape getShape() {
        return BPShapeFactory.handler(this);
    }


    public HandlerPosition getHandlerPosition() {
        return this.handlerPosition;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(final Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(final Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public boolean isAt(final Point p) {
        if (p.x >= this.x - 2 && p.x <= this.x + this.width + 4 && p.y >= this.y - 2 && p.y <= this.y + this.height + 4)
            return true;

        return false;
    }
}
