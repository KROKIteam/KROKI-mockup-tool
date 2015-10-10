package bp.model.graphic.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;

import bp.model.graphic.BPComponent;
import bp.view.Strokes;
import bp.view.painter.BPShapeFactory;

public class ElementHandlers implements IHandlers {

    public static final Integer NO_HANDLERS = 0;
    public static final Integer RECTANGLE_HANDLERS = 1;
    public static final Integer SQUARE_HANDLERS = 2;

    private BPComponent component;
    private Integer handlerType;

    private Stroke lineStroke = Strokes.getDashedLine(Strokes.THIN_LINE);
    private Color lineColor = Color.BLACK;

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    private Handler nw;
    private Handler ne;
    private Handler se;
    private Handler sw;
    private Handler n;
    private Handler e;
    private Handler s;
    private Handler w;


    public ElementHandlers(final BPComponent component, final Integer handlerType) {
        this.component = component;
        this.handlerType = handlerType;

        initializeHandlers();
        updateHandlers();
    }

    public ElementHandlers() {
		super();
	}

	public void setComponent(BPComponent component) {
		this.component = component;
	}

	public void setHandlerType(Integer handlerType) {
		this.handlerType = handlerType;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setNw(Handler nw) {
		this.nw = nw;
	}

	public void setNe(Handler ne) {
		this.ne = ne;
	}

	public void setSe(Handler se) {
		this.se = se;
	}

	public void setSw(Handler sw) {
		this.sw = sw;
	}

	public void setN(Handler n) {
		this.n = n;
	}

	public void setE(Handler e) {
		this.e = e;
	}

	public void setS(Handler s) {
		this.s = s;
	}

	public void setW(Handler w) {
		this.w = w;
	}

	private void initializeHandlers() {
        if (this.handlerType == SQUARE_HANDLERS) {
            initializeSquareHandlers();
        } else if (this.handlerType == RECTANGLE_HANDLERS) {
            initializeSquareHandlers();
            initializeRectangleHandlers();
        }
    }

    private void initializeSquareHandlers() {
        this.nw = new Handler(0, 0, HandlerPosition.NORTH_WEST);
        this.ne = new Handler(0, 0, HandlerPosition.NORTH_EAST);
        this.sw = new Handler(0, 0, HandlerPosition.SOUTH_WEST);
        this.se = new Handler(0, 0, HandlerPosition.SOUTH_EAST);
    }

    private void initializeRectangleHandlers() {
        this.n = new Handler(0, 0, HandlerPosition.NORTH);
        this.e = new Handler(0, 0, HandlerPosition.EAST);
        this.s = new Handler(0, 0, HandlerPosition.SOUTH);
        this.w = new Handler(0, 0, HandlerPosition.WEST);
    }

    @Override
    public void updateHandlers() {
        final Integer x = getComponent().getX();
        final Integer y = getComponent().getY();
        final Integer w = getComponent().getWidth();
        final Integer h = getComponent().getHeight();

        this.x = x - 3;
        this.y = y - 3;
        this.width = w + 6;
        this.height = h + 6;

        if (this.handlerType == SQUARE_HANDLERS || this.handlerType == RECTANGLE_HANDLERS) {
            this.nw.setX(x - this.nw.getWidth());
            this.nw.setY(y - this.nw.getHeight());

            this.ne.setX(x + w);
            this.ne.setY(y - this.ne.getHeight());

            this.se.setX(x + w);
            this.se.setY(y + h);

            this.sw.setX(x - this.sw.getWidth());
            this.sw.setY(y + h);

            if (this.handlerType == RECTANGLE_HANDLERS) {
                this.n.setX(x + (w - this.n.getWidth()) / 2);
                this.n.setY(y - this.n.getHeight());

                this.e.setX(x + w);
                this.e.setY(y + (h - this.e.getHeight()) / 2);

                this.s.setX(x + (w - this.s.getWidth()) / 2);
                this.s.setY(y + h);

                this.w.setX(x - this.w.getWidth());
                this.w.setY(y + (h - this.w.getHeight()) / 2);
            }
        }
    }

    public BPComponent getComponent() {
        return this.component;
    }

    public Integer getHandlerType() {
        return this.handlerType;
    }

    public Handler getNw() {
        return this.nw;
    }

    public Handler getNe() {
        return this.ne;
    }

    public Handler getSe() {
        return this.se;
    }

    public Handler getSw() {
        return this.sw;
    }

    public Handler getN() {
        return this.n;
    }

    public Handler getE() {
        return this.e;
    }

    public Handler getS() {
        return this.s;
    }

    public Handler getW() {
        return this.w;
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Stroke getLineStroke() {
        return this.lineStroke;
    }

    public void setLineStroke(final Stroke lineStroke) {
        this.lineStroke = lineStroke;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    public void setLineColor(final Color lineColor) {
        this.lineColor = lineColor;
    }

    public Shape getShape() {
        return BPShapeFactory.handlerBorder(this);
    }

    @Override
    public boolean isHandlerAt(final Point p) {
        if (this.handlerType == NO_HANDLERS) {
            return false;
        }

        final Boolean squareHandlers = this.nw.isAt(p) || this.ne.isAt(p) || this.se.isAt(p) || this.sw.isAt(p);
        if (this.handlerType == RECTANGLE_HANDLERS) {
            final Boolean rectangleHandlers = this.n.isAt(p) || this.e.isAt(p) || this.s.isAt(p) || this.w.isAt(p);
            return squareHandlers || rectangleHandlers;
        }
        return squareHandlers;
    }

    @Override
    public Handler getHandlerAt(final Point p) {
        if (this.handlerType == NO_HANDLERS) {
            return null;
        }

        if (this.nw.isAt(p))
            return this.nw;
        if (this.ne.isAt(p))
            return this.ne;
        if (this.se.isAt(p))
            return this.se;
        if (this.sw.isAt(p))
            return this.sw;
        if (this.handlerType == RECTANGLE_HANDLERS) {
            if (this.n.isAt(p))
                return this.n;
            if (this.e.isAt(p))
                return this.e;
            if (this.s.isAt(p))
                return this.s;
            if (this.w.isAt(p))
                return this.w;
        }

        return null;
    }

}
