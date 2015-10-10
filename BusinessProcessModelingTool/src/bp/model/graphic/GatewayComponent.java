package bp.model.graphic;

import java.awt.Shape;

import bp.app.AppCore;
import bp.event.AttributeChangeListener;
import bp.model.data.Edge;
import bp.model.data.Gateway;
import bp.model.graphic.util.ElementHandlers;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.view.BasicPainter;
import bp.view.painter.BPElementTextPainter;
import bp.view.painter.BPShapeFactory;

public class GatewayComponent extends SquareComponent implements BPText {

    private BPElementTextPainter painter;
    private Gateway gateway;

    private String text;

    public GatewayComponent(final Gateway gateway) {
        super();
        this.gateway = gateway;
        this.painter = new BPElementTextPainter(this);

        addDataListener();
    }

    public GatewayComponent() {
		super();
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public void setPainter(BPElementTextPainter painter) {
		this.painter = painter;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
    public Integer getDefaultHeight() {
        return 50;
    }

    @Override
    public Shape getShape() {
        return BPShapeFactory.gateway(this);
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
    public void moveComponent(final Integer diffX, final Integer diffY) {
        if (diffX == null && diffY == null)
            return;

        super.moveComponent(diffX, diffY);

        for (final Edge edge : this.gateway.getInputEdges()) {
            edge.getEdgeComponent().updateComponent(null, this);
        }
        for (final Edge edge : this.gateway.getOutputEdges()) {
            edge.getEdgeComponent().updateComponent(this, null);
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
        this.gateway.addAttributeChangeListener(new AttributeChangeListener() {

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

}
