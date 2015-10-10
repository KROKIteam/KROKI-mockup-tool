package bp.model.graphic;

import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;

import bp.model.data.ActivityEvent;
import bp.model.data.Edge;
import bp.model.data.Task;
import bp.model.graphic.util.ElementHandlers;
import bp.util.ActivityEventLocationHelper;
import bp.util.ImageRes;
import bp.view.BasicPainter;
import bp.view.painter.BPElementPainter;
import bp.view.painter.BPElementTextPainter;
import bp.view.painter.BPShapeFactory;

public class ActivityEventComponent extends SquareComponent implements BPImage {

    private BPElementPainter painter;
    private ActivityEvent event;

    private Image image;

    public ActivityEventComponent(final ActivityEvent event) {
        this(event, null, null);
    }

    public ActivityEventComponent(final ActivityEvent event, final ImageRes image, final Stroke fgStroke) {
        super();
        this.event = event;
        this.painter = new BPElementTextPainter(this);

        if (image != null) {
            this.image = image.getImage();
        }

        if (fgStroke != null) {
            setFgStroke(fgStroke);
        }

    }

    public ActivityEventComponent() {
		super();
	}

	public ActivityEvent getEvent() {
		return event;
	}

	public void setEvent(ActivityEvent event) {
		this.event = event;
	}

	public void setPainter(BPElementPainter painter) {
		this.painter = painter;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
    public Integer getDefaultHeight() {
        return 50;
    }

    @Override
    public Shape getShape() {
        return BPShapeFactory.event(this);
    }

    @Override
    protected void initializeElementHandlers() {
        this.handlers = new ElementHandlers(this, ElementHandlers.NO_HANDLERS);
    }

    @Override
    public BasicPainter getPainter() {
        return this.painter;
    }

    @Override
    public Integer getMinimumHeight() {
        if (this.image == null) {
            return super.getMinimumHeight();
        } else {
            final Integer minImageHeight = getImageHeight() + getImageMargins() * 2;
            if (minImageHeight > super.getMinimumHeight()) {
                return minImageHeight;
            } else {
                return super.getMinimumHeight();
            }
        }
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public Integer getImageWidth() {
        if (this.image != null) {
            return 32;
        }
        return 0;
    }

    @Override
    public Integer getImageHeight() {
        if (this.image != null) {
            return 32;
        }
        return 0;
    }

    @Override
    public Integer getImageMargins() {
        if (this.image != null) {
            return 32;
        }

        return 0;
    }

    public ActivityEvent getActivityEvent() {
        return this.event;
    }

    public void updatePosition() {
        final Point currentPos = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
        if (this.event.getActivity() instanceof Task) {
            final Task task = (Task) this.event.getActivity();
            final BPComponent component = task.getTaskComponent();
            final Point newPos = ActivityEventLocationHelper.getValidPoint(component, this, currentPos);
            setX(newPos.x);
            setY(newPos.y);
        }
    }

    @Override
    public void moveComponent(final Integer diffX, final Integer diffY) {
        if (diffX == null && diffY == null)
            return;

        super.moveComponent(diffX, diffY);

        updatePosition();

        for (final Edge edge : this.event.getInputEdges()) {
            edge.getEdgeComponent().updateComponent(null, this);
        }
        for (final Edge edge : this.event.getOutputEdges()) {
            edge.getEdgeComponent().updateComponent(this, null);
        }
    }

}
