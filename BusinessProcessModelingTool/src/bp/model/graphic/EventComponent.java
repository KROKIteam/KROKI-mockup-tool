package bp.model.graphic;

import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;

import bp.app.AppCore;
import bp.event.AttributeChangeListener;
import bp.model.data.Edge;
import bp.model.data.Event;
import bp.model.graphic.util.ElementHandlers;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.BasicPainter;
import bp.view.painter.BPElementTextPainter;
import bp.view.painter.BPShapeFactory;

public class EventComponent extends SquareComponent implements BPImage, BPText {

    private BPElementTextPainter painter;
    private Event event;

    private String text;
    private Image image;

    public EventComponent(final Event event) {
        this(event, null, null);
    }

    public EventComponent(final Event event, final ImageRes image, final Stroke fgStroke) {
        super();
        this.event = event;
        this.painter = new BPElementTextPainter(this);

        if (image != null) {
            this.image = image.getImage();
        }

        if (fgStroke != null) {
            setFgStroke(fgStroke);
        }

        addDataListener();
    }

    public EventComponent() {
		super();
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setPainter(BPElementTextPainter painter) {
		this.painter = painter;
	}

	public void setText(String text) {
		this.text = text;
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
        this.handlers = new ElementHandlers(this, ElementHandlers.SQUARE_HANDLERS);
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
    public String getText() {
        return this.text;
    }

    @Override
    public void updateText(final String text) {
        this.text = text;
        AppCore.getInstance().getBpPanel().getGraphicsPanel().repaint();
    }

    @Override
    public Integer getTextSeparatorSize() {
        return 10;
    }

    protected void addDataListener() {
        this.event.addAttributeChangeListener(new AttributeChangeListener() {

            @Override
            public Controller getController() {
                return Controller.GRAPHIC;
            }

            @Override
            public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
                if (value != null) {
                    if (keyWord == BPKeyWords.NAME) {
                        updateText((String) value);
                    }
                }
            }
        });
    }

    @Override
    public void moveComponent(final Integer diffX, final Integer diffY) {
        if (diffX == null && diffY == null)
            return;

        super.moveComponent(diffX, diffY);

        for (final Edge edge : this.event.getInputEdges()) {
            edge.getEdgeComponent().updateComponent(null, this);
        }
        for (final Edge edge : this.event.getOutputEdges()) {
            edge.getEdgeComponent().updateComponent(this, null);
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
            return 5;
        }
        return 0;
    }

}
