package bp.model.data;

import bp.details.ElementDetails;
import bp.model.graphic.EventComponent;
import bp.util.ImageRes;
import bp.view.Strokes;

public class LinkCatchEvent extends Event {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LinkCatchEvent(final String uniqueName) {
        super(uniqueName);
    }

	public LinkCatchEvent() { }
	
    @Override
    public boolean canHaveInput() {
        return false;
    }

    @Override
    public boolean canHaveOutput() {
        if (getOutputEdges().size() > 0)
            return false;
        return true;
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.LINK, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ElementDetails(this);
    }

}
