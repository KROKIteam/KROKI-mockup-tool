package bp.model.graphic;

import java.awt.Point;
import java.awt.Shape;

import bp.model.graphic.util.EdgeHandlers;
import bp.util.PointHelper;
import bp.view.BasicPainter;
import bp.view.Strokes;
import bp.view.painter.BPEdgePainter;
import bp.view.painter.BPShapeFactory;

public class BPEdge extends BPElement {

    public static final Integer REGULAR_EDGE = 1;
    public static final Integer CONDITIONAL_EDGE = 2;

    private BPEdgePainter painter;

    private Integer sourceX;
    private Integer sourceY;
    private Integer targetX;
    private Integer targetY;

    private EdgeHandlers handlers;

    private Integer edgeType;

    public BPEdge(final Integer edgeType) {
        this.sourceX = 0;
        this.sourceY = 0;
        this.targetX = 0;
        this.targetY = 0;

        this.painter = new BPEdgePainter(this);
        setFgStroke(Strokes.getLine(Strokes.EDGE_LINE));
        this.handlers = new EdgeHandlers(this);

        if (edgeType == null) {
            this.edgeType = REGULAR_EDGE;
        } else {
            this.edgeType = edgeType;
        }
    }

    public BPEdge() {
		super();
	}

	public void setHandlers(EdgeHandlers handlers) {
		this.handlers = handlers;
	}

	public void setEdgeType(Integer edgeType) {
		this.edgeType = edgeType;
	}

	public void setPainter(BPEdgePainter painter) {
		this.painter = painter;
	}

	public Integer getSourceX() {
        return this.sourceX;
    }

    public void setSourceX(final Integer sourceX) {
        this.sourceX = sourceX;
        getHandlers().updateHandlers();
    }

    public Integer getSourceY() {
        return this.sourceY;
    }

    public void setSourceY(final Integer sourceY) {
        this.sourceY = sourceY;
        getHandlers().updateHandlers();
    }

    public Integer getTargetX() {
        return this.targetX;
    }

    public void setTargetX(final Integer targetX) {
        this.targetX = targetX;
        getHandlers().updateHandlers();
    }

    public Integer getTargetY() {
        return this.targetY;
    }

    public void setTargetY(final Integer targetY) {
        this.targetY = targetY;
        getHandlers().updateHandlers();
    }

    @Override
    public BasicPainter getPainter() {
        return this.painter;
    }

    public Shape getStartShape() {
        return BPShapeFactory.diamond();
    }

    public Shape getEndShape() {
        return BPShapeFactory.arrow();
    }

    @Override
    public EdgeHandlers getHandlers() {
        return this.handlers;
    }

    public Integer getEdgeType() {
        return this.edgeType;
    }

    public void updateComponent(final BPComponent sourceComponent, final BPComponent targetComponent) {
        if (sourceComponent != null) {
            final Integer x = sourceComponent.getX();
            final Integer y = sourceComponent.getY();
            final Integer w = sourceComponent.getWidth();
            final Integer h = sourceComponent.getHeight();
            final Point validPoint = PointHelper.findClosestPoint(sourceComponent.getValidEdgePoints(), getSourceX(),
                    getSourceY(), x, y, w, h);
            setSourceX(validPoint.x);
            setSourceY(validPoint.y);
        }
        if (targetComponent != null) {
            final Integer x = targetComponent.getX();
            final Integer y = targetComponent.getY();
            final Integer w = targetComponent.getWidth();
            final Integer h = targetComponent.getHeight();
            final Point validPoint = PointHelper.findClosestPoint(targetComponent.getValidEdgePoints(), getTargetX(),
                    getTargetY(), x, y, w, h);
            setTargetX(validPoint.x);
            setTargetY(validPoint.y);

        }
    }
}
