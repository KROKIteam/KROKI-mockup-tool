package bp.model.data;

import bp.details.ErrorActivityEventDetails;
import bp.model.graphic.ActivityEventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class ErrorActivityEvent extends ActivityEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorName;

    public ErrorActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public ErrorActivityEvent() { }
    
    public String getErrorName() {
        return this.errorName;
    }

    public void updateErrorName(final String errorName, final Controller source) {
        this.errorName = errorName;
        fireAttributeChanged(BPKeyWords.ERROR_NAME, this.errorName, source);
    }

    public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	@Override
    protected void initializeComponent() {
        this.component = new ActivityEventComponent(this, ImageRes.ERROR, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(201);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ErrorActivityEventDetails(this);
    }

}
