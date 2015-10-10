package bp.model.data;

import bp.details.LinkThrowEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class LinkThrowEvent extends Event {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LinkCatchEvent link;

    public LinkThrowEvent(final String uniqueName) {
        super(uniqueName);
    }

    public LinkThrowEvent() { }
    
    public void setLink(LinkCatchEvent link) {
		this.link = link;
	}

	@Override
    public boolean canHaveInput() {
        if (getInputEdges().size() > 0)
            return false;
        return true;
    }

    @Override
    public boolean canHaveOutput() {
        return false;
    }

    public LinkCatchEvent getLink() {
        return this.link;
    }

    public void updateLink(final LinkCatchEvent link, final Controller source) {
        this.link = link;
        fireAttributeChanged(BPKeyWords.LINK, this.link, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.LINK, Strokes.getDashedLine(Strokes.THICK_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new LinkThrowEventDetails(this);
    }


}
